#!/bin/bash
echo "Added tags:"
git diff $1|egrep '(\+object|(^\+\+.*Tags.scala))' |sed "s/+object/    /g;s/ extends[^(]*//g;s/ {//g;s_+++ b/src/_  _;s_/Tags.scala_:_;s_/_._g"
