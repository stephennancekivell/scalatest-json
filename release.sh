#!/usr/bin/env bash

version="$1"

echo "step 1"
git flow release start "$version"
echo "step 2"
git flow release finish -m '$version' "$version"
echo "step 3"
git checkout develop