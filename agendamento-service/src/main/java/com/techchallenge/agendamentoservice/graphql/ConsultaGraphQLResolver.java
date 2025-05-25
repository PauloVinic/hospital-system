package com.techchallenge.agendamentoservice.graphql;

import com.techchallenge.agendamentoservice.domain.Consulta;
import com.techchallenge.agendamentoservice.service.ConsultaService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ConsultaGraphQLResolver {

    private final ConsultaService consultaService;

    public ConsultaGraphQLResolver(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @QueryMapping
    public List<Consulta> consultasPorPaciente(@Argument Long pacienteId) {
        return consultaService.buscarPorPaciente(pacienteId);
    }

    @QueryMapping
    public List<Consulta> proximasConsultas(@Argument Long pacienteId) {
        return consultaService.buscarProximasConsultas(pacienteId);
    }
}
