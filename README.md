# 🏦 Sistema Bancário com Microsserviços

## 📖 Sobre o Projeto

Sistema distribuído desenvolvido com arquitetura de microsserviços para simular operações bancárias, incluindo gerenciamento de clientes e contas bancárias.

O projeto foi criado com foco em boas práticas de desenvolvimento backend utilizando o ecossistema Spring, comunicação entre serviços e mecanismos de resiliência para lidar com falhas em ambientes distribuídos.

Além da comunicação síncrona utilizando OpenFeign, a aplicação implementa padrões de tolerância a falhas através do Resilience4j, garantindo que operações importantes não sejam perdidas mesmo quando um microsserviço estiver temporariamente indisponível.

---

# 🏗️ Arquitetura

A solução é composta por dois microsserviços independentes:

### 👤 Customer Service

Responsável pelo gerenciamento dos clientes.

Funcionalidades:

* Cadastro de clientes
* Consulta de clientes
* Atualização de clientes
* Exclusão de clientes

### 💳 Bank Account Service

Responsável pelo gerenciamento das contas bancárias.

Funcionalidades:

* Cadastro de contas
* Consulta de contas
* Atualização de contas
* Exclusão de contas
* Associação de contas a clientes

---

## Diagrama da Arquitetura

```text
┌───────────────────┐
│  Customer Service │
└─────────┬─────────┘
          │
          │ OpenFeign
          ▼
┌───────────────────┐
│ Bank Account API  │
└───────────────────┘
```

---

# 🚀 Tecnologias Utilizadas

### Backend

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Cloud OpenFeign
* Spring Scheduler

### Resiliência

* Resilience4j
* Circuit Breaker
* Retry

### Banco de Dados

* PostgreSQL
* Hibernate

### Ferramentas

* Maven
* Lombok

---

# ⚙️ Fluxo de Negócio

Quando um novo cliente é cadastrado, o sistema cria automaticamente uma conta bancária associada.

Fluxo principal:

```text
1. Cliente é cadastrado
2. Customer Service cria o cliente
3. Customer Service chama o Bank Account Service
4. Conta bancária é criada
5. Operação concluída
```

```text
Cliente
   │
   ▼
Customer Service
   │
   ▼
OpenFeign
   │
   ▼
Bank Account Service
```

---

# 🔗 Comunicação Entre Microsserviços

A comunicação entre os serviços é realizada utilizando Spring Cloud OpenFeign.

```java
@FeignClient(
        name = "accounts",
        url = "http://localhost:8081",
        fallback = AccountClientFallback.class
)
public interface AccountsClient {

    @PostMapping("/v1/accounts")
    void createAccount(
        @RequestBody CreateAccountRequestDto dto
    );
}
```

Antes de criar uma conta bancária, o sistema valida e consulta as informações necessárias através da integração entre os microsserviços.

---

# 🛡️ Resiliência e Tolerância a Falhas

O projeto implementa mecanismos para evitar perda de dados quando o microsserviço de contas estiver indisponível.

### Tecnologias utilizadas

* Circuit Breaker
* Retry
* Fallback
* Scheduler
* Consistência Eventual

---

## 🔄 Retry Automático

Ao ocorrer uma falha temporária de comunicação, o sistema realiza novas tentativas automaticamente.

```java
@Retry(name = "AccountServiceRetry")
@CircuitBreaker(
        name = "accountServiceCB",
        fallbackMethod = "fallbackCreateAccount"
)
public void createAccount(CreateAccountRequestDto dto) {
    accountsClient.createAccount(dto);
}
```

Benefícios:

* Tratamento de falhas transitórias
* Redução de erros temporários
* Maior disponibilidade do sistema

---

## ⚡ Circuit Breaker

O Circuit Breaker monitora continuamente a comunicação com o microsserviço de contas.

Quando o serviço apresenta falhas consecutivas:

```text
CLOSED
   │
   ▼
Falhas sucessivas
   │
   ▼
OPEN
   │
   ▼
Bloqueia novas chamadas
```

Após um período configurado:

