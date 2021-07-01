[![](https://img.shields.io/maven-central/v/org.scijava/scijava-log-slf4j.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.scijava%22%20AND%20a%3A%22scijava-log-slf4j%22)
[![](https://github.com/scijava/scijava-log-slf4j/actions/workflows/build-main.yml/badge.svg)](https://github.com/scijava/scijava-log-slf4j/actions/workflows/build-main.yml)

This adapter package enables the use of [SLF4J](http://www.slf4j.org/)-based
logging within [SciJava Common](https://github.com/scijava/scijava-common)'s
logging framework. It is kept separate from the SciJava Common core classes to
avoid proliferating SLF4J dependencies to downstream code that does not need
SLF4J-based logging.
