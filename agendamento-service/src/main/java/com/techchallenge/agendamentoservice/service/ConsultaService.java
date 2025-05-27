package com.techchallenge.agendamentoservice.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.techchallenge.agendamentoservice.domain.Consulta;
import com.techchallenge.agendamentoservice.domain.Paciente;
import com.techchallenge.agendamentoservice.dto.ConsultaPageResponseDTO;
import com.techchallenge.agendamentoservice.dto.ConsultaRequestDTO;
import com.techchallenge.agendamentoservice.dto.ConsultaResponseDTO;
import com.techchallenge.agendamentoservice.dto.ConsultaUpdateDTO;
import com.techchallenge.agendamentoservice.dto.NotificacaoConsultaDTO;
import com.techchallenge.agendamentoservice.exception.BusinessException;
import com.techchallenge.agendamentoservice.repository.ConsultaRepository;
import com.techchallenge.agendamentoservice.repository.PacienteRepository;
import com.techchallenge.agendamentoservice.service.notifier.ConsultaNotifier;
import com.techchallenge.agendamentoservice.service.validator.ConsultaValidator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final ConsultaNotifier notifier;
    private final ConsultaValidator validator;

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
        Paciente paciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new BusinessException("Paciente informado não existe"));

        notifier.enviarEvento("consulta.create", new NotificacaoConsultaDTO(
                consultaSalva.getId(),
                consultaSalva.getPacienteId(),
                paciente.getEmail(),
                consultaSalva.getDataHora()));

        return consultaSalva;
    }

    @Transactional
    public Consulta editarConsultaComDTO(Long id, ConsultaUpdateDTO dto) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Consulta com ID " + id + " não encontrada"));

        if (dto.dataHora().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Não é possível reagendar para uma data/hora no passado");
        }

        consulta.setDataHora(dto.dataHora());
        consulta.setObservacoes(dto.observacoes());

        Consulta atualizada = consultaRepository.save(consulta);
        Paciente paciente = pacienteRepository.findById(atualizada.getPacienteId())
                .orElseThrow(() -> new BusinessException("Paciente informado não existe"));

        notifier.enviarEvento("consulta.update", new NotificacaoConsultaDTO(
                atualizada.getId(),
                atualizada.getPacienteId(),
                paciente.getEmail(),
                atualizada.getDataHora()));

        return atualizada;
    }

    public ConsultaPageResponseDTO listarConsultasPaginadas(Pageable pageable) {
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

    public List<Consulta> listarTodasConsultas() {
        return consultaRepository.findAll();
    }

    public List<Consulta> buscarPorPaciente(Long pacienteId) {
        return consultaRepository.findByPacienteId(pacienteId);
    }

    public List<Consulta> buscarProximasConsultas(Long pacienteId) {
        return consultaRepository.findByPacienteIdAndDataHoraAfter(pacienteId, LocalDateTime.now());
    }
}
