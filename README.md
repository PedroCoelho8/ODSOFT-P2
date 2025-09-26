# ODSOFT-P2 — DevOps e Independência de Deploys no LMS Distribuído (Projeto 2)

## Contexto

Este repositório corresponde ao Projeto 2 da unidade curricular de **Software Development Organization** (2024/2025). O sistema LMS evoluiu para uma arquitetura distribuída, composta por várias aplicações independentes, cada uma podendo ser desenvolvida, testada e lançada de forma autónoma. Esta evolução traz desafios de integração, compatibilidade de interfaces e garantia de qualidade entre serviços.

## Objetivo

- **Permitir a releasability e deployability independentes de cada aplicação** do sistema LMS, detetando automaticamente problemas de integração.
- **Automatizar ambientes de desenvolvimento, teste e produção**, com processos de rollback e zero downtime nos deploys.
- **Notificar utilizadores** (por email ou outra via) com links para aceitação/rejeição de serviços em ambiente de staging, antes de promoção para produção.

## Requisitos

### Não-funcionais
- Melhoria de performance (>25% em situações de elevada procura)
- Utilização eficiente de hardware (auto-scaling, uso parcimonioso)
- Deploy e release independentes para cada aplicação
- Rollback automático de cada serviço para versões anteriores
- Zero downtime em atualizações
- Ambientes separados: desenvolvimento, teste, produção

### Funcionais
- Pipeline deve notificar o utilizador que lançou o deploy, fornecendo o link do serviço para aceitação/rejeição em staging

## Abordagem e Soluções Técnicas

- **Pipelines CI/CD por serviço** usando Jenkins:
  - Build, teste (unitário/integrado/aceitação), release e deploy totalmente independentes por aplicação
  - Deploy automatizado em ambientes distintos (dev, test, prod)
  - Rollback automático (ex: via tags Docker, Jenkins plugins, scripts customizados)
  - Zero downtime implementado via blue-green deployment ou rolling updates
- **Deteção de problemas de integração**:
  - Testes de integração contínua entre serviços antes da promoção para produção
  - Geração de relatórios e bloqueio automático de deploys em caso de falha
- **Notificação automatizada**:
  - Envio de emails (ou Slack, Teams, etc.) ao utilizador trigger, com link para serviço em staging
  - Aprovação manual antes de promover a produção
- **Ambientes reprodutíveis**:
  - Docker/Docker Compose para isolar aplicações e respetivas dependências
  - Gestão de versões e releases por serviço (ex: tags Docker, versionamento semântico)

## Tecnologias Utilizadas

- **Jenkins** (CI/CD pipelines multi-serviço)
- **Docker & Docker Compose** (contenorização e orquestração local)
- **Java 17, Spring Boot** (backend dos serviços)
- **Maven** (build)
- **JUnit, PIT, Mockito** (testes automáticos)
- **SonarQube** (análise estática)
- **Mail/SMTP, Slack, ou Teams API** (notificações)
- **GitHub** (SCM e triggers)

## Organização do Repositório

- `service-*/` — Pastas para cada microserviço (ex: `service-books/`, `service-authors/`, etc.)
- `.jenkinsfile-*` — Pipelines Jenkins para cada serviço
- `docker-compose.yaml` — Orquestração de ambientes multi-serviço
- `Docs/` — Relatórios técnicos, evidências e documentação geral
- `HELP.md` — Instruções e FAQ

## Racional

A aposta na independência de release e deployment por serviço permite uma evolução mais ágil, segura e escalável do sistema, suportando inovação contínua e mitigando riscos de integração. O pipeline automatizado, aliado ao rollback e zero downtime, garante fiabilidade máxima e resposta rápida a falhas ou regressões.

---

> **Software Development Organization, 2024/2025**  
