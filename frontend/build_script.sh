#!/bin/bash

# DO NOT CHANGE WHERE THIS SCRIPT IS

echo "build script running"

cd $(dirname $0)

npm run build

cd ..

[ -d "docs" ] && rm -r docs
mkdir docs

cp -r frontend/build/ docs

