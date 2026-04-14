@echo off
title NRO Auto Update Restart

setlocal
set PATH=C:\maven\bin;%PATH%

cd /d C:\nro_goc

echo ===============================
echo Set UTF-8 Encoding
echo ===============================
chcp 65001 > nul
set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8

echo ===============================
echo Stop old server
echo ===============================
taskkill /F /IM java.exe >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq GameServer*" >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq LoginServer*" >nul 2>&1
taskkill /F /IM cmd.exe /FI "WINDOWTITLE eq GameServer*" >nul 2>&1
taskkill /F /IM cmd.exe /FI "WINDOWTITLE eq LoginServer*" >nul 2>&1

timeout /t 3 >nul

echo ===============================
echo Clean old build folders
echo ===============================
rmdir /s /q C:\nro_goc\target >nul 2>&1
rmdir /s /q C:\nro_goc\ServerLogin\target >nul 2>&1

echo ===============================
echo Update source from GitHub
echo ===============================
git fetch origin
git reset --hard origin/main

if %errorlevel% neq 0 (
    echo GIT UPDATE FAILED
    exit /b 1
)

echo ===============================
echo Build Login Server
echo ===============================
cd /d C:\nro_goc\ServerLogin
call mvn package -DskipTests

if %errorlevel% neq 0 (
    echo LOGIN BUILD FAILED
    exit /b 1
)

echo ===============================
echo Build Game Server
echo ===============================
cd /d C:\nro_goc
call mvn package -DskipTests

if %errorlevel% neq 0 (
    echo GAME BUILD FAILED
    exit /b 1
)

echo ===============================
echo Start Login Server
echo ===============================
cd /d C:\nro_goc\ServerLogin
start "LoginServer" cmd /c bat.cmd

timeout /t 3 >nul

echo ===============================
echo Start Game Server
echo ===============================
cd /d C:\nro_goc
start "GameServer" cmd /c run.bat

echo ===============================
echo Server Started
echo ===============================
endlocal
exit