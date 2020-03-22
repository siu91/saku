[![License](https://img.shields.io/github/license/apache/incubator-streampipes.svg)](http://www.apache.org/licenses/LICENSE-2.0)

# Saku <img src="./assets/LOGO.png" align="right" />

> **Saku**一个高性能的短链服务







:mask:疫情无情，人有情，来了就**Star**:star:一下吧 :point_up:



## 整体设计

![skau](./assets/saku.png)

- 1、解决内容中url太长影响阅读体验和内容质量
- 2、短链容易转换（二维码）、传播分享
- 3、长链第三方无法识别（微信、钉钉）

### 短链请求过程&原理

![saku-1](./assets/saku1.png)

## Features

- 支持MurmurHash/DB Auto Incr 高性能生成短链

## Quick Start

* git clone

- 配置

  ```yml
  saku:
    type: hash # 默认hash（基于Murmurhash）,可选dbincr（数据库自增）
  
  spring:
    datasource:
      url: jdbc:postgresql://postgres.host:5432/xxx
      username: postgres
      password: postgres
      driver-class-name: org.postgresql.Driver
  
  # 服务启动的端口
  server:
    port: 9555
  
  ```

- 启动服务

- 生成短链 /v1/saku/s/{long_url}

  ![image-20200322213919662](./assets/image-20200322213919662.png)

- 使用短链换取原URL:/v1/saku/l/{short_url}
- ![image-20200322214105681](./assets/image-20200322214105681.png)



## TODO

1. 去重过滤（目前主要考虑没有重复的场景，如果有大量相同的短链生成请求，数据库自增的方式会冗余大量数据）
2. 提供压测数据
3. 支持多种数据库（mysql 、Oracle）


## Feedback

 [gshiwen@gmail.com](mailto:gshiwen@gmail.com)

## License

[Apache License 2.0](LICENSE)



