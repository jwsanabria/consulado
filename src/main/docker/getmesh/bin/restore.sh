#!/usr/bin/env sh

BASE_URL="localhost:8080"

RESTORED_FLAG=./.backup_restored
if [ -f "$RESTORED_FLAG" ]; then
    echo "Backup was already restored on first execution"
else
    sleep ${MESH_DELAY_RESTORE} 
    echo "Backup restoration started"
    curl -c ./cookies.txt ${MESH_ADMIN_USER}":"${MESH_ADMIN_PASSWORD}"@"${BASE_URL}"/api/v2/auth/login"
#    curl -X POST -b ./cookies.txt ${BASE_URL}"/api/v2/admin/graphdb/restore"
    curl -X POST -b ./cookies.txt ${BASE_URL}"/api/v2/admin/graphdb/import"
    touch ./.backup_restored
#    curl -X POST -b ./cookies.txt ${BASE_URL}"/api/v2/search/sync"
    rm -f ./cookies.txt 
    echo "Backup restoration ended"
    halt
fi

