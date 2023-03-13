package com.ph.mdc.application.instrument.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Quote(
    val price: BigDecimal,
    val isin: String,
    val updateTime: LocalDateTime
)
