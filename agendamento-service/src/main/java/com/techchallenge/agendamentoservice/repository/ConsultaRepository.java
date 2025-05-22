package com.techchallenge.agendamentoservice.repository;

import com.techchallenge.agendamentoservice.domain.Consulta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    boolean existsByMedicoIdAndDataHora(Long medicoId, LocalDateTime dataHora);

    @Query(value = "SELECT c FROM Consulta c WHERE c.pacienteId IS NOT NULL AND c.medicoId IS NOT NULL", countQuery = "SELECT COUNT(c) FROM Consulta c WHERE c.pacienteId IS NOT NULL AND c.medicoId IS NOT NULL")
    Page<Consulta> findAllValid(Pageable pageable);

}
