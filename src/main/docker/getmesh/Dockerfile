FROM gentics/mesh:0.36.0

USER root
RUN apk --no-cache add curl

USER mesh
WORKDIR /mesh
COPY ./backup/backups/* /backups/
COPY ./backup/uploads  /uploads
COPY ./config/  /mesh/config
COPY ./bin /mesh/bin

USER root
RUN chown -R mesh: /uploads
RUN chown -R mesh: /backups
RUN chown -R mesh: /mesh/bin/
USER mesh


CMD [ "sh", "/mesh/bin/entrypoint.sh" ]
