FROM openjdk:7-alpine

RUN apk add --no-cache curl && \
    mkdir -p /opt/tomcat && \
    curl -SL https://archive.apache.org/dist/tomcat/tomcat-7/v7.0.109/bin/apache-tomcat-7.0.109.tar.gz \
    | tar -xzC /opt/tomcat --strip-components=1 && \
    apk del curl

ENV CATALINA_HOME /opt/tomcat

# Remove all files and folders in webapps directory
RUN rm -rf $CATALINA_HOME/webapps/*

# Copy ROOT.war to Tomcat webapps directory
COPY target/cuoi5s.war $CATALINA_HOME/webapps/ROOT.war

EXPOSE 8080

CMD ["/opt/tomcat/bin/catalina.sh", "run"]
