#!/bin/bash

if [ "$1" != "" ]
then
   ~/maven-3/bin/mvn clean install
fi

java  -cp $HADOOP_CLASSPATH:target/hiveserver2-jdbc-1.0-jar-with-dependencies.jar  com.test.HiveJDBC
