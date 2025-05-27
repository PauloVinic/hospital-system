package com.techchallenge.agendamentoservice.graphql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
import com.techchallenge.agendamentoservice.exception.BusinessException;
import com.techchallenge.agendamentoservice.service.ConsultaService;

@ActiveProfiles("test")
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
                dataHora: \"2025-06-01T14:00:00\",
                observacoes: \"Consulta via GraphQL\"
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

    when(consultaService.listarTodasConsultas()).thenReturn(List.of(consulta));

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
        .path("listarConsultas[0].id").entity(Long.class).isEqualTo(1L)
        .path("listarConsultas[0].observacoes").entity(String.class).isEqualTo("Rotina");
  }

  @Test
  void deveLancarErroAoCriarConsultaComDataNoPassado() {
    String mutation = """
            mutation {
              criarConsulta(input: {
                pacienteId: 1,
                medicoId: 2,
                dataHora: \"2020-01-01T10:00:00\",
                observacoes: \"Fora do prazo\"
              }) {
                id
              }
            }
        """;

    Mockito.when(consultaService.criarConsultaComDTO(any()))
           .thenThrow(new BusinessException("A data da consulta deve ser futura"));

    graphQlTester.document(mutation)
        .execute()
        .errors()
        .expect(error -> Optional.ofNullable(error.getMessage()).orElse("").contains("futura"));
  }

  @Test
  void deveLancarErroAoCriarConsultaComDadosNulos() {
    String mutation = """
            mutation {
              criarConsulta(input: {
                pacienteId: null,
                medicoId: 2,
                dataHora: \"2025-06-01T10:00:00\",
                observacoes: \"Teste\"
              }) {
                id
              }
            }
        """;

    graphQlTester.document(mutation)
        .execute()
        .errors()
        .expect(error -> Optional.ofNullable(error.getMessage()).orElse("").toLowerCase().contains("null"));
  }

  @Test
  void deveLancarErroQuandoObservacoesForemNulas() {
    String mutation = """
            mutation {
              criarConsulta(input: {
                pacienteId: 1,
                medicoId: 2,
                dataHora: \"2025-06-01T10:00:00\",
                observacoes: null
              }) {
                id
              }
            }
        """;

    Mockito.when(consultaService.criarConsultaComDTO(any()))
           .thenThrow(new BusinessException("Observações obrigatórias"));

    graphQlTester.document(mutation)
        .execute()
        .errors()
        .expect(error -> Optional.ofNullable(error.getMessage()).orElse("").contains("Observações"));
  }

  @Test
  void deveLancarErroComDataHoraEmFormatoInvalido() {
    String mutation = """
            mutation {
              criarConsulta(input: {
                pacienteId: 1,
                medicoId: 2,
                dataHora: \"erro-formato\",
                observacoes: \"Falha\"
              }) {
                id
              }
            }
        """;

    graphQlTester.document(mutation)
        .execute()
        .errors()
        .expect(error -> Optional.ofNullable(error.getMessage()).orElse("").toLowerCase().contains("erro"));
  }

  @Test
  void deveLancarErroQuandoMedicoIdForInvalido() {
    String mutation = """
            mutation {
              criarConsulta(input: {
                pacienteId: 1,
                medicoId: \"abc\",
                dataHora: \"2025-06-01T10:00:00\",
                observacoes: \"Teste com medico inválido\"
              }) {
                id
              }
            }
        """;

    graphQlTester.document(mutation)
        .execute()
        .errors()
        .expect(error -> Optional.ofNullable(error.getMessage()).orElse("").toLowerCase().contains("medico"));
  }

  @Test
  void deveLancarErroQuandoPacienteIdForNegativo() {
    String mutation = """
            mutation {
              criarConsulta(input: {
                pacienteId: -1,
                medicoId: 2,
                dataHora: "2025-06-01T10:00:00",
                observacoes: "Paciente inválido"
              }) {
                id
              }
            }
        """;

    graphQlTester.document(mutation)
        .execute()
        .errors()
        .expect(error -> true); // erro é esperado, sem depender da mensagem
  }

  @Test
  void deveLancarErroQuandoObservacoesForemVazias() {
    String mutation = """
            mutation {
              criarConsulta(input: {
                pacienteId: 1,
                medicoId: 2,
                dataHora: \"2025-06-01T10:00:00\",
                observacoes: \"\"
              }) {
                id
              }
            }
        """;

    Mockito.when(consultaService.criarConsultaComDTO(any()))
           .thenThrow(new BusinessException("Observações obrigatórias"));

    graphQlTester.document(mutation)
        .execute()
        .errors()
        .expect(error -> Optional.ofNullable(error.getMessage()).orElse("").contains("Observações"));
  }

  @Test
  void deveLancarErroQuandoDataHoraForNula() {
    String mutation = """
            mutation {
              criarConsulta(input: {
                pacienteId: 1,
                medicoId: 2,
                dataHora: null,
                observacoes: \"Sem data\"
              }) {
                id
              }
            }
        """;

    graphQlTester.document(mutation)
        .execute()
        .errors()
        .expect(error -> true); // erro esperado
  }
}
