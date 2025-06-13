# 📝 The Blog Project

> Une application web de blog communautaire développée en Java avec **Vaadin + Spring Boot**, intégrant la **programmation asynchrone**, le **modèle MVP**, et une architecture en couches moderne.

---

## 📸 Aperçu

Interface utilisateur simple, minimaliste et responsive, inspirée d’un design Figma.

---

## ⚛️ Fonctionnalités principales

- 🧑 Authentification des utilisateurs (connexion, inscription)
- ✍️ Création et gestion de posts
- 💬 Commentaires sur les articles
- 🔡 Sécurité Spring avec mots de passe encryptés (BCrypt)
- 🌑 Mode sombre / clair intégré (Vaadin `Lumo`)
- 🧠 Architecture **MVP** (Model - View - Presenter)
- ⚡ Programmation asynchrone avec `CompletableFuture`
- 📋 Page d’administration réservée à l’admin
- 🧪 Tests unitaires & montée en charge (prévu)
- ☁️ Déploiement cloud-ready (Docker & GCP Cloud Run)

---

## 🧱 Architecture du projet

```bash
the-blog-project/
├── model/           # Entités JPA (Utilisateur, Post, Commentaire, etc.)
├── repository/      # Interfaces JPA
├── service/         # Logique métier (auth, posts, commentaires)
├── presenter/       # MVP Presenter : liaison logique UI ↔ service
├── view/            # Vues Vaadin (LoginView, HomeView, etc.)
├── config/          # Sécurité, encodage, filterchain
├── util/            # Utils communs (hash, date, etc.)
└── resources/       # application.properties
```

---

## ⚒️ Technologies utilisées

| Catégorie     | Stack technique                          |
|---------------|-------------------------------------------|
| Frontend      | Vaadin Flow (Java-based UI)              |
| Backend       | Spring Boot                              |
| Base de données | MySQL + Spring Data JPA                  |
| Sécurité      | Spring Security + BCrypt                 |
| Asynchrone    | `CompletableFuture`, `@Async`            |
| Pattern       | MVP (Model – View – Presenter)           |
| Tests         | JUnit, Maven Surefire Plugin             |
| Déploiement   | Docker, Cloud Run (Google Cloud)         |

---

## ⚙️ Configuration

### 📁 `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/the_blog_db
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

vaadin.servlet.productionMode=false
```

---

## 🔐 Sécurité

- Authentification via formulaire `/login`
- Utilisation de `AuthenticationManager` et `SecurityContextHolder`
- `PasswordEncoder` (BCrypt)
- Règles de filtrage via `SecurityConfig.java`

---

## 🦪 Exécution locale

### 1. Prérequis

- Java 17
- Maven
- MySQL

### 2. Lancer l’application

```bash
# Créer la base de données dans MySQL :
CREATE DATABASE the_blog_db;

# Puis lancer :
mvn clean install
mvn spring-boot:run
```

Accès : [http://localhost:8080](http://localhost:8080)

---

## 📂 Déploiement Docker (optionnel)

```bash
docker build -t the-blog-project .
docker run -p 8080:8080 the-blog-project
```

---

## 👨‍🎓 Auteur

- **Nom** : Amar Wade  
- **École** : École Supérieure Polytechnique de Dakar  
- **Projet de fin de DUT Génie Informatique**  

---

## 📄 Licence

Ce projet est réalisé dans un cadre académique et peut être librement consulté et amélioré à des fins pédagogiques.
