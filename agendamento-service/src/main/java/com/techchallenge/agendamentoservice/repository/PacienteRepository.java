package com.techchallenge.agendamentoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techchallenge.agendamentoservice.domain.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
