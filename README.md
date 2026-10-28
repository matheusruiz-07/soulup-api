# SoulUp API

API REST do SoulUp, feita em Spring Boot. O front-end do projeto consome esta API para cadastrar usuários, registrar ações sustentáveis, concluir missões, resgatar recompensas e conversar com o avatar Soul.

Equipe SoulUp, turma 1TDSPO

- Matheus Ruiz Giatti, RM 570701
- Matheus Leite Souza das Virgens, RM 573416

## Links

| Item | Link |
|---|---|
| API publicada | https://soulup-api-7h17.onrender.com |
| Repositório da API | https://github.com/matheusruiz-07/soulup-api |
| Front-end | https://github.com/matheusruiz-07/Challenge-1TDSPO |

## Tecnologias

- Java 21
- Spring Boot 4.1.1 (Spring Web MVC)
- JDBC com o driver Oracle (ojdbc17), sem JPA
- Banco Oracle da FIAP, tabelas `t_su_*`
- Maven, pelo wrapper `mvnw` que já vem no projeto

## Organização do código

```
br.com.soulup
├── model       entidades e regras de domínio (nível, saldo, impacto)
├── dao         acesso ao banco: ConnectionFactory, BaseDAO e um DAO por tabela
├── bo          validações e regras de negócio, com controle de transação
├── resource    endpoints REST
├── config      CorsFilter
└── exception   exceções da aplicação e tratamento centralizado de erros
```

O caminho de uma requisição é sempre o mesmo: `Resource` recebe o JSON, chama o `BO`, que valida e abre a conexão, e o `BO` usa os `DAO`s para ler e gravar. Quando uma operação mexe em mais de uma tabela (por exemplo, registrar uma ação grava o registro e soma pontos no usuário), tudo roda numa transação só: ou grava tudo, ou nada.

## Como rodar

Requisitos: JDK 21 ou mais novo e acesso ao Oracle da FIAP. Qualquer IDE serve (IntelliJ, VS Code, Eclipse); os comandos abaixo funcionam no terminal.

1. Clone o repositório e entre na pasta `soulup-api`.
2. Defina a senha do banco numa variável de ambiente. Ela não fica no código para não ir parar no GitHub.

   macOS ou Linux:
   ```
   export DB_PASSWORD=sua_senha
   ```
   Windows (PowerShell):
   ```
   $env:DB_PASSWORD="sua_senha"
   ```
   Opcionalmente, `DB_URL` e `DB_USER` trocam o servidor e o usuário. O padrão é `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL` e `rm570701`.
3. Suba a API:
   ```
   ./mvnw spring-boot:run
   ```
   No Windows, use `mvnw.cmd spring-boot:run`.
4. Abra `http://localhost:8080/`. A resposta lista os recursos disponíveis.

No IntelliJ, também dá para importar a pasta como projeto Maven, colocar `DB_PASSWORD` em Run > Edit Configurations > Environment variables e rodar a classe `SoulupApiApplication`.

## Deploy

O deploy é feito no Render a partir do `Dockerfile` e do `render.yaml`. As variáveis `DB_PASSWORD` (e, se precisar, `DB_URL` e `DB_USER`) são cadastradas no painel do Render, nunca no repositório. O Render informa a porta pela variável `PORT`, que a aplicação já lê.

No plano gratuito o Render desliga a API depois de 15 minutos sem uso. A primeira requisição depois disso leva cerca de 50 segundos para responder; as seguintes são normais.

## Endpoints

Todas as respostas são JSON. Em caso de erro, o corpo segue o formato abaixo:

```json
{ "status": 404, "erro": "Not Found", "mensagem": "Usuário 99 não encontrado.", "caminho": "/usuarios/99", "momento": "2026-10-20T22:07:09" }
```

Códigos usados: `200` OK, `201` criado, `204` excluído sem corpo, `400` dado inválido, `404` não encontrado, `409` regra de negócio impediu a operação, `500` erro no servidor ou no banco.

### Usuários e avatar

