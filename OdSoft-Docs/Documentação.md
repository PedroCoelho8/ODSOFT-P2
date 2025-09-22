# Documentação ARQSOFT P2 - Requisitos Não Funcionais

O segundo projeto tem como objetivo principal a migração de um sistema base para uma arquitetura distribuída baseada em microserviços, garantindo escalabilidade, independência e operação contínua. Esta mudança vem acompanhada de requisitos não funcionais que norteiam as diretrizes para o desenvolvimento e manutenção do sistema. Abaixo estão descritos os principais requisitos não funcionais para este projeto:

## **Desempenho**
- O sistema deve aumentar o desempenho em pelo menos 25% durante períodos de alta demanda (>Y solicitações/tempo).

## **Uso de Recursos**
- O sistema deve utilizar hardware de forma parcimoniosa, ajustando a demanda conforme as necessidades de execução.
- Picos de demanda (>Y solicitações/tempo) devem ser raros, mas suportados eficientemente.

## **Releasability**
- Cada aplicação deve manter (ou melhorar) sua capacidade de lançar versões.

## **Independência de Implantação**
- Apesar da descentralização/distribuição e do desenvolvimento independente das aplicações, cada aplicação deve poder ser implantada de forma independente.

## **Ambientes de Desenvolvimento**
- Devem ser implementados e adotados ambientes para Desenvolvimento, Teste e Produção.

## **Rollback Automático**
- Deve ser possível realizar rollback automático para versões anteriores de cada serviço.

## **Operação Contínua**
- O sistema não pode sofrer downtime durante a atualização de um serviço.

---

### **Validação dos Requisitos**
Para garantir que os requisitos acima sejam atendidos, métricas específicas serão monitoradas, incluindo:
- Tempo médio de resposta da API.
- Utilização de recursos em períodos de alta e baixa demanda.
- Utilização de pipelines no Jenkins, tanto em ambientes locais quanto em máquinas virtuais, para monitorar e facilitar processos de CI/CD.


## System as Is
Esta secção apresenta uma análise do sistema atual (System as Is) utilizando o modelo arquitetural 4+1. Vão ser explorados os aspectos principais da arquitetura monolítica existente, as suas funcionalidades e limitações, com foco em identificar os desafios que motivam a transição para uma arquitetura baseada em microserviços.

## Modelo de Arquitetura 4+1
O modelo 4+1 é uma abordagem para a documentação e análise de arquiteturas de software, organizada em cinco perspetivas complementares:
- **Vista Lógica:**
    - Foca na funcionalidade do sistema, mostrando como os elementos se relacionam para atender aos requisitos.
- **Vista de Processo:**
    - Descreve os aspetos dinâmicos, como a comunicação e o comportamento em tempo de execução.
- **Vista de Implementação:**
    - Representa a estrutura estática do software, incluindo a organização dos módulos e componentes.
- **Vista Física:**
    - Detalha como o sistema será implementado na infraestrutura física, como servidores e redes.
- **Vista de Casos de Uso:**
    - Conecta as outras vistas, descrevendo os requisitos funcionais e como os utilizadores interagem com o sistema.


Neste caso vamos apenas abordar as 4 primeiras vistas, tendo em conta que o principal objetivo deste tópico é uma descrição técnica e da arquitetura do sistema.

### Vista Lógica
#### Vista Lógica Nível 1
![VLN1 System as Is.png](Imagens%2FSystem-As-Is%2FVLN1%20System%20as%20Is.png)
#### Vista Lógica Nível 2
![VLN2 System as is.png](Imagens%2FSystem-As-Is%2FVLN2%20System%20as%20is.png)
#### Vista Lógica Nível 3
![VLN3 System as is.png](Imagens%2FSystem-As-Is%2FVLN3%20System%20as%20is.png)


### Vista de Implementação
#### Vista de Implementação Nível 1
![VIN1 System as is.png](Imagens%2FSystem-As-Is%2FVIN1%20System%20as%20is.png)
#### Vista de Implementação Nível 2
![VIN2 System as is.png](Imagens%2FSystem-As-Is%2FVIN2%20System%20as%20is.png)
#### Vista de Implementação Nível 3
![VIN3 System as is.png](Imagens%2FSystem-As-Is%2FVIN3%20System%20as%20is.png)
#### Vista de Implementação Nível 4
![VIN4 System as is.png](Imagens%2FSystem-As-Is%2FVIN4%20System%20as%20is.png)


### Vista Física
![VFN1 System as is.png](Imagens%2FSystem-As-Is%2FVFN1%20System%20as%20is.png)

### Vista Lógica x Vista de Implementação
![VLxVI.png](Imagens%2FSystem-As-Is%2FVLxVI.png)

## System To Be

