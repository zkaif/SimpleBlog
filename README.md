# SimpleBlog

create application.yml in classpath:application.yml

application.yml:

```yaml

zhoukaifan:
  simpleblog:
    savePath: /home/blogsave/
    gitPath: /home/zkaif.github.io/
    blogName: blog
    ssh:
      username: 
      host: 
      password: 
      port: 
    email:
      addresse: 
      username: 
      password: 
spring:
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8

  thymeleaf:
    prefix: classpath:/templates/
    suffix: .html
    mode: HTML5
    encoding: UTF-8
    cache: true
  resources:
    chain:
      strategy:
        content:
          enabled: true
          paths: /**
server:
  port: 443
  ssl:
    key-store: 
    key-store-type: 
    key-store-password: 
http:
  port: 80

```