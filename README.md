# 📌 API REST de Aluguel de Automóveis

API REST construída com **Spring Boot** para gerenciamento de um sistema de aluguel de automóveis — incluindo usuários, automóveis e aluguéis. 

---

## 🧠 Descrição do Projeto

Esse projeto é uma **API RESTful** desenvolvida para permitir o gerenciamento de um sistema de aluguel de carros. A aplicação segue boas práticas de desenvolvimento de APIs, incluindo autenticação via JWT, estrutura em camadas, validação de entrada, testes automatizados e documentação interativa. 

---

## 🚀 Tecnologias Utilizadas

| Tecnologia                         | Utilização                                                                                         |
| ---------------------------------- | -------------------------------------------------------------------------------------------------- |
| **Java + Spring Boot**             | Framework principal para a criação e execução da API REST. ([GitHub][1])                           |
| **Spring WebFlux + WebTestClient** | Ferramenta utilizada para testes de integração dos endpoints. ([GitHub][1])                        |
| **Spring Data JPA**                | Abstração para persistência e operações com banco de dados. ([GitHub][1])                          |
| **Spring HATEOAS**                 | Suporte a hiperlinks nas respostas, seguindo o conceito REST HATEOAS. ([GitHub][1])                |
| **Spring Security + JWT**          | Autenticação e autorização via JSON Web Token para proteger os recursos. ([GitHub][1])             |
| **Swagger / Springdoc OpenAPI**    | Geração automática de documentação interativa dos endpoints. ([GitHub][1])                         |
| **ModelMapper**                    | Mapeamento entre entidades e DTOs. ([GitHub][1])                                                   |
| **Lombok**                         | Redução de código boilerplate (getters/setters, construtores, etc.). ([GitHub][1])                 |
| **MySQL**                          | Banco de dados relacional utilizado em ambiente de produção/desenvolvimento. ([GitHub][1])         |
| **PostgreSQL**                     | Utilizado especialmente em testes de integração. ([GitHub][1])                                     |
| **Docker / Docker Compose**        | Para dockerizar a aplicação e facilitar a execução no ambiente local ou em produção. ([GitHub][1]) |

---

## 📁 Estrutura do Projeto

```
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── config/               # Configurações (ex.: segurança, JWT)
│   │   │   ├── controller/           # Endpoints REST
│   │   │   ├── service/              # Lógica de negócio
│   │   │   ├── repository/           # Interfaces de acesso a dados
│   │   │   ├── entity/               # Entidades (usuários, carros, aluguéis)
│   │   │   ├── exception/            # Exceções personalizadas
│   │   │   ├── jwt/                  # Classes relacionadas à autenticação com JWT token
│   │   │   ├── utils/                # Classes utilitárias
│   │   │   └── dtos/                 # Data Transfer Objects
│   │   │
│   │   └── resources/
│   │       └── application.properties # Configurações da aplicação
│   │
│   └── test/                          
│       ├── java/
│       │   └── com/gsalles/carrental/
│       │       ├── AluguelIT.java                 # Testes de integração relacionados ao processo de aluguel.
│       │       ├── AuthenticationIT.java          # Testes de integração relacionados à autenticação
│       │       ├── AutomovelIT.java               # Testes de integração das operações relacionadas à entidade Automóvel
│       │       ├── CarrentalApplicationTests.java 
│       │       ├── JwtAuthentication.java         
│       │       └── UsuarioIT.java                 # Testes de integração das operações relacionadas à entidade Usuário
│       │
│       └── resources/
│           ├── sql/                               # SQLs para adicionar e remover dados de teste.
│           └── application-test.yaml          # Configurações específicas para ambiente de teste
│
├── Dockerfile                        # Imagem Docker da aplicação
├── docker-compose-prod.yml           # Composição de serviços para produção
├── pom.xml                           # Dependências e build do projeto
└── README.md                        
```

---

## 🔐 Autenticação e Segurança

O sistema utiliza **JWT (JSON Web Token)** para autenticação. Os usuários devem se autenticar via endpoint específico para receber um token de acesso, que deve ser enviado em todas as requisições protegidas no cabeçalho `Authorization: Bearer <token>`.

A segurança é configurada usando **Spring Security**, que diferencia acessos por **roles/perfis (ex.: ADMIN, CLIENTE)** para proteger recursos específicos. 


## 📄 Documentação Interativa

A API inclui documentação automática usando **Swagger / Springdoc OpenAPI**:

Essa interface permite visualizar todos os endpoints, parâmetros, modelos de dados e testar as rotas diretamente pelo navegador.

---

## ⚙️ Como Executar

### 🚀 Com Docker

1. Clone o repositório

   ```bash
   git clone https://github.com/GCouzzi/rentalapirest.git
   ```
2. Construa e inicie com Docker

   ```bash
   docker compose -f docker-compose-prod.yml up --build
   ```

---

## 🧪 Testes Automatizados

A aplicação inclui **testes de integração** usando **Spring WebFlux com WebTestClient**, que validam os endpoints e garantem que a API está respondendo corretamente — incluindo respostas de sucesso, erros e segurança. 

---

## 💡 Boas Práticas Implementadas

✔️ Arquitetura em camadas (Controller → Service → Repository)
✔️ DTOs para separar modelo de domínio da API
✔️ Validações de entrada com Spring Validation
✔️ Autenticação JWT segura
✔️ Respostas HATEOAS onde aplicável
✔️ Documentação automática Swagger
✔️ Containerização com Docker
✔️ Testes integrados automatizados ([GitHub][1])
✔️ Tratamento completo de exceções
✔️ Buscas paginadas

---

## 🧑‍💻 Sobre o Autor

Projeto desenvolvido com foco em aprendizado e aplicação de boas práticas de APIs REST com Spring Boot. 