### Vista Lógica
#### Vista Lógica Nível 2
![VLN2 System to be.png](Imagens%2FSystem-To-Be%2FVista%20Logica.svg)

### Vista Física
#### Vista Física
![VLN2 System to be.png](Imagens%2FSystem-To-Be%2FVista%20Fisica.svg)


# Pipeline

## Local
A pipeline local está dividida em 15 stages, incluindo uma secção de ambiente (environment) e inclui também um serviço SMTP capaz de enviar um email de notificação aos utilizadores após a execução da mesma, com informações relevantes para todos os stakeholders da aplicação LMS a ser desenvolvida.

Na configuração do ambiente da pipeline, são definidas informações essenciais, como o repositório Git, credenciais, e o nome da imagem a ser criada no Docker. Estas informações definidas no ambiente serão posteriormente utilizadas em uma ou mais stages da pipeline.<br>

```groovy
environment {
  MAVEN_HOME = 'C:\\Program Files\\apache-maven-3.9.9'
  GIT_REPO_URL = 'https://github.com/leonardogomes3/ODSOFT2024-ProjectP2-1240485-1211239.git/'
  GIT_BRANCH = 'main'
  CREDENTIALS_ID = 'GITHUB_TOKEN'
  SERVER_PORT = '8084'
  IMAGE_NAME = 'lmsuserss'
  IMAGE_TAG = 'latest'
  GHCR_IMAGE = 'ghcr.io/leonardogomes3/odsoft2024-projectp2-1240485-1211239:lmsuserss-latest'
  RECIPIENT_EMAIL = 'odsoft2024@gmail.com'
}
```
### Stages
#### Debug Environment
Nesta stage, verificamos as variáveis de ambiente definidas no sistema para garantir que tudo está corretamente configurado.
```groovy
stage('Debug Environment') {
  steps {
    bat 'set'
  }
}
```

#### Check Docker
Verifica a versão do Docker instalada, assegurando que o ambiente está preparado para criar imagens Docker.

```groovy
stage('Check Docker') {
    steps {
        bat 'docker --version'
    }
}
```

#### Checkout
Faz o checkout do repositório Git a partir do URL especificado, utilizando as credenciais definidas para autenticação.

```groovy
stage('Checkout') {
    steps {
        checkout([$class: 'GitSCM', branches: [[name: "${GIT_BRANCH}"]],
                  userRemoteConfigs: [[url: "${GIT_REPO_URL}", credentialsId: "${CREDENTIALS_ID}"]]])
    }
}
```

#### Clean
Na stage de limpeza, executa-se um comando mvn clean para limpar qualquer artefacto de builds anteriores no diretório do projeto lms-authnusers.

```groovy
stage('Clean') {
    steps {
        dir('lms-authnusers') {
            bat """
                "${MAVEN_HOME}\\bin\\mvn" clean
            """
        }
    }
}
```

#### Package
Cria o pacote do projeto, ignorando os testes para acelerar o processo de build.

```groovy
stage('Package') {
    steps {
        dir('lms-authnusers') {
            bat """
                "${MAVEN_HOME}\\bin\\mvn" package -DskipTests
            """
        }
    }
}
```

#### Test
Executa os testes definidos no projeto. Esta stage é crucial para garantir que o código está funcional antes de prosseguir para o próximo passo.
```groovy
stage('Test') {
    steps {
        dir('lms-authnusers') {
            bat """
                "${MAVEN_HOME}\\bin\\mvn" test
            """
        }
    }
}
```

#### Build Docker Image
Na stage de construção da imagem Docker, cria-se a imagem a partir do Dockerfile do projeto, com o nome e tag definidos.
```groovy
stage('Build Docker Image') {
    steps {
        dir('lms-authnusers') {
            bat "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
        }
    }
}
```

#### Login to GHCR
Faz login no GitHub Container Registry (GHCR) para permitir o envio da imagem Docker para o repositório remoto.

```groovy
stage('Login to GHCR') {
    steps {
        bat """
            echo ${GITHUB_TOKEN_2} | docker login ghcr.io -u ${env.GIT_USERNAME} --password-stdin
        """
    }
}
```

#### Push Docker Image
Marca e envia a imagem Docker para o GitHub Container Registry (GHCR) após a sua criação.

```groovy
stage('Push Docker Image') {
    steps {
        bat "docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${GHCR_IMAGE}"
        bat "docker push ${GHCR_IMAGE}"
    }
}
```

#### Notify User for Approval
Envia um email para notificar os utilizadores de que a imagem Docker foi criada e precisa de aprovação para prosseguir com o deploy.

```groovy
stage('Notify User for Approval') {
    steps {
        emailext (
            subject: "LMSUserss Jenkins Pipeline - Deployment Ready",
            body: """A pipeline Jenkins do LMSUserss foi construída com sucesso e a imagem Docker foi enviada.
                    Por favor, aprove o deploy no Jenkins [aqui](${BUILD_URL}input/) para continuar.""",
            to: 'odsoft2024@gmail.com'
        )
    }
}
```

