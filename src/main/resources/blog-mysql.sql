create table m_user
(
    id         bigint auto_increment
        primary key,
    username   varchar(64)  null,
    avatar     varchar(255) null,
    email      varchar(64)  null,
    password   varchar(64)  null,
    status     int(5)       not null,
    created    datetime     null,
    last_login datetime     null
)
    charset = utf8;

create index UK_USERNAME
    on m_user (username);

INSERT INTO `db-it`.m_user (id, username, avatar, email, password, status, created, last_login) VALUES (1, 'jessewu', 'https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg', 'wyyxwzx@163.com', '96e79218965eb72c92a549dd5a330112', 0, '2020-04-20 10:44:01', '2020-08-13 17:19:25');


create table m_blog
(
    id          bigint auto_increment
        primary key,
    user_id     bigint       not null,
    title       varchar(255) not null,
    description varchar(255) not null,
    content     longtext     null,
    created     datetime     not null on update CURRENT_TIMESTAMP,
    status      tinyint      null
)
    charset = utf8mb4;

INSERT INTO `db-it`.m_blog (id, user_id, title, description, content, created, status) VALUES (1, 1, '生活就像海洋，只有意志坚强的人才能到达彼岸', '这里是摘要哈哈哈', '内容？？？', '2020-08-14 18:41:19', 1);
INSERT INTO `db-it`.m_blog (id, user_id, title, description, content, created, status) VALUES (2, 1, '最值得学习的博客项目eblog', 'eblog是一个基于Springboot2.1.2开发的博客学习项目，为了让项目融合更多的知识点，达到学习目的，编写了详细的从0到1开发文档。主要学习包括：自定义Freemarker标签，使用shiro+redis完成了会话共享，redis的zset结构完成本周热议排行榜，t-io+websocket完成即时消息通知和群聊，rabbitmq+elasticsearch完成博客内容搜索引擎等。值得学习的地方很多！', '**推荐阅读：**

[分享一套SpringBoot开发博客系统源码，以及完整开发文档！速度保存！](https://mp.weixin.qq.com/s/jz6e977xP-OyaAKNjNca8w)

[Github上最值得学习的100个Java开源项目，涵盖各种技术栈！](https://mp.weixin.qq.com/s/N-U0TaEUXnBFfBsmt_OESQ)

[2020年最新的常问企业面试题大全以及答案](https://mp.weixin.qq.com/s/lR5LC5GnD2Gs59ecV5R0XA)', '2020-08-14 18:41:19', 1);
INSERT INTO `db-it`.m_blog (id, user_id, title, description, content, created, status) VALUES (3, 1, '公众号MarkerHub文章索引', '梳理Java知识，解析开源项目！ 公众号【MarkerHub】的文章分类索引，直联公众号文章链接！https://github.com/MarkerHub/JavaIndex', '**推荐阅读：**

[分享一套SpringBoot开发博客系统源码，以及完整开发文档！速度保存！](https://mp.weixin.qq.com/s/jz6e977xP-OyaAKNjNca8w)

[Github上最值得学习的100个Java开源项目，涵盖各种技术栈！](https://mp.weixin.qq.com/s/N-U0TaEUXnBFfBsmt_OESQ)

[2020年最新的常问企业面试题大全以及答案](https://mp.weixin.qq.com/s/lR5LC5GnD2Gs59ecV5R0XA)', '2020-08-14 18:41:19', 1);
INSERT INTO `db-it`.m_blog (id, user_id, title, description, content, created, status) VALUES (7, 1, '你真的会写单例模式吗?', '单例模式可能是代码最少的模式了，但是少不一定意味着简单，想要用好、用对单例模式，还真得费一番脑筋。本文对 Java 中常见的单例模式写法做了一个总结，如有错漏之处，恳请读者指正。', '> 作者：吃桔子的攻城狮 来源：http://www.tekbroaden.com/singleton-java.html


单例模式可能是代码最少的模式了，但是少不一定意味着简单，想要用好、用对单例模式，还真得费一番脑筋。本文对 Java 中常见的单例模式写法做了一个总结，如有错漏之处，恳请读者指正。

饿汉法
===

顾名思义，饿汉法就是在第一次引用该类的时候就创建对象实例，而不管实际是否需要创建。代码如下：

```
public class Singleton {
    private static Singleton = new Singleton();
    private Singleton() {}
    public static getSignleton(){
        return singleton;
    }
}

```

这样做的好处是编写简单，但是无法做到延迟创建对象。但是我们很多时候都希望对象可以尽可能地延迟加载，从而减小负载，所以就需要下面的懒汉法：
', '2020-08-14 18:41:19', 1);
INSERT INTO `db-it`.m_blog (id, user_id, title, description, content, created, status) VALUES (9, 1, '真正理解Mysql的四种隔离级别@', '事务是应用程序中一系列严密的操作，所有操作必须成功完成，否则在每个操作中所作的所有更改都会被撤消。也就是事务具有原子性，一个事务中的一系列的操作要么全部成功，要么一个都不做。

事务的结束有两种，当事务中的所以步骤全部成功执行时，事务提交。如果其中一个步骤失败，将发生回滚操作，撤消撤消之前到事务开始时的所以操作。', '### 什么是事务

> 事务是应用程序中一系列严密的操作，所有操作必须成功完成，否则在每个操作中所作的所有更改都会被撤消。也就是事务具有原子性，一个事务中的一系列的操作要么全部成功，要么一个都不做。
>
> 事务的结束有两种，当事务中的所以步骤全部成功执行时，事务提交。如果其中一个步骤失败，将发生回滚操作，撤消撤消之前到事务开始时的所以操作。

**事务的 ACID**

事务具有四个特征：原子性（ Atomicity ）、一致性（ Consistency ）、隔离性（ Isolation ）和持续性（ Durability ）。这四个特性简称为 ACID 特性。

> 1 、原子性。事务是数据库的逻辑工作单位，事务中包含的各操作要么都做，要么都不做
>
> 2 、一致性。事 务执行的结果必须是使数据库从一个一致性状态变到另一个一致性状态。因此当数据库只包含成功事务提交的结果时，就说数据库处于一致性状态。如果数据库系统 运行中发生故障，有些事务尚未完成就被迫中断，这些未完成事务对数据库所做的修改有一部分已写入物理数据库，这时数据库就处于一种不正确的状态，或者说是 不一致的状态。', '2020-08-15 01:21:17', 1);
INSERT INTO `db-it`.m_blog (id, user_id, title, description, content, created, status) VALUES (10, 1, '博客项目eblog讲解视频上线啦，长达17个小时！！', '1. 慕课网免费资源好久都没更新了，新教程大都付费
2. B站上的视频繁多，通过收藏和弹幕数量通常很容易判断出视频是否优质
3. 讲真，B站的弹幕文化，让我觉得，我不是一个在学习，自古人才出评论。哈哈哈
4. B站视频通常广告少，up主的用心录制，通常只为了你关注他', 'ok，再回到我们的eblog项目，源码、文档、视频我都开源出来了。来些基本操作：github上给个star，B站视频给个三连支持咧。

eblog源码：https://github.com/MarkerHub/eblog

点击这里：[10+篇完整开发文档](https://mp.weixin.qq.com/mp/homepage?__biz=MzIwODkzOTc1MQ==&hid=1&sn=8e512316c3dfe140e636d0c996951166)

![](//image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/20200508/c290d945b7d24c79b172759bdb5b94e0.png)

视频讲解：（记得关注我噢！）

https://www.bilibili.com/video/BV1ri4y1x71A

![](//image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/20200508/983b5abc1c934360a1a1362347a275f7.png)

项目其实还很多bug的，哈哈，我还需要进行二次迭代，到时候再发迭代文档出来。

关注下我的B站，作为一个自媒体的自由职业者，没有什么比涨粉更让我开心的了，嘻嘻。

近期即将推出的视频教程：

1. 搭建脚手架，前后端分离首秀
2. Shiro入门到精通教程
3. SpringBoot2.2.6最新入门教程', '2020-08-14 18:41:19', 1);
INSERT INTO `db-it`.m_blog (id, user_id, title, description, content, created, status) VALUES (17, 1, 'demo', 'test111', 'dfafdfafa', '2020-08-15 02:07:35', 1);