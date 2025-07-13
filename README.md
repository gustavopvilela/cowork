# CoWork: gerenciamento de espaços de _coworking_ com Spring Boot

![Backend](https://img.shields.io/badge/IFMG-Desenvolvimento%20Backend-b43757)  [![Spring](https://img.shields.io/badge/Spring%20Boot-3.5.3-d21f3c)](https://spring.io/projects/spring-boot) [![Java](https://img.shields.io/badge/Java-21.0.6-e0115f)](https://www.java.com/)

## Introdução

Segundo trabalho prático da disciplina de Desenvolvimento Backend, desenvolvido pelos alunos Gustavo Henrique Pereira Vilela e Iasmim Garcia Castro.

**CoWork** é um sistema de gerenciamento de espaços de trabalho compartilhados, desenvolvido como uma API baseada nos princípios RESTful. Ele oferece funcionalidades para administração de espaços, serviços, usuários e reservas, além de recursos de autenticação e autorização baseados nos papéis (_roles_) do usuário.

## Principais funcionalidades

O projeto é dividido em módulos que representam as principais entidades do sistema.

### Usuários
- **CRUD de usuários:** criação, leitura, atualização e exclusão de usuários. 
- **Autenticação e autorização:**
  - Sistema de login com autenticação via OAuth2 e JWT, utilizando um _custom grant type_ de senha.
  - Dois níveis de acesso: `ROLE_ADMIN` e `ROLE_PROFISSIONAL`.

### Espaços
- **CRUD de espaços:** gerenciamento completo dos espaços do coworking, incluindo nome, descrição, capacidade e tipo (como mesa individual, sala de reunião, etc.). 
- **Consulta de disponibilidade:** endpoint para verificar quais espaços estão livres em um determinado período de tempo, com filtros opcionais por tipo e capacidade.

### Reservas
- **CRUD de reservas:** agendamento e gerenciamento de reservas, associando um usuário a um espaço em um período específico. 
- **Cancelamento em lote:** funcionalidade para administradores cancelarem múltiplas reservas dentro de um intervalo de datas.

### Avaliações
- **Sistema de avaliação:** usuários podem avaliar os espaços após a conclusão de uma reserva, com nota e comentário. 
- **Consulta de avaliações:** endpoints para listar avaliações por espaço ou por usuário, além de um endpoint para o usuário autenticado ver suas próprias avaliações.

### Relatórios
- **Taxa de ocupação:** relatório que calcula a taxa de ocupação percentual dos espaços em um determinado mês e ano. 
- **_Ranking_ de serviços:** endpoint que lista os serviços adicionais mais utilizados nas reservas, em ordem decrescente.

## Tecnologias utilizadas
Para o backend, foram utilizados:
- **Java 21**; 
- **Spring Boot 3.5.3**; 
- **Spring Data JPA:** para persistência dos dados; 
- **Spring Security:** para autenticação e autorização com OAuth2 e JWT.

Para o banco de dados:
- **H2 Database:** banco de dados em memória para ambiente de desenvolvimento e testes.

Para a documentação:
- **Springdoc OpenAPI (_Swagger_):** para documentação interativa da API.

Build e dependências:
- **Maven**.

## Endpoints da API
A seguir, uma visão geral dos principais endpoints da API. Para a documentação completa e interativa, acesse `/swagger-ui/index.html` com a aplicação em execução.

### Operações padrão de CRUD
Todas as entidades têm as operações básicas de CRUD: criação, leitura, atualização e deleção.

### Operações especiais
- Endpoint de autenticação para obter um token JWT;
- Retorno dos dados do usuário autenticado;
- Lista dos espaços disponíveis por período, tipo e capacidade; 
- Lista das reservas do usuário autenticado; 
- O usuário pode avaliar uma reserva concluída; 
- Cancelamento em lote todas as reservas em um determinado período; 
- Retorno da taxa de ocupação dos espaços para um dado mês e ano;
- Exibição do ranking dos serviços adicionais mais utilizados;
- Listagem das avaliações de um espaço específico;

## Como executar o projeto
1. **Pré-requisitos:** Java 21+, Maven 3.9.9+;
2. **Clonando o repositório:**
```bash
git clone https://github.com/gustavopvilela/cowork.git
cd cowork
```
3. **Executando a aplicação:** caso não queira executar diretamente em sua IDE de preferência, execute:
```bash
mvn spring-boot:run
```
4. **API:** estará disponível em `http://localhost:8080`.
5. **Documentação Swagger:** estará disponível em `http://localhost:8080/swagger-ui/index.html`.
6. **Console do banco H2:** estará disponível em `http://localhost:8080/h2-console`, com as credenciais definidas em `src/main/resources/application-test.properties`.

## Dados iniciais
O sistema é inicializado com um conjunto de dados para facilitar os testes, que podem ser encontrados em `src/main/resources/import.sql`.
- **Roles:** `ROLE_ADMIN` e `ROLE_PROFISSIONAL`;
- **Usuários:** diversos usuários com senhas pré-definidas e criptografadas;
- **Espaços:** uma variedade de espaços, como mesas individuais, salas de reunião e escritórios privativos; 
- **Serviços:** mais de 20 serviços adicionais, como "Café Premium", "Internet de Alta Velocidade" e "Estacionamento";
- **Reservas e avaliações:** dezenas de registros de reservas e avaliações para popular o sistema e permitir o teste dos relatórios.