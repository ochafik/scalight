#!/bin/bash

# Revert @specialized removal
#find . -name '*.scala' -exec mv -f '{}.bak' '{}' ';'

cd scala-scalight || exit 1
git diff src/compiler > ../scalight-compiler.diff
git diff src/library > ../scalight-library.diff

