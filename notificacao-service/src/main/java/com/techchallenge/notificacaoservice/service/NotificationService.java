package com.techchallenge.notificacaoservice.service;

import org.springframework.stereotype.Service;

import com.techchallenge.notificacaoservice.dto.NotificacaoConsultaDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {

    public void processarConsulta(NotificacaoConsultaDTO dto, String routingKey) {
        switch (routingKey) {
            case "consulta.created", "consulta.create" -> log.info("Evento recebido: consulta criada.");
            case "consulta.updated", "consulta.update" -> log.info("Evento recebido: consulta atualizada.");
            default -> {
                log.warn("Evento não reconhecido: {}", routingKey);
                return;
            }
        }

        log.info("Dados da consulta recebida:");
        log.info(" - Consulta ID: {}", dto.getIdConsulta());
        log.info(" - Paciente ID: {}", dto.getPacienteId());
        log.info(" - Email do paciente: {}", dto.getEmailPaciente());
        log.info(" - Data e hora da consulta: {}", dto.getDataHora());
        log.info("Simulando envio de notificação ao paciente.");
    }
}
