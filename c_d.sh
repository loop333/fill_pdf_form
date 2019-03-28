#!/bin/sh

docker run -t --rm --name VisaAppForm -v "$PWD":/myapp -w /myapp openjdk:12 javac -classpath '*' VisaAppForm.java
