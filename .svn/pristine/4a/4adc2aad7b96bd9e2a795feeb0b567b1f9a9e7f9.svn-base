GWT_HOME=$MAVEN_REPO/gwt/jars
validation_api=$GWT_HOME/validation-api-1.0.0.GA-sources.jar
gwt_user=$GWT_HOME/gwt-user-2.1.1.jar
gwt_dev=$GWT_HOME/gwt-dev-2.1.1.jar
gwt_crypto=$GWT_HOME/gwt-crypto-1.0.3.jar
gwt_voices=$GWT_HOME/gwt-voices-2.0.0.jar
gwtext=$GWT_HOME/gwtext-2.0.5.jar

GWT_JAR=$validation_api:$gwt_user:$gwt_dev:$gwt_crypto:$gwt_voices:$gwtext

thorn5_client=$MAVEN_REPO/thorn5/jars/thorn5-client-5.1.2.3.jar
thorn5_rule=$MAVEN_REPO/thorn5/jars/thorn5-rule-5.1.2.3.jar
thorn5_rmc=$MAVEN_REPO/thorn5/jars/thorn5-rmc-5.1.2.3.jar

thorn5_wms=$MAVEN_REPO/wms5/jars/wms5-5.2.1.5.jar

THORN_JAR=$thorn5_client:$thorn5_rule:$thorn5_rmc:$thorn5_wms

CURRENT_PROJECT=$(pwd)/src/main/java:$(pwd)/target/tcl_wms/classes

ALL_JARS=$CURRENT_PROJECT:$GWT_JAR:$THORN_JAR

/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Commands/java -Xms256m -Xmx1024m -cp $ALL_JARS com.google.gwt.dev.Compiler -war www $* com.vtradex.wms.WMS
