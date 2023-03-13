package com.ph.mdc.api

import com.ph.mdc.application.alert.model.Alert
import com.ph.mdc.application.alert.service.AlertService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux


@RestController
class AlertsController(
    private val alertService: AlertService
) {

    @GetMapping("/alerts/new")
    fun createAlerts(): Flux<Alert> {
        return alertService.createAlert()
    }
}