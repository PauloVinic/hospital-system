package com.techchallenge.historicoservice.controller;

import com.techchallenge.historicoservice.domain.Consulta;
import com.techchallenge.historicoservice.service.ConsultaService;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ConsultaQueryResolver {

    private final ConsultaService service;

    public ConsultaQueryResolver(ConsultaService service) {
        this.service = service;
    }

    @QueryMapping
    public List<Consulta> listarConsultas() {
        return service.listarTodas();
    }
}
