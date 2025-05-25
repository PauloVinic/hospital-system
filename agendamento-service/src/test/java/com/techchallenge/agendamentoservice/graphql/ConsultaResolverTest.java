package com.techchallenge.agendamentoservice.graphql;

import com.techchallenge.agendamentoservice.domain.Consulta;
import com.techchallenge.agendamentoservice.service.ConsultaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;

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
}
