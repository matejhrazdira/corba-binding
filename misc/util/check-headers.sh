#!/bin/bash
echo "checking headers in .java files"
find ../../. -type f -name "*.java" -exec ./check-header.sh {} \;

