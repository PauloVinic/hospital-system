package com.techchallenge.notificacaoservice.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

import static org.assertj.core.api.Assertions.assertThat;

public class RabbitMQConfigTest {
/*/
    private final RabbitMQConfig config = new RabbitMQConfig();

    @Test
    void deveCriarExchange() {
        DirectExchange exchange = config.exchange();
        assertThat(exchange).isNotNull();
        assertThat(exchange.getName()).isEqualTo("consulta.exchange");
    }

    @Test
    void deveCriarQueue() {
        Queue queue = config.consultaCreatedQueue();
        assertThat(queue).isNotNull();
        assertThat(queue.getName()).isEqualTo("consulta.queue");
    }

    @Test
    void deveCriarBindingCreated() {
        Queue queue = config.consultaCreatedQueue();
        DirectExchange exchange = config.exchange();
        Binding binding = config.bindingCreated(queue, exchange);

        assertThat(binding).isNotNull();
        assertThat(binding.getRoutingKey()).isEqualTo("consulta.created");
    }

    @Test
    void deveCriarBindingUpdated() {
        Queue queue = config.consultaCreatedQueue();
        DirectExchange exchange = config.exchange();
        Binding binding = config.bindingUpdated(queue, exchange);

        assertThat(binding).isNotNull();
        assertThat(binding.getRoutingKey()).isEqualTo("consulta.updated");
    }
        */
}
