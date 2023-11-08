server:
  port: ${serverPort}
  undertow:
    buffer-size: 1024
    direct-buffers: true
    threads:
      io: 4
      worker: 20

logging:
  level:
    root: info

knife4j:
  enable: true
  basic:
    # basic authentication, used to access swagger-ui and doc
    enable: false
    username: admin
    password: streampark

springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    path: /swagger-ui.html
  packages-to-scan: org.apache.streampark.console

spring:
  application.name: StreamPark
  devtools.restart.enabled: false
  mvc.pathmatch.matching-strategy: ant_path_matcher
  servlet:
    multipart:
      enabled: true
      max-file-size: 500MB
      max-request-size: 500MB
  aop.proxy-target-class: true
  messages.encoding: utf-8
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
    deserialization:
      fail-on-unknown-properties: false
  main:
    allow-circular-references: true
    banner-mode: off
  mvc:
    converters:
      preferred-json-mapper: jackson
  datasource:
    username: ${username}
    password: ${password}
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: ${databaseUrl}

management:
  endpoints:
    web:
      exposure:
        include: [ 'health', 'httptrace', 'metrics' ]
  endpoint:
    health:
      enabled: true
      show-details: always
      probes:
        enabled: true
  health:
    ldap:
      enabled: false

streampark:
  proxy:
    # knox process address https://cdpsit02.example.cn:8443/gateway/cdp-proxy/yarn
    yarn-url:
    # lark alert proxy,default https://open.feishu.cn
    lark-url:
  yarn:
    # default sample, or kerberos
    http-auth: sample

  # HADOOP_USER_NAME
  hadoop-user-name: ${hadoopUserName}
  # local workspace, used to store source code and build dir etc.
  workspace:
    local: ${workspaceLocal}
    remote: ${workspaceRemote}   # support hdfs:///streamx/ 、 /streamx 、hdfs://host:ip/streamx/

  # remote docker register namespace for streampark
  docker:
    # instantiating DockerHttpClient
    http-client:
      max-connections: 10000
      connection-timeout-sec: 10000
      response-timeout-sec: 12000
      docker-host: ""

  # flink-k8s tracking configuration
  flink-k8s:
    tracking:
      silent-state-keep-sec: 10
      polling-task-timeout-sec:
        job-status: 120
        cluster-metric: 120
      polling-interval-sec:
        job-status: 2
        cluster-metric: 3

  # packer garbage resources collection configuration
  packer-gc:
    # maximum retention time for temporary build resources
    max-resource-expired-hours: 120
    # gc task running interval hours
    exec-cron: 0 0 0/6 * * ?

  shiro:
    # token timeout, unit second
    jwtTimeOut: 86400
    # backend authentication-free resources url
    anonUrl: >

ldap:
  # Is ldap enabled? If so, please modify the urls
  enable: false
  ## AD server IP, default port 389
  urls: ldap://99.99.99.99:389
  ## Login Account
  base-dn: dc=streampark,dc=com
  username: cn=Manager,dc=streampark,dc=com
  password: streampark
  user:
    identity-attribute: uid
    email-attribute: mail
