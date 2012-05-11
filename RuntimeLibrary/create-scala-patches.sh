#!/bin/bash

cd scala-scalight || exit 1
git diff src/compiler > ../scalight-compiler.diff
git diff src/library > ../scalight-library.diff

