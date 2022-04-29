# Upload, download files as mysql blobs with Spring [![tests](https://github.com/daggerok/spring-mvc-upload-download-files-as-mysql-blobs/actions/workflows/tests.yml/badge.svg)](https://github.com/daggerok/spring-mvc-upload-download-files-as-mysql-blobs/actions/workflows/tests.yml)
Spring boot app based on Spring MVC, Spring Data JPA, MySQL and Liquibase which can store (download) file content in MySQL database as
blobs and upload them and their metadata back 

## app-0-content-as-byte-array
This app stores data as a kotlin byte-array, but mysql longblob

```kotlin
@Entity
@Table(name = "report_items")
data class ReportItem(

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false, updatable = false)
    val id: Long? = null,

    // @Lob // Use columnDefinition if you want string
    // @Column(nullable = false, updatable = true, columnDefinition="LONGBLOB NOT NULL")
    // val content: String = "",

    @Lob // No needs to use columnDefinition when using byte array
    @Suppress("ArrayInDataClass")
    @Column(nullable = false, updatable = true)
    val content: ByteArray = ByteArray(0),
    
    // skipped...
)
```

### test and build

```bash
./mvnw
```

### run and verify

```bash
if [[ "" != `docker ps -aq` ]] ; then docker rm -f -v `docker ps -aq` ; fi
docker run --rm --name mysql --platform linux/x86_64 \
           --health-cmd='mysqladmin ping -h 127.0.0.1 -u $MYSQL_USER --password=$MYSQL_PASSWORD || exit 1' \
           --health-start-period=1s --health-retries=1111 --health-interval=1s --health-timeout=5s \
           -e MYSQL_USER=user -e MYSQL_PASSWORD=password \
           -e MYSQL_DATABASE=database -e MYSQL_ROOT_PASSWORD=password \
           -p 3306:3306 \
           -d mysql:8.0.24
while [[ $(docker ps -n 1 -q -f health=healthy -f status=running | wc -l) -lt 1 ]] ; do sleep 3 ; echo -n '.' ; done ; sleep 15; echo 'MySQL is ready.'
docker logs -f mysql &

./mvnw -f apps/app-0-content-as-byte-array compile spring-boot:start

content=`cat README.md`
http :8000 name=README.md content=$content
http :8000

./mvnw -f apps/app-0-content-as-byte-array spring-boot:stop
docker stop mysql
```

## app-1-content-as-string
This app stores data as a kotlin text (string), but mysql longblob

```kotlin
@Entity
@Table(name = "report_items")
data class ReportItem(

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false, updatable = false)
    val id: Long? = null,

    @Lob // Use columnDefinition if you want to serialize mysql blob as kotlin string
    @Column(nullable = false, updatable = true, columnDefinition="LONGBLOB NOT NULL")
    val content: String = "",

    // @Lob // No needs to use columnDefinition with bytes
    // @Suppress("ArrayInDataClass")
    // @Column(nullable = false, updatable = true)
    // val content: ByteArray = ByteArray(0),

    // skipped..
)
```

### test and build

```bash
./mvnw
```

### run and verify

```bash
if [[ "" != `docker ps -aq` ]] ; then docker rm -f -v `docker ps -aq` ; fi
./mvnw -f docker -P down ; ./mvnw -f docker -P up ; ./mvnw -f docker -P logs &
while [[ $(docker ps -n 1 -q -f health=healthy -f status=running | wc -l) -lt 1 ]] ; do sleep 3 ; echo -n '.' ; done ; sleep 15; echo 'MySQL is ready.'

./mvnw -f apps/app-1-content-as-string compile spring-boot:start

content=`cat README.md`
http :8001 name=README.md content=$content
http :8001

./mvnw -f apps/app-1-content-as-string spring-boot:stop
docker rm -f -v `docker ps -aq`
```

## app-2-upload-file
This app implements upload functionality

### test and build

```bash
./mvnw
```

### run and verify

