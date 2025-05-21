package com.techchallenge.agendamentoservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techchallenge.agendamentoservice.domain.Consulta;
import com.techchallenge.agendamentoservice.dto.ConsultaRequestDTO;
import com.techchallenge.agendamentoservice.dto.ConsultaUpdateDTO;
import com.techchallenge.agendamentoservice.repository.ConsultaRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper; // ✅ serializador JSON

    public ConsultaService(ConsultaRepository consultaRepository, RabbitTemplate rabbitTemplate) {
        this.consultaRepository = consultaRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // ✅ necessário para LocalDateTime
    }


    public Consulta criarConsulta(Consulta consulta) {
        Consulta consultaSalva = consultaRepository.save(consulta);

        try {
            String json = objectMapper.writeValueAsString(consultaSalva);
            rabbitTemplate.convertAndSend("consulta.exchange", "consulta.created", json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar consulta para JSON", e);
        }

        return consultaSalva;
    }

    public Consulta editarConsulta(Long id, Consulta consultaAtualizada) {
        Consulta consultaExistente = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        consultaExistente.setDataHora(consultaAtualizada.getDataHora());
        consultaExistente.setObservacoes(consultaAtualizada.getObservacoes());
        consultaExistente.setMedicoId(consultaAtualizada.getMedicoId());
        consultaExistente.setPacienteId(consultaAtualizada.getPacienteId());

        Consulta consultaSalva = consultaRepository.save(consultaExistente);

        try {
            String json = objectMapper.writeValueAsString(consultaSalva);
            rabbitTemplate.convertAndSend("consulta.exchange", "consulta.updated", json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar consulta para JSON", e);
        }

        return consultaSalva;
    }

    public List<Consulta> listarConsultas() {
        return consultaRepository.findAll();
    }
    public Consulta criarConsultaComDTO(ConsultaRequestDTO dto) {
    Consulta nova = Consulta.builder()
            .pacienteId(dto.pacienteId())
            .medicoId(dto.medicoId())
            .dataHora(dto.dataHora())
            .observacoes(dto.observacoes())
            .build();
    return criarConsulta(nova); // reutiliza o método que envia para o Rabbit
}
public Consulta editarConsultaComDTO(Long id, ConsultaUpdateDTO dto) {
    Consulta consulta = consultaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

    consulta.setDataHora(dto.dataHora());
    consulta.setObservacoes(dto.observacoes());

    return consultaRepository.save(consulta);
}
}
