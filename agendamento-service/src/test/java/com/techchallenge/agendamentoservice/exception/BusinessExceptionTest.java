package com.techchallenge.agendamentoservice.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class BusinessExceptionTest {

    @Test
    void deveLancarBusinessExceptionComMensagem() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            throw new BusinessException("Erro de negócio");
        });

        assertEquals("Erro de negócio", exception.getMessage());
    }
}
