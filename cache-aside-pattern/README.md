# Quick Start

查看[关联 issue](https://github.com/HAibiiin/system-design-codebase/issues/1)了解开发内容以及每次 `commit` 变化。通过以下命令运行与检测结果：

```shell
mvn clean test
```

## 应用层样例

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

## Query Engine 设计

在 `/doc` 目录下存放着基于 plantuml 的 Query Engine 设计文件，并未设计出全部可能用到的内容，仅满足核心思想为设计提供参考。

## 简单 Cache-aside 模式查询场景示例

在模块 `query-engine` 中 `/cache` 包路径下定义 `CacheCommands` 接口，目的是为了抽象出不同缓存操作组件，方便基于实际情况替换不同的实现。

在单元测试 `SimpleQueryEngineTests.java` 中我们基于 Query Engine 的设计中缓存与数据库操作的两个阶段，提供了两个简易的实现，分别为 `SampleCachePhase` 和 `SampleDatabasePhase` 。

在测试用例 `testJedisWrapper()` 和 `testMapWrapper` 演示了基于 `CacheCommands` 的多样性。其中 `testJedisWrapper()` 的运行需要依赖本地的 Redis 服务。

## 应用层样例调整

提供 `SkuRepository` 接口的新实现 `SkuRepositoryCacheAsideAdaptor`，并在 adaptor 层实现 `CachePhase` 和 `DatabasePhase`，其中 `DatabasePhase` 实现复用 `SkuRepositoryAdaptor`。

其次需要修改 application 层的 `Initializer` 对  `SkuRepositoryCacheAsideAdaptor` 进行组装，关于缓存的部分为了便于单元测试我们使用了基于 ConcurrentMap 实现的 `MapWrapper`。