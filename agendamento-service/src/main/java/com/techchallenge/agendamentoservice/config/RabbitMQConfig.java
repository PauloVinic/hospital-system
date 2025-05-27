package com.techchallenge.agendamentoservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "consulta.exchange";
    public static final String QUEUE_NAME = "consulta.queue";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue consultaQueue() {
        return new Queue(QUEUE_NAME, true); // durable
    }

    @Bean
    public Binding bindingCreate(Queue consultaQueue, DirectExchange exchange) {
        return BindingBuilder.bind(consultaQueue).to(exchange).with("consulta.create");
    }

    @Bean
    public Binding bindingUpdate(Queue consultaQueue, DirectExchange exchange) {
        return BindingBuilder.bind(consultaQueue).to(exchange).with("consulta.update");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
