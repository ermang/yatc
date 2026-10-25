# yatc

yatc is a simple twitter clone - microservice arch.

##  Projects:
* yatc-auth port:8081
* yatc-core port:8083
* yatc-gateway port:8080
* yatc-service-discovery port:8761
* yatc-user  port:8082

## Features:
* Create/Login User  
* Create/Read/Delete Tweet  
* Like Thread 
* Follow User
* Timeline

## 1.Requirements

* jdk 17  
* maven  
* postgres
* kafka

## 2.How to build&run

### From cmd
cd to project_dir  
mvn package  
cd target  
java -jar yafi-1.0-SNAPSHOT.jar  

### From IDE

open com.eg.yafi.Application  
IDE should show a play icon at the class name. Click it

### EVERYTHING LOCAL SETUP MODE 
*  ./bin/elasticsearch
* ./bin/kibana
* ./filebeat -e -c filebeat.yml
* java -jar zipkin.jar


* service discovery eureka http://localhost:8761/
* Elasticsearch http://localhost:9200/
* Kibana http://localhost:5601
* Zipkin http://127.0.0.1:9411/zipkin/

### 3.Package Structure

**controller:** Rest endpoint classes are stored here  
**dto:** DTO classes are here, controller classes receive DTO return DTO, for separation of concerns  
**entity:** @Entity annotated classes are stored here, ORM mapped classes  
**projection:** for read-only SELECT queries use/add classes here - more performant than selecting @Entity annotated classes  
**repo:** JPA/Hibernate query methods are stored here, each @Entity class has its own Repo such as Company and CompanyRepo  
**service:** service classes are stored here, operations are done inside these classes  


**src/main/resources/application.properties:** project configuration file  
**important features:**  
* spring.jpa.hibernate.ddl-auto:
  * create => drop existing tables create new tables
  * none => don't change existing db tables
* server.port => port for the application to run

## 4.Postman Collection

**yafi.postman_collection.json** postman collection for sample requests - using v2.1 

