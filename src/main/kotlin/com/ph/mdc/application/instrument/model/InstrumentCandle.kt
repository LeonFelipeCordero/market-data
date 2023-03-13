package com.ph.mdc.application.instrument.model

import java.math.BigDecimal

data class InstrumentCandle(
    val isin: String,
    val description: String,
    val priceAggregation: List<PriceAggregation>
)

data class PriceAggregation(
    val openPosition: Price,
    val closePosition: Price?,
    val higherPrice: BigDecimal,
    val lowerPrice: BigDecimal
)