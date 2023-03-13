package com.ph.mdc.application.instrument.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.ph.mdc.application.instrument.model.Instrument
import com.ph.mdc.application.instrument.model.InstrumentCandle
import com.ph.mdc.application.instrument.model.Quote
import com.ph.mdc.application.instrument.repository.InstrumentRepository
import com.ph.mdc.messaging.model.MessagingProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.LocalDateTime

@Service
class InstrumentService(
    private val instrumentRepository: InstrumentRepository,
    private val candleStickService: CandleStickService,
    private val rabbitTemplate: RabbitTemplate,
    private val messagingProperties: MessagingProperties,
    private val objectMapper: ObjectMapper,
) {

    fun save(instrument: Instrument): Mono<Instrument> {
        return instrumentRepository.findByIsin(instrument.isin)
            .switchIfEmpty {
                Mono.defer { instrumentRepository.save(instrument) }
            }
    }

    fun delete(isin: String): Mono<Void> = instrumentRepository.deleteByIsin(isin)

    fun updatePrice(quote: Quote): Mono<Instrument> {
        return instrumentRepository.findByIsin(quote.isin)
            .map {
                it.addPrice(quote.price, quote.updateTime)
            }
            .flatMap {
                instrumentRepository.save(it)
            }
            .doOnSuccess {
                val quoteString = objectMapper.writeValueAsString(quote)
                rabbitTemplate.convertAndSend(
                    messagingProperties.name,
                    messagingProperties.quoteCapturedTopic,
                    quoteString
                )
            }
    }

    fun getLatestInstrumentsPrice(): Flux<Instrument> {
        return instrumentRepository.findAll()
            .map { instrument ->
                instrument.latestPrice()?.let { price ->
                    instrument.copy(
                        prices = mutableListOf(price)
                    )
                } ?: instrument
            }
    }

    fun getCandleInfo(isin: String): Mono<InstrumentCandle> {
        return instrumentRepository.findByIsin(isin)
            .map {
                InstrumentCandle(
                    isin = it.isin,
                    description = it.description,
                    priceAggregation = candleStickService.getAggregatedPrices(it.prices, LocalDateTime.now())
                )
            }
            .switchIfEmpty { throw RuntimeException("Instrument not found with isin $isin") }
    }
}
