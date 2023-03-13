package com.ph.mdc.messaging.config

import com.ph.mdc.messaging.model.MessagingProperties
import org.springframework.amqp.core.AnonymousQueue
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.TopicExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMqConfig(
    private val messagingProperties: MessagingProperties
) {

    @Bean
    fun exchange(): TopicExchange {
        return TopicExchange(messagingProperties.name)
    }

    @Bean
    fun addsQueue(): AnonymousQueue {
        return AnonymousQueue()
    }

    @Bean
    fun deletesQueue(): AnonymousQueue {
        return AnonymousQueue()
    }

    @Bean
    fun quotesQueue(): AnonymousQueue {
        return AnonymousQueue()
    }

    @Bean
    fun quoteCapturedBusQueue(): AnonymousQueue {
        return AnonymousQueue()
    }

    @Bean
    fun quoteCaptureAlertQueue(): AnonymousQueue {
        return AnonymousQueue()
    }


    @Bean
    fun bindingAdds(exchange: TopicExchange, addsQueue: AnonymousQueue): Binding {
        return BindingBuilder.bind(addsQueue)
            .to(exchange)
            .with(messagingProperties.addIsinTopic)
    }

    @Bean
    fun bindingDeletes(exchange: TopicExchange, deletesQueue: AnonymousQueue): Binding {
        return BindingBuilder.bind(deletesQueue)
            .to(exchange)
            .with(messagingProperties.deleteIsinTopic)
    }

    @Bean
    fun bindingQuotes(exchange: TopicExchange, quotesQueue: AnonymousQueue): Binding {
        return BindingBuilder.bind(quotesQueue)
            .to(exchange)
            .with(messagingProperties.quoteTopic)
    }

    @Bean
    fun bindingQuoteCapturedBus(exchange: TopicExchange, quoteCapturedBusQueue: AnonymousQueue): Binding {
        return BindingBuilder.bind(quoteCapturedBusQueue)
            .to(exchange)
            .with(messagingProperties.quoteCapturedTopic)
    }

    @Bean
    fun bindingQuoteCapturedAlert(exchange: TopicExchange, quoteCaptureAlertQueue: AnonymousQueue): Binding {
        return BindingBuilder.bind(quoteCaptureAlertQueue)
            .to(exchange)
            .with(messagingProperties.quoteCapturedTopic)
    }
}
