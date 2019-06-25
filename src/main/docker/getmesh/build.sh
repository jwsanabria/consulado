#!/usr/bin/env bash

export build_version='0.1'

docker build -t co_gov_cancilleria/getmesh:${build_version} ./
