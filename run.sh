#!/usr/bin/env bash

java -jar target/scala-2.11/scala-case-class-exporter-assembly-1.0.jar --jars  ../akka-http-receiving/target/scala-2.11/akka-http-receiving_2.11-1.0.jar,../akka-http-receiving/target/scala-2.11/joda-time-2.9.2.jar,../akka-http-receiving/target/scala-2.11/akka-persistence_2.11-2.4.2.jar,../akka-http-receiving/target/scala-2.11/akka-actor_2.11-2.4.2.jar --classes Class1,Class2
