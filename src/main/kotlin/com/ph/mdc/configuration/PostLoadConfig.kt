package com.ph.mdc.configuration

import com.ph.mdc.client.InstrumentClient
import com.ph.mdc.client.QuoteClient
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
class PostLoadConfig(
    private val instrumentClient: InstrumentClient,
    private val quoteClient: QuoteClient
) {

    @EventListener(ApplicationReadyEvent::class)
    fun establishConnections() {
        instrumentClient.connect()
        quoteClient.connect()
    }
}
