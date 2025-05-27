package com.techchallenge.notificacaoservice.listener;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.notificacaoservice.dto.NotificacaoConsultaDTO;
import com.techchallenge.notificacaoservice.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConsultaMessageProcessor {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public ConsultaMessageProcessor(ObjectMapper objectMapper, NotificationService notificationService) {
        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
    }

    public void process(String payload, String routingKey) {
        try {
            NotificacaoConsultaDTO dto = objectMapper.readValue(payload, NotificacaoConsultaDTO.class);
            notificationService.processarConsulta(dto, routingKey);
        } catch (IOException e) {
            log.error("Erro ao desserializar JSON", e);
        }
    }
}
