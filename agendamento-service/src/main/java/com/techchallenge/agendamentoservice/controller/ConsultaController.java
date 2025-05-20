package com.techchallenge.agendamentoservice.controller;

import com.techchallenge.agendamentoservice.domain.Consulta;
import com.techchallenge.agendamentoservice.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService service;

    @PostMapping
    public ResponseEntity<Consulta> criar(@RequestBody Consulta consulta) {
        return ResponseEntity.ok(service.criarConsulta(consulta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consulta> editar(@PathVariable Long id, @RequestBody Consulta consulta) {
        return ResponseEntity.ok(service.editarConsulta(id, consulta));
    }
}
