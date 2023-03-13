package com.ph.mdc.messaging.model

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "messaging.exchange")
class MessagingProperties {
    var name: String = ""
    var addIsinTopic = ""
    var deleteIsinTopic = ""
    var quoteTopic = ""
    var quoteCapturedTopic = ""
    var alertFilledTopic = ""
}
