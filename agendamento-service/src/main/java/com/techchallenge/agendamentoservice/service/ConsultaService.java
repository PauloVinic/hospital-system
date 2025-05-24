package com.techchallenge.agendamentoservice.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.techchallenge.agendamentoservice.domain.Consulta;
import com.techchallenge.agendamentoservice.dto.ConsultaPageResponseDTO;
import com.techchallenge.agendamentoservice.dto.ConsultaRequestDTO;
import com.techchallenge.agendamentoservice.dto.ConsultaResponseDTO;
import com.techchallenge.agendamentoservice.dto.ConsultaUpdateDTO;
import com.techchallenge.agendamentoservice.exception.BusinessException;
import com.techchallenge.agendamentoservice.repository.ConsultaRepository;
import com.techchallenge.agendamentoservice.service.notifier.ConsultaNotifier;
import com.techchallenge.agendamentoservice.service.validator.ConsultaValidator;

import jakarta.transaction.Transactional;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final ConsultaValidator validator;
    private final ConsultaNotifier notifier;

    public ConsultaService(ConsultaRepository consultaRepository,
                           ConsultaValidator validator,
                           ConsultaNotifier notifier) {
        this.consultaRepository = consultaRepository;
        this.validator = validator;
        this.notifier = notifier;
    }

    @Transactional
    public Consulta criarConsultaComDTO(ConsultaRequestDTO dto) {
        validator.validar(dto);

        Consulta consulta = Consulta.builder()
                .pacienteId(dto.pacienteId())
                .medicoId(dto.medicoId())
                .dataHora(dto.dataHora())
                .observacoes(dto.observacoes())
                .build();

        Consulta consultaSalva = consultaRepository.save(consulta);
        notifier.enviarEvento("consulta.created", consultaSalva);
        return consultaSalva;
    }

    @Transactional
    public Consulta editarConsulta(Long id, Consulta consultaAtualizada) {
        Consulta consultaExistente = consultaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Consulta com ID " + id + " não encontrada"));

        consultaExistente.setDataHora(consultaAtualizada.getDataHora());
        consultaExistente.setObservacoes(consultaAtualizada.getObservacoes());
        consultaExistente.setMedicoId(consultaAtualizada.getMedicoId());
        consultaExistente.setPacienteId(consultaAtualizada.getPacienteId());

        Consulta consultaSalva = consultaRepository.save(consultaExistente);
        notifier.enviarEvento("consulta.updated", consultaSalva);
        return consultaSalva;
    }

    @Transactional
    public Consulta editarConsultaComDTO(Long id, ConsultaUpdateDTO dto) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Consulta com ID " + id + " não encontrada"));

        consulta.setDataHora(dto.dataHora());
        consulta.setObservacoes(dto.observacoes());

        return consultaRepository.save(consulta);
    }

    public List<Consulta> listarConsultas() {
        return consultaRepository.findAll();
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
}
