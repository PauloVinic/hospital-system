package com.techchallenge.agendamentoservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AgendamentoServiceApplicationTest {

    @Test
    void contextLoads() {
        // Teste que garante que o contexto da aplicação carrega corretamente
    }

    @Test
    void deveExecutarMetodoMain() {
        AgendamentoServiceApplication.main(new String[] {});
    }
}
