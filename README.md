# Minha Dieta API

![Java](https://img.shields.io/badge/Java-17+-ED8B00?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?logo=spring&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-4.0.0-C71A36?logo=apache-maven&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Authentication-000000?logo=jsonwebtokens&logoColor=white)

## 📖 Sobre o Projeto

**Minha Dieta API** é o backend de uma aplicação full-stack projetada para ajudar usuários a gerenciar seus planos de dieta de forma personalizada e eficiente. A API permite criar perfis de dieta, configurar refeições, gerenciar uma lista de ingredientes e montar cardápios detalhados, calculando as quantidades necessárias para cada refeição.

Este projeto foi construído utilizando as melhores práticas de desenvolvimento, com uma arquitetura robusta, segura e escalável.

---

## ✨ Funcionalidades

- **Autenticação e Segurança:** Sistema de login e registro com autenticação baseada em JWT (JSON Web Tokens) e autorização baseada em papéis (Roles).
- **Gerenciamento de Usuários:** Endpoints para registro e autenticação de usuários.
- **Perfis de Dieta:** Um usuário pode criar e gerenciar múltiplos perfis de dieta (ex: "Ganho de Massa", "Perda de Peso").
- **Configuração de Refeições:** Dentro de cada perfil, o usuário pode definir a estrutura de suas refeições diárias (ex: "Refeição 1 - Café da Manhã", "Refeição 2 - Almoço").
- **Banco de Ingredientes:** Um CRUD completo para gerenciar uma lista mestre de ingredientes disponíveis na aplicação.
- **Montagem de Refeições:** Funcionalidade para adicionar ingredientes com quantidades e unidades específicas a cada refeição configurada.

---

## 🛠️ Tecnologias Utilizadas

Este projeto é dividido em um backend e um frontend, utilizando as seguintes tecnologias:

### **Backend (API)**
- **Java 17**
- **Spring Boot 3**
- **Spring Security 6:** Para autenticação e autorização.
- **Spring Data JPA (Hibernate):** Para persistência de dados.
- **JSON Web Tokens (JWT):** Para gerenciamento de sessões seguras.
- **H2 Database:** Banco de dados em memória para ambiente de desenvolvimento.
- **PostgreSQL (Opcional):** Preparado para ambiente de produção.
- **Maven:** Para gerenciamento de dependências.
- **Lombok:** Para reduzir código boilerplate.

### **Frontend (Planejado)**
- **Angular 17**
- **TypeScript**
- **HTML5 & CSS3**

---

## 🚀 Como Executar o Projeto (Localmente)

Siga os passos abaixo para configurar e executar o ambiente de desenvolvimento localmente.

### Pré-requisitos
* JDK 17 ou superior
* Maven 3.6+
* Sua IDE favorita (IntelliJ, VS Code, Eclipse)
* Um cliente de API como Postman ou Insomnia

### Passos para Instalação (Backend)

1.  **Clone o repositório:**
    ```bash
    git clone [URL_DO_SEU_REPOSITORIO]
    cd [NOME_DA_PASTA_DO_PROJETO]
    ```

2.  **Configure o `application.properties`:**
    O arquivo se encontra em `src/main/resources/application.properties`. Verifique as configurações do banco de dados e a chave secreta do JWT.
    ```properties
    # Exemplo para banco H2
    spring.h2.console.enabled=true
    spring.datasource.url=jdbc:h2:mem:minhadieta-db
    spring.datasource.driverClassName=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=password
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

    # Chave secreta para JWT (altere para um valor seguro)
    jwt.secret=SUA_CHAVE_SECRETA_SUPER_SECRETA
    ```

3.  **Instale as dependências com o Maven:**
    ```bash
    mvn clean install
    ```

4.  **Execute a aplicação:**
    ```bash
    mvn spring-boot:run
    ```

A API estará rodando em `http://localhost:8080`.

##  API Endpoints

Aqui está uma visão geral dos principais endpoints disponíveis na API.

*(Nota: Rotas que contêm `{userId}`, `{profileId}`, etc., exigem um token JWT válido no header `Authorization: Bearer <token>`)*

| Método   | URL                                                                              | Descrição                                         |
| :------- | :------------------------------------------------------------------------------- | :------------------------------------------------ |
| `POST`   | `/api/auth/register`                                                             | Registra um novo usuário.                         |
| `POST`   | `/api/auth/login`                                                                | Realiza o login e retorna um token JWT.           |
| `POST`   | `/api/users/{userId}/diet-profiles`                                              | Cria um novo perfil de dieta para o usuário.      |
| `GET`    | `/api/users/{userId}/diet-profiles`                                              | Lista todos os perfis de dieta do usuário.        |
| `GET`    | `/api/ingredients`                                                               | Lista todos os ingredientes. (Requer Auth)        |
| `POST`   | `/api/ingredients`                                                               | Cria um novo ingrediente. (Requer Role ADMIN)     |
| `POST`   | `/api/users/{userId}/diet-profiles/{profileId}/meals`                            | Cria uma nova configuração de refeição.           |
| `GET`    | `/api/users/{userId}/diet-profiles/{profileId}/meals`                            | Lista as configurações de refeição de um perfil.  |
| `POST`   | `/api/users/{userId}/diet-profiles/{profileId}/meals/{mealId}/items`             | Adiciona um ingrediente a uma refeição.           |
| `GET`    | `/api/users/{userId}/diet-profiles/{profileId}/meals/{mealId}/items`             | Lista todos os ingredientes de uma refeição.      |
| `PUT`    | `/api/users/{userId}/diet-profiles/{profileId}/meals/{mealId}/items/{itemId}`    | Atualiza um ingrediente em uma refeição.          |
| `DELETE` | `/api/users/{userId}/diet-profiles/{profileId}/meals/{mealId}/items/{itemId}`    | Remove um ingrediente de uma refeição.            |
