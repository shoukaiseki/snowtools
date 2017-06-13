#安装

1.将该项目git到至本机,例如 E:\snowtools
2.并将E:\snowtools\bin加入到 PATH 系统变量
3.修改 snowtools\config\configJdk.bat 中的jdk配置,配置中的默认JAVA_HOME需要 jdk1.8 以上
4.修改 snowtools\appconf\snow\DruidDataSource-configure.xml 中的 jdbc 连接配置



#示例
##iem命令
导入excel文件至数据库

详细可见 samples\iem 里面的例子
