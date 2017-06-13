
set JAVA_JDK18=D:/usr/Java/jdk1.8.0_65
set JAVA_JDK17=D:/usr/Java/jdk1.7.0_51

@REM JAVA_ANTPROJECTLIB SNOW_LIB
set SNOW_LIB=%SNOW_HOME%/libs
set JAVA_HOME=%JAVA_JDK18%
@REM #$JAVA_HOME/lib/dt.jar;$JAVA_HOME/lib/tools.jar;$JAVA_HOME/jre/lib/rt.jar;/home/fedora/.vim/;
set CLASSPATH=.;
set CLASSPATH=%CLASSPATH%;%JAVA_HOME%/jre/lib/resources.jar
set CLASSPATH=%CLASSPATH%;%JAVA_HOME%/jre/lib/rt.jar
set CLASSPATH=%CLASSPATH%;%JAVA_HOME%/jre/lib/jsse.jar
set CLASSPATH=%CLASSPATH%;%JAVA_HOME%/jre/lib/jce.jar
set CLASSPATH=%CLASSPATH%;%JAVA_HOME%/jre/lib/charsets.jar
set CLASSPATH=%CLASSPATH%;%JAVA_HOME%/jre/lib/jfr.jar
set CLASSPATH=%CLASSPATH%;%JAVA_HOME%/jre/lib/ext/localedata.jar
set CLASSPATH=%CLASSPATH%;%JAVA_HOME%/jre/lib/ext/sunec.jar
set CLASSPATH=%CLASSPATH%;%JAVA_HOME%/jre/lib/ext/dnsns.jar
set CLASSPATH=%CLASSPATH%;%JAVA_HOME%/jre/lib/ext/sunjce_provider.jar
set CLASSPATH=%CLASSPATH%;%JAVA_HOME%/jre/lib/ext/sunpkcs11.jar
set CLASSPATH=%CLASSPATH%;%JAVA_HOME%/jre/lib/ext/zipfs.jar
set CLASSPATH=%CLASSPATH%;%JAVA_HOME%/lib/tools.jar


rem Check if we have a usable JDK
if "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
if not exist "%JAVA_HOME%\bin\javaw.exe" goto noJavaHome
set _RUNJAVA="%JAVA_HOME%/bin/java.exe"
goto okJava

:noJavaHome
echo The JAVA_HOME environment variable is not defined correctly.
echo It is needed to run this program in debug mode.
echo NB: JAVA_HOME should point to a JDK not a JRE.
goto exit

:okJava
goto end

:exit
exit /b 1

:end
exit /b 0
