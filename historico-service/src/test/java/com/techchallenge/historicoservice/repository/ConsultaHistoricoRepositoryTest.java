package com.techchallenge.historicoservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.techchallenge.historicoservice.domain.ConsultaHistorico;

@DataJpaTest
public class ConsultaHistoricoRepositoryTest {

    @Autowired
    private ConsultaHistoricoRepository repository;

    @Test
    void deveSalvarERecuperarConsultaHistorico() {
        // Arrange
        ConsultaHistorico consulta = new ConsultaHistorico();
        consulta.setPacienteId(1L);
        consulta.setMedicoId(2L);
        consulta.setDataHora(LocalDateTime.of(2025, 6, 1, 10, 0));
        consulta.setObservacoes("Avaliação geral");

        // Act
        repository.save(consulta);
        List<ConsultaHistorico> resultado = repository.findAll();

        // Assert
        assertThat(resultado).hasSize(1);
        ConsultaHistorico salvo = resultado.get(0);
        assertThat(salvo.getPacienteId()).isEqualTo(1L);
        assertThat(salvo.getMedicoId()).isEqualTo(2L);
        assertThat(salvo.getObservacoes()).isEqualTo("Avaliação geral");
    }
}
