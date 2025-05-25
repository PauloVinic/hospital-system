package com.techchallenge.agendamentoservice.service.notifier;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techchallenge.agendamentoservice.exception.BusinessException;

@Component
public class ConsultaNotifier {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public ConsultaNotifier(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    public void enviarEvento(String routingKey, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            rabbitTemplate.convertAndSend("consulta.exchange", routingKey, json);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Erro ao serializar mensagem para JSON");
        }
    }
}
