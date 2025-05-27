package com.techchallenge.notificacaoservice.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsultaListener {

    private final ConsultaMessageProcessor processor;

    @RabbitListener(queues = "consulta.queue")
    public void receberMensagem(String payload,
                                 @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        log.info("[RabbitMQ] Mensagem recebida. RoutingKey: {}", routingKey);
        log.debug("[RabbitMQ] Payload recebido: {}", payload);
        processor.process(payload, routingKey);
    }
}
