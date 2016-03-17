#!/usr/bin/env bash

java -jar target/scala-2.11/scala-case-class-exporter-assembly-1.0.jar --jars  src/test/resources/scala-case-class-exporter-test-creator_2.11-1.0.jar,src/test/resources/joda-time-2.9.2.jar --classes Car,Door
