package com.techchallenge.agendamentoservice.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "DTO para solicitação de criação de uma nova consulta")
public record ConsultaRequestDTO(

    @Schema(description = "ID do paciente que será atendido", example = "1")
    @NotNull(message = "{consulta.paciente.obrigatorio}")
    @Positive(message = "ID do paciente deve ser positivo")
    Long pacienteId,

    @Schema(description = "ID do médico responsável pela consulta", example = "1")
    @NotNull(message = "{consulta.medico.obrigatorio}")
    @Positive(message = "ID do médico deve ser positivo")
    Long medicoId,

    @Schema(description = "Data e hora da consulta (deve estar no futuro)", example = "2025-05-25T10:00:00")
    @NotNull(message = "{consulta.datahora.obrigatoria}")
    @Future(message = "{consulta.datahora.futuro}")
    LocalDateTime dataHora,

    @Schema(description = "Observações adicionais da consulta (opcional)", example = "Consulta de rotina")
    String observacoes

) {}
