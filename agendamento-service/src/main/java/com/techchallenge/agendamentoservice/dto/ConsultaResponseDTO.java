package com.techchallenge.agendamentoservice.dto;

import java.time.LocalDateTime;

import com.techchallenge.agendamentoservice.domain.Consulta;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta contendo os dados da consulta agendada")
public record ConsultaResponseDTO(

    @Schema(description = "ID da consulta", example = "1")
    Long id,

    @Schema(description = "ID do paciente que será atendido", example = "1")
    Long pacienteId,

    @Schema(description = "ID do médico responsável pela consulta", example = "1")
    Long medicoId,

    @Schema(description = "Data e hora agendada para a consulta", example = "2025-05-25T10:00:00")
    LocalDateTime dataHora,

    @Schema(description = "Observações sobre a consulta", example = "Consulta de rotina")
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
