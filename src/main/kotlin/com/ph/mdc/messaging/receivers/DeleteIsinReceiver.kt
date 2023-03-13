package com.ph.mdc.messaging.receivers

import com.fasterxml.jackson.databind.ObjectMapper
import com.ph.mdc.application.instrument.service.InstrumentService
import com.ph.mdc.client.model.InstrumentData
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class DeleteIsinReceiver(
    private val objectMapper: ObjectMapper,
    private val instrumentService: InstrumentService
) {

    companion object {
        private val logger = LoggerFactory.getLogger(DeleteIsinReceiver::class.java)
    }

    @RabbitListener(queues = ["#{deletesQueue.name}"])
    fun onMessage(msg: String) {
        val instrument = objectMapper.readValue(msg, InstrumentData::class.java)
        instrumentService.delete(instrument.isin)
            .doOnSuccess {
                logger.info("Received command to delete isin ${instrument.isin}, desc: ${instrument.isin}")
            }.subscribe()
    }
}