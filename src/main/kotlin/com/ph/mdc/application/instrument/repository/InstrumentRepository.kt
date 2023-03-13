package com.ph.mdc.application.instrument.repository

import com.ph.mdc.application.instrument.model.Instrument
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface InstrumentRepository : ReactiveMongoRepository<Instrument, String> {

    fun findByIsin(isin: String): Mono<Instrument>

    fun deleteByIsin(ising: String): Mono<Void>
}
