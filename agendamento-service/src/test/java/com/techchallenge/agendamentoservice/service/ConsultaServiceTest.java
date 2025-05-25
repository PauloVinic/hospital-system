package com.techchallenge.agendamentoservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
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
        ConsultaRequestDTO dto = new ConsultaRequestDTO(1L, 2L, LocalDateTime.now().plusDays(1), "Rotina");
        Consulta consultaSalva = Consulta.builder()
                .id(10L)
                .pacienteId(1L)
                .medicoId(2L)
                .dataHora(dto.dataHora())
                .observacoes("Rotina")
                .build();

        when(consultaRepository.save(any(Consulta.class))).thenReturn(consultaSalva);

        // act
        Consulta result = service.criarConsultaComDTO(dto);

        // assert
        assertEquals(10L, result.getId());
        verify(validator).validar(dto);
        verify(notifier).enviarEvento("consulta.created", consultaSalva);
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
}
