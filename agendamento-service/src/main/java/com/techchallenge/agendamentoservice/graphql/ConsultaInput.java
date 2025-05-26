package com.techchallenge.agendamentoservice.graphql;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.techchallenge.agendamentoservice.exception.BusinessException;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ConsultaInput(

    @NotNull(message = "ID do paciente é obrigatório")
    @Positive(message = "ID do paciente deve ser positivo")
    Long pacienteId,

    @NotNull(message = "ID do médico é obrigatório")
    @Positive(message = "ID do médico deve ser positivo")
    Long medicoId,

    @NotNull(message = "Data/hora da consulta é obrigatória")
    String dataHora, // formato ISO: yyyy-MM-dd'T'HH:mm:ss

    @Size(max = 500, message = "Observações não pode exceder 500 caracteres")
    String observacoes

) {

    /**
     * Converte a string para LocalDateTime e aplica validações de negócio:
     * - Data deve estar no futuro
     * - Consulta deve ser em intervalo de 15 minutos
     */
    public LocalDateTime getValidatedDataHora() {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(dataHora, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            if (dateTime.isBefore(LocalDateTime.now())) {
                throw new BusinessException("A data da consulta deve ser no futuro.");
            }

            if (dateTime.getMinute() % 15 != 0) {
                throw new BusinessException("Consultas devem ser agendadas em intervalos de 15 minutos.");
            }

            return dateTime;

        } catch (DateTimeParseException e) {
            throw new BusinessException("Formato de data inválido. Use: yyyy-MM-dd'T'HH:mm:ss (ex: 2025-05-24T14:30:00)");
        }
    }
}
