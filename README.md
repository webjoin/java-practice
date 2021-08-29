# 多数据源配置非常简单 且 支持事物
### 优点：正对于数据源不需要写任何代码 只是需要简单配置
### 缺点：目前只支持druid连接池

1. 环境
````
    a库：
create table a
(
    id      int unsigned auto_increment primary key,
    id_name varchar(10)  null comment 'name',
    age     int          null,
    `desc`  varchar(100) null
) charset = utf8;

create table template
(
    id_name varchar(32)  null,
    value   varchar(128) null
);    
    b库：
    CREATE TABLE b (
    id int(11) unsigned NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
    
  ````
  
1. 启动 cn.iwuliao.ds.MultiDsConfApp
2. 正常访问数据 http://localhost:8080/hi/hi  
3. 正常新增数据 http://localhost:8080/hi/tx/commit
4. 正常回滚数据 http://localhost:8080/hi/tx/rollback