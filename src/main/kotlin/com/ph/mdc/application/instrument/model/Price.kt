package com.ph.mdc.application.instrument.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.time.LocalDateTime

data class Price(
    val price: BigDecimal,
    @JsonFormat(pattern = "yyyy-mm-dd'TT'HH:MM:ss")
    val dateTime: LocalDateTime
)
