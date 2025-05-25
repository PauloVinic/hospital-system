package com.techchallenge.historicoservice.dto;

import java.time.LocalDateTime;

public record ConsultaHistoricoDTO(
    Long id,
    Long pacienteId,
    Long medicoId,
    LocalDateTime dataHora,
    String observacoes
) {}
