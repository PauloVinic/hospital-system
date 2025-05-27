package com.techchallenge.agendamentoservice.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.techchallenge.agendamentoservice.domain.Medico;
import com.techchallenge.agendamentoservice.domain.Paciente;
import com.techchallenge.agendamentoservice.repository.MedicoRepository;
import com.techchallenge.agendamentoservice.repository.PacienteRepository;

@Configuration
public class DataSeeder {

    @Bean
    public ApplicationRunner seedDatabase(MedicoRepository medicoRepository, PacienteRepository pacienteRepository) {
        return args -> {
            if (medicoRepository.count() == 0) {
                medicoRepository.save(new Medico(1L, "Dra. Ana Beatriz"));
                medicoRepository.save(new Medico(2L, "Dr. João Carlos"));
                medicoRepository.save(new Medico(3L, "Dr. Rafael Souza"));
            }

            if (pacienteRepository.count() == 0) {
                pacienteRepository.save(new Paciente(1L, "Paulo Martins", "paulo@email.com"));
                pacienteRepository.save(new Paciente(2L, "Mariana Silva", "mariana@email.com"));
                pacienteRepository.save(new Paciente(3L, "Carlos Eduardo", "carlos@email.com"));
            }
        };
    }
}
