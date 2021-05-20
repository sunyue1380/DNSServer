#!/bin/bash
java -Dspring.profiles.active=release -DlogbackPath=/opt/schoolwow/$1/logback.xml -Dfile.encoding=utf-8 -jar DNSServer.jar