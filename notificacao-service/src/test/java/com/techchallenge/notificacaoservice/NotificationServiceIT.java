package com.techchallenge.notificacaoservice;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.techchallenge.notificacaoservice.dto.NotificacaoConsultaDTO;
import com.techchallenge.notificacaoservice.service.NotificationService;

@SpringBootTest
class NotificationServiceIT {

    @Autowired
    private NotificationService service;

    @Test
    void deveProcessarEventoDeCriacao() {
        NotificacaoConsultaDTO dto = new NotificacaoConsultaDTO(1L, 2L, "email@teste.com", LocalDateTime.now());
        service.processarConsulta(dto, "consulta.created");
    }

    @Test
    void deveProcessarEventoDeAtualizacao() {
        NotificacaoConsultaDTO dto = new NotificacaoConsultaDTO(1L, 2L, "email@teste.com", LocalDateTime.now());
        service.processarConsulta(dto, "consulta.updated");
    }

    @Test
    void deveIgnorarEventoDesconhecido() {
        NotificacaoConsultaDTO dto = new NotificacaoConsultaDTO(1L, 2L, "email@teste.com", LocalDateTime.now());
        service.processarConsulta(dto, "evento.desconhecido");
    }
}
