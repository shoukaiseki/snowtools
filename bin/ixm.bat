@echo off
setlocal
@REM 当前所在目录
set "CURRENT_DIR=%cd%"
@REM bat所在目录
set "COMM_DIR=%~dp0"
if not "%SNOW_HOME%" == "" goto gotHome
set "SNOW_HOME=%COMM_DIR%"
if exist "%SNOW_HOME%\bin\snowconfig.bat" goto okHome
cd /D %COMM_DIR%
cd ..
set "SNOW_HOME=%cd%"
cd "%CURRENT_DIR%"

:gotHome
if exist "%SNOW_HOME%\bin\snowconfig.bat" goto okHome
echo The SNOW_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
goto end

:okHome
@REM 
echo SNOW_HOME=%SNOW_HOME%
call "%SNOW_HOME%\bin\snowconfig.bat"
call %SNOW_HOME%/bin/maximoToolsJavajars.bat

@REM echo CLASSPATH=%CLASSPATH%
cd /D %CURRENT_DIR%
@ECHO ON
%_RUNJAVA%  -Dsnow.home="%SNOW_HOME%" -Dsnow.config="%SNOW_HOME%\config" org.maximo.tools.impxml.ImpXmlMain %*
@ECHO OFF
goto end
:end
@ECHO ON
