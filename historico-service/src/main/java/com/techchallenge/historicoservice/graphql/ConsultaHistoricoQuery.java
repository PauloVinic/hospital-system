package com.techchallenge.historicoservice.graphql;

import com.techchallenge.historicoservice.domain.ConsultaHistorico;
import com.techchallenge.historicoservice.dto.ConsultaHistoricoDTO;
import com.techchallenge.historicoservice.repository.ConsultaHistoricoRepository;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ConsultaHistoricoQuery {

    private final ConsultaHistoricoRepository repository;

    public ConsultaHistoricoQuery(ConsultaHistoricoRepository repository) {
        this.repository = repository;
    }

    @QueryMapping
    public List<ConsultaHistoricoDTO> listarHistorico() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @QueryMapping
    public List<ConsultaHistoricoDTO> listarHistoricoPorPaciente(Long pacienteId) {
        return repository.findAll().stream()
                .filter(c -> c.getPacienteId().equals(pacienteId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ConsultaHistoricoDTO toDTO(ConsultaHistorico c) {
        return new ConsultaHistoricoDTO(
                c.getId(),
                c.getPacienteId(),
                c.getMedicoId(),
                c.getDataHora(),
                c.getObservacoes()
        );
    }
}
