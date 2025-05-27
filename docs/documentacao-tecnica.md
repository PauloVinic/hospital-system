# Documentação Técnica — Sistema Hospitalar (Hospital System)

## 1. Visão Geral do Projeto

Este projeto consiste em uma aplicação backend distribuída, baseada em microsserviços, voltada ao gerenciamento hospitalar com foco em **agendamento de consultas**, **histórico clínico** e **notificações automatizadas**. Desenvolvido com Java e Spring Boot, o sistema emprega **comunicação assíncrona via RabbitMQ**, além de utilizar **GraphQL** para consultas médicas detalhadas.

### Objetivos:

* Garantir o agendamento eficaz e validado de consultas médicas.
* Registrar e recuperar o histórico de consultas por paciente.
* Automatizar o envio de lembretes ao paciente sobre consultas futuras.
* Assegurar modularidade, organização e escalabilidade dos serviços.

### Componentes:

* **agendamento-service:** criação e alteração de consultas.
* **notificacao-service:** processa eventos e envia lembretes.
* **historico-service (opcional):** fornece histórico de consultas via GraphQL.

---

## 2. Arquitetura do Sistema

### Estilo Arquitetural

* Baseado em **Microsserviços** com arquitetura modular.
* Modelo interno baseado em **camadas (MVC estruturado)** por serviço.

### Tecnologias

* Linguagem: Java 21
* Framework: Spring Boot 3+
* Banco de Dados: PostgreSQL
* Fila de Mensagens: RabbitMQ
* APIs: REST + GraphQL
* Documentação: Swagger UI + Markdown
* Testes: JUnit, Mockito, Jacoco
* Containers: Docker e Docker Compose

### Organização em Camadas:

```
|-- controller (camada de entrada: REST / GraphQL)
|-- service (regra de negócio)
|-- repository (persistência de dados)
|-- dto (objetos de transporte)
|-- config (configurações globais)
|-- exception (tratamento de exceções)
```

---

## 3. Instalação e Execução Local

### Pré-requisitos

* Java 21+
* Maven 3.8+
* Docker + Docker Compose

### Instruções

```bash
git clone https://github.com/PauloVinic/hospital-system.git
cd hospital-system
docker-compose up --build
```

### Endpoints por Serviço

| Serviço     | URL Base                                       |
| ----------- | ---------------------------------------------- |
| Agendamento | [http://localhost:8080](http://localhost:8080) |
| Notificação | [http://localhost:8081](http://localhost:8081) |
| Histórico   | [http://localhost:8082](http://localhost:8082) |

---

## 4. Operações por Serviço

### REST — Agendamento

* **POST /consultas** — Criar nova consulta
* **GET /consultas/futuras** — Listar próximas consultas

**Exemplo de Requisição:**

```json
{
  "pacienteId": 1,
  "medicoId": 2,
  "dataHora": "2025-06-01T10:00:00"
}
```

### GraphQL — Histórico

```graphql
query {
  historicoPorPaciente(pacienteId: 1) {
    dataHora
    medico {
      nome
    }
    descricao
  }
}
```

---

## 5. Comunicação Assíncrona com RabbitMQ

### Topologia

* **Exchange:** `consultas.exchange` (Direct)
* **Fila:** `notificacoes.queue`
* **Routing Keys:** `consulta.created`, `consulta.updated`

### Funcionamento

1. O serviço de agendamento publica um evento ao registrar ou editar uma consulta.
2. O serviço de notificação consome a mensagem e envia um lembrete automático ao paciente.

---

## 6. Segurança (Em Planejamento)

### A Implementar:

* Autenticação com Spring Security
* Perfis de usuário com acesso controlado:

  * Paciente: acessa apenas suas consultas
  * Enfermeiro: registra e lista
  * Médico: consulta histórico e edita

---

## 7. Validações e Tratamento de Erros

### Validações

* Anotações padrão como `@Valid`, `@NotNull`, `@NotBlank` nos DTOs.

### Tratamento Global

* `@ControllerAdvice` com interceptação de exceções personalizadas.
* Resposta padrão:

```json
{
  "timestamp": "2025-05-27T12:00:00",
  "status": 400,
  "mensagem": "Campo obrigatório: pacienteId"
}
```

---

## 8. Testes Automatizados e Qualidade

### Estratégia de Testes

Os testes foram aplicados principalmente aos serviços de agendamento e histórico, com foco em validações de regras de negócio e consultas via GraphQL.

* **Unitários:** `ConsultaServiceTest`, `ConsultaHistoricoQueryTest`
* **Integração (parcial):** usando contexto real do Spring

### Cobertura de Código

* Gerada via Jacoco (acessar `target/site/jacoco/index.html`)

---

## 9. Recursos de Apoio

### Swagger UI

* Documentação REST disponível em: `http://localhost:8080/swagger-ui.html`

### Collection Postman

* Local: `postman/HospitalSystem-Fase3.postman_collection.json`
* Instruções:

  * Importar a collection no Postman
  * Testar endpoints REST (`/consultas`, `/consultas/futuras`)
  * Testar queries GraphQL (`/graphql` com query `historicoPorPaciente`)
  * Verificar tratamento de erros: data no passado, paciente inexistente, etc.

---

## 10. Considerações Finais

* O sistema foi desenvolvido com foco em modularidade, clareza e validação robusta.
* A arquitetura adota boas práticas com separação de responsabilidades.
* A segurança será implementada como etapa final.
* O `historico-service` foi incluído, mesmo sendo opcional, com suporte a GraphQL e testes dedicados.