#### Wait for Approval
A pipeline aguarda aprovação do utilizador antes de prosseguir com o deploy. O utilizador pode optar por continuar ou abortar o processo.

```groovy
stage('Wait for Approval') {
    steps {
        script {
            catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                echo "A pipeline está à espera da sua aprovação. Clique no link abaixo para continuar ou abortar o deploy."
                def userInput = input(
                    message: "Deseja continuar com o deploy?",
                    parameters: [
                        choice(name: 'Approve Deployment', choices: ['Yes', 'No'], description: 'Selecione "Yes" para continuar ou "No" para abortar.')
                    ]
                )
                if (userInput == 'No') {
                    currentBuild.result = 'ABORTED'
                    error("Deploy abortado pelo utilizador.")
                }
            }
        }
    }
}
```

#### Deploy
Realiza o deploy do microserviço no ambiente Docker.
```groovy
stage('Deploy') {
    steps {
        script {
            dir('lms-authnusers') {
                bat """
                "C:\\Program Files\\Git\\bin\\bash.exe" ./runL.sh 1
                """
            }
        }
    }
}
```

#### Run Tests Against Container
Executa os testes contra o microserviço em execução no Docker.
```groovy
stage('Run Tests Against Container') {
    steps {
        dir('lms-authnusers') {
            script {
                bat """
                    "%MAVEN_HOME%\\bin\\mvn" verify -Dtest.container.url=http://localhost:%SERVER_PORT%
                """
            }
        }
    }
}
```

#### Scale Up and Scale Down
Permite ao utilizador ajustar o número de instâncias do microserviço, escalando para cima ou para baixo conforme necessário.
```groovy
stage('Scale up and Scale Down') {
    steps {
        script {
            def scaleCount = input(
                message: 'Quantas instâncias deseja escalonar (para cima ou para baixo)?',
                parameters: [
                    choice(name: 'COUNT', choices: ['1', '2', '3', '4', '5'], description: 'Número de instâncias por serviço')
                ]
            )

            dir('lms-authnusers') {
                bat """
                "C:\\Program Files\\Git\\bin\\bash.exe" ./runL.sh ${scaleCount}
                """
            }
        }
    }
}
```

#### Shutdown Microservices with shutdown.sh
Desliga os microserviços em execução utilizando o script shutdown.sh.

```groovy
stage('Shutdown Microservices with shutdown.sh') {
  steps {
    script {
      dir('lms-authnusers') {
        bat """
                "C:\\Program Files\\Git\\bin\\bash.exe" ./shutdown.sh
                """
      }
    }
  }
}
```

#### Pós-execução
##### Sucesso
Quando a pipeline termina com sucesso, um email é enviado para notificar os utilizadores do sucesso do deploy.
```groovy
post {
    success {
        echo 'Pipeline completada com sucesso!'
        emailext (
            subject: "Serviço ${IMAGE_NAME} Deployed",
            body: """
            Olá,

            O serviço foi implantado com sucesso.

            Pode aceder ao serviço através do seguinte link: ${RUN_TESTS_DISPLAY_URL}.

            Atenciosamente,
            Jenkins
            """,
            to: "${RECIPIENT_EMAIL}",
            from: "${RECIPIENT_EMAIL}",
        )
    }
```

#### Falha
Se a pipeline falhar, um email é enviado com informações sobre o erro.

```groovy
    failure {
        echo 'A pipeline falhou!'
```

# Performance 

De forma a entender melhoria da performance entre a aplicação base e a aplicação em microservicos foram realizados testes através da ferramenta jmeter. Nestes testes simulamos 4 diferentes cenários para entender como as aplicações se comportavam.

Esses 4 cenários foram um Load Test, um Soak Test, um Stress Test e por fim um teste para verificar o tempo que demora a processar 5000 pedidos.

Para o Load test consideramos o seguinte cenário, onde simulamos uma carga esperada de utilizadores sobre o sistema e analisamos o seu comportamento, considerando erros todos os pedidos que excediam os 3 segundos.
<br>
![Load Test.png](Imagens%2FPerformance%2FLoad%20Test.png)

Para o Soak test consideramos o seguinte cenário, onde simulamos uma carga constante e contínua sobre o sistema por um longo período e analisamos seu comportamento para identificar possíveis falhas ou degradações ao longo do tempo,  considerando erros todos os pedidos que excediam os 3 segundos.
<br>
![Soak Test.png](Imagens%2FPerformance%2FSoak%20Test.png)

