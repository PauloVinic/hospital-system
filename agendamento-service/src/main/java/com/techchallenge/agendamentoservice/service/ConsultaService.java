package com.techchallenge.agendamentoservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techchallenge.agendamentoservice.domain.Consulta;
import com.techchallenge.agendamentoservice.dto.ConsultaPageResponseDTO;
import com.techchallenge.agendamentoservice.dto.ConsultaRequestDTO;
import com.techchallenge.agendamentoservice.dto.ConsultaResponseDTO;
import com.techchallenge.agendamentoservice.dto.ConsultaUpdateDTO;
import com.techchallenge.agendamentoservice.exception.BusinessException;
import com.techchallenge.agendamentoservice.repository.ConsultaRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public ConsultaService(ConsultaRepository consultaRepository, RabbitTemplate rabbitTemplate) {
        this.consultaRepository = consultaRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    public Consulta criarConsulta(Consulta consulta) {
        Consulta consultaSalva = consultaRepository.save(consulta);
        enviarEvento("consulta.created", consultaSalva);
        return consultaSalva;
    }

    public Consulta editarConsulta(Long id, Consulta consultaAtualizada) {
        Consulta consultaExistente = consultaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Consulta com ID " + id + " não encontrada"));

        consultaExistente.setDataHora(consultaAtualizada.getDataHora());
        consultaExistente.setObservacoes(consultaAtualizada.getObservacoes());
        consultaExistente.setMedicoId(consultaAtualizada.getMedicoId());
        consultaExistente.setPacienteId(consultaAtualizada.getPacienteId());

        Consulta consultaSalva = consultaRepository.save(consultaExistente);
        enviarEvento("consulta.updated", consultaSalva);
        return consultaSalva;
    }

    public List<Consulta> listarConsultas() {
        return consultaRepository.findAll();
    }

    public Consulta criarConsultaComDTO(ConsultaRequestDTO dto) {
        if (!medicoExiste(dto.medicoId())) {
            throw new BusinessException("Médico ID " + dto.medicoId() + " não encontrado");
        }

        if (!pacienteExiste(dto.pacienteId())) {
            throw new BusinessException("Paciente ID " + dto.pacienteId() + " não encontrado");
        }

        if (consultaRepository.existsByMedicoIdAndDataHora(dto.medicoId(), dto.dataHora())) {
            throw new BusinessException("Médico já possui consulta agendada neste horário");
        }

        Consulta consulta = Consulta.builder()
                .pacienteId(dto.pacienteId())
                .medicoId(dto.medicoId())
                .dataHora(dto.dataHora())
                .observacoes(dto.observacoes())
                .build();

        Consulta consultaSalva = consultaRepository.save(consulta);
        enviarEvento("consulta.created", consultaSalva);
        return consultaSalva;
    }

    public Consulta editarConsultaComDTO(Long id, ConsultaUpdateDTO dto) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Consulta com ID " + id + " não encontrada"));

        consulta.setDataHora(dto.dataHora());
        consulta.setObservacoes(dto.observacoes());

        return consultaRepository.save(consulta);
    }

    public ConsultaPageResponseDTO listarConsultas(Pageable pageable) {
        Page<Consulta> page = consultaRepository.findAllValid(pageable);

        List<ConsultaResponseDTO> dtos = page.getContent()
                .stream()
                .map(ConsultaResponseDTO::new)
                .toList();

        return new ConsultaPageResponseDTO(
                dtos,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements());
    }

    private void enviarEvento(String routingKey, Consulta consulta) {
        try {
            String json = objectMapper.writeValueAsString(consulta);
            rabbitTemplate.convertAndSend("consulta.exchange", routingKey, json);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Erro ao serializar consulta para JSON");
        }
    }

    private boolean medicoExiste(Long id) {
        return List.of(1L, 2L, 3L).contains(id);
    }

    private boolean pacienteExiste(Long id) {
        return List.of(1L, 2L, 3L).contains(id);
    }
}
