package com.techchallenge.agendamentoservice.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techchallenge.agendamentoservice.domain.Consulta;
import com.techchallenge.agendamentoservice.dto.ConsultaPageResponseDTO;
import com.techchallenge.agendamentoservice.dto.ConsultaRequestDTO;
import com.techchallenge.agendamentoservice.dto.ConsultaResponseDTO;
import com.techchallenge.agendamentoservice.dto.ConsultaUpdateDTO;
import com.techchallenge.agendamentoservice.service.ConsultaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService service;

    @PostMapping
    public ResponseEntity<ConsultaResponseDTO> criar(@Valid @RequestBody ConsultaRequestDTO dto) {
        Consulta nova = service.criarConsultaComDTO(dto);
        return ResponseEntity.status(201).body(new ConsultaResponseDTO(
                nova.getId(),
                nova.getPacienteId(),
                nova.getMedicoId(),
                nova.getDataHora(),
                nova.getObservacoes()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consulta> editar(@PathVariable Long id, @Valid @RequestBody ConsultaUpdateDTO dto) {
        return ResponseEntity.ok(service.editarConsultaComDTO(id, dto));
    }

    @GetMapping
    public ResponseEntity<ConsultaPageResponseDTO> listarConsultas(
            @PageableDefault(sort = { "dataHora", "id" }, direction = Sort.Direction.ASC) Pageable pageable) {

        var page = service.listarConsultas(pageable);
        return ResponseEntity.ok(page); // ✅ já é um ConsultaPageResponseDTO
    }

}
