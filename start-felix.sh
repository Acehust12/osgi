#!/bin/bash 

#Property definitions
FELIX_HOME=/Users/ahorne/Osgi/felix-framework-4.0.0
CONFIG_FILE=$FELIX_HOME/conf/config.properties
SYSTEM_PROPERTIES=$FELIX_HOME/conf/system.properties
BUNDLE_DIR=$FELIX_HOME/bundle
BUNDLE_CACHE=/tmp/felix-bundle-cache

#Clear the cache
rm -rf $BUNDLE_CACHE

#Start the container
$JAVA_HOME/bin/java -Dfelix.config.properties=file:$CONFIG_FILE -Dfelix.system.properties=file:$SYSTEM_PROPERTIES -jar $FELIX_HOME/bin/felix.jar -b $BUNDLE_DIR $BUNDLE_CACHE


