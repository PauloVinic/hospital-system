package com.techchallenge.agendamentoservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.techchallenge.agendamentoservice.domain.Consulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    boolean existsByMedicoIdAndDataHora(Long medicoId, LocalDateTime dataHora);

    List<Consulta> findByPacienteId(Long pacienteId);

    List<Consulta> findByPacienteIdAndDataHoraAfter(Long pacienteId, LocalDateTime dataHora);

    @Query(value = "SELECT c FROM Consulta c WHERE c.pacienteId IS NOT NULL AND c.medicoId IS NOT NULL", countQuery = "SELECT COUNT(c) FROM Consulta c WHERE c.pacienteId IS NOT NULL AND c.medicoId IS NOT NULL")
    Page<Consulta> findAllValid(Pageable pageable);

}
