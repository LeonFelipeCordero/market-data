package com.ph.mdc.application.alert.model

import com.ph.mdc.application.instrument.model.Direction
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Document("alert")
data class Alert(
    @Id val id: String? = null,
    val isin: String,
    val price: BigDecimal,
    val direction: Direction
)
