package com.techchallenge.agendamentoservice.dto;

import com.techchallenge.agendamentoservice.domain.Consulta;

import java.time.LocalDateTime;

public record ConsultaResponseDTO(
    Long id,
    Long pacienteId,
    Long medicoId,
    LocalDateTime dataHora,
    String observacoes
) {
    public ConsultaResponseDTO(Consulta consulta) {
        this(
            consulta.getId(),
            consulta.getPacienteId(),
            consulta.getMedicoId(),
            consulta.getDataHora(),
            consulta.getObservacoes()
        );
    }
}
