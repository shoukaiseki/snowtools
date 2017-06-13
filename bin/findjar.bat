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

set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/mylib/testGUI.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/msutil.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/poi-3.8-20120326.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/commons-logging-1.1.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/javax.servlet_1.0.0.0_2-5.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/poi-ooxml-schemas-3.8-20120326.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/poi-examples-3.8-20120326.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/stax-api-1.0.1.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/modelapi.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/jargs-1.0.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/xmlbeans-2.3.0.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/poi-scratchpad-3.8-20120326.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/log4j-1.2.17.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/cpdetector_1.0.10.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/dom4j-1.6.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/antlr-2.7.4.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/chardet-1.0.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/xom-1.1.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/mssqlserver.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/poi-ooxml-3.8-20120326.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/junit-3.8.1.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/poi-excelant-3.8-20120326.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/msbase.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/dom4j-1.6.1.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/classes12.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/com.ibm.icu_3.8.1.v20080530.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/pinyin4j-2.5.0.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/org.eclipse.birt.core_2.3.2.r232_20090204a.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/javatools.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/uic.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/jxl.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/mylib/shoukaiseki.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/javalib/jeval-0.9.4.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/jdklib/jdk1.7.0_04/tools.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/jdklib/jdk1.7.0_04/rt.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/weblib/hibernate/hibernate4.1.3/hibernate-core-4.1.3.Final.jar
set CLASSPATH=%CLASSPATH%;%SNOW_LIB%/apache/commons-lang3-3.5.jar
set CLASSPATH=%CLASSPATH%;.;


@REM echo _RUNJAVA=%_RUNJAVA%

@REM 查找路径 查找的类名
@REM  -classpath "%CLASSPATH%" 
@ECHO ON
%_RUNJAVA%  -Dsnow.home="%SNOW_HOME%" -Dsnow.config="%SNOW_HOME%\config" bennkyou.classes.path.FindClass %*


@ECHO OFF
goto end
:end
@ECHO ON
