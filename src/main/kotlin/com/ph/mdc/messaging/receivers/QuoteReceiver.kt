package com.ph.mdc.messaging.receivers

import com.fasterxml.jackson.databind.ObjectMapper
import com.ph.mdc.application.instrument.service.InstrumentService
import com.ph.mdc.client.model.QuoteData
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class QuoteReceiver(
    private val objectMapper: ObjectMapper,
    private val instrumentService: InstrumentService
) {

    companion object {
        private val logger = LoggerFactory.getLogger(QuoteReceiver::class.java)
    }

    @RabbitListener(queues = ["#{quotesQueue.name}"])
    fun onMessage(msg: String) {
        val quote = objectMapper.readValue(msg, QuoteData::class.java)
        instrumentService.updatePrice(quote.toQuote())
            .doOnSuccess {
                logger.info("Received quote ${quote.price} for isin ${quote.isin}")
            }
            .subscribe()
    }
}