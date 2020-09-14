# Spring Boot
## 实现

此后端基于springboot实现，特点有：

- 采用MVC架构风格
- 使用 ORM 框架来访问数据库
- 使用 IoC在分层结构中注入具体接口的实现类以及其他参数
- 工程中体现出了合理的分层架构，包括表示层、控制层、 服务层、实体层等，始终遵循接口与实现分离原则

## 安全

安全包括认证（Authentication）和授权（Authorization）。

springboot的后端采用拦截器的方式对前端请求进行拦截，Authentication部分首先调用login函数，验证前端发送的用户名和密码，若正确，将用户信息存储在session中，后端会在后续Authorization中利用存储在session中的用户信息进行用户权限的判断。

## 部署

我们利用docker将后端部署于ec2中
