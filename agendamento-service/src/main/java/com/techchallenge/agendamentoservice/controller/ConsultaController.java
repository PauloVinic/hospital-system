package com.techchallenge.agendamentoservice.controller;

import com.techchallenge.agendamentoservice.domain.Consulta;
import com.techchallenge.agendamentoservice.dto.ConsultaRequestDTO;
import com.techchallenge.agendamentoservice.dto.ConsultaResponseDTO;
import com.techchallenge.agendamentoservice.service.ConsultaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService service;

@PostMapping
@PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
public ResponseEntity<ConsultaResponseDTO> criar(@Valid @RequestBody ConsultaRequestDTO dto) {
    Consulta nova = service.criarConsultaComDTO(dto);
    return ResponseEntity.status(201).body(new ConsultaResponseDTO(
        nova.getId(),
        nova.getPacienteId(),
        nova.getMedicoId(),
        nova.getDataHora(),
        nova.getObservacoes()
    ));
}

    @PutMapping("/{id}")
    public ResponseEntity<Consulta> editar(@PathVariable Long id, @RequestBody Consulta consulta) {
        return ResponseEntity.ok(service.editarConsulta(id, consulta));
    }
}
