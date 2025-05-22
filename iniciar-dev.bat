@echo off
cd /d %~dp0

echo Iniciando backend...
start "Backend" cmd /k "cd backend && node index.js"

echo Iniciando frontend Angular...
start "Frontend" cmd /k "npx ng serve --open"