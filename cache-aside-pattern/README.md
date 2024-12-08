# Quick Start

在 application-sample 模块下是我们的应用样例代码，划分如下图所示的三个层级，分别为最核心的 domain 层，以及实现数据库操作相关的 adaptor 层，最上层为 application 层用于组装应用服务。


```
 ┌───────────────────────────────────────┐ 
 │domain ┌─────────────┐                 │ 
 │       │SkuRepository│                 │ 
 │       └────▲────────┘                 │ 
 └────────────│──────────────────────────┘ 
 ┌────────────│──────────────────────────┐ 
 │adaptor     │           ┌─────────┐    │ 
 │            │           │SkuMapper│    │ 
 │            │           └─▲───────┘    │ 
 │        ┌───┴─────────────┴──┐         │ 
 │        │SkuRepositoryAdaptor│         │ 
 │        └─────────▲──────────┘         │ 
 └──────────────────│────────────────────┘ 
 ┌──────────────────│────────────────────┐ 
 │application       │                    │ 
 │           ┌──────┴──────┐             │ 
 │           │SkuAppService│             │ 
 │           └─────────────┘             │ 
 └───────────────────────────────────────┘                                   
```

在项目目录下执行：

```shell
mvn clean test
```

在 application-sample 样例工程中，基于 MyBatis 实现对数据库的简单查询，在单元测试`SkuApplicationServiceTests.java` 中启动内存模式下 H2 数据库进行功能验证。