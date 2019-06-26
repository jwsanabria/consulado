FROM gentics/mesh

USER root
RUN apk --no-cache add curl

USER mesh
WORKDIR /mesh
COPY ./backup/graphdb /backups/
COPY ./backup/binaryImageCache  /mesh/data/binaryImageCache
COPY ./config/  /mesh/config
COPY ./bin /mesh/bin

CMD [ "java", "-jar" , "meshdemo.jar" ]
CMD [ "sh", "/mesh/bin/entrypoint.sh" ]
