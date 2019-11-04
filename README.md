# DbUnit 적용해보기 예제코드

### MySQL 설치
[Docker](https://www.docker.com/) 설치 후

docker run --name mysql57 \
    -p 3306:3306 \
    -e MYSQL_ROOT_PASSWORD=root \
    -e MYSQL_ROOT_HOST='%' \
    --restart=unless-stopped \
    -d \
    mysql/mysql-server:5.7```
```
MySQL 콘솔접속
```
docker exec -it mysql57 bash
```
데이터베이스 생성
```
create database dbunit
```

### 최초 테이블 생성
콘솔창에서 프로젝트 폴더로 이동 후 아래의 명령어를 실행하여 테이블을 초기화 해주어야 합니다.
```
./gradlew flywayClean flywayBaseline flywayMigrate
```