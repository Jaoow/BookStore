# 📚 Bookstore Management System

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen)
![Java](https://img.shields.io/badge/Java-21-blue)

## 🚀 Visão Geral

Bem-vindo ao **Bookstore Management System**! Este projeto é uma aplicação **Spring Boot** projetada para gerenciar uma
livraria, com funcionalidades como gerenciamento de livros e autores, implementando **CRUD** completo com uso de **JPA
**, **DTOs**, e práticas de **Clean Architecture**.

## 🛠️ Tecnologias Utilizadas

### Backend

- **[Spring Boot](https://spring.io/projects/spring-boot)**: Framework Java para criação de aplicações robustas e
  escaláveis.
- **[Java 21](https://www.oracle.com/java/technologies/downloads/#java21)**: Linguagem de programação para construção da
  aplicação.
- **[Spring Data JPA](https://spring.io/projects/spring-data-jpa)**: Facilita a integração com bancos de dados, usando a
  especificação JPA.
- **[Hibernate Validator](https://hibernate.org/validator/)**: Implementação de Bean Validation para validação de dados.
- **[H2 Database](http://www.h2database.com/html/main.html)**: Banco de dados em memória para desenvolvimento e testes.
- **[Lombok](https://projectlombok.org/)**: Biblioteca para reduzir a verbosidade do código Java.
- **[JUnit 5](https://junit.org/junit5/)**: Framework de testes para Java.
- **[Maven](https://maven.apache.org/)**: Ferramenta de gerenciamento de build e dependências.

### 🏗️ Arquitetura

O projeto segue a **Clean Architecture** com uma separação clara de responsabilidades:

- **Domain Layer**: Contém as entidades e repositórios.
- **Service Layer**: Contém a lógica de negócios e interações com os repositórios.
- **Presentation Layer**: Contém os controladores REST e DTOs para comunicação com o cliente.
- **Exception Handling**: Tratamento centralizado de exceções para respostas consistentes.

## 📋 Funcionalidades

- **Gerenciamento de Livros**: CRUD completo para livros, incluindo atributos como título, autor, data de publicação,
  ISBN e preço.
- **Gerenciamento de Autores**: CRUD completo para autores, incluindo atributos como nome, biografia e data de
  nascimento.
- **Validação**: Uso de Bean Validation para garantir a integridade dos dados.
- **Testes**: Testes unitários e de integração para validar funcionalidades e regras de negócio.
