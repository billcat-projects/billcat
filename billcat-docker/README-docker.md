
## 开发: 只需要启动 DB(mysql 或 pg)

```shell
cd billcat-docker
docker-compose -f docker-compose-postgres.yaml -f docker-compose-mysql.yaml -p billcat-docker up

[+] Running 2/2
 ✔ Container billcat_postgres  Started                                                                                                                  1.0s 
 ✔ Container billcat_mysql     Started                                                                                                                  1.0s 
 
docker-compose -f docker-compose-postgres.yaml -f docker-compose-mysql.yaml -p billcat-docker down --remove-orphans
[+] Running 3/3
 ✔ Container billcat_mysql     Removed                                                                                                                                                2.1s 
 ✔ Container billcat_postgres  Removed                                                                                                                                                0.5s 
 ✔ Network billcat-network     Removed                                                                                                                                                0.2s 


```
