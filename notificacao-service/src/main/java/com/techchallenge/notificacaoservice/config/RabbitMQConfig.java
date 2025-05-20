package com.techchallenge.notificacaoservice.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private static final String EXCHANGE_NAME = "consultas-exchange";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue consultaCriadaQueue() {
        return new Queue("consulta.criada");
    }

    @Bean
    public Queue consultaEditadaQueue() {
        return new Queue("consulta.editada");
    }

    @Bean
    public Binding bindingCriada(Queue consultaCriadaQueue, DirectExchange exchange) {
        return BindingBuilder.bind(consultaCriadaQueue).to(exchange).with("consulta.criada");
    }

    @Bean
    public Binding bindingEditada(Queue consultaEditadaQueue, DirectExchange exchange) {
        return BindingBuilder.bind(consultaEditadaQueue).to(exchange).with("consulta.editada");
    }
}
