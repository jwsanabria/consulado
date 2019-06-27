#!/usr/bin/env sh

sleep ${MESH_DELAY_RESTORE} 

BASE_URL="localhost:8080"

echo ${MESH_ADMIN_USER}":"${MESH_ADMIN_PASSWORD}"@"${BASE_URL}"/api/v2/auth/login"

curl -c /mesh/cookies.txt ${MESH_ADMIN_USER}":"${MESH_ADMIN_PASSWORD}"@"${BASE_URL}"/api/v2/auth/login"
curl -X POST -b /mesh/cookies.txt ${BASE_URL}"/api/v2/admin/graphdb/restore"
curl -c /mesh/cookies.txt ${MESH_ADMIN_USER}":"${MESH_ADMIN_PASSWORD}"@"${BASE_URL}"/api/v2/auth/login"
curl -X POST -b /mesh/cookies.txt ${BASE_URL}"/api/v2/search/sync"
