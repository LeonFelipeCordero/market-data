package com.ph.mdc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MarketDataConsumerApplication

fun main(args: Array<String>) {
    runApplication<MarketDataConsumerApplication>(*args)
}
