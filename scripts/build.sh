#!/usr/bin/env bash
set -euo pipefail
mkdir -p out
find src/main/java -name "*.java" > sources.txt
javac -d out @sources.txt
echo "Build successful. Classes in ./out"
