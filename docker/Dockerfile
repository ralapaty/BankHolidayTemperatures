FROM nimmis/java-centos:oracle-8-jdk
# copy spring-boot jar file
COPY bankholidays.jar /app/
COPY start_service.sh /app/
RUN chmod -R 755 /app
# expose ports
EXPOSE 8080
# set up entry point and default command
CMD [ "/app/start_service.sh" ]
