## canal整合springboot 实现数据同步通用框架

通用框架使用流程:
1.
application文件里配置canal对应的相关参数
2.
编写每个业务对应的EventHandler
tips:
由于考虑到对不同的表,会有不同的业务逻辑操作, 所以数据库+表名+mysql里事件类型 组合成一个唯一的key, 可以对应为一个EventHandler
当然也可以满足共用表的处理,即把application文件的custom设置为false
3.
在EventHandlerFactoryConfig类里, 将业务Handler注入到工厂里初始化
4.
继承DataSyncApp类, 重写createHandlerByEventType方法. 从工厂里拿


mysql同步一张表的数据到redis:
1.
1)准备好redis的pom包, 工具类 ;
2)application文件里配置相关的参数

2.
将编写好的RedisAddHandler RedisDeleteHandler RedisUpdateHandler在EventHandlerFactoryConfig类里注入工厂

3.
springboot启动时, 自动调用RedisSyncApp的work方法,即可监听拉取数据

4.
当有update delete或insert时间的时候, 即可触发对应表的对应update delete或insert EventHandler类,去同步CRUD操作到redis里



