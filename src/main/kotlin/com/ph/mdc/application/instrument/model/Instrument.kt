package com.ph.mdc.application.instrument.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document("instruments")
data class Instrument(
    @Id val id: String? = null,
    val isin: String,
    val description: String,
    val prices: MutableList<Price>
) {
    fun addPrice(price: BigDecimal, updateTime: LocalDateTime): Instrument {
        this.prices.add(Price(price = price, updateTime))
        return this
    }

    fun latestPrice(): Price? {
        return this.prices.maxByOrNull { it.dateTime }
    }
}
