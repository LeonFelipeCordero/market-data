package com.ph.mdc.application.instrument.service

import com.ph.mdc.application.instrument.model.Price
import com.ph.mdc.application.instrument.model.PriceAggregation
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.temporal.ChronoField

@Service
class CandleStickService {

    companion object {
        private const val defaultCandleGraphTime = 30L
    }

    fun getAggregatedPrices(prices: MutableList<Price>, now: LocalDateTime): List<PriceAggregation> {
        return collectAggregationByMinute(prices, now)
            .map { buildPriceAggregation(it.value, now) }
    }

    private fun collectAggregationByMinute(
        prices: MutableList<Price>, now: LocalDateTime
    ): Map<Int, List<Price>> {
        return prices.filter { now.minusMinutes(defaultCandleGraphTime) < it.dateTime }
            .groupBy { it.dateTime.get(ChronoField.MINUTE_OF_HOUR) }
    }

    private fun buildPriceAggregation(prices: List<Price>, now: LocalDateTime): PriceAggregation {
        return PriceAggregation(
            openPosition = getOpenPosition(prices),
            closePosition = if (isPositionClosed(now, prices[0].dateTime.minute)) getClosePosition(prices) else null,
            higherPrice = getHigherPrice(prices),
            lowerPrice = getLowerPrice(prices)
        )
    }

    private fun getOpenPosition(prices: List<Price>): Price {
        return prices.minByOrNull { it.dateTime }
            ?: throw RuntimeException("Something went wrong trying to get an open position")
    }

    private fun isPositionClosed(now: LocalDateTime, aggregatedPries: Int): Boolean {
        return now.minute != aggregatedPries
    }

    private fun getClosePosition(prices: List<Price>): Price {
        return prices.maxByOrNull { it.dateTime }
            ?: throw RuntimeException("Something went wrong trying to get a close position")
    }

    private fun getHigherPrice(prices: List<Price>): BigDecimal {
        return prices
            .maxByOrNull { it.price }?.price
            ?: throw RuntimeException("Something went wrong tying to get higher price")
    }

    private fun getLowerPrice(prices: List<Price>): BigDecimal {
        return prices
            .minByOrNull { it.price }?.price
            ?: throw RuntimeException("Something went wrong trying to get lower price")
    }
}
