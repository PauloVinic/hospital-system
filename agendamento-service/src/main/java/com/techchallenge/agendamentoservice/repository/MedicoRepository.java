package com.techchallenge.agendamentoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techchallenge.agendamentoservice.domain.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
}
