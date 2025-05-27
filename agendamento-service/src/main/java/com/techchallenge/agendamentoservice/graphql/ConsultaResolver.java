package com.techchallenge.agendamentoservice.graphql;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.techchallenge.agendamentoservice.domain.Consulta;
import com.techchallenge.agendamentoservice.dto.ConsultaRequestDTO;
import com.techchallenge.agendamentoservice.service.ConsultaService;

import jakarta.validation.Valid;

@Controller
public class ConsultaResolver {

    private final ConsultaService service;

    public ConsultaResolver(ConsultaService service) {
        this.service = service;
    }

    @QueryMapping
    public List<Consulta> listarConsultas() {
        return service.listarTodasConsultas();
    }

    @MutationMapping
    public Consulta criarConsulta(@Argument("input") @Valid ConsultaInput input) {
        ConsultaRequestDTO dto = new ConsultaRequestDTO(
                input.pacienteId(),
                input.medicoId(),
                LocalDateTime.parse(input.dataHora()),
                input.observacoes()
        );

        return service.criarConsultaComDTO(dto);
    }
}
