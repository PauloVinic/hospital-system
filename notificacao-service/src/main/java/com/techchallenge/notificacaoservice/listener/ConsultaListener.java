package com.techchallenge.notificacaoservice.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsultaListener {

    @RabbitListener(queues = {"consulta.criada", "consulta.editada"})
    public void receberMensagem(String mensagem) {
        log.info("📬 Notificação recebida: {}", mensagem);
        log.info("📢 Enviando lembrete ao paciente...");
    }
}
