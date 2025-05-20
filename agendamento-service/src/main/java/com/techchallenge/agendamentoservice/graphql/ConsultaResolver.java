package com.techchallenge.agendamentoservice.graphql;

import com.techchallenge.agendamentoservice.domain.Consulta;
import com.techchallenge.agendamentoservice.service.ConsultaService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ConsultaResolver {

    private final ConsultaService service;

    public ConsultaResolver(ConsultaService service) {
        this.service = service;
    }

    @QueryMapping
    public List<Consulta> listarConsultas() {
        return service.listarConsultas();
    }

    @MutationMapping
    public Consulta criarConsulta(@Argument("input") ConsultaInput input) {
        Consulta nova = Consulta.builder()
                .pacienteId(input.pacienteId())
                .medicoId(input.medicoId())
                .dataHora(LocalDateTime.parse(input.dataHora()))
                .observacoes(input.observacoes())
                .build();

        return service.criarConsulta(nova);
    }
    
}
