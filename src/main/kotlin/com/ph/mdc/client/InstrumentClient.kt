package com.ph.mdc.client

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.ph.mdc.client.model.ClientProperties
import com.ph.mdc.client.model.MessageParsingException
import com.ph.mdc.client.model.MessageType
import com.ph.mdc.client.model.PartnerInstrument
import com.ph.mdc.messaging.model.MessagingProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Sinks
import reactor.core.scheduler.Schedulers
import java.net.URI
import java.net.URISyntaxException

@Component
class InstrumentClient(
    private val clientProperties: ClientProperties,
    private val messagingProperties: MessagingProperties,
    private val objectMapper: ObjectMapper,
    private val rabbitTemplate: RabbitTemplate
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(InstrumentClient::class.java)
    }

    fun connect() {
        val sink = Sinks.many().multicast().onBackpressureBuffer<PartnerInstrument>()
        sink.asFlux()
            .publishOn(Schedulers.boundedElastic())
            .doOnSubscribe { createReactorClient(sink).subscribe() }
            .doOnNext { processMessage(it) }
            .subscribe()
    }

    private fun createReactorClient(sink: Sinks.Many<PartnerInstrument>) =
        ReactorNettyWebSocketClient()
            .execute(createUri()) { session -> handleSession(session, sink) }
            .then()

    private fun handleSession(session: WebSocketSession, sink: Sinks.Many<PartnerInstrument>) =
        session.receive()
            .map { it.payloadAsText }
            .map { handleMessage(it) }
            .doOnNext { sink.tryEmitNext(it) }
            .then()

    fun handleMessage(message: String): PartnerInstrument {
        try {
            return objectMapper.readValue(message, PartnerInstrument::class.java)
        } catch (e: JsonProcessingException) {
            logger.error(e.originalMessage)
            throw MessageParsingException("Something went wrong trying to parse message $message")
        }
    }

    private fun processMessage(partnerInstrument: PartnerInstrument) {
        val message = objectMapper.writeValueAsString(partnerInstrument.data)
        when (partnerInstrument.type) {
            MessageType.ADD -> rabbitTemplate.convertAndSend(
                messagingProperties.name,
                messagingProperties.addIsinTopic,
                message
            )

            MessageType.DELETE -> rabbitTemplate.convertAndSend(
                messagingProperties.name,
                messagingProperties.deleteIsinTopic,
                message
            )

            else -> logger.error("Something went wrong processing message $partnerInstrument")
        }
    }

    private fun createUri(): URI {
        try {
            return URI(clientProperties.url + clientProperties.instruments)
        } catch (e: URISyntaxException) {
            logger.error("Something went wrong trying to connect to partner api ${clientProperties.url}")
            throw RuntimeException("${clientProperties.url}/${clientProperties.instruments} endpoint is not valid")
        }
    }
}
