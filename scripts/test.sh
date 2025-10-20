#!/usr/bin/env bash
set -euo pipefail
if [ ! -d out ]; then
  bash scripts/build.sh
fi
java -cp out com.smartwaste.test.TestRunner
