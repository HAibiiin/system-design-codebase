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

## 简单 Cache-aside 模式基于 lease 方式查询场景示例

在模块 `query-engine` 中 `/cache` 包路径下增加 `LeaseWrapper` 实现，其中 Lua 脚本通过 `LuaScripts.groovy` 存储，这里使用 Groovy 主要利用其 text block 方便编写存储脚本。

[参考阅读](https://haibiiin.github.io/posts/system_design_in_action/The_design_for_ensuring_data_consistency_between_cache_and_primary_replica.html#%E5%A6%82%E4%BD%95%E8%A7%A3%E5%86%B3%E5%B9%B6%E5%8F%91%E6%95%B0%E6%8D%AE%E4%B8%8D%E4%B8%80%E8%87%B4%E5%8F%88%E8%83%BD%E9%81%BF%E5%85%8D%E5%BB%B6%E8%BF%9F%E5%8F%8C%E5%88%A0%E5%B8%A6%E6%9D%A5%E7%9A%84%E6%83%8A%E7%BE%A4%E9%97%AE%E9%A2%98)及效果如图所示：![lease](https://i-blog.csdnimg.cn/direct/d87b8eb7271340e980eeb6a60e665918.gif)

## 应用层样例调整

应用层代码无需变得，只需要调整初始化装配即可。我们通过在 `SkuApplicationServiceTests`中的`testGet_from_SkuRepositoryCacheAsideAdaptor_use_LeaseWrapper()`方法来调整装配模拟这个过程，同样这个测试用例的运行需要依赖本地的 Redis 服务。

## 简单 Cache-aside 模式基于 version 方式查询场景示例

该方式借鉴与 [Uber](https://www.uber.com/en-JP/blog/how-uber-serves-over-40-million-reads-per-second-using-an-integrated-cache/) 的方案。

> **Deduplicating Cache Writes Between Query Engine and Flux**
>
> However, the above cache invalidation strategy has a flaw. Since writes happen to the cache simultaneously between the read path as well as the write path, it is possible that we inadvertently write a stale row to the cache, overwriting the newest value that was retrieved from the database. To solve this, we deduplicate writes based on the timestamp of the row set in MySQL, which effectively serves as its version. The timestamp is parsed out from the encoded row value in Redis (see later section on codec).
>
> Redis supports executing custom Lua scripts atomically using the [EVAL](https://redis.io/commands/eval/) command. This script takes the same parameters as [MSET](https://redis.io/commands/eval/), however, it also performs the deduplication logic, checking the timestamp values of any rows already written to the cache and ensuring that the value to be written is newer. By using EVAL, all of this can be performed in a single request instead of requiring multiple round trips between the query engine layer and cache.

除了提供一个 `versionSet()` 脚本与 `VersionWrapper` 实现以外，因为存在版本号的缘故，所以需要扩展 `CacheCommands` 接口中的方法。

在单元测试中，提供了 `VersionCachePhase` 来调用 `CacheCommands` 中新增的带有版本号的 `set(String key, String value, String version)` 方法。

该实现未提供对应的应用层修改，如有兴趣可自行尝试。