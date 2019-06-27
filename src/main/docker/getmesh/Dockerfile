FROM gentics/mesh

USER root
RUN apk --no-cache add curl

USER mesh
WORKDIR /mesh
COPY ./backup/graphdb /backups/
#COPY ./backup/binaryImageCache  /mesh/data/binaryImageCache
COPY ./config/  /mesh/config
COPY ./bin /mesh/bin

USER root
#RUN chown -R mesh: /mesh/data/
RUN chown -R mesh: /mesh/bin/
USER mesh


CMD [ "sh", "/mesh/bin/entrypoint.sh" ]
