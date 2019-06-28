#!/usr/bin/env sh

MESH_START_DELAY=60
#sleep ${MESH_DELAY_RESTORE}

MESH_ADMIN_USER="admin"
MESH_ADMIN_PASSWORD="admin"


BASE_URL="localhost:8005"
CONTAINER_ID=$(docker ps | grep 'miconsulado/getmesh:latest' | awk '{ print $1 }')

curl -c /mesh/cookies.txt ${MESH_ADMIN_USER}":"${MESH_ADMIN_PASSWORD}"@"${BASE_URL}"/api/v2/auth/login"
curl -X POST -b ./cookies.txt ${BASE_URL}"/api/v2/admin/graphdb/restore"
docker restart CONTAINER_ID
sleep ${MESH_START_DELAY}
curl -c /mesh/cookies.txt ${MESH_ADMIN_USER}":"${MESH_ADMIN_PASSWORD}"@"${BASE_URL}"/api/v2/auth/login"
curl -X POST -b ./cookies.txt ${BASE_URL}"/api/v2/search/sync"
