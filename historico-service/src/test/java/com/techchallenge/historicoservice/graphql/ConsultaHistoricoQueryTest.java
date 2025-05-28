package com.techchallenge.historicoservice.graphql;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techchallenge.historicoservice.domain.ConsultaHistorico;
import com.techchallenge.historicoservice.dto.ConsultaHistoricoDTO;
import com.techchallenge.historicoservice.repository.ConsultaHistoricoRepository;

public class ConsultaHistoricoQueryTest {

    private ConsultaHistoricoRepository repository;
    private ConsultaHistoricoQuery query;

    @BeforeEach
    void setUp() {
        repository = mock(ConsultaHistoricoRepository.class);
        query = new ConsultaHistoricoQuery(repository);
    }

    @Test
    void deveRetornarTodosOsHistoricos() {
        var c1 = new ConsultaHistorico();
        c1.setId(1L);
        c1.setPacienteId(100L);
        c1.setMedicoId(200L);
        c1.setDataHora(LocalDateTime.of(2025, 6, 1, 10, 0));
        c1.setObservacoes("Consulta de rotina");

        var c2 = new ConsultaHistorico();
        c2.setId(2L);
        c2.setPacienteId(101L);
        c2.setMedicoId(201L);
        c2.setDataHora(LocalDateTime.of(2025, 6, 2, 15, 0));
        c2.setObservacoes("Retorno clínico");

        when(repository.findAll()).thenReturn(List.of(c1, c2));

        List<ConsultaHistoricoDTO> resultado = query.listarHistorico();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).pacienteId()).isEqualTo(100L);
        assertThat(resultado.get(1).observacoes()).isEqualTo("Retorno clínico");
    }

    @Test
    void deveFiltrarPorPacienteId() {
        var c1 = new ConsultaHistorico();
        c1.setId(1L);
        c1.setPacienteId(321L);
        c1.setMedicoId(999L);
        c1.setDataHora(LocalDateTime.of(2025, 6, 3, 14, 0));
        c1.setObservacoes("Avaliação pré-operatória");

        var c2 = new ConsultaHistorico();
        c2.setId(2L);
        c2.setPacienteId(123L);
        c2.setMedicoId(888L);

        when(repository.findAll()).thenReturn(List.of(c1, c2));

        List<ConsultaHistoricoDTO> resultado = query.listarHistoricoPorPaciente(321L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).medicoId()).isEqualTo(999L);
        assertThat(resultado.get(0).pacienteId()).isEqualTo(321L);
    }
}
