package com.techchallenge.agendamentoservice.graphql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ConsultaInputTest {

    @Test
    void deveInstanciarConsultaInputECobrirAtributos() {
        ConsultaInput input = new ConsultaInput(1L, 2L, "2025-06-01T14:00:00", "Rotina");

        assertEquals(1L, input.pacienteId());
        assertEquals(2L, input.medicoId());
        assertEquals("2025-06-01T14:00:00", input.dataHora());
        assertEquals("Rotina", input.observacoes());
    }

    @Test
    void deveCompararRegistrosDeConsultaInput() {
        ConsultaInput input1 = new ConsultaInput(1L, 2L, "2025-06-01T14:00:00", "Rotina");
        ConsultaInput input2 = new ConsultaInput(1L, 2L, "2025-06-01T14:00:00", "Rotina");

        assertEquals(input1, input2);
        assertEquals(input1.hashCode(), input2.hashCode());
    }
}
