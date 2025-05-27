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
public class ConsultaListener {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public ConsultaListener(NotificationService notificationService) {
        this.notificationService = notificationService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // Habilita suporte ao LocalDateTime
    }

    @RabbitListener(queues = "consulta.queue")
    public void receberMensagem(String payload,
                                 @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        try {
            NotificacaoConsultaDTO dto = objectMapper.readValue(payload, NotificacaoConsultaDTO.class);
            notificationService.processarConsulta(dto, routingKey);
        } catch (IOException e) {
            log.error("Erro ao desserializar a mensagem JSON recebida do RabbitMQ", e);
        }
    }
}
