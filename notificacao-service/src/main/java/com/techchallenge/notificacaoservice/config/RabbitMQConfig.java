package com.techchallenge.notificacaoservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("consulta.exchange");
    }

    @Bean
    public Queue consultaQueue() {
        return new Queue("consulta.queue", true);
    }

    @Bean
    public Binding bindingCreated(Queue consultaQueue, DirectExchange exchange) {
        return BindingBuilder.bind(consultaQueue).to(exchange).with("consulta.created");
    }

    @Bean
    public Binding bindingUpdated(Queue consultaQueue, DirectExchange exchange) {
        return BindingBuilder.bind(consultaQueue).to(exchange).with("consulta.updated");
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }
}
