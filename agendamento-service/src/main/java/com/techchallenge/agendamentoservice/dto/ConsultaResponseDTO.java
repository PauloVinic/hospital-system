package com.techchallenge.agendamentoservice.dto;

import java.time.LocalDateTime;

public record ConsultaResponseDTO(
    Long id,
    Long pacienteId,
    Long medicoId,
    LocalDateTime dataHora,
    String observacoes
) {}
