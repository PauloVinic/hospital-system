# Hospital System - Tech Challenge (Fase 3)

Sistema hospitalar distribuído com microsserviços para agendamento de consultas, envio de notificações e (opcionalmente) gerenciamento de histórico, utilizando comunicação assíncrona via RabbitMQ e suporte a GraphQL.

## 🏗️ Arquitetura

O sistema é composto por três microsserviços independentes:

- **agendamento-service**: gerenciamento de consultas médicas.
- **notificacao-service**: envio de lembretes de consultas futuras aos pacientes.
- **historico-service** *(opcional)*: histórico médico acessível via GraphQL.

### 🔁 Comunicação entre serviços

A comunicação entre os microsserviços é realizada de forma assíncrona utilizando **RabbitMQ**, através de eventos como `consulta.created` e `consulta.updated`.

```
[ Frontend ou API Client ]
           ↓
   [ agendamento-service ]
           ↓ RabbitMQ (evento)
   [ notificacao-service ]
```

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Web / Spring WebFlux (para segurança no histórico)
- GraphQL (Spring GraphQL)
- RabbitMQ
- PostgreSQL
- JUnit 5 + Mockito
- Maven
- Docker + Docker Compose

## 📦 Estrutura do Projeto

```
hospital-system/
├── agendamento-service/        # API REST e GraphQL para consultas
├── notificacao-service/        # Consumidor RabbitMQ para envio de notificações
├── historico-service/          # (opcional) GraphQL para histórico de atendimentos
├── docker-compose.yml          # Subida de PostgreSQL e RabbitMQ
└── README.md
```

## 📚 Funcionalidades

### 📅 Agendamento de Consultas

- Criar, editar e listar consultas médicas.
- Validações de conflito de horário, existência de paciente e médico.
- Envio de eventos `consulta.created` e `consulta.updated` via RabbitMQ.

### 🔔 Notificações

- Escuta eventos do RabbitMQ.
- Envia mensagens simuladas para pacientes sobre suas próximas consultas.

### 🧾 Histórico Médico *(opcional)*

- Exposição de histórico via GraphQL.
- Queries por paciente, data futura, etc.

## 🎯 Endpoints Principais

### REST (Agendamento)

| Método | Endpoint       | Descrição                          |
|--------|----------------|-------------------------------------|
| POST   | /consultas     | Cria uma nova consulta              |
| PUT    | /consultas/{id}| Edita uma consulta existente        |
| GET    | /consultas     | Lista todas as consultas            |

### GraphQL

```
# Consulta exemplo
query {
  consultas {
    id
    pacienteId
    medicoId
    dataHora
    observacoes
  }
}
```

## 🧪 Testes Automatizados

- Testes unitários com `JUnit` e `Mockito`
- Testes GraphQL com `@GraphQlTest`
- Cobertura parcial já implementada, com foco nos serviços de domínio

## 📦 Executando o Projeto

### Pré-requisitos

- Java 17+
- Maven
- Docker e Docker Compose

### 1. Subir infraestrutura com Docker

```bash
docker-compose up -d
```

### 2. Rodar o serviço de agendamento

```bash
cd agendamento-service
mvn spring-boot:run
```

> Repita o processo para os demais serviços conforme desejado.

## 📬 Collection Postman

Incluímos uma collection Postman com os principais testes de API REST e GraphQL para facilitar a validação.

📁 Caminho: `postman/TechChallenge-Fase3.postman_collection.json`

## 📌 Status do Projeto

| Serviço             | Status       |
|---------------------|--------------|
| Agendamento         | ✅ Completo  |
| Notificações        | ✅ Completo  |
| Histórico (GraphQL) | 🟡 Opcional / Em desenvolvimento |

## 👨‍💻 Autor

Desenvolvido por Paulo Vinícius como parte da Fase 3 do [Tech Challenge ADJT - Banco do Brasil](https://github.com/PauloVinic/hospital-system)

## 📄 Licença

Este projeto é licenciado sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.