package com.techchallenge.notificacaoservice.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConsultaListener {

    @RabbitListener(queues = "consulta.queue")
    public void receberMensagem(String mensagem, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        log.info("📬 Notificação recebida via routingKey [{}]: {}", routingKey, mensagem);
        log.info("📢 Enviando lembrete ao paciente...");
    }
}
