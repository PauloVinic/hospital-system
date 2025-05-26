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
import com.techchallenge.agendamentoservice.dto.ConsultaRequestDTO;
import com.techchallenge.agendamentoservice.dto.ConsultaUpdateDTO;
import com.techchallenge.agendamentoservice.exception.BusinessException;
import com.techchallenge.agendamentoservice.repository.ConsultaRepository;
import com.techchallenge.agendamentoservice.service.notifier.ConsultaNotifier;
import com.techchallenge.agendamentoservice.service.validator.ConsultaValidator;

class ConsultaServiceTest {

    private ConsultaService service;
    private ConsultaRepository consultaRepository;
    private ConsultaValidator validator;
    private ConsultaNotifier notifier;

    @BeforeEach
    void setup() {
        consultaRepository = mock(ConsultaRepository.class);
        validator = mock(ConsultaValidator.class);
        notifier = mock(ConsultaNotifier.class);
        service = new ConsultaService(consultaRepository, validator, notifier);
    }

    @Test
    void deveCriarConsultaComDTO() {
        // arrange
        ConsultaRequestDTO dto = new ConsultaRequestDTO(1L, 2L, LocalDateTime.of(2025, 6, 1, 14, 0), "Rotina");
        Consulta consultaSalva = Consulta.builder()
                .id(10L)
                .pacienteId(1L)
                .medicoId(2L)
                .dataHora(dto.dataHora())
                .observacoes("Rotina")
                .build();

        when(consultaRepository.save(any(Consulta.class))).thenReturn(consultaSalva);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);

        // act
        Consulta result = service.criarConsultaComDTO(dto);

        // assert
        assertEquals(10L, result.getId());
        verify(validator).validar(dto);
        verify(notifier).enviarEvento(eq("consulta.created"), captor.capture());

        Object enviado = captor.getValue();
        assertThat(enviado).isInstanceOf(com.techchallenge.agendamentoservice.dto.NotificacaoConsultaDTO.class);

        var dtoEnviado = (com.techchallenge.agendamentoservice.dto.NotificacaoConsultaDTO) enviado;
        assertThat(dtoEnviado.getPacienteId()).isEqualTo(1L);
        assertThat(dtoEnviado.getIdConsulta()).isEqualTo(10L);
        assertThat(dtoEnviado.getDataHora()).isEqualTo(dto.dataHora());
    }

    @Test
    void deveEditarConsultaComDTO() {
        // arrange
        Long id = 1L;
        Consulta existente = Consulta.builder()
                .id(id)
                .dataHora(LocalDateTime.now().plusDays(1))
                .observacoes("Antiga")
                .build();

        ConsultaUpdateDTO dto = new ConsultaUpdateDTO(LocalDateTime.now().plusDays(2), "Atualizada");

        when(consultaRepository.findById(id)).thenReturn(Optional.of(existente));
        when(consultaRepository.save(any())).thenReturn(existente);

        // act
        Consulta result = service.editarConsultaComDTO(id, dto);

        // assert
        assertEquals("Atualizada", result.getObservacoes());
    }

    @Test
    void deveLancarExcecaoQuandoEditarConsultaInexistente() {
        when(consultaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.editarConsultaComDTO(999L,
                new ConsultaUpdateDTO(LocalDateTime.now().plusDays(1), "x")));
    }

    @Test
    void deveListarConsultasPaginadas() {
        // arrange
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

        // act
        var resultado = service.listarConsultas(pageable);

        // assert
        assertEquals(1, resultado.content().size());
        assertEquals(1L, resultado.content().get(0).id());
    }

    @Test
    void deveLancarExcecaoAoCriarConsultaComConflito() {
        // arrange
        ConsultaRequestDTO dto = new ConsultaRequestDTO(1L, 2L, LocalDateTime.of(2025, 6, 1, 14, 0), "Rotina");

        doThrow(new BusinessException("Horário indisponível")).when(validator).validar(dto);

        // act + assert
        assertThrows(BusinessException.class, () -> service.criarConsultaComDTO(dto));
    }

    @Test
    void deveEditarConsultaEEnviarEventoDeAtualizacao() {
        // arrange
        Long id = 2L;
        LocalDateTime novaDataHora = LocalDateTime.of(2025, 6, 5, 9, 0);
        String novaObs = "Revisão";

        Consulta existente = Consulta.builder()
                .id(id)
                .pacienteId(1L)
                .medicoId(2L)
                .dataHora(LocalDateTime.of(2025, 6, 1, 14, 0))
                .observacoes("Antiga")
                .build();

        ConsultaUpdateDTO dto = new ConsultaUpdateDTO(novaDataHora, novaObs);

        when(consultaRepository.findById(id)).thenReturn(Optional.of(existente));
        when(consultaRepository.save(any())).thenReturn(existente);

        // act
        Consulta atualizado = service.editarConsultaComDTO(id, dto);

        // assert
        assertEquals(novaObs, atualizado.getObservacoes());
        assertEquals(novaDataHora, atualizado.getDataHora());
    }

    @Test
    void deveRetornarPaginaVaziaAoListarConsultas() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("dataHora").ascending());
        when(consultaRepository.findAllValid(pageable))
                .thenReturn(new PageImpl<>(List.of(), pageable, 0));

        var resultado = service.listarConsultas(pageable);

        assertEquals(0, resultado.content().size());
    }

    @Test
    void deveRetornarListaVaziaDeConsultas() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dataHora"));
        when(consultaRepository.findAllValid(pageable))
                .thenReturn(new PageImpl<>(List.of(), pageable, 0));

        var resultado = service.listarConsultas(pageable);

        assertEquals(0, resultado.content().size());
    }

    @Test
    void deveEditarConsultaMesmoSemAlterarValores() {
        Long id = 3L;
        LocalDateTime dataHora = LocalDateTime.of(2025, 6, 1, 10, 0);
        String obs = "Mesma observação";

        Consulta existente = Consulta.builder()
                .id(id)
                .pacienteId(1L)
                .medicoId(2L)
                .dataHora(dataHora)
                .observacoes(obs)
                .build();

        ConsultaUpdateDTO dto = new ConsultaUpdateDTO(dataHora, obs);

        when(consultaRepository.findById(id)).thenReturn(Optional.of(existente));
        when(consultaRepository.save(any())).thenReturn(existente);

        Consulta atualizado = service.editarConsultaComDTO(id, dto);

        assertEquals(obs, atualizado.getObservacoes());
        assertEquals(dataHora, atualizado.getDataHora());
    }

    @Test
    void deveCriarConsultaComObservacoesVazias() {
        ConsultaRequestDTO dto = new ConsultaRequestDTO(1L, 2L, LocalDateTime.of(2025, 6, 2, 15, 0), null);
        Consulta consultaSalva = Consulta.builder()
                .id(20L)
                .pacienteId(1L)
                .medicoId(2L)
                .dataHora(dto.dataHora())
                .observacoes(null)
                .build();

        when(consultaRepository.save(any())).thenReturn(consultaSalva);

        Consulta result = service.criarConsultaComDTO(dto);

        assertEquals(20L, result.getId());
        assertEquals(2L, result.getMedicoId());
        assertEquals(1L, result.getPacienteId());
        assertEquals(null, result.getObservacoes());
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
