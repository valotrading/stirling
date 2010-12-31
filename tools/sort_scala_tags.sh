#!/bin/bash
SCRIPTPATH=$(dirname "$0")
find . -name 'Tags.scala'|xargs -I {} $SCRIPTPATH/objectsorter.py {} {}
