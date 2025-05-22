package com.techchallenge.agendamentoservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ConsultaRequestDTO(

    @NotNull(message = "{consulta.paciente.obrigatorio}")
    Long pacienteId,

    @NotNull(message = "{consulta.medico.obrigatorio}")
    Long medicoId,

    @NotNull(message = "{consulta.datahora.obrigatoria}")
    @Future(message = "{consulta.datahora.futuro}")
    LocalDateTime dataHora,

    String observacoes
    

) {}