| Verbo | URI | O que faz | Status |
|---|---|---|---|
| GET | `/usuarios` | Lista os usuários, do maior para o menor em experiência | 200 |
| GET | `/usuarios/{id}` | Busca um usuário | 200, 404 |
| POST | `/usuarios` | Cadastra usuário e cria o avatar Soul junto | 201, 400, 409 |
| PUT | `/usuarios/{id}` | Atualiza nome e e-mail | 200, 400, 404, 409 |
| DELETE | `/usuarios/{id}` | Exclui o usuário e todo o histórico dele | 204, 404 |
| GET | `/usuarios/{id}/avatar` | Busca o avatar do usuário | 200, 404 |
| PUT | `/usuarios/{id}/avatar` | Renomeia o avatar ou muda a personalidade | 200, 400, 404 |
| GET | `/usuarios/{id}/interacoes` | Histórico de conversas com o avatar | 200, 404 |
| POST | `/usuarios/{id}/interacoes` | Pede sugestão, motivação ou progresso ao avatar | 201, 400, 404 |

### Catálogo de ações e registros

| Verbo | URI | O que faz | Status |
|---|---|---|---|
| GET | `/categorias` | Lista as categorias de ação | 200 |
| GET | `/acoes?categoria={id}` | Lista as ações, com filtro opcional por categoria | 200, 400 |
| GET | `/acoes/{id}` | Busca uma ação | 200, 404 |
| POST | `/acoes` | Cadastra ação no catálogo | 201, 400 |
| PUT | `/acoes/{id}` | Atualiza ação | 200, 400, 404 |
| DELETE | `/acoes/{id}` | Exclui ação que ainda não foi registrada por ninguém | 204, 404, 409 |
| GET | `/usuarios/{id}/registros` | Ações registradas pelo usuário | 200, 404 |
| POST | `/usuarios/{id}/registros` | Registra ação: pontua pelo catálogo, classifica o impacto e soma pontos | 201, 400, 404 |
| GET | `/registros/{id}` | Busca um registro | 200, 404 |
| PUT | `/registros/{id}` | Corrige a descrição do registro | 200, 400, 404 |
| DELETE | `/registros/{id}` | Exclui o registro e estorna os pontos | 204, 404, 409 |

### Missões

| Verbo | URI | O que faz | Status |
|---|---|---|---|
| GET | `/missoes` | Lista as missões | 200 |
| GET | `/missoes/{id}` | Busca uma missão | 200, 404 |
| POST | `/missoes` | Cadastra missão | 201, 400, 409 |
| PUT | `/missoes/{id}` | Atualiza missão | 200, 400, 404, 409 |
| DELETE | `/missoes/{id}` | Exclui missão sem participantes | 204, 404, 409 |
| GET | `/usuarios/{id}/missoes` | Missões com o status do usuário (pendente ou concluída) | 200, 404 |
| POST | `/usuarios/{id}/missoes/{idMissao}/conclusao` | Conclui a missão e credita os pontos | 200, 404, 409 |

### Recompensas e resgates

| Verbo | URI | O que faz | Status |
|---|---|---|---|
| GET | `/recompensas?disponivel=true` | Lista as recompensas, com filtro opcional | 200 |
| GET | `/recompensas/{id}` | Busca uma recompensa | 200, 404 |
| POST | `/recompensas` | Cadastra recompensa | 201, 400 |
| PUT | `/recompensas/{id}` | Atualiza recompensa | 200, 400, 404 |
| DELETE | `/recompensas/{id}` | Exclui recompensa nunca resgatada | 204, 404, 409 |
| GET | `/usuarios/{id}/resgates` | Resgates feitos pelo usuário | 200, 404 |
| POST | `/usuarios/{id}/resgates` | Troca saldo por recompensa | 201, 400, 404, 409 |

### Exemplos de corpo

```
POST /usuarios
{ "nome": "Ana Souza", "email": "ana@exemplo.com" }

POST /usuarios/1/registros
{ "idAcao": 3, "descricao": "Fui de metrô para o trabalho" }

POST /usuarios/1/interacoes
{ "tipo": "SUGESTAO" }

POST /usuarios/1/resgates
{ "idRecompensa": 2 }

POST /missoes
{ "nome": "Semana sem carro", "descricao": "Use transporte público por 5 dias", "pontosRecompensa": 150, "dificuldade": "MEDIO" }

POST /recompensas
{ "nome": "Desconto de R$ 10 na conta de luz", "custoPontos": 300, "tipo": "DESCONTO", "disponivel": true }
```

Valores aceitos: `dificuldade` = `FACIL`, `MEDIO`, `DIFICIL`; `tipo` da recompensa = `DESCONTO`, `BRINDE`, `EXPERIENCIA`; `tipo` da interação = `SUGESTAO`, `MOTIVACAO`, `PROGRESSO`, `CONVERSA` (este último exige `mensagemUsuario`).
