package com.techchallenge.agendamentoservice.repository;

import com.techchallenge.agendamentoservice.domain.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
}
