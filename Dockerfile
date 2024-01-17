
# Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
# Click nbfs://nbhost/SystemFileSystem/Templates/Other/Dockerfile to edit this template

FROM openjdk:17
ADD target/crud-docker.jar crud-docker.jar
ENTRYPOINT ["java","-jar","/crud-docker.jar"]
