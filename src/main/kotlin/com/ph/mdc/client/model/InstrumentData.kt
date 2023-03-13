package com.ph.mdc.client.model

import com.ph.mdc.application.instrument.model.Instrument

data class InstrumentData(val isin: String, val description: String) {
    fun toInstrument(): Instrument {
        return Instrument(
            isin = isin,
            description = description,
            prices = mutableListOf()
        )
    }
}
