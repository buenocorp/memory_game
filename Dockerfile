FROM tomcat:10.1-jdk17

# Limpa webapps padrão
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia JSPs
COPY ./WebContent/ /usr/local/tomcat/webapps/ROOT/

# Copia servlets compilados
COPY ./WebContent/WEB-INF/classes /usr/local/tomcat/webapps/ROOT/WEB-INF/classes

# Copia web.xml
COPY ./WebContent/WEB-INF/web.xml /usr/local/tomcat/webapps/ROOT/WEB-INF/web.xml

EXPOSE 8080
CMD ["catalina.sh", "run"]
