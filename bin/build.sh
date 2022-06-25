#!/bin/bash

base_dir=$(dirname $0)
echo $base_dir

mvn clean package -Pdocker -f $base_dir/../pom.xml
mvn dockerfile:build -f $base_dir/../backend-manager/ruoyi-admin/pom.xml