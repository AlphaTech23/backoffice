@echo off
setlocal enabledelayedexpansion

REM ==========================================================
REM === CONFIGURATION PERSONNALISABLE ========================
REM ==========================================================
set "JAVA_HOME=C:\Program Files\jdk-17.0.13.11-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"
set "CATALINA_HOME=C:\apache-tomcat-10.1.34"
set "PROJECT_DIR=%~dp0"
set "BUILD_DIR=%PROJECT_DIR%build"
set "LIB=%PROJECT_DIR%lib\jakarta.servlet-api-5.0.0.jar;%PROJECT_DIR%lib\framework.jar"

REM ==========================================================
REM === NETTOYAGE DES ANCIENS BUILD ==========================
REM ==========================================================
echo.
echo === Nettoyage du dossier build ===
if exist "%BUILD_DIR%" rmdir /S /Q "%BUILD_DIR%"
mkdir "%BUILD_DIR%"

REM ==========================================================
REM === COMPILATION DU PROJET DE PROJECT ========================
REM ==========================================================
echo.
echo === Compilation du projet de PROJECT ===
set "PROJECT_CLASSES=%BUILD_DIR%\backoffice-classes"
mkdir "%PROJECT_CLASSES%"

set "SRC_FILES="
for /r "%PROJECT_DIR%\src\main\java" %%f in (*.java) do (
    set "SRC_FILES=!SRC_FILES! "%%f""
)

if not defined SRC_FILES (
    echo Aucun fichier source trouve dans %PROJECT_DIR%\src\main\java
    exit /b 1
)

javac -encoding UTF-8 -parameters -cp "%LIB%" -d "%PROJECT_CLASSES%" !SRC_FILES!
if errorlevel 1 (
    echo Erreur de compilation du projet PROJECT
    exit /b 1
)

echo Compilation du projet PROJECT terminee

REM ==========================================================
REM === CReATION DU WAR ======================================
REM ==========================================================
echo.
echo === Preparation de la structure WAR ===
set "PROJECT_BUILD=%BUILD_DIR%\war"
mkdir "%PROJECT_BUILD%"
mkdir "%PROJECT_BUILD%\WEB-INF"
mkdir "%PROJECT_BUILD%\WEB-INF\lib"
mkdir "%PROJECT_BUILD%\WEB-INF\classes"

xcopy "%PROJECT_DIR%src\webapp" "%PROJECT_BUILD%" /E /I /Y >nul
xcopy "%PROJECT_DIR%\lib\framework.jar" "%PROJECT_BUILD%\WEB-INF\lib" /E /I /Y >nul
xcopy "%PROJECT_CLASSES%" "%PROJECT_BUILD%\WEB-INF\classes" /E /I /Y >nul

echo.
echo === Creation du fichier WAR ===
cd "%PROJECT_BUILD%"
jar cf "%BUILD_DIR%\backoffice.war" *
cd "%PROJECT_DIR%"

echo WAR genere : %BUILD_DIR%\backoffice.war

REM ==========================================================
REM === DePLOIEMENT SUR TOMCAT ===============================
REM ==========================================================
echo.
echo === Deploiement sur Tomcat ===
copy "%BUILD_DIR%\backoffice.war" "%CATALINA_HOME%\webapps" /Y >nul

if errorlevel 1 (
    echo echec du deploiement sur Tomcat
    exit /b 1
)

echo WAR copie dans : %CATALINA_HOME%\webapps

REM ==========================================================
REM === FIN DU BUILD =========================================
REM ==========================================================
echo.
echo Compilation et deploiement termines avec succes !
echo.
pause