```bash
if [[ "" != `docker ps -aq` ]] ; then docker rm -f -v `docker ps -aq` ; fi
./mvnw -f docker -P down ; ./mvnw -f docker -P up ; ./mvnw -f docker -P logs &
while [[ $(docker ps -n 1 -q -f health=healthy -f status=running | wc -l) -lt 1 ]] ; do sleep 3 ; echo -n '.' ; done ; sleep 15; echo 'MySQL is ready.'

./mvnw -f apps/app-2-upload-file compile spring-boot:start

content=`cat README.md`
http -f post :8002/upload file@README.md
http -f post :8002/upload file@$PWD/README.md
http -f post :8002/upload file@`pwd`/README.md
http     get :8002

./mvnw -f apps/app-2-upload-file spring-boot:stop
docker rm -f -v `docker ps -aq`
```

## app-3-download-file
This app implements download functionality

### test and build

```bash
./mvnw
```

### run and verify

```bash
if [[ "" != `docker ps -aq` ]] ; then docker rm -f -v `docker ps -aq` ; fi
./mvnw -f docker -P down ; ./mvnw -f docker -P up ; ./mvnw -f docker -P logs &
while [[ $(docker ps -n 1 -q -f health=healthy -f status=running | wc -l) -lt 1 ]] ; do sleep 3 ; echo -n '.' ; done ; sleep 15; echo 'MySQL is ready.'

./mvnw -f apps/app-3-download-file compile spring-boot:start

http --form --multipart --boundary=xoxo post :8003/upload file@README.md
http -f                                 post :8003/upload file@$PWD/pom.xml
http                                     get :8003

cd /tmp ; http --download get :8003/download\?id=2 ; ls -lah . | grep pom.xml
cd /tmp ; http -f         get :8003/download\?id=1 > README.md ; ls -lah . | grep README.md

./mvnw -f apps/app-3-download-file spring-boot:stop
docker rm -f -v `docker ps -aq`
```

## app-4-binary-files

### test and build

```bash
./mvnw
```

### run and verify

```bash
if [[ "" != `docker ps -aq` ]] ; then docker rm -f -v `docker ps -aq` ; fi
./mvnw -f docker -P down ; ./mvnw -f docker -P up ; ./mvnw -f docker -P logs &
while [[ $(docker ps -n 1 -q -f health=healthy -f status=running | wc -l) -lt 1 ]] ; do sleep 3 ; echo -n '.' ; done ; sleep 15; echo 'MySQL is ready.'

./mvnw -f apps/app-4-binary-files compile spring-boot:start

http --form --multipart --boundary=xoxo post :8004/upload file@README.md
http -f                                 post :8004/upload file@$PWD/pom.xml
http                                     get :8004

cd /tmp ; http --download get :8004/download\?id=2 ; ls -lah . | grep pom.xml
cd /tmp ; http -f         get :8004/download\?id=1 > README.md ; ls -lah . | grep README.md

./mvnw -f apps/app-4-binary-files spring-boot:stop
docker rm -f -v `docker ps -aq`
```

## app

### test and build

```bash
./mvnw
```

### run and verify

```bash
if [[ "" != `docker ps -aq` ]] ; then docker rm -f -v `docker ps -aq` ; fi
./mvnw -f docker -P down ; ./mvnw -f docker -P up ; ./mvnw -f docker -P logs &
while [[ $(docker ps -n 1 -q -f health=healthy -f status=running | wc -l) -lt 1 ]] ; do sleep 3 ; echo -n '.' ; done ; sleep 15; echo 'MySQL is ready.'

./mvnw -f apps/app compile spring-boot:start

http --form --multipart --boundary=xoxo post :8080/upload file@README.md
http -f                                 post :8080/upload file@$PWD/pom.xml
http                                     get :8080

cd /tmp ; http --download get :8080/download\?id=2 ; ls -lah . | grep pom.xml
cd /tmp ; http -f         get :8080/download\?id=1 > README.md ; ls -lah . | grep README.md

./mvnw -f apps/app spring-boot:stop
docker rm -f -v `docker ps -aq`
```

## RTFM
* [Upload files with IDEA HTTP requests REST client](https://www.jetbrains.com/help/idea/exploring-http-syntax.html#use-multipart-form-data)
* [HTTPie files upload / download](https://httpie.io/docs/cli/file-upload-forms)
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
