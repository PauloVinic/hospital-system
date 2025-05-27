package com.techchallenge.notificacaoservice.listener;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techchallenge.notificacaoservice.dto.NotificacaoConsultaDTO;
import com.techchallenge.notificacaoservice.service.NotificationService;

class ConsultaMessageProcessorTest {

    private NotificationService mockService;
    private ObjectMapper mapper;
    private ConsultaMessageProcessor processor;

    @BeforeEach
    void setUp() {
        mockService = mock(NotificationService.class);
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        processor = new ConsultaMessageProcessor(mapper, mockService);
    }

    @Test
    void deveProcessarMensagemValida() throws Exception {
        NotificacaoConsultaDTO dto = new NotificacaoConsultaDTO(1L, 2L, "email@email.com", LocalDateTime.now());
        String json = mapper.writeValueAsString(dto);

        processor.process(json, "consulta.created");

        verify(mockService).processarConsulta(any(), eq("consulta.created"));
    }

    @Test
    void deveTratarMensagemInvalida() {
        processor.process("{invalido}", "consulta.created");

        verifyNoInteractions(mockService);
    }
}
