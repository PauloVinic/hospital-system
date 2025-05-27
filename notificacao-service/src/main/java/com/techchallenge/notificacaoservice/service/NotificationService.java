package com.techchallenge.notificacaoservice.service;

import org.springframework.stereotype.Service;

import com.techchallenge.notificacaoservice.dto.NotificacaoConsultaDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {

    public void processarConsulta(NotificacaoConsultaDTO dto, String routingKey) {
        log.info("[Notificação] Iniciando processamento da consulta. RoutingKey: {}", routingKey);

        switch (routingKey) {
            case "consulta.created", "consulta.create" -> log.info("[Notificação] Tipo de evento: criação de consulta.");
            case "consulta.updated", "consulta.update" -> log.info("[Notificação] Tipo de evento: atualização de consulta.");
            default -> {
                log.warn("[Notificação] Tipo de evento desconhecido: {}. Ignorando processamento.", routingKey);
                return;
            }
        }

        log.info("[Notificação] Detalhes da consulta:");
        log.info(" • ID da Consulta: {}", dto.getIdConsulta());
        log.info(" • ID do Paciente: {}", dto.getPacienteId());
        log.info(" • Email do Paciente: {}", dto.getEmailPaciente());
        log.info(" • Data e Hora: {}", dto.getDataHora());

        log.info("[Notificação] Simulando envio de notificação ao paciente.");
    }
}
