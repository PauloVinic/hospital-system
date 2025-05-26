package com.techchallenge.agendamentoservice.graphql;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.ActiveProfiles;

import com.techchallenge.agendamentoservice.domain.Consulta;
import com.techchallenge.agendamentoservice.service.ConsultaService;

@ActiveProfiles("test") // ✅ Aqui você ativa o profile que desativa a segurança
@GraphQlTest(ConsultaResolver.class)
class ConsultaResolverTest {

  @Autowired
  private GraphQlTester graphQlTester;

  @MockBean
  private ConsultaService consultaService;

  @Test
  void deveCriarConsultaComInputValido() {
    var consulta = Consulta.builder()
        .id(1L)
        .pacienteId(1L)
        .medicoId(2L)
        .dataHora(LocalDateTime.of(2025, 6, 1, 14, 0))
        .observacoes("Consulta via GraphQL")
        .build();

    Mockito.when(consultaService.criarConsultaComDTO(any())).thenReturn(consulta);

    String mutation = """
            mutation {
              criarConsulta(input: {
                pacienteId: 1,
                medicoId: 2,
                dataHora: "2025-06-01T14:00:00",
                observacoes: "Consulta via GraphQL"
              }) {
                id
                pacienteId
                medicoId
                dataHora
                observacoes
              }
            }
        """;

    graphQlTester.document(mutation)
        .execute()
        .path("criarConsulta.id").entity(Long.class).isEqualTo(1L)
        .path("criarConsulta.pacienteId").entity(Long.class).isEqualTo(1L)
        .path("criarConsulta.observacoes").entity(String.class).isEqualTo("Consulta via GraphQL");
  }

  @Test
  void deveRetornarConsultasViaGraphQL() {
    Consulta consulta = new Consulta(1L, 1L, 2L, LocalDateTime.now().plusDays(1), "Rotina");

    when(consultaService.listarConsultas()).thenReturn(List.of(consulta));

    String query = """
        {
            listarConsultas {
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
        .path("listarConsultas[0].id").entity(Long.class).isEqualTo(1L);
  }

}
