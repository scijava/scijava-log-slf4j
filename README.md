[![](http://jenkins.imagej.net/job/SciJava-Log-SLF4J/lastBuild/badge/icon)](http://jenkins.imagej.net/job/SciJava-Log-SLF4J/)

This adapter package enables the use of [SLF4J](http://www.slf4j.org/)-based
logging within [SciJava Common](https://github.com/scijava/scijava-common)'s
logging framework. It is kept separate from the SciJava Common core classes to
avoid proliferating SLF4J dependencies to downstream code that does not need
SLF4J-based logging.
