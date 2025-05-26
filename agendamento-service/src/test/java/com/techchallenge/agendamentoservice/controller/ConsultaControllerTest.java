package com.techchallenge.agendamentoservice.controller;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.agendamentoservice.domain.Consulta;
import com.techchallenge.agendamentoservice.dto.ConsultaUpdateDTO;
import com.techchallenge.agendamentoservice.exception.BusinessException;
import com.techchallenge.agendamentoservice.service.ConsultaService;

@ActiveProfiles("test")
@WebMvcTest(ConsultaController.class)
class ConsultaControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ConsultaService consultaService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void deveAtualizarConsultaComSucesso() throws Exception {
                Long id = 1L;
                var dto = new ConsultaUpdateDTO(LocalDateTime.now().plusDays(2), "Atualizado");

                var consultaAtualizada = Consulta.builder()
                                .id(id)
                                .pacienteId(1L)
                                .medicoId(2L)
                                .dataHora(dto.dataHora())
                                .observacoes(dto.observacoes())
                                .build();

                Mockito.when(consultaService.editarConsultaComDTO(eq(id), any())).thenReturn(consultaAtualizada);

                mockMvc.perform(put("/consultas/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(id))
                                .andExpect(jsonPath("$.dataHora")
                                                .value(org.hamcrest.Matchers
                                                                .startsWith(dto.dataHora().toLocalDate().toString())))
                                .andExpect(jsonPath("$.observacoes").value("Atualizado"));
        }

        @Test
        void deveRetornar400ParaDataPassada() throws Exception {
                Long id = 1L;
                var dto = new ConsultaUpdateDTO(LocalDateTime.now().minusDays(1), "Data passada");

                mockMvc.perform(put("/consultas/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void deveRetornar404QuandoConsultaNaoExiste() throws Exception {
                Long idInexistente = 999L;
                String dataHora = LocalDateTime.now().plusDays(1).toString();

                var dto = new ConsultaUpdateDTO(LocalDateTime.parse(dataHora), "x");

                when(consultaService.editarConsultaComDTO(eq(idInexistente), any())).thenThrow(
                                new BusinessException("Consulta com ID 999 não encontrada"));

                mockMvc.perform(put("/consultas/{id}", idInexistente)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isBadRequest()) // ou .isNotFound() se preferir 404
                                .andExpect(content()
                                                .string(org.hamcrest.Matchers.containsString("Consulta com ID 999")));
        }

        @Test
        void deveCriarConsultaViaController() throws Exception {
                Consulta consulta = Consulta.builder()
                                .id(1L)
                                .pacienteId(1L)
                                .medicoId(2L)
                                .dataHora(LocalDateTime.parse("2025-06-01T14:00:00"))
                                .observacoes("Rotina")
                                .build();

                when(consultaService.criarConsultaComDTO(any())).thenReturn(consulta);

                var json = """
                                {
                                    "pacienteId": 1,
                                    "medicoId": 2,
                                    "dataHora": "2025-06-01T14:00:00",
                                    "observacoes": "Rotina"
                                }
                                """;

                mockMvc.perform(post("/consultas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isCreated());
        }

}
