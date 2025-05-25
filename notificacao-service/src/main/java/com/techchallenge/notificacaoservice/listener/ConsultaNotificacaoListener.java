package com.techchallenge.notificacaoservice.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.techchallenge.notificacaoservice.dto.NotificacaoConsultaDTO;

@Component
public class ConsultaNotificacaoListener {

    @RabbitListener(queues = "consulta.criada")
    public void aoReceberConsultaCriada(NotificacaoConsultaDTO dto) {
        System.out.println("📨 Consulta criada:");
        imprimirDados(dto);
    }

    @RabbitListener(queues = "consulta.editada")
    public void aoReceberConsultaEditada(NotificacaoConsultaDTO dto) {
        System.out.println("✏️ Consulta editada:");
        imprimirDados(dto);
    }

    private void imprimirDados(NotificacaoConsultaDTO dto) {
        System.out.println("Consulta ID: " + dto.getIdConsulta());
        System.out.println("Paciente ID: " + dto.getPacienteId());
        System.out.println("Email: " + dto.getEmailPaciente());
        System.out.println("Data e Hora: " + dto.getDataHora());
    }
}
