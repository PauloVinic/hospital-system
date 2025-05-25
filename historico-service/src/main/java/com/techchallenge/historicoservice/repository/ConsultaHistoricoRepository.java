package com.techchallenge.historicoservice.repository;

import com.techchallenge.historicoservice.domain.ConsultaHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaHistoricoRepository extends JpaRepository<ConsultaHistorico, Long> {
}
