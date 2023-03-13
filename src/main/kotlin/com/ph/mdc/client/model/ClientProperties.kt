package com.ph.mdc.client.model

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "exchange")
class ClientProperties {
    var url: String = ""
    var instruments: String = ""
    var quotes: String = ""
}