```text
OPEN
   │
   ▼
HALF_OPEN
   │
   ▼
Teste de recuperação
```

Se o serviço voltar a responder:

```text
HALF_OPEN
   │
   ▼
CLOSED
```

---

## 📦 Fallback e Persistência de Pendências

Quando o serviço de contas está indisponível, os dados necessários para criação da conta são armazenados em uma tabela de pendências.

```java
@Override
public void createAccount(
        CreateAccountRequestDto dto
) {

    PendingAccount pendingAccount =
            new PendingAccount();

    pendingAccount.setCustomerId(
            dto.customerId()
    );

    pendingAccountRepository.save(
            pendingAccount
    );
}
```

Dessa forma, nenhuma solicitação é perdida.

---

## ⏰ Reprocessamento Automático

As contas pendentes são reprocessadas automaticamente através de um Scheduler.

```java
@Scheduled(fixedDelay = 10000)
@Transactional
public void processPendingAccounts() {

    List<PendingAccount> pendingAccounts =
            customerService.getPendingAccounts();

    for (PendingAccount pending : pendingAccounts) {

        CreateAccountRequestDto dto =
                new CreateAccountRequestDto(
                        pending.getCustomerId()
                );

        accountIntegrationService
                .createAccount(dto);
    }
}
```

Fluxo completo:

```text
Cliente
   │
   ▼
Customer Service
   │
   ▼
OpenFeign
   │
   ▼
Bank Account Service
   │
   ├── Sucesso
   │      ▼
   │   Conta Criada
   │
   └── Falha
          ▼
    Circuit Breaker
          ▼
         Retry
          ▼
    Pending Account
          ▼
       Scheduler
          ▼
    Nova Tentativa
```

Essa abordagem garante consistência eventual entre os microsserviços.

---

# 📂 Estrutura do Projeto

```text
bank-system
│
├── customer-service
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   ├── scheduler
│   └── integration
│
├── bank-account-service
│   ├── controller
│   ├── service
│   ├── repository
│   ├── clients
│   ├── entity
│   └── dto
│
└── README.md
```

---

# 🗄️ Modelo de Dados

### Customer

```text
Customer
├── id
├── name
├── email
└── cpf
```

### Bank Account

```text
BankAccount
├── id
├── agency
├── accountNumber
├── balance
└── customerId
```

### Pending Account

```text
PendingAccount
├── id
├── customerId
├── accountStatus
├── retryCount
└── lastTimeAttempt
```

---

# ▶️ Como Executar

### Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/bank-system.git
```

### Criar Bancos de Dados

```sql
CREATE DATABASE customers_db;
CREATE DATABASE accounts_db;
```

### Configurar Credenciais

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/customers_db
    username: postgres
    password: postgres
```

### Executar os Microsserviços

Customer Service:

```bash
mvn spring-boot:run
```

Bank Account Service:

```bash
mvn spring-boot:run
```

---

# 📌 Endpoints

## Customer Service

| Método | Endpoint        |
| ------ | --------------- |
| POST   | /v1/customers      |
| GET    | /v1/customers      |
| GET    | /v1/customers/{id} |
| GET    | /v1/customers/pending |
| DELETE | /v1/customers/{id} |

## Bank Account Service

| Método | Endpoint       |
| ------ | -------------- |
| POST   | /accounts      |
| GET    | /accounts      |
| GET    | /accounts/{id} |
| PUT    | /accounts/{id} |
| DELETE | /accounts/{id} |

---

# 🎯 Objetivos de Aprendizado

Este projeto foi desenvolvido para praticar:

* Arquitetura de Microsserviços
* Comunicação entre Serviços com OpenFeign
* Circuit Breaker com Resilience4j
* Retry Automático
* Fallbacks
* Scheduler para Reprocessamento
* Consistência Eventual
* APIs REST
* PostgreSQL
* JPA/Hibernate
* Boas Práticas de Backend

---

# 👨‍💻 Autor

**Rafael Nascimento Andrade**

Desenvolvedor Backend Java

* Java
* Spring Boot
* Microsserviços
* OpenFeign
* Resilience4j
* PostgreSQL
* APIs REST
