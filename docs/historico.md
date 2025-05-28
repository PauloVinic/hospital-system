
# Documentação Técnica — historico-service

## Visão Geral

O `historico-service` é um microsserviço opcional do sistema hospitalar, responsável por fornecer acesso ao histórico de consultas dos pacientes. Ele permite realizar consultas por meio de uma API GraphQL, retornando dados de atendimentos passados registrados no banco.

## Objetivos

- Consultar histórico completo de consultas.
- Filtrar histórico por paciente.
- Oferecer acesso via GraphQL com esquema estruturado.
- Operar de forma desacoplada, apenas com leitura dos dados registrados.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.2.5
- Spring GraphQL
- H2 (testes)
- PostgreSQL (produção)
- JUnit 5
- JaCoCo (cobertura de testes)
- Maven

## Estrutura do Projeto

```
historico-service/
├── graphql     (consultas GraphQL)
├── repository  (acesso a dados JPA)
├── domain      (entidades JPA)
├── dto         (transferência de dados)
├── config      (segurança e banco)
├── test        (testes unitários e integração)
```

## API GraphQL

### Endpoint

```
POST /graphql
```

### Schema disponível

```graphql
type ConsultaHistoricoDTO {
  id: ID
  pacienteId: Long
  medicoId: Long
  dataHora: String
  observacoes: String
}

type Query {
  listarHistorico: [ConsultaHistoricoDTO]
  listarHistoricoPorPaciente(pacienteId: Long): [ConsultaHistoricoDTO]
}
```

### Exemplo de Query: listar todo histórico

```graphql
query {
  listarHistorico {
    id
    pacienteId
    medicoId
    dataHora
    observacoes
  }
}
```

### Exemplo de Query: filtrar por paciente

```graphql
query {
  listarHistoricoPorPaciente(pacienteId: 123) {
    id
    dataHora
  }
}
```

## Segurança

- Habilitado apenas autenticação básica para ambiente de testes.
- Sem exigência de autenticação no ambiente de produção.

## Testes Automatizados

### Tipos de Testes

- **Unitários:** mapeamento DTO, conversão, domínio.
- **Repositório com banco real:** usando `@DataJpaTest`.
- **Sanity Check:** validação de carga mínima de contexto.

### Cobertura de Código

- 93% de cobertura global com foco em confiabilidade.
- Relatório gerado via JaCoCo:

```
target/site/jacoco/index.html
```

## Considerações

- O serviço não realiza persistência direta — depende de eventos externos para preenchimento do histórico.
- Totalmente isolado, permite consultas mesmo sem RabbitMQ.
- A abordagem via GraphQL proporciona maior flexibilidade para integrações futuras.
