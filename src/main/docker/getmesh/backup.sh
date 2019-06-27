#!/usr/bin/env sh

BASE_URL="localhost:8005"
MESH_ADMIN_USER="admin"
MESH_ADMIN_PASSWORD="admin"


curl -c /mesh/cookies.txt ${MESH_ADMIN_USER}":"${MESH_ADMIN_PASSWORD}"@"${BASE_URL}"/api/v2/auth/login"
curl -X POST -b /mesh/cookies.txt ${BASE_URL}"/api/v2/admin/graphdb/backup"

docker cp ${CONTAINER_ID}":/backups/* ./backup/graphdb/"
docker cp ${CONTAINER_ID}":/uploads/ ./backup/uploads/"

