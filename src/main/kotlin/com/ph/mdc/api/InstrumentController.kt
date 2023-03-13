package com.ph.mdc.api

import com.ph.mdc.application.instrument.model.Instrument
import com.ph.mdc.application.instrument.model.InstrumentCandle
import com.ph.mdc.application.instrument.service.InstrumentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@RestController
class InstrumentController(
    private val instrumentService: InstrumentService,
) {

    @GetMapping("/instrument")
    fun getActiveInstruments(): Flux<Instrument> {
        return instrumentService.getLatestInstrumentsPrice()
    }

    @GetMapping("/instrument/{isin}")
    fun getInstrument(@PathVariable(value = "isin") isin: String): Mono<InstrumentCandle> {
        return instrumentService.getCandleInfo(isin)
    }
}