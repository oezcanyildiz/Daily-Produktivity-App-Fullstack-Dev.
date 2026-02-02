# Deployment Guide for Daily Productivity App

This guide explains how to deploy your fullstack application to the cloud for free (or very cheap) to share it with recruiters.

## Prerequisites
- A **GitHub Account**.
- Your code pushed to GitHub repositories (Frontend and Backend).

---

## Part 1: Backend Deployment (Railway.app)
We will use **Railway** because it's the easiest way to host Java + PostgreSQL.

1.  **Sign Up**: Go to [railway.app](https://railway.app/) and login with GitHub.
2.  **Create Project**: Click **"New Project"** -> **"GitHub Repo"**.
3.  **Select Repository**: Choose your backend repository (`.../Backend/todo-app`).
4.  **Add Database**:
    - Right-click on the canvas or click "New" -> **"Database"** -> **"PostgreSQL"**.
    - Railway will create a database for you.
5.  **Connect Backend to Database**:
    - Click on your Backend service card.
    - Go to **"Variables"**.
    - Add the following variables (Railway provides the values if you inspect the Database card -> Connect):
        - `SPRING_DATASOURCE_URL`: `jdbc:postgresql://${PGHOST}:${PGPORT}/${PGDATABASE}`
        - `SPRING_DATASOURCE_USERNAME`: `${PGUSER}`
        - `SPRING_DATASOURCE_PASSWORD`: `${PGPASSWORD}`
        - `JWT_SECRET`: `mySecretKeyForJWTTokenGenerationThisIsAVeryLongSecretKey12345` (or generate a new one)
        - `JWT_EXPIRATION`: `86400000`
    - Railway automatically injects `PGHOST`, `PGUSER`, etc. if you link the services, but explicitly setting the Spring variables ensures it works.
6.  **Public Domain**:
    - Go to **"Settings"** -> **"Networking"**.
    - Click **"Generate Domain"**.
    - Copy this URL (e.g., `https://todo-app-production.up.railway.app`). **This is your Backend URL.**

---

## Part 2: Frontend Deployment (Vercel)
We will use **Vercel** for the React Frontend.

1.  **Sign Up**: Go to [vercel.com](https://vercel.com/) and login with GitHub.
2.  **Add New Project**: Click **"Add New..."** -> **"Project"**.
3.  **Import Git Repository**: Select your repo (ensure you point to the `Frontend` folder if it's in a subfolder).
4.  **Configure Project**:
    - **Framework Preset**: Create React App (should be auto-detected).
    - **Root Directory**: If your frontend is in a folder (e.g., `Frontend`), click "Edit" and select it.
    - **Environment Variables**:
        - Name: `REACT_APP_API_URL`
        - Value: The **Backend URL** you copied from Railway (e.g., `https://todo-app-production.up.railway.app`). **IMPORTANT: Do NOT add a trailing slash `/` at the end.**
5.  **Deploy**: Click **"Deploy"**.

---

## Part 3: Verify
1.  Open the Vercel URL (e.g., `https://daily-productivity-app.vercel.app`).
2.  Register a new user (this writes to your cloud database!).
3.  Login and create a Daily.

🎉 **Done! You can now put the Vercel URL on your CV.**
