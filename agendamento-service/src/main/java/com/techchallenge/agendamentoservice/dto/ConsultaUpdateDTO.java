package com.techchallenge.agendamentoservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ConsultaUpdateDTO(

    @NotNull(message = "Data e hora da consulta são obrigatórias")
    @Future(message = "A data da consulta deve estar no futuro")
    LocalDateTime dataHora,

    String observacoes

) {}
