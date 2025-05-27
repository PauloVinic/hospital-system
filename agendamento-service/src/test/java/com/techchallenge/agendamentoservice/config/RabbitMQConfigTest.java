package com.techchallenge.agendamentoservice.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

class RabbitMQConfigTest {

    private final RabbitMQConfig config = new RabbitMQConfig();

    @Test
    void deveCriarExchangeComNomeCorreto() {
        DirectExchange exchange = config.exchange();
        assertThat(exchange.getName()).isEqualTo("consulta.exchange");
    }

    @Test
    void deveCriarFilaComNomeCorreto() {
        Queue queue = config.consultaQueue();
        assertThat(queue.getName()).isEqualTo("consulta.queue");
        assertThat(queue.isDurable()).isTrue();
    }

    @Test
    void deveCriarBindingsParaCreateEUpdate() {
        Queue queue = config.consultaQueue();
        DirectExchange exchange = config.exchange();

        Binding bindingCreate = config.bindingCreate(queue, exchange);
        Binding bindingUpdate = config.bindingUpdate(queue, exchange);

        assertThat(bindingCreate.getRoutingKey()).isEqualTo("consulta.create");
        assertThat(bindingUpdate.getRoutingKey()).isEqualTo("consulta.update");
    }
}
