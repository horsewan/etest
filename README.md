# futruedao-server

后台系统，主要是对各个模块（用户中心、积分中心、商城中心、支付中心、IM、数据中心、API等）管控。

基础配置和模块如下：
    aipen-admin-web后台管理模块 
    aipen-api-web接口模块（用户接口、积分接口、商城接口） 
    aipen-pay支付模块 
    
    全局配置文件：
        application.yml基础配置文件 
        application-dev.yml默认（开发环境）配置文件 
        application-prod.yml生产环境配置文件 


JWT拦截器配置 
    后台管理配置 
        WebMvcConfig.java不建议修改（权限、管理配置混乱） 

    接口配置
        aipen-api-web (WebMvcConfig.java)可配置具体接口授权和验证级别 
        
前端采用Layui框架（https://www.layui.com/）

+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

技术选型
    主框架：Spring Boot 2.0、Spring Framework 5.0、Apache Shiro 1.4
    持久层：Apache MyBatis 3.4、Hibernate Validation 6.0、Alibaba Druid 1.1
    视图层：Spring MVC 5.0替换JSP、Bootstrap 3.3、AdminLTE 2.4
    前端组件：jQuery 1.12、jqGrid 4.7、layer 3.0、zTree 3.5、jquery-validation
    工具组件：Apache Commons、Logback 1.1、Jackson 2.8、POI 3.14、Quartz 2.2


分布式微服务系统（Spring Cloud）
技术选型
    分布式系统套件版本：Spring Cloud Finchley
    服务治理注册与发现：Spring Cloud Netflix Eureka
    服务容错保护限流降级：Spring Cloud Netflix Hystrix
    分布式统一配置中心：Spring Cloud Config
    网关路由代理调用：Spring Cloud Gateway
    声明式服务调用：Spring Cloud OpenFeign
    分布式链路追踪：Spring Cloud Zipkin
    
子项目介绍
    服务治理：aipen-cloud-eureka ： http://127.0.0.1:8970
    配置中心：aipen-cloud-config ： http://127.0.0.1:8971/project/default
    网关路由：aipen-cloud-gateway ： http://127.0.0.1:8980/ai/a/login
    核心模块（统一授权认证）：aipen-cloud-module-core ： http://127.0.0.1:8981/ai
    
