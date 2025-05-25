package com.techchallenge.agendamentoservice.service.validator;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techchallenge.agendamentoservice.dto.ConsultaRequestDTO;
import com.techchallenge.agendamentoservice.exception.BusinessException;
import com.techchallenge.agendamentoservice.repository.ConsultaRepository;
import com.techchallenge.agendamentoservice.repository.MedicoRepository;
import com.techchallenge.agendamentoservice.repository.PacienteRepository;

class ConsultaValidatorTest {

    private MedicoRepository medicoRepository;
    private PacienteRepository pacienteRepository;
    private ConsultaRepository consultaRepository;
    private ConsultaValidator validator;

    @BeforeEach
    void setup() {
        medicoRepository = mock(MedicoRepository.class);
        pacienteRepository = mock(PacienteRepository.class);
        consultaRepository = mock(ConsultaRepository.class);
        validator = new ConsultaValidator(medicoRepository, pacienteRepository, consultaRepository);
    }

    @Test
    void deveValidarConsultaComSucesso() {
        var dto = new ConsultaRequestDTO(1L, 2L, LocalDateTime.now().plusDays(1), "Rotina");

        when(medicoRepository.existsById(2L)).thenReturn(true);
        when(pacienteRepository.existsById(1L)).thenReturn(true);
        when(consultaRepository.existsByMedicoIdAndDataHora(2L, dto.dataHora())).thenReturn(false);

        assertDoesNotThrow(() -> validator.validar(dto));
    }

    @Test
    void deveFalharQuandoMedicoNaoExiste() {
        var dto = new ConsultaRequestDTO(1L, 999L, LocalDateTime.now().plusDays(1), "Rotina");

        when(medicoRepository.existsById(999L)).thenReturn(false);

        BusinessException ex = assertThrows(BusinessException.class, () -> validator.validar(dto));
        assertEquals("Médico ID 999 não encontrado", ex.getMessage());
    }

    @Test
    void deveFalharQuandoPacienteNaoExiste() {
        var dto = new ConsultaRequestDTO(999L, 2L, LocalDateTime.now().plusDays(1), "Rotina");

        when(medicoRepository.existsById(2L)).thenReturn(true);
        when(pacienteRepository.existsById(999L)).thenReturn(false);

        BusinessException ex = assertThrows(BusinessException.class, () -> validator.validar(dto));
        assertEquals("Paciente ID 999 não encontrado", ex.getMessage());
    }

    @Test
    void deveFalharQuandoExisteConflitoDeHorario() {
        var dto = new ConsultaRequestDTO(1L, 2L, LocalDateTime.now().plusDays(1), "Rotina");

        when(medicoRepository.existsById(2L)).thenReturn(true);
        when(pacienteRepository.existsById(1L)).thenReturn(true);
        when(consultaRepository.existsByMedicoIdAndDataHora(2L, dto.dataHora())).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> validator.validar(dto));
        assertEquals("Médico já possui consulta agendada neste horário", ex.getMessage());
    }
}
