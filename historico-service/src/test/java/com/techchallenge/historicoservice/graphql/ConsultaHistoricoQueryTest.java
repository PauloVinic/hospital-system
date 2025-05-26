package com.techchallenge.historicoservice.graphql;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.techchallenge.historicoservice.domain.ConsultaHistorico;
import com.techchallenge.historicoservice.repository.ConsultaHistoricoRepository;

@Import(org.springframework.boot.autoconfigure.graphql.GraphQlAutoConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
public class ConsultaHistoricoQueryTest {

    @Autowired
    private WebTestClient webTestClient;

    private GraphQlTester graphQlTester;

    @MockBean
    private ConsultaHistoricoRepository repository;

    @BeforeEach
    void setup() {
        this.graphQlTester = HttpGraphQlTester.builder(
                webTestClient.mutate()
                        .defaultHeaders(headers -> headers.setBasicAuth("testuser", "testpass")))
                .url("/graphql")
                .build();
    }

    @Test
    void deveListarHistorico() {
        ConsultaHistorico historico = new ConsultaHistorico();
        historico.setId(1L);
        historico.setPacienteId(123L);
        historico.setMedicoId(456L);
        historico.setDataHora(LocalDateTime.of(2025, 5, 20, 10, 0));
        historico.setObservacoes("Consulta de rotina");

        when(repository.findAll()).thenReturn(List.of(historico));

        String query = """
                    query {
                        listarHistorico {
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
                .path("listarHistorico[0].pacienteId")
                .entity(Long.class)
                .isEqualTo(123L);
    }

    @Test
    void deveListarHistoricoPorPaciente() {
        ConsultaHistorico historico = new ConsultaHistorico();
        historico.setId(2L);
        historico.setPacienteId(321L);
        historico.setMedicoId(999L);
        historico.setDataHora(LocalDateTime.of(2025, 5, 22, 14, 30));
        historico.setObservacoes("Consulta de retorno");

        when(repository.findAll()).thenReturn(List.of(historico));

        String query = """
                    query {
                        listarHistoricoPorPaciente(pacienteId: 321) {
                            id
                            dataHora
                        }
                    }
                """;

        graphQlTester.document(query)
                .execute()
                .path("listarHistoricoPorPaciente[0].id")
                .entity(Long.class)
                .isEqualTo(2L);
    }
}
