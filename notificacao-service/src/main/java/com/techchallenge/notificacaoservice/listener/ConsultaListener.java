package com.techchallenge.notificacaoservice.listener;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techchallenge.notificacaoservice.dto.NotificacaoConsultaDTO;
import com.techchallenge.notificacaoservice.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = "consulta.queue", errorHandler = "rabbitMQErrorHandler")
public class ConsultaListener {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public ConsultaListener(NotificationService notificationService) {
        this.notificationService = notificationService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // Suporte a LocalDateTime
    }

    @RabbitListener(queues = "consulta.queue")
    public void receberMensagem(String payload,
                                 @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        log.info("[RabbitMQ] Mensagem recebida. RoutingKey: {}", routingKey);
        log.debug("[RabbitMQ] Payload recebido: {}", payload);

        try {
            NotificacaoConsultaDTO dto = objectMapper.readValue(payload, NotificacaoConsultaDTO.class);
            log.info("[RabbitMQ] Payload desserializado com sucesso para NotificacaoConsultaDTO.");
            notificationService.processarConsulta(dto, routingKey);
        } catch (IOException e) {
            log.error("[RabbitMQ] Falha ao desserializar JSON. Payload: {}", payload, e);
        }
    }
}
