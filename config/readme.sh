#---系統變量.linux--- #{{{1
#---/etc/profile--- #{{{2
JAVA_JDK17=/media/linux/data/opt/java/jdk1.7.0_04
JAVA_JDK7U25=/media/linux/data/opt/java/jdk1.7.0_25
JAVA_JDK16=/media/linux/data/opt/java/jdk160_05
JAVA_ANTPROJECT=/media/develop/antProject

export PATHOLD=$PATH
#JAVA_HOME=/usr/lib/jvm/jrockit_150_12
#JAVA_HOME=/usr/lib/jvm/java
JAVA_HOME=$JAVA_JDK16
export JAVA_HOME
#echo $JAVA_HOME
PATH=$JAVA_HOME/bin:$PATH
export PATH
#echo $PATH
CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/jre/lib/rt.jar
export CLASSPATH
#echo $CLASSPATH
LINUX_JAVA=/media/linux/data/java
export LINUX_JAVA JAVA_JDK16 JAVA_JDK17 JAVA_JDK7U25 JAVA_ANTPROJECT

#---~/.bashrc--- #{{{2
PATHOLD=/media/linux/data/bin:$PATHOLD
PATH=/media/linux/data/bin:$PATH
export PATHOLD PATH
AXIS2_HOME=/media/linux/data/opt/axis2-1.6.2
export AXIS2_HOME
CLASSOUT=$LINUX_JAVA/hello/classes
export CLASSOUT
complete -C "$JAVA_ANTPROJECT/javaant/complete/complete-tssls-cmd.pl" tssls

#---系統變量.windows--- #{{{1
#---win7--- #{{{2
#指定antProject目录
JAVA_ANTPROJECT=E:/antProject
#该文件指定JDK信息,在各个其它应用都会调用该配置
JAVA_ANTPROJECT_CONFIGFILE=%JAVA_ANTPROJECT%/javaant/config/ant_java_jdk17_win7.bat
PATH変数增加	;%JAVA_ANTPROJECT%/javaant/bin;%JAVA_ANTPROJECT%/pythonant/bin;

#把项目上的 businessobjects.jar 复制到 E:\antProject\javaant\libs\maximolib\maximo7.5\jscrp\businessobjects_js.jar


#python共享脚本
#还需将python加入环境变量
PATH変数增加	;%JAVA_ANTPROJECT%/pythonant/bin;



echo %JAVA_ANTPROJECT%
echo %JAVA_ANTPROJECT_CONFIGFILE%
echo %PATH%
