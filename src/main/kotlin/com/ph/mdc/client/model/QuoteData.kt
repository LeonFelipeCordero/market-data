package com.ph.mdc.client.model

import com.ph.mdc.application.instrument.model.Quote
import java.math.BigDecimal
import java.time.LocalDateTime

data class QuoteData(val price: BigDecimal, val isin: String) {

    fun toQuote(): Quote {
        return Quote(
            isin = isin,
            price = price,
            updateTime = LocalDateTime.now()
        )
    }
}
