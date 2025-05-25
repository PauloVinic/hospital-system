package com.techchallenge.notificacaoservice.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.techchallenge.notificacaoservice.dto.NotificacaoConsultaDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConsultaNotificacaoListener {

    @RabbitListener(queues = "consulta.queue")
    public void aoReceberMensagem(NotificacaoConsultaDTO dto,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {

        switch (routingKey) {
            case "consulta.created" -> log.info("📨 Consulta criada:");
            case "consulta.updated" -> log.info("✏️ Consulta atualizada:");
            default -> {
                log.warn("❓ Evento não reconhecido: {}", routingKey);
                return;
            }
        }

        imprimirDados(dto);
    }

    private void imprimirDados(NotificacaoConsultaDTO dto) {
        log.info("Consulta ID: {}", dto.getIdConsulta());
        log.info("Paciente ID: {}", dto.getPacienteId());
        log.info("Email: {}", dto.getEmailPaciente());
        log.info("Data e Hora: {}", dto.getDataHora());
    }
}
