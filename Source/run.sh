#!/bin/bash
#cd /D %~dp0
export CLASSPATH=.:dist/lib/gson-2.6.2.jar:lib/httpclient-4.3.2.jar:lib/httpclient-4.5.1.jar:lib/httpcore-4.3.jar:.:
export CLASSPATH=$CLASSPATH:dist/lib/org-apache-commons-logging.jar:lib/jackson-annotations-2.8.6.jar:.:
export CLASSPATH=$CLASSPATH:dist/lib/jackson-core-2.8.6.jar:lib/jackson-databind-2.8.6.jar:lib/jcl-over-slf4j-1.7.22.jar:.:
export CLASSPATH=$CLASSPATH:dist/lib/jul-to-slf4j-1.7.22.jar:lib/log4j-over-slf4j-1.7.22.jar:lib/logback-classic-1.1.9.jar:.:
export CLASSPATH=$CLASSPATH:dist/lib/logback-core-1.1.9.jar:lib/slf4j-api-1.7.22.jar:lib/snakeyaml-1.17.jar:.:
export CLASSPATH=$CLASSPATH:dist/lib/spring-aop-4.3.6.RELEASE.jar:lib/spring-beans-4.3.6.RELEASE.jar:.:
export CLASSPATH=$CLASSPATH:dist/lib/spring-boot-1.4.4.RELEASE.jar:lib/spring-boot-autoconfigure-1.4.4.RELEASE.jar:.:
export CLASSPATH=$CLASSPATH:dist/lib/spring-boot-starter-1.4.4.RELEASE.jar:lib/spring-boot-starter-logging-1.4.4.RELEASE.jar:.:
export CLASSPATH=$CLASSPATH:dist/lib/spring-boot-starter-tomcat-1.4.4.RELEASE.jar:lib/spring-boot-starter-web-1.4.4.RELEASE.jar:.:
export CLASSPATH=$CLASSPATH:dist/lib/spring-context-4.3.6.RELEASE.jar:lib/spring-core-4.3.6.RELEASE.jar:lib/spring-expression-4.3.6.RELEASE.jar:.:
export CLASSPATH=$CLASSPATH:dist/lib/spring-web-4.3.6.RELEASE.jar:lib/spring-webmvc-4.3.6.RELEASE.jar:lib/tomcat-embed-core-8.5.11.jar:.:
export CLASSPATH=$CLASSPATH:dist/lib/tomcat-embed-el-8.5.11.jar:lib/tomcat-embed-websocket-8.5.11.jar:.:
export CLASSPATH=$CLASSPATH:dist/lib/mail-1.4.7.jar:lib/javax.mail-api-1.6.0.jar:lib/jna-4.5.1.jar:.:
export CLASSPATH=$CLASSPATH:dist/TableCheck.jar
# echo $CLASSPATH
java -classpath $CLASSPATH com.tablecheck.client.main.Main
# java -classpath $CLASSPATH com.tablecheck.client.main.Main > runserver.log
