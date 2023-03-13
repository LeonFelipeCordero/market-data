package com.ph.mdc.application.alert.service

import com.ph.mdc.application.alert.model.Alert
import com.ph.mdc.application.alert.repository.AlertRepository
import com.ph.mdc.application.instrument.model.Direction
import com.ph.mdc.application.instrument.repository.InstrumentRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.math.BigDecimal

@Service
class AlertService(
    private val alertRepository: AlertRepository,
    private val instrumentRepository: InstrumentRepository,
) {

    companion object {
        private val logger = LoggerFactory.getLogger(AlertService::class.java)
    }

    fun createAlert(): Flux<Alert> {
        return instrumentRepository
            .findAll()
            .filter {
                val instrumentPrice = it.latestPrice()
                instrumentPrice != null
            }
            .flatMap { instrument ->
                val instrumentPrice = instrument.latestPrice()!!
                Alert(
                    isin = instrument.isin,
                    price = instrumentPrice.price * BigDecimal(0.8),
                    direction = Direction.DOWN
                ).let {
                    alertRepository.save(it)
                }
            }
    }

//    fun validateAlert(quote: Quote) {
//        alertRepository.findByIsin(quote.isin)
//            .flatMap { alert ->
//                if (alert.direction == Direction.UP && quote.price >= alert.price) {
//                    alertRepository.save(alert.copy(direction = Direction.DOWN))
//                        .doOnSuccess {
//                            logger.info("alert ${alert.id} matched, ${Direction.UP}, ${alert.price}")
//                            alertEventEmitter.publish(alert)
//                        }
//                } else if (alert.direction == Direction.DOWN && quote.price <= alert.price) {
//                    alertRepository.save(alert.copy(direction = Direction.UP))
//                        .doOnSuccess {
//                            logger.info("alert ${alert.id} matched, ${Direction.DOWN}, ${alert.price}")
//                            alertEventEmitter.publish(alert)
//                        }
//                } else {
//                    Mono.just(alert)
//                }
//            }.subscribe()
//    }
}