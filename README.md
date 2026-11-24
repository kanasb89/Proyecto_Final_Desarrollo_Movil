# PerfilApp (Android + Node.js + PostgreSQL)

## Estructura
- `database/database.sql`  → script de la base de datos
- `backend/`               → API Node.js + Express
- `android-app/`           → proyecto Android Studio (Kotlin + Compose)

## 1. Base de datos
    psql -U postgres -f database/database.sql

## 2. Backend
    cd backend
    # edita .env (DB_PASSWORD y JWT_SECRET)
    npm install
    npm run dev          # http://localhost:3000

## 3. App Android
1. Abre la carpeta `android-app` con Android Studio (Ladybug o superior, JDK 17) y espera el Gradle Sync.
2. Emulador: la URL ya apunta a `http://10.0.2.2:3000/` (RetrofitClient.kt).
   Celular físico: cambia BASE_URL por la IP LAN de tu PC (ej. `http://192.168.1.50:3000/`).
3. Ejecuta con Run.

La foto de perfil se maneja solo en el dispositivo; nunca se envía al servidor, por lo que se borrar al momento de cerrar la app tal como se indica en las instrucciones de desarrollo. 
