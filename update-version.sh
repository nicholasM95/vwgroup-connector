#!/bin/bash

BRANCH=$1
VERSION=$2

if [[ ! -z "$SKIP_UPDATE_VERSION_IN_POM" ]]; then
  echo $VERSION > .image_tag
else
  if [ "$BRANCH" = "develop" ]; then
    VERSION=${VERSION:0:14}
    echo $VERSION
  fi

  ./mvnw versions:set -DnewVersion="$VERSION"
  echo "Maven version updated to $VERSION"
  git add pom.xml
  git commit -m "chore(release): Bump version to $VERSION"
  git push

fi



