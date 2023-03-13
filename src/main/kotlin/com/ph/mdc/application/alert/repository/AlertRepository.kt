package com.ph.mdc.application.alert.repository

import com.ph.mdc.application.alert.model.Alert
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface AlertRepository : ReactiveMongoRepository<Alert, String> {
    fun findByIsin(isin: String): Flux<Alert>
}