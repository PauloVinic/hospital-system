package com.techchallenge.historicoservice.service;

import com.techchallenge.historicoservice.domain.Consulta;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {

    public List<Consulta> listarTodas() {
        return List.of(
            Consulta.builder()
                .id(1L)
                .pacienteId(123L)
                .medicoId(456L)
                .dataHora("2025-05-18T10:00:00")
                .observacoes("Consulta de rotina")
                .build()
        );
    }
}
