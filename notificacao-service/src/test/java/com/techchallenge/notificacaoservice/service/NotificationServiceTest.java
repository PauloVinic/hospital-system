package com.techchallenge.notificacaoservice.service;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.techchallenge.notificacaoservice.dto.NotificacaoConsultaDTO;

class NotificationServiceTest {

    private NotificationService service;
    private NotificacaoConsultaDTO dto;

    @BeforeEach
    void setUp() {
        service = new NotificationService();

        dto = new NotificacaoConsultaDTO();
        dto.setIdConsulta(1L);
        dto.setPacienteId(2L);
        dto.setEmailPaciente("paciente@email.com");
        dto.setDataHora(LocalDateTime.of(2025, 5, 30, 14, 0));
    }

    @Test
    void deveProcessarEventoCreated() {
        service.processarConsulta(dto, "consulta.created");
    }

    @Test
    void deveProcessarEventoUpdated() {
        service.processarConsulta(dto, "consulta.updated");
    }

    @Test
    void deveIgnorarEventoDesconhecido() {
        service.processarConsulta(dto, "evento.desconhecido");
    }
}
