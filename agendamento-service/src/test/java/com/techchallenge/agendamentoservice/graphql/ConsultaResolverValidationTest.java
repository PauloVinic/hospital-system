package com.techchallenge.agendamentoservice.graphql;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import com.techchallenge.agendamentoservice.exception.BusinessException;
import com.techchallenge.agendamentoservice.service.ConsultaService;

@GraphQlTest(ConsultaResolver.class)
class ConsultaResolverValidationTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private ConsultaService consultaService;

    @Test
    void deveRetornarErro_QuandoDataInvalida() {
        // Arrange: simula exceção de regra de negócio
        Mockito.doThrow(new BusinessException("A data da consulta deve ser no futuro."))
                .when(consultaService).criarConsultaComDTO(Mockito.any());

        // Mutation com data inválida
        String mutation = """
                    mutation {
                      criarConsulta(input: {
                        pacienteId: 1,
                        medicoId: 1,
                        dataHora: "2022-01-01T10:00:00",
                        observacoes: "Consulta inválida"
                      }) {
                        id
                      }
                    }
                """;

        // Act + Assert
        graphQlTester.document(mutation)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).isNotEmpty();
                    assertThat(errors.get(0).getMessage())
                            .isNotNull()
                            .contains("A data da consulta deve ser no futuro");
                });
    }

    @Test
    void deveRetornarErro_QuandoHorarioNaoForMultiploDe15() {
        // Arrange: simula exceção de negócio específica
        Mockito.doThrow(new BusinessException("Consultas devem ser agendadas em intervalos de 15 minutos."))
                .when(consultaService).criarConsultaComDTO(Mockito.any());

        // Mutation com horário inválido (14:07)
        String mutation = """
                    mutation {
                      criarConsulta(input: {
                        pacienteId: 1,
                        medicoId: 2,
                        dataHora: "2025-06-01T14:07:00",
                        observacoes: "Consulta com horário irregular"
                      }) {
                        id
                      }
                    }
                """;

        // Act + Assert
        graphQlTester.document(mutation)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).isNotEmpty();
                    assertThat(errors.get(0).getMessage())
                            .isNotNull()
                            .contains("Consultas devem ser agendadas em intervalos de 15 minutos");
                });
    }

}
