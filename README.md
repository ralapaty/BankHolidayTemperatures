# BankHolidayTemperatures
Restful API to return temperatures for a UK city on bank holidays.

# Software & dependencies used
- JDK 1.8.0_241
- Maven 3.6.3_1
- Spring Boot
- Project Lombok
- Swagger 2
- Docker

# Build Instructions to generate the final jar with all dependencies
`mvn clean install
`
# Example Application URL
http://localhost:8080/bank-holidays/London/temps?startDate=2018-01-01&endDate=2018-06-01
        
# Swagger Documentation
http://localhost:8080/bank-holidays/swagger-ui.html

# Docker containter installation instructions
- Install Docker
- Change to docker directory in this repo
- Copy final jar to docker directory as `bankholidays.jar`
- Build docker container using the below instructions
- `docker build -t bankholidays:1 .`
- `docker run -d --name bank-app -p 8080:8080 bankholidays:1`
- Confirm container is running using
- `docker ps -a`
- Login to the running docker container (use the container id, user docker)
- `docker exec -it 948a9cba0ae6 bash`
- Access the service from command line
- `curl -H "Accept: application/json" http://localhost:8080/bank-holidays/London/temps\?startDate=2018-01-01\&endDate=2018-06-01`
