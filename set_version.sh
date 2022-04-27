#!/bin/bash

if [ $# -eq 0 -o -z "$1" ]
then
    echo "Usage"
    echo "./set_version.sh <version name>"
    exit 1
fi

ROOTDIR=`dirname "$0"`
ROOTDIR=`readlink -e "$ROOTDIR"`
cd "$ROOTDIR"

sed -i '/^version := /c\version := "'$1'"' build.sbt
sed -i '/^libraryDependencies/c\libraryDependencies += "org.nisshiee" %% "towerdefense-scala" % "'$1'"' README.md
sed -i '/^http:\/\/nisshiee\.github\.io\/towerdefense-scala\/doc\//c\http://nisshiee.github.io/towerdefense-scala/doc/'$1'/' README.md
