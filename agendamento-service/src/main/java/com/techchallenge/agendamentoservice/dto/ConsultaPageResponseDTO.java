package com.techchallenge.agendamentoservice.dto;

import java.util.List;

public record ConsultaPageResponseDTO(
    List<ConsultaResponseDTO> content,
    int page,
    int size,
    long totalElements
) {}
