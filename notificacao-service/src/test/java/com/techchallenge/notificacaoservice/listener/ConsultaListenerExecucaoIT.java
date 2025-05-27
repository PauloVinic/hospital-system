package com.techchallenge.notificacaoservice.listener;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techchallenge.notificacaoservice.dto.NotificacaoConsultaDTO;

@SpringBootTest
class ConsultaListenerExecucaoIT {

    @Autowired
    private ConsultaListener listener;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void deveExecutarMetodoComJsonValido() throws Exception {
        NotificacaoConsultaDTO dto = new NotificacaoConsultaDTO(99L, 88L, "teste@email.com", LocalDateTime.now());
        String payload = mapper.writeValueAsString(dto);

        // Executa diretamente o método como se o listener tivesse recebido
        listener.receberMensagem(payload, "consulta.created");
    }

    @Test
    void deveExecutarMetodoComJsonInvalido() {
        // JSON inválido para forçar o caminho de exceção
        String payloadInvalido = "{json: invalido}";
        listener.receberMensagem(payloadInvalido, "consulta.created");
    }
}
