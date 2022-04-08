#!/bin/bash

# DO NOT CHANGE WHERE THIS SCRIPT IS

echo "build script running"

cd $(dirname $0) || { exit 1; }

npm run build || { exit 1; }

cd .. || { exit 1; }

#[ -d "docs" ] && rm -r docs
[ ! -d "docs" ] && mkdir docs

cp -r frontend/build/ docs || { exit 1; }
