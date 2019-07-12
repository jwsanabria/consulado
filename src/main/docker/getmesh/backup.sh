#!/usr/bin/env sh

BASE_URL="localhost:8080"
MESH_ADMIN_USER="admin"
MESH_ADMIN_PASSWORD="admin"
CONTAINER_ID=$(docker ps | grep 'miconsulado/getmesh:latest' | awk '{ print $1 }')
echo "CONTAINER:"${CONTAINER_ID}

#curl -c ./cookies.txt ${MESH_ADMIN_USER}":"${MESH_ADMIN_PASSWORD}"@"${BASE_URL}"/api/v2/auth/login"
#curl -X POST -b ./cookies.txt ${BASE_URL}"/api/v2/admin/graphdb/backup"

rm -rf ./backup/*
docker cp ${CONTAINER_ID}":/backups" "./backup/"
docker cp ${CONTAINER_ID}":/uploads" "./backup/"
rm ./cookies.txt

