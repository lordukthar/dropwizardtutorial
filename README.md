# Blog

How to start the Blog application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/Blog-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8960/blog-service`


curl -v -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJTT01FSUQxMjM0IiwiaWF0IjoxNTgzMjMxMjc4LCJzdWIiOiJBbmRyZXciLCJpc3MiOiJKV1QgRGVtbyIsImV4cCI6MTU4MzIzMjA3OH0.AXLwk0MUS17Zr3GAI_jt1aGrH2-shK5Y71QesGCz14M"  http://localhost:8960/blog-service/users
