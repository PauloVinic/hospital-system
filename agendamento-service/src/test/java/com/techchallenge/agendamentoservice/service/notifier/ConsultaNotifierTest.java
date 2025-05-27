package com.techchallenge.agendamentoservice.service.notifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.techchallenge.agendamentoservice.dto.NotificacaoConsultaDTO;

class ConsultaNotifierTest {

    private RabbitTemplate rabbitTemplate;
    private ConsultaNotifier notifier;

    @BeforeEach
    void setup() {
        rabbitTemplate = mock(RabbitTemplate.class);
        notifier = new ConsultaNotifier(rabbitTemplate);
    }

    @Test
    void deveEnviarMensagemParaExchangeComRoutingKey() {
        NotificacaoConsultaDTO dto = new NotificacaoConsultaDTO(1L, 1L, "teste@email.com", java.time.LocalDateTime.now());

        notifier.enviarEvento("consulta.create", dto);

        verify(rabbitTemplate).convertAndSend(eq("consulta.exchange"), eq("consulta.create"), any(Object.class));
    }
}
