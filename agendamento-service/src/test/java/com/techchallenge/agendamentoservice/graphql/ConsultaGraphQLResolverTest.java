package com.techchallenge.agendamentoservice.graphql;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.ActiveProfiles;

import com.techchallenge.agendamentoservice.domain.Consulta;
import com.techchallenge.agendamentoservice.service.ConsultaService;

@GraphQlTest(ConsultaGraphQLResolver.class)
@ActiveProfiles("test")
class ConsultaGraphQLResolverTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private ConsultaService consultaService;

    @Test
    void deveListarConsultasPorPaciente() {
        var consulta = Consulta.builder()
                .id(1L).pacienteId(1L).medicoId(2L)
                .dataHora(LocalDateTime.now().plusDays(1))
                .observacoes("Teste").build();

        Mockito.when(consultaService.buscarPorPaciente(1L))
               .thenReturn(List.of(consulta));

        var query = """
            query {
              consultasPorPaciente(pacienteId: 1) {
                id
                pacienteId
                medicoId
                dataHora
                observacoes
              }
            }
        """;

        graphQlTester.document(query)
            .execute()
            .path("consultasPorPaciente[0].id").entity(Long.class).isEqualTo(1L);
    }

    @Test
    void deveListarProximasConsultas() {
        var consulta = Consulta.builder()
                .id(2L).pacienteId(1L).medicoId(3L)
                .dataHora(LocalDateTime.now().plusDays(2))
                .observacoes("Retorno").build();

        Mockito.when(consultaService.buscarProximasConsultas(1L))
               .thenReturn(List.of(consulta));

        var query = """
            query {
              proximasConsultas(pacienteId: 1) {
                id
                pacienteId
                medicoId
                dataHora
                observacoes
              }
            }
        """;

        graphQlTester.document(query)
            .execute()
            .path("proximasConsultas[0].id").entity(Long.class).isEqualTo(2L);
    }
}
