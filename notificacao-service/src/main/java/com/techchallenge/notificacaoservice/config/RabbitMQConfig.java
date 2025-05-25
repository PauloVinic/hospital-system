package com.techchallenge.notificacaoservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private static final String EXCHANGE_NAME = "consulta.exchange";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue consultaCreatedQueue() {
        return new Queue("consulta.queue", true); // mesma fila usada pelo produtor
    }

    @Bean
    public Binding bindingCreated(Queue consultaCreatedQueue, DirectExchange exchange) {
        return BindingBuilder.bind(consultaCreatedQueue).to(exchange).with("consulta.created");
    }

    @Bean
    public Binding bindingUpdated(Queue consultaCreatedQueue, DirectExchange exchange) {
        return BindingBuilder.bind(consultaCreatedQueue).to(exchange).with("consulta.updated");
    }
}
