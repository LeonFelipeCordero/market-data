package com.ph.mdc.messaging.receivers

import com.fasterxml.jackson.databind.ObjectMapper
import com.ph.mdc.application.instrument.service.InstrumentService
import com.ph.mdc.client.model.InstrumentData
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class AddIsinReceiver(
    private val objectMapper: ObjectMapper,
    private val instrumentService: InstrumentService
) {

    companion object {
        private val logger = LoggerFactory.getLogger(AddIsinReceiver::class.java)
    }

    @RabbitListener(queues = ["#{addsQueue.name}"])
    fun onMessage(msg: String) {
        val instrument = objectMapper.readValue(msg, InstrumentData::class.java)
        instrumentService.save(instrument.toInstrument())
            .doOnSuccess {
                logger.info("Received command to add isin ${it.isin}, desc: ${it.description}")
            }.subscribe()
    }
}