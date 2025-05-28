# Agendamento Service

## Objetivo

Este serviço é responsável por gerenciar o agendamento de consultas médicas entre pacientes e médicos. Ele realiza validações nos dados de entrada, previne conflitos de horário e publica eventos para outros serviços por meio do RabbitMQ.

## Funcionalidades

- Criar uma nova consulta com validação completa.
- Listar todas as consultas futuras.
- Publicar eventos `consulta.created` e `consulta.updated` no RabbitMQ.

## Tecnologias Utilizadas

- Spring Boot 3+
- Java 21
- PostgreSQL
- RabbitMQ
- REST API
- Swagger/OpenAPI
- JUnit, Mockito, JaCoCo

## Estrutura do Projeto

```
agendamento-service/
├── controller
├── service
├── repository
├── dto
├── config
├── exception
```

## Endpoints REST

### Criar uma consulta

```
POST /consultas
```

**Corpo da requisição:**

```json
{
  "pacienteId": 1,
  "medicoId": 2,
  "dataHora": "2025-06-01T10:00:00",
  "observacoes": "Consulta de rotina"
}
```

**Respostas esperadas:**

- 201 Created: Consulta criada com sucesso
- 400 Bad Request: Data no passado, conflito de horário, paciente ou médico inexistente

### Listar consultas futuras

```
GET /consultas/futuras
```

## Validações

- Anotações: `@NotNull`, `@NotBlank`, `@Future`
- Conflito de horário para o mesmo médico
- Verificação de existência de médico e paciente

## Tratamento de Erros

Utilização de `@ControllerAdvice` para tratamento global das exceções, com respostas padronizadas.

**Exemplo de resposta:**

```json
{
  "timestamp": "2025-05-27T12:00:00",
  "status": 400,
  "mensagem": "Campo obrigatório: pacienteId"
}
```

## Integração com RabbitMQ

### Publicação de eventos

Sempre que uma consulta é criada ou editada, o serviço publica um evento no exchange `consulta.exchange` com as seguintes routing keys:

- `consulta.created`
- `consulta.updated`

**Exemplo de payload enviado:**

```json
{
  "idConsulta": 1,
  "pacienteId": 2,
  "emailPaciente": "paciente@email.com",
  "dataHora": "2025-06-01T10:00:00"
}
```

## Testes

- Testes unitários completos da classe `ConsultaService`
- Testes de regras de negócio e validações
- Relatório de cobertura de código gerado com JaCoCo

## Documentação Swagger

A documentação REST está disponível em:

```
http://localhost:8080/swagger-ui.html
```

## Postman Collection

Arquivo disponível em:

```
postman/HospitalSystem-Fase3.postman_collection.json
```

Inclui testes para:

- Criação de consultas
- Listagem de consultas futuras
- Validações de dados inválidos
