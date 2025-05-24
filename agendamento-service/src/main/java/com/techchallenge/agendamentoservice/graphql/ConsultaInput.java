package com.techchallenge.agendamentoservice.graphql;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.techchallenge.agendamentoservice.exception.BusinessException;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Representa uma entrada de consulta para o GraphQL.
 * Validações são automaticamente aplicadas pelo Spring GraphQL.
 */
public record ConsultaInput(
    @NotNull(message = "ID do paciente é obrigatório")
    @Positive(message = "ID do paciente deve ser positivo")
    Long pacienteId,

    @NotNull(message = "ID do médico é obrigatório")
    @Positive(message = "ID do médico deve ser positivo")
    Long medicoId,

    @NotNull(message = "Data/hora é obrigatória")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", 
             message = "Formato inválido. Use ISO-8601 (ex: 2025-05-24T14:30:00)")
    String dataHora,

    @Size(max = 500, message = "Observações não pode exceder 500 caracteres")
    String observacoes
) {
    /**
     * Converte a string dataHora para LocalDateTime.
     * @throws BusinessException se o formato for inválido
     */
    public LocalDateTime getDataHoraAsLocalDateTime() {
        try {
            return LocalDateTime.parse(dataHora, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new BusinessException("Formato de data/hora inválido. Use ISO-8601 (ex: 2025-05-24T14:30:00)");
        }
    }
}