package com.techchallenge.agendamentoservice.service.validator;

import org.springframework.stereotype.Component;

import com.techchallenge.agendamentoservice.dto.ConsultaRequestDTO;
import com.techchallenge.agendamentoservice.exception.BusinessException;
import com.techchallenge.agendamentoservice.repository.ConsultaRepository;
import com.techchallenge.agendamentoservice.repository.MedicoRepository;
import com.techchallenge.agendamentoservice.repository.PacienteRepository;

@Component
public class ConsultaValidator {

    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final ConsultaRepository consultaRepository;

    public ConsultaValidator(MedicoRepository medicoRepository,
                             PacienteRepository pacienteRepository,
                             ConsultaRepository consultaRepository) {
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.consultaRepository = consultaRepository;
    }

    public void validar(ConsultaRequestDTO dto) {
        if (!medicoRepository.existsById(dto.medicoId())) {
            throw new BusinessException("Médico ID " + dto.medicoId() + " não encontrado");
        }

        if (!pacienteRepository.existsById(dto.pacienteId())) {
            throw new BusinessException("Paciente ID " + dto.pacienteId() + " não encontrado");
        }

        if (consultaRepository.existsByMedicoIdAndDataHora(dto.medicoId(), dto.dataHora())) {
            throw new BusinessException("Médico já possui consulta agendada neste horário");
        }
    }
}