Para o Stress test consideramos o seguinte cenário, onde simulamos uma carga extrema, além do esperado para o sistema, e analisamos seu comportamento ao atingir ou ultrapassar seus limites de capacidade, considerando erros todos os pedidos que excediam os 5 segundos.
<br>
![Stress Test.png](Imagens%2FPerformance%2FStress%20Test.png)


## Performance Aplicação Monolítica
Os resultados da aplicação monolítica foram os seguintes

Load Test:
<br>
![Load Result.png](Imagens%2FPerformance%2FBase%2FLoad%20Result.png)
Soak Test:
<br>
![Soak Result.png](Imagens%2FPerformance%2FBase%2FSoak%20Result.png)
Stress Test:
<br>
![Stress Result.png](Imagens%2FPerformance%2FBase%2FStress%20Result.png)
5000 Pedidos:
<br>
![5000Pedidos Results.png](Imagens%2FPerformance%2FBase%2F5000Pedidos%20Results.png)
Para calcularmos o tempo total do pedido, basta utilizarmos o **Throughput** (métrica que identifica o número de pedidos por segundo) e o **número total de pedidos**.

---

**Cálculo**:

**Tempo Total** = Número de Pedidos / Throughput

**Substituindo os valores**:  
**Tempo Total** = 5000 / 152.8 ≈ 32.71 segundos

---

### **Tempo Total: ~32.71 segundos**

## Performance da Aplicação baseada em Microserviços

## 2 Instancias do microserviço Books
Load Test:
<br>
![Load Result MicroServicos.png](Imagens%2FPerformance%2F2%20Instancias%2FLoad%20Result%20MicroServicos.png)
Soak Test:
<br>
![Soak Result MicroServicos.png](Imagens%2FPerformance%2F2%20Instancias%2FSoak%20Result%20MicroServicos.png)
Stress Test:
<br>
![Stress Result MicroServicos.png](Imagens%2FPerformance%2F2%20Instancias%2FStress%20Result%20MicroServicos.png)
5000 Pedidos:
<br>
![5000Pedidos Results MicroServicos.png](Imagens%2FPerformance%2F2%20Instancias%2F5000Pedidos%20Results%20MicroServicos.png)
Para calcularmos o tempo total do pedido, basta utilizarmos o **Throughput** (métrica que identifica o número de pedidos por segundo) e o **número total de pedidos**.
---

**Cálculo**:

**Tempo Total** = Número de Pedidos / Throughput

**Substituindo os valores**:  
**Tempo Total** = 5000 / 213 ≈ 23.44 segundos

---

### **Tempo Total: ~23.44 segundos**

## 3 Instancias do microserviço Books
Load Test:
<br>
![Load Result MicroServicos 3 Instancias.png](Imagens%2FPerformance%2F3%20Instancias%2FLoad%20Result%20MicroServicos%203%20Instancias.png)
Soak Test:
<br>
![Soak Result MicroServicos 3 Instancias.png](Imagens%2FPerformance%2F3%20Instancias%2FSoak%20Result%20MicroServicos%203%20Instancias.png)
Stress Test:
<br>
![Stress Result MicroServicos 3 Instancias.png](Imagens%2FPerformance%2F3%20Instancias%2FStress%20Result%20MicroServicos%203%20Instancias.png)
5000 Pedidos:
<br>
![5000Pedidos Results MicroServicos 3 Instancias.png](Imagens%2FPerformance%2F3%20Instancias%2F5000Pedidos%20Results%20MicroServicos%203%20Instancias.png)
Para calcularmos o tempo total do pedido, basta utilizarmos o **Throughput** (métrica que identifica o número de pedidos por segundo) e o **número total de pedidos**.
---

**Cálculo**:

**Tempo Total** = Número de Pedidos / Throughput

**Substituindo os valores**:  
**Tempo Total** = 5000 / 172 ≈ 28.94 segundos

---

### **Tempo Total: ~28.94 segundos**


## Conclusão
Com base nos resultados, observa-se uma performance consistente e positiva do endpoint em todas as situações testadas, tanto na aplicação monolítica quanto na arquitetura baseada em microserviços.

Embora os resultados sejam próximos, destaca-se uma melhoria de aproximadamente 28% no tempo de resposta para 5000 requisições, conforme demonstrado no cálculo abaixo. Além disso, os tempos médios registados são amplamente similares aos da aplicação base. A baixa porcentagem de erros reforça que a aplicação suporta bem os cenários que simulamos.

---

**Fórmula:**

Melhoria (%) = (Tempo inicial - Tempo final) / Tempo inicial × 100

---

**Cálculo:**

Tempo Inicial = 32.71 segundos  
Tempo Final = 23.44 segundos

Melhoria (%) = (32.71 - 23.44) / 32.71 × 100

Substituindo os valores:  
Melhoria (%) = 9.27 / 32.71 × 100 ≈ 28,34%

---

