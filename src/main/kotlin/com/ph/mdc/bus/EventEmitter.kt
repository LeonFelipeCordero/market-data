package com.ph.mdc.bus

import com.ph.mdc.application.alert.model.Alert
import com.ph.mdc.application.instrument.model.Quote
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

interface EventEmitter<T> {
    fun publish(event: T)
    fun stream(): Flux<T>
}

@Component
class QuoteEventEmitter : EventEmitter<Quote> {
    private val sink = Sinks.many().multicast().onBackpressureBuffer<Quote>()

    override fun publish(event: Quote) {
        sink.tryEmitNext(event)
    }

    override fun stream(): Flux<Quote> {
        return sink.asFlux()
    }
}

@Component
class AlertEventEmitter : EventEmitter<Alert> {
    private val sink = Sinks.many().multicast().onBackpressureBuffer<Alert>()

    override fun publish(event: Alert) {
        sink.tryEmitNext(event)
    }

    override fun stream(): Flux<Alert> {
        return sink.asFlux()
    }
}