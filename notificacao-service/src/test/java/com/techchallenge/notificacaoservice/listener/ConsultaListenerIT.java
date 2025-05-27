package com.techchallenge.notificacaoservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techchallenge.notificacaoservice.dto.NotificacaoConsultaDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class ConsultaListenerIT {

    @Autowired
    private ConsultaListener consultaListener;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void deveProcessarMensagemJsonValida() throws Exception {
        NotificacaoConsultaDTO dto = new NotificacaoConsultaDTO(1L, 2L, "teste@email.com", LocalDateTime.now());
        String payload = objectMapper.writeValueAsString(dto);

        consultaListener.receberMensagem(payload, "consulta.created");
    }

    @Test
    void deveTratarMensagemJsonInvalida() {
        String payloadInvalido = "{json: invalido}";
        consultaListener.receberMensagem(payloadInvalido, "consulta.created");
    }
}
