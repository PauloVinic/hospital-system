package com.techchallenge.notificacaoservice.listener;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techchallenge.notificacaoservice.dto.NotificacaoConsultaDTO;

@SpringBootTest
class ConsultaListenerRoutingKeyIT {

    @Autowired
    private ConsultaListener listener;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("Deve processar consulta.created corretamente")
    void deveProcessarConsultaCreated() throws Exception {
        NotificacaoConsultaDTO dto = new NotificacaoConsultaDTO(10L, 20L, "created@email.com", LocalDateTime.now());
        String payload = mapper.writeValueAsString(dto);
        listener.receberMensagem(payload, "consulta.created");
    }

    @Test
    @DisplayName("Deve processar consulta.updated corretamente")
    void deveProcessarConsultaUpdated() throws Exception {
        NotificacaoConsultaDTO dto = new NotificacaoConsultaDTO(11L, 21L, "updated@email.com", LocalDateTime.now());
        String payload = mapper.writeValueAsString(dto);
        listener.receberMensagem(payload, "consulta.updated");
    }

    @Test
    @DisplayName("Deve ignorar evento com routingKey desconhecida")
    void deveIgnorarRoutingKeyDesconhecida() throws Exception {
        NotificacaoConsultaDTO dto = new NotificacaoConsultaDTO(12L, 22L, "desconhecido@email.com", LocalDateTime.now());
        String payload = mapper.writeValueAsString(dto);
        listener.receberMensagem(payload, "evento.naoReconhecido");
    }
}
