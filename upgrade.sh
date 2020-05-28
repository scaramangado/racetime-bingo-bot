#!/bin/bash

./gradlew clean
git pull
./gradlew build
docker-compose build --pull
docker-compose up -d
