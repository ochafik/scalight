#!/bin/bash

# Revert @specialized removal
#find . -name '*.scala' -exec mv -f '{}.bak' '{}' ';'

cd scala-scalight || exit 1
#git diff src/compiler > ../scalight-compiler2.diff
#git diff src/library > ../scalight-library2.diff

for PART in compiler reflect library; do
  git diff src/$PART > ../scalight-$PART.diff
done

#git diff src/library/scala/runtime/ScalaRunTime.scala src/library/scala/Product.scala > ../scalight-library.diff
