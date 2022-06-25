#!/bin/bash

base_dir=$(dirname $0)
echo $base_dir

instance_name=`dd bs=4 count=1 if=/dev/urandom 2>/dev/null | xxd -p `
echo $instance_name

filename=$base_dir/../pom.xml

pwd $filename
start=`grep -n "<properties>" $filename|head -n 1|awk -F ":" '{print $1}'`
end=`grep -n "</properties>" $filename|head -n 1|awk -F ":" '{print $1}'`
parent=`sed -n $start,$end"p" $filename`
# echo $parent
version=`echo $parent|awk -F "<ruoyi.version>" 'BEGIN{IGNORECASE=1} {print $2}'|awk -F "</ruoyi.version>" 'BEGIN{IGNORECASE=1} {print $1}'`
echo $version

docker run --name points-manager_$instance_name -d -p 80:8080 com.ruoyi/ruoyi-admin:$version