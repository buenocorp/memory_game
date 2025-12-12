FROM tomcat:10.1-jdk21

# Remove apps padrão
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia o WAR exportado do Eclipse
COPY Memoria.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
