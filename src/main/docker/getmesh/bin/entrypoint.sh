#!/usr/bin/env bash


#/mesh/bin/restore.sh &

cd /mesh
java -jar mesh.jar

sleep 60s
/mesh/bin/restore.sh &

