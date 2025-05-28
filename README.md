# Hospital System

**Repositório no GitHub:** [https://github.com/PauloVinic/hospital-system](https://github.com/PauloVinic/hospital-system)

Sistema hospitalar distribuído, baseado em microsserviços, que permite agendamento de consultas, histórico clínico e envio de notificações automatizadas.

## Módulos

* **agendamento-service:** Gerencia criação e edição de consultas médicas.
* **notificacao-service:** Processa eventos e envia notificações de consulta.
* **historico-service:** (Opcional) Fornece histórico de consultas via GraphQL.

## Como executar

### Pré-requisitos

* Java 21+
* Maven 3.8+
* Docker e Docker Compose

### Executar com Docker

```bash
git clone https://github.com/PauloVinic/hospital-system.git
cd hospital-system
docker-compose up --build
```

### Endpoints por serviço

| Serviço     | Porta Local                                    |
| ----------- | ---------------------------------------------- |
| Agendamento | [http://localhost:8080](http://localhost:8080) |
| Notificação | [http://localhost:8081](http://localhost:8081) |
| Histórico   | [http://localhost:8082](http://localhost:8082) |

## Documentação

A documentação técnica completa está disponível em:

```
docs/documentacao-tecnica.md
```

Inclui:

* Arquitetura do sistema
* Endpoints REST e queries GraphQL
* Comunicação assíncrona com RabbitMQ
* Testes automatizados
* Collection Postman

## Testes

Execute os testes com:

```bash
mvn clean test
```

Relatório de cobertura (Jacoco):

```
start agendamento-service/target/site/jacoco/index.html
```

## Collection Postman

A collection para testes manuais está disponível em:

```
postman/HospitalSystem-Fase3.postman_collection.json
```

Importe no Postman para testar os fluxos principais (criação de consulta, listagem, erros etc.).

---

## Link do Repositório

Acesse o código-fonte completo neste repositório público:

[https://github.com/PauloVinic/hospital-system](https://github.com/PauloVinic/hospital-system)

---

Projeto desenvolvido para o Tech Challenge — Fase 3.
