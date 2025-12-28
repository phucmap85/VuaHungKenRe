#!/bin/bash
rm -rf build game.jar
mkdir build

javac -encoding UTF-8 -d build $(find src -name "*.java")

cp -r src/image build/
cp -r src/sound build/

jar cfe game.jar main.Main -C build .