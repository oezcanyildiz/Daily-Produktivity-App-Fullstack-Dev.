# Daily Productivity App

![Project Status](https://img.shields.io/badge/status-active-success.svg)
![License](https://img.shields.io/badge/license-MIT-blue.svg)

- **Live Demo:** [Hier klicken](https://dailysapp-frontend-vercel.vercel.app/login)
- **GitHub Repository:** [Hier klicken](https://github.com/oezcanyildiz/Daily-Produktivity-App-Fullstack-Dev)

## Demo & Screenshots

Hier ist ein Einblick in die Anwendung:

### Dashboard
![Dashboard](./Scrennshots/dashboard.png)

### Login
![Login](./Scrennshots/Login.png)

User: admin@example.com
Password : 123456789
---

## Beschreibung

Die **Daily Productivity App** ist eine Fullstack-Anwendung, die entwickelt wurde, um Benutzern zu helfen, ihre täglichen Aufgaben zu verwalten und ihre Produktivität zu steigern. Sie bietet eine benutzerfreundliche Oberfläche zur Erstellung, Verfolgung und Verwaltung von "Dailies" (täglichen Aufgaben).

Die App löst das Problem der Aufgaben-Desorganisation, indem sie eine zentrale Plattform bietet, auf der Benutzer ihre Ziele klar definieren und ihren Fortschritt visualisieren können.

---

## Tech Stack

Diese Anwendung verwendet moderne Technologien für Frontend und Backend:

### Frontend
*   **React** (v19) - JavaScript-Bibliothek für Benutzeroberflächen
*   **React Router** - Für die Navigation innerhalb der App
*   **Axios** - Für HTTP-Anfragen an das Backend
*   **CSS** - Für das Styling der Komponenten

### Backend
*   **Java 17** - Programmiersprache
*   **Spring Boot** (v3) - Framework für die Backend-Entwicklung
*   **Spring Security** - Für Authentifizierung und Autorisierung
*   **JWT (JSON Web Tokens)** - Für sichere, statuslose Authentifizierung
*   **PostgreSQL** - Relationale Datenbank
*   **Lombok** - Um Boilerplate-Code zu reduzieren

---

## Installation & Setup

Folge diesen Schritten, um das Projekt lokal zu starten.

### Voraussetzungen
Stelle sicher, dass du Folgendes installiert hast:
*   Node.js & npm
*   Java JDK 17
*   PostgreSQL
*   Maven (optional, falls nicht im IDE enthalten)

### 1. Repository klonen

```bash
git clone https://github.com/oezcanyildiz/Daily-Produktivity-App-Fullstack-Dev.git
cd Daily-Produktivity-App-Fullstack-Dev
```

### 2. Backend einrichten

1.  Navigiere in den Backend-Ordner:
    ```bash
    cd Backend/todo-app
    ```
2.  Konfiguriere die Datenbank in `src/main/resources/application.properties` (falls notwendig, update Benutzername/Passwort für deine lokale PostgreSQL-Instanz).
3.  Starte die Anwendung:
    ```bash
    ./mvnw spring-boot:run
    ```
    Das Backend läuft nun unter `http://localhost:8080`.

### 3. Frontend einrichten

1.  Öffne ein neues Terminal und navigiere in den Frontend-Ordner:
    ```bash
    cd Frontend
    ```
2.  Installiere die Abhängigkeiten:
    ```bash
    npm install
    ```
3.  Starte den Entwicklungsserver:
    ```bash
    npm start
    ```
    Das Frontend ist nun unter `http://localhost:3000` erreichbar.

---

Viel Spaß
