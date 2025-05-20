package com.techchallenge.historicoservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consulta {
    private Long id;
    private Long pacienteId;
    private Long medicoId;
    private String dataHora;
    private String observacoes;
}
