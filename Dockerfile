FROM tomcat:9-jdk17-temurin

# Remove ROOT padrão e copia seu WAR/arquivos
RUN rm -rf /usr/local/tomcat/webapps/*

# Copie seu projeto para o Tomcat
COPY ./src/main/webapp /usr/local/tomcat/webapps/ROOT

EXPOSE 8080

CMD ["catalina.sh", "run"]
