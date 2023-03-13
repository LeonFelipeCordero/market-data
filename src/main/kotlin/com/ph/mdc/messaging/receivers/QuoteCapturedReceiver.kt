package com.ph.mdc.messaging.receivers

import com.fasterxml.jackson.databind.ObjectMapper
import com.ph.mdc.application.alert.service.AlertService
import com.ph.mdc.application.instrument.model.Quote
import com.ph.mdc.bus.QuoteEventEmitter
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class QuoteCapturedReceiver(
    val objectMapper: ObjectMapper,
    val quoteEventEmitter: QuoteEventEmitter,
    val alertService: AlertService
) {

    @RabbitListener(queues = ["#{quoteCapturedBusQueue.name}"])
    fun handleBusQueue(msg: String) {
        val quote = objectMapper.readValue(msg, Quote::class.java)
        quoteEventEmitter.publish(quote)
    }

    @RabbitListener(queues = ["#{quoteCaptureAlertQueue.name}"])
    fun handleAlertQueue(msg: String) {
        val quote = objectMapper.readValue(msg, Quote::class.java)
        alertService.validateAlert(quote)
    }
}
