FROM tomcat:10.1-jdk17

# Remove webapps padrão
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia seu projeto para o Tomcat
COPY ./src/main/webapp /usr/local/tomcat/webapps/ROOT

EXPOSE 8080

CMD ["catalina.sh", "run"]
