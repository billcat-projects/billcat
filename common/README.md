1. 对 lombok 和 junit 的依赖放在了 root pom 中
2. 对 swagger annotation 的依赖放在了对应的 common 模块.
3. 依赖关系: `web -> common-core -> (common-model + common-util)` (所以先做 model)
4. 安装到本地: `mvn install -pl SubModule`
5. 部署 mvn deploy -pl SubModule, 如: `mvn deploy -pl common/billcat-common-model`

## 1. Core
exception

我们从 405 开始来测试框架对异常的处理

```shell
curl -si "localhost:8801/api/v1/product-service/product/1"
```

## 2. model

### domain
Page相关

### result
包含: ResultCode 和 R
依赖: lombok 和 swagger annotation

- BaseResultCode: 基础架构相关的异常码, 比如数据库连接失败 90001
- SystemResultCode: Http状态码, 如 200, 400, 401

## 3. Util
包含: StringUtils 和 Symbol
