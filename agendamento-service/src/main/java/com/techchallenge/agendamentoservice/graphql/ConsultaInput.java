package com.techchallenge.agendamentoservice.graphql;

public record ConsultaInput(
    Long pacienteId,
    Long medicoId,
    String dataHora,
    String observacoes
) {}
