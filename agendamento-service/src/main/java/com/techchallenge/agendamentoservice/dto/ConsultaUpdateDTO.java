package com.techchallenge.agendamentoservice.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para atualização da data e observações de uma consulta existente")
public record ConsultaUpdateDTO(

    @Schema(description = "Nova data e hora da consulta (deve estar no futuro)", example = "2025-05-30T14:00:00")
    @NotNull(message = "Data e hora da consulta são obrigatórias")
    @Future(message = "A data da consulta deve estar no futuro")
    LocalDateTime dataHora,

    @Schema(description = "Novas observações sobre a consulta (opcional)", example = "Horário alterado a pedido do paciente")
    String observacoes

) {}
