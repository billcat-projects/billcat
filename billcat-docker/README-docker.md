## For now, use MySQL only

start mysql

```shell
docker rm -f billcat_mysql
docker volume rm -f billcat-docker_mysql
docker volume create billcat-docker_mysql
docker-compose -f docker-compose-mysql.yaml up
docker ps
```

stop mysql

```shell
docker-compose -f docker-compose-mysql.yaml down
```


## Start up both databases(Backup)

```shell
cd billcat-docker
docker-compose -f docker-compose-postgres.yaml -f docker-compose-mysql.yaml up -d
```
