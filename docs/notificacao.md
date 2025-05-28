# Notificacao Service

## Visão Geral

O `notificacao-service` é responsável por receber eventos de criação e atualização de consultas médicas, processando essas mensagens e simulando o envio de notificações aos pacientes. Ele faz parte da arquitetura de microsserviços do sistema hospitalar.

## Objetivos

- Consumir eventos provenientes do agendamento de consultas.
- Simular o envio de lembretes aos pacientes.
- Garantir comunicação assíncrona por meio de RabbitMQ.
- Operar de forma desacoplada dos demais serviços.
- Tratar falhas com suporte a Dead Letter Queue (DLQ).

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.1.4
- RabbitMQ
- Jacoco (cobertura de testes)
- JUnit 5 + Mockito (testes unitários)
- Docker e Docker Compose

## Estrutura do Projeto

```
|-- config (configurações RabbitMQ)
|-- dto (transferência de dados)
|-- listener (consumo de eventos RabbitMQ)
|-- service (lógica de notificação)
|-- test (testes unitários e de integração)
```

## Execução Local

### Pré-requisitos

- Java 21+
- Maven 3.8+
- RabbitMQ local ou via Docker

### Instruções

```bash
# Clone o projeto
git clone https://github.com/PauloVinic/hospital-system.git
cd hospital-system

# Inicie o RabbitMQ e demais serviços
docker-compose up

# Navegue até o notificacao-service
cd notificacao-service

# Rode a aplicação
mvn spring-boot:run
```

## Comunicação com RabbitMQ

- **Exchange principal:** `consulta.exchange` (Direct)
- **Fila principal:** `consulta.queue`
- **Routing Keys:** `consulta.created`, `consulta.updated`

Ao receber uma mensagem válida, o serviço processa o JSON e simula o envio da notificação por log.

## Suporte a Dead Letter Queue (DLQ)

Mensagens que falharem durante o processamento na fila principal são redirecionadas automaticamente para a fila morta (`consulta.dlq.queue`).  
Essa fila está associada à exchange `dlx.exchange` e utiliza a routing key `consulta.dlq`.

Esse mecanismo aumenta a resiliência do sistema, evitando perda de dados e permitindo análise posterior de mensagens problemáticas.

## Exemplo de Payload

```json
{
  "idConsulta": 1,
  "pacienteId": 2,
  "emailPaciente": "paciente@email.com",
  "dataHora": "2025-06-01T10:00:00"
}
```

## Testes Automatizados

### Tipos de Testes

- **Unitários:** NotificationService, ConsultaMessageProcessor
- **Integração:** ConsultaListener com contexto real do Spring

### Cobertura de Código

- Geração com Jacoco (`mvn test && mvn jacoco:report`)
- Arquivo de saída: `target/site/jacoco/index.html`

## Considerações

- O serviço foi implementado com foco em confiabilidade, isolamento e robustez.
- O mecanismo de DLQ garante tolerância a falhas no processamento assíncrono.
- A simulação de envio permite testes independentes de sistemas externos.
- Alcançou 90%+ de cobertura de testes com validações de fluxos reais e inválidos.
