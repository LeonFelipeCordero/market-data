# Market data project
This is the code used in the talk "Desarrollo de aplicaciones financieras en 2023" on 13.03.2023
by "La semana de ingenieria de la UACH"

##  Setup

### External systems
```shell
cd env
docker build -t partner-api .
docker compose up
```

This will start 3 things, MongoDB, RabbitMQ and the partner API

### Run
```shell
./gradlew bootRun
```
The application runs on port 8081
