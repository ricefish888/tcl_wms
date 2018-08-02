@echo off
java -Xmx512M -cp "%~dp0\src\main\java;%~dp0\target\tcl_wms\WEB-INF\classes;%MAVEN_REPO%\thorn5\jars\thorn5-client-5.1.4.1.jar;%MAVEN_REPO%\thorn5\jars\thorn5-rmc-5.1.4.1.jar;%MAVEN_REPO%\thorn5\jars\thorn5-rule-5.1.4.1.jar;%MAVEN_REPO%\wms5\jars\wms5-5.2.1.5.1.jar;%MAVEN_REPO%\gwt\jars\gwt-user-2.1.1.jar;%MAVEN_REPO%\gwt\jars\gwt-dev-2.1.1.jar;%MAVEN_REPO%\gwt\jars\gwt-voices-1.5.5.jar;%MAVEN_REPO%/gwt-crypto/jars/gwt-crypto-2.3.0.jar;%MAVEN_REPO%\gxt\jars\gwtext-2.0.5.jar" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %* com.vtradex.wms.WMS
if ERRORLEVEL 1 goto error
if ERRORLEVEL 0 goto exit
:error
pause
:exit
