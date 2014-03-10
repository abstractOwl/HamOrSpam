# Makefile for CS165A MP-2

all:
  mvn package
  cp target/*.jar ./Classifier.jar
  
clean:
  rm Classifier.jar
  mvn clean