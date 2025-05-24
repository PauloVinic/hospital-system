package com.techchallenge.agendamentoservice.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService service;

    @Operation(
        summary = "Criar uma nova consulta médica",
        description = "Cria uma nova consulta entre médico e paciente, com validações de disponibilidade e existência."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Consulta criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação ou conflito de horário"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping
    public ResponseEntity<ConsultaResponseDTO> criar(@Valid @RequestBody ConsultaRequestDTO dto) {
        Consulta nova = service.criarConsultaComDTO(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ConsultaResponseDTO(
                nova.getId(),
                nova.getPacienteId(),
                nova.getMedicoId(),
                nova.getDataHora(),
                nova.getObservacoes()));
    }

    @Operation(
        summary = "Atualizar uma consulta existente",
        description = "Atualiza data e observações de uma consulta pelo ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consulta atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Consulta não encontrada"),
        @ApiResponse(responseCode = "400", description = "Erro de validação"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Consulta> editar(@PathVariable Long id, @Valid @RequestBody ConsultaUpdateDTO dto) {
        return ResponseEntity.ok(service.editarConsultaComDTO(id, dto));
    }

    @Operation(
        summary = "Listar consultas com paginação",
        description = "Retorna uma lista paginada de consultas ordenadas por data e ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consultas listadas com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno ao listar")
    })
    @GetMapping
    public ResponseEntity<ConsultaPageResponseDTO> listarConsultas(
            @PageableDefault(sort = { "dataHora", "id" }, direction = Sort.Direction.ASC) Pageable pageable) {
        var page = service.listarConsultas(pageable);
        return ResponseEntity.ok(page);
    }
}
