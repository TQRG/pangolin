#!/bin/zsh

java -javaagent:target/pangolin-core-0.0.1.jar -Xbootclasspath/a:lib/javassist.jar -jar lib/ArgoUML.jar org.argouml.application.Main