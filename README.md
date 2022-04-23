# Upload, download files as mysql blobs with Spring [![tests](https://github.com/daggerok/spring-mvc-upload-download-files-as-mysql-blobs/actions/workflows/tests.yml/badge.svg)](https://github.com/daggerok/spring-mvc-upload-download-files-as-mysql-blobs/actions/workflows/tests.yml)
Spring boot app based on Spring MVC, Spring Data JPA, MySQL and Liquibase which can store (download) file content in MySQL database as
blobs and upload them and their metadata back 

## Test and build

```bash
./mvnw
```

## Run and verify

```bash
./mvnw -f docker -P down ; ./mvnw -f docker -P up ; ./mvnw -f docker -P logs & 
while [[ $(docker ps -n 1 -q -f health=healthy -f status=running | wc -l) -lt 1 ]] ; do sleep 3 ; echo -n '.' ; done ; sleep 15; echo 'MySQL is ready.'

DB_USERNAME=user BD_PASSWORD=password DB_NAME=database ./mvnw -f app compile spring-boot:start

content=`cat README.md`
http :8080 name=README.md content=$content

./mvnw -f app spring-boot:stop
docker rm -f -v `docker ps -aq`
```

## RTFM
* 
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.6/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.6/maven-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.6.6/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.6.6/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.6.6/reference/htmlsingle/#configuration-metadata-annotation-processor)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
