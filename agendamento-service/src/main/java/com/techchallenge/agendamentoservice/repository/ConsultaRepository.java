package com.techchallenge.agendamentoservice.repository;

import com.techchallenge.agendamentoservice.domain.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    boolean existsByMedicoIdAndDataHora(Long medicoId, LocalDateTime dataHora);
}
