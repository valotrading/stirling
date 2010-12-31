#!/usr/bin/env python3.1
import re
import sys

rows = []

with open(sys.argv[1], encoding='utf-8') as file:
    block = ""
    location_index = 0
    for row in file:
        if re.match("^object", row):
            rows.append((location_index, block))
            location_index = int(re.search("\((.*)\)", row).group(1))
            block = row
        else:
            block += row.rstrip() + ("\n")
    rows.append((location_index, block))

def writeout(destination):
    for obj in sorted(rows):
        destination.write(obj[1])

if (len(sys.argv) > 2):
    with open(sys.argv[2], encoding='utf-8', mode='w') as outfile:
        writeout(outfile)
else:
    writeout(sys.stdout)
