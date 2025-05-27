package com.techchallenge.agendamentoservice.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.boot.ApplicationRunner;

import com.techchallenge.agendamentoservice.domain.Medico;
import com.techchallenge.agendamentoservice.domain.Paciente;
import com.techchallenge.agendamentoservice.repository.MedicoRepository;
import com.techchallenge.agendamentoservice.repository.PacienteRepository;

class DataSeederTest {

    private MedicoRepository medicoRepository;
    private PacienteRepository pacienteRepository;
    private DataSeeder dataSeeder;

    @BeforeEach
    void setup() {
        medicoRepository = mock(MedicoRepository.class);
        pacienteRepository = mock(PacienteRepository.class);
        dataSeeder = new DataSeeder();
    }

    @Test
    void devePopularDadosQuandoRepositoriosEstaoVazios() throws Exception {
        when(medicoRepository.count()).thenReturn(0L);
        when(pacienteRepository.count()).thenReturn(0L);

        ApplicationRunner runner = dataSeeder.seedDatabase(medicoRepository, pacienteRepository);
        runner.run(null);

        verify(medicoRepository, times(3)).save(any(Medico.class));
        verify(pacienteRepository, times(3)).save(any(Paciente.class));
    }

    @Test
    void naoDevePopularDadosQuandoRepositoriosJaContemRegistros() throws Exception {
        when(medicoRepository.count()).thenReturn(2L);
        when(pacienteRepository.count()).thenReturn(2L);

        ApplicationRunner runner = dataSeeder.seedDatabase(medicoRepository, pacienteRepository);
        runner.run(null);

        verify(medicoRepository, never()).save(any());
        verify(pacienteRepository, never()).save(any());
    }
}
