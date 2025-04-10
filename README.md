# API REST de Aluguel de Automóveis

Esta é uma API REST construída com **Spring Boot** para um sistema de aluguel de automóveis, permitindo o gerenciamento de aluguéis, automóveis e usuários.

## Tecnologias e Suas Utilizações no Projeto

- **Spring Boot**: Usado como framework principal para a criação da API REST.
- **Spring WebFlux com WebTestClient**: Utilizado para realizar testes de integração da API, permitindo verificar o comportamento dos endpoints, validar os dados retornados e testar a segurança, como a autenticação via JWT.
- **Spring Data JPA**: Utilizado para abstrair o acesso ao banco de dados **MySQL** e realizar auditoria, registrando alterações nas entidades de domínio, como criação e modificação de registros.
- **Spring HATEOAS**: Implementado para adicionar links em respostas de endpoints, seguindo o conceito de HATEOAS.
- **MySQL**: Utilizado como o banco de dados relacional principal da aplicação.
- **PostgreSQL**: Usado exclusivamente no ambiente de testes de integração.
- **Spring Boot Starter Validation**: Facilitou a validação dos dados de entrada da API, como a criação de usuários e aluguéis.
- **JWT (JSON Web Token)**: Implementado para autenticação e autorização. Cada vez que um usuário se autentica, um token JWT é gerado e retornado.
- **Spring Security**: Garantiu a segurança da aplicação, protegendo os endpoints da API. Ele trabalha em conjunto com o JWT para validar os tokens de autenticação e controlar o acesso a diferentes recursos com base no papel (role) do usuário, como **administrador** ou **cliente**.
- **Swagger / Springdoc OpenAPI**: Foi utilizado para gerar automaticamente a documentação interativa da API.
- **ModelMapper**: Usado para mapear as entidades de domínio para **DTOs** (Data Transfer Objects).
- **Lombok**: Usado para reduzir a quantidade de código.
- **Content Negotiation**: A aplicação suporta os formatos **JSON** e **XML** na comunicação.
- **Docker**: Foi utilizado para dockerizar a aplicação. Um `Dockerfile` foi criado para gerar uma imagem da aplicação, e um `docker-compose` foi configurado para executar a aplicação principal.
