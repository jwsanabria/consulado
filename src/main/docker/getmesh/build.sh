#!/usr/bin/env bash

export build_version='0.1'

docker build -t co.gov.cancilleria/getmesh:${build_version} ./
