@echo off
echo ================================
echo   Switching JAVA_HOME to JDK 17
echo ================================

set "JAVA_HOME=C:\Program Files\jdk-17.0.13.11-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo JAVA_HOME is now: %JAVA_HOME%
echo.

echo ================================
echo   Starting Apache Tomcat
echo ================================

cd /d "C:\apache-tomcat-10.1.34\bin"

startup.bat

cd /d "%~dp0"
