# Use alpine:3.14 as the base image
FROM alpine:3.14

# Set environment variables for Tomcat version and CATALINA_HOME directory
ENV TOMCAT_MAJOR_VERSION=7 \
    TOMCAT_MINOR_VERSION=7.0.109 \
    CATALINA_HOME=/usr/local/tomcat

# Set environment variables for database connection
ENV DB_URL=jdbc:mysql://mysql:3306/cuoi5s
ENV DB_USERNAME=cuoi5s
ENV DB_PASSWORD=TranQuang2007

# Update the image and install necessary packages
RUN apk update && \
    apk add --no-cache openjdk7-jre bash wget && \
    rm -rf /var/cache/apk/*

# Download and install Tomcat
RUN wget -O /tmp/apache-tomcat.tar.gz \
    https://archive.apache.org/dist/tomcat/tomcat-$TOMCAT_MAJOR_VERSION/v$TOMCAT_MINOR_VERSION/bin/apache-tomcat-$TOMCAT_MINOR_VERSION.tar.gz && \
    tar -xzvf /tmp/apache-tomcat.tar.gz -C /usr/local/ && \
    mv /usr/local/apache-tomcat-$TOMCAT_MINOR_VERSION $CATALINA_HOME && \
    rm /tmp/apache-tomcat.tar.gz

# Set appropriate permissions for the Tomcat directory
RUN addgroup -S tomcat && \
    adduser -S -G tomcat -h $CATALINA_HOME -s /bin/false tomcat && \
    chown -R tomcat:tomcat $CATALINA_HOME && \
    chmod -R 755 $CATALINA_HOME && \
    chmod -R g+s $CATALINA_HOME && \
    chmod -R g+w $CATALINA_HOME/logs $CATALINA_HOME/temp $CATALINA_HOME/work

# Remove unnecessary files from the Tomcat installation
RUN rm -rf $CATALINA_HOME/webapps/*

# Copy the cuoi5s.war file to the Tomcat webapps directory
COPY target/cuoi5s.war $CATALINA_HOME/webapps/ROOT.war

# Expose the default Tomcat port
EXPOSE 8080

# Start Tomcat
CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]
