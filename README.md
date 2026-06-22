## 🛡️ Resiliência e Tolerância a Falhas

O projeto implementa mecanismos de resiliência utilizando **Resilience4j**, através dos padrões:

* Circuit Breaker
* Retry
* Scheduler para reprocessamento

Esses mecanismos garantem maior disponibilidade do sistema e evitam a perda de informações quando um microsserviço está temporariamente indisponível.

---

## 🔄 Fluxo de Criação de Cliente e Conta

Quando um novo cliente é cadastrado no **Customer Service**, o sistema realiza automaticamente uma chamada ao **Bank Account Service** para criar sua conta bancária padrão.

```text
Customer Service
       │
       ▼
Bank Account Service
```

---

### Cenário Normal

```text
1. Cliente é cadastrado
2. Customer Service chama o Bank Account Service
3. Conta bancária é criada
4. Operação finalizada com sucesso
```

```text
┌──────────────────┐
│ Customer Service │
└─────────┬────────┘
          │
          ▼
┌──────────────────┐
│ Bank Account API │
└──────────────────┘
```

---

### Cenário de Falha

Caso o **Bank Account Service** esteja indisponível:

```text
1. Cliente é cadastrado
2. Chamada para o serviço de contas falha
3. Retry é executado automaticamente
4. Circuit Breaker protege o sistema
5. Dados da conta são armazenados como pendentes
6. Cliente continua sendo criado normalmente
```

```text
┌──────────────────┐
│ Customer Service │
└─────────┬────────┘
          │
          ▼
       Falha
          │
          ▼
  Conta Pendente
          │
          ▼
 Banco de Pendências
```

---

## 🔁 Retry

Antes de considerar a operação indisponível, o sistema realiza novas tentativas automáticas de comunicação com o microsserviço de contas.

Objetivos:

* Tratar falhas temporárias
* Reduzir erros transitórios
* Aumentar a taxa de sucesso das integrações

---

## ⚡ Circuit Breaker

O Circuit Breaker monitora as chamadas ao microsserviço de contas.

Quando uma quantidade configurada de falhas é atingida:

```text
CLOSED
   │
   ▼
Falhas consecutivas
   │
   ▼
OPEN
   │
   ▼
Bloqueia novas chamadas
```

Após um período de espera:

```text
OPEN
   │
   ▼
HALF_OPEN
   │
   ▼
Teste de recuperação
```

Caso o serviço volte a responder:

```text
HALF_OPEN
   │
   ▼
CLOSED
```

---

## ⏰ Reprocessamento Automático

Para evitar perda de dados, as contas que não puderam ser criadas são armazenadas em uma tabela de pendências.

```text
Pending Accounts
├── customerId
├── accountType
├── createdAt
└── status
```

Um Scheduler executa periodicamente a tentativa de criação dessas contas.

Fluxo:

```text
Scheduler
    │
    ▼
Busca contas pendentes
    │
    ▼
Tenta criar novamente
    │
 ┌──┴──┐
 ▼     ▼
Sucesso Falha
 │       │
 ▼       ▼
Remove Mantém pendente
```

Essa estratégia garante consistência eventual entre os microsserviços mesmo em cenários de indisponibilidade temporária.
