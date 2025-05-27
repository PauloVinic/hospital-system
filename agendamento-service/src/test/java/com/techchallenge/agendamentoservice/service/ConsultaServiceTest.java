package com.techchallenge.agendamentoservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.techchallenge.agendamentoservice.domain.Consulta;
import com.techchallenge.agendamentoservice.domain.Paciente;
import com.techchallenge.agendamentoservice.dto.ConsultaRequestDTO;
import com.techchallenge.agendamentoservice.dto.ConsultaUpdateDTO;
import com.techchallenge.agendamentoservice.exception.BusinessException;
import com.techchallenge.agendamentoservice.repository.ConsultaRepository;
import com.techchallenge.agendamentoservice.repository.PacienteRepository;
import com.techchallenge.agendamentoservice.service.notifier.ConsultaNotifier;
import com.techchallenge.agendamentoservice.service.validator.ConsultaValidator;

class ConsultaServiceTest {

    private ConsultaService service;
    private ConsultaRepository consultaRepository;
    private PacienteRepository pacienteRepository;
    private ConsultaNotifier notifier;
    private ConsultaValidator validator;

    @BeforeEach
    void setup() {
        consultaRepository = mock(ConsultaRepository.class);
        pacienteRepository = mock(PacienteRepository.class);
        notifier = mock(ConsultaNotifier.class);
        validator = mock(ConsultaValidator.class);
        service = new ConsultaService(consultaRepository, pacienteRepository, notifier, validator);
    }

    @Test
    void deveCriarConsultaComDTO() {
        ConsultaRequestDTO dto = new ConsultaRequestDTO(1L, 2L, LocalDateTime.of(2025, 6, 1, 14, 0), "Rotina");
        Consulta consultaSalva = Consulta.builder()
                .id(10L)
                .pacienteId(1L)
                .medicoId(2L)
                .dataHora(dto.dataHora())
                .observacoes("Rotina")
                .build();

        Paciente paciente = new Paciente();
        paciente.setEmail("paulo@email.com");

        when(consultaRepository.save(any(Consulta.class))).thenReturn(consultaSalva);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);

        Consulta result = service.criarConsultaComDTO(dto);

        assertEquals(10L, result.getId());
        verify(validator).validar(dto);
        verify(notifier).enviarEvento(eq("consulta.create"), captor.capture());

        Object enviado = captor.getValue();
        assertThat(enviado).isInstanceOf(com.techchallenge.agendamentoservice.dto.NotificacaoConsultaDTO.class);
    }

    @Test
    void deveEditarConsultaComDTO() {
        Long id = 1L;
        Consulta existente = Consulta.builder()
                .id(id)
                .pacienteId(1L)
                .dataHora(LocalDateTime.now().plusDays(1))
                .observacoes("Antiga")
                .build();

        ConsultaUpdateDTO dto = new ConsultaUpdateDTO(LocalDateTime.now().plusDays(2), "Atualizada");
        Paciente paciente = new Paciente();
        paciente.setEmail("teste@email.com");

        when(consultaRepository.findById(id)).thenReturn(Optional.of(existente));
        when(consultaRepository.save(any())).thenReturn(existente);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        Consulta result = service.editarConsultaComDTO(id, dto);

        assertEquals("Atualizada", result.getObservacoes());
        verify(notifier).enviarEvento(eq("consulta.update"), any());
    }

    @Test
    void deveLancarExcecaoQuandoEditarConsultaInexistente() {
        when(consultaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> service.editarConsultaComDTO(999L, new ConsultaUpdateDTO(LocalDateTime.now().plusDays(1), "x")));
    }

    @Test
    void deveListarConsultasPaginadas() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("dataHora").ascending());
        Consulta consulta = Consulta.builder()
                .id(1L)
                .pacienteId(1L)
                .medicoId(1L)
                .dataHora(LocalDateTime.now().plusDays(1))
                .observacoes("x")
                .build();

        when(consultaRepository.findAllValid(pageable))
                .thenReturn(new PageImpl<>(List.of(consulta), pageable, 1));

        var resultado = service.listarConsultasPaginadas(pageable);

        assertEquals(1, resultado.content().size());
        assertEquals(1L, resultado.content().get(0).id());
    }

    @Test
    void deveRetornarTodasAsConsultas() {
        Consulta consulta = new Consulta(1L, 1L, 2L, LocalDateTime.now(), "Rotina");
        when(consultaRepository.findAll()).thenReturn(List.of(consulta));

        List<Consulta> resultado = service.listarTodasConsultas();

        assertEquals(1, resultado.size());
        assertEquals(consulta, resultado.get(0));
    }

    @Test
    void deveLancarExcecaoAoCriarConsultaComConflito() {
        ConsultaRequestDTO dto = new ConsultaRequestDTO(1L, 2L, LocalDateTime.of(2025, 6, 1, 14, 0), "Rotina");

        doThrow(new BusinessException("Horário indisponível")).when(validator).validar(dto);

        assertThrows(BusinessException.class, () -> service.criarConsultaComDTO(dto));
    }

    @Test
    void deveBuscarConsultasPorPaciente() {
        Long pacienteId = 1L;
        Consulta consulta = Consulta.builder()
                .id(1L)
                .pacienteId(pacienteId)
                .medicoId(2L)
                .dataHora(LocalDateTime.now().plusDays(1))
                .observacoes("Teste")
                .build();

        when(consultaRepository.findByPacienteId(pacienteId)).thenReturn(List.of(consulta));

        var resultado = service.buscarPorPaciente(pacienteId);

        assertEquals(1, resultado.size());
        assertEquals(pacienteId, resultado.get(0).getPacienteId());
    }
}
