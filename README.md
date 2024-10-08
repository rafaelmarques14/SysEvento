# 📅 **Sistema de Agenda de Compromissos**

[![Java](https://img.shields.io/badge/Java-18+-orange)](https://www.oracle.com/java/technologies/javase-jdk18-downloads.html)
[![EJB](https://img.shields.io/badge/EJB-Enterprise-green)](https://jakarta.ee/specifications/enterprise-beans/)
[![Glassfish](https://img.shields.io/badge/Server-GlassFish-blue)](https://javaee.github.io/glassfish/)
[![MySQL](https://img.shields.io/badge/Database-MySQL-blue)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-lightgrey)](https://opensource.org/licenses/MIT)

## **Sobre o Projeto**

Este projeto é uma **Agenda de Compromissos** desenvolvido com **Enterprise JavaBeans (EJB)** e executado no servidor **Glassfish**, com **MySQL** como banco de dados. O objetivo é permitir que usuários registrem seus compromissos e que administradores gerenciem os usuários do sistema.

### **Funcionalidades Principais**:

- **Autenticação e Autorização**: Implementada diretamente pelo **Glassfish**, garantindo que:
  - Usuários logados como **admin** tenham acesso apenas às funcionalidades administrativas.
  - Usuários logados como **usuários comuns** possam apenas registrar, editar e gerenciar seus compromissos.
- **Sistema de Cadastro**: O sistema conta com:
  - Cadastro e autenticação de usuários.
  - Gerenciamento completo de compromissos (criação, atualização e exclusão) para usuários comuns.
- **Painel Administrativo**:
  - Permite ao **administrador** gerenciar os usuários do sistema (cadastro, edição e exclusão de usuários).
  
## **Tecnologias Utilizadas**

- **Java EE** com **EJB**: Lógica de negócio robusta e escalável.
- **Glassfish**: Servidor de aplicações para hospedar a aplicação e gerenciar sessões, segurança e transações.
- **MySQL**: Banco de dados relacional para persistir informações de usuários e compromissos.
- **PrimeFaces** (ou outro framework JSF, caso seja utilizado): Interface moderna e interativa para o usuário.

## **Arquitetura do Sistema**

O sistema segue uma arquitetura **MVC** (Model-View-Controller):

- **Model**: Representa as entidades de usuário e compromissos.
- **Controller**: Controla o fluxo de dados entre o modelo e a visualização.
- **View**: Interface com o usuário, composta de páginas JSF.

### **Divisão de Usuários**:

- **Usuário Comum**:
  - Página de cadastro de compromissos (CRUD).
  - Acesso restrito apenas a suas funcionalidades.
  
- **Administrador**:
  - Gerenciador de usuários, com possibilidade de criar, editar e excluir qualquer usuário.
  - Página de gerenciamento com total controle do sistema.

### **Regras de Acesso**:
- **Autenticação e autorização** gerenciadas via **Glassfish**.
- O sistema impede que:
  - Usuários comuns acessem áreas administrativas.
  - Administradores acessem áreas destinadas a usuários comuns.

## **Instalação e Configuração**

### **Requisitos:**
- **Java 18+** 
- **Glassfish 7+**
- **MySQL 8+**
