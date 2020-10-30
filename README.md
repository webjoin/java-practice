# 多数据源配置非常简单 且 支持事物
### 优点：正对于数据源不需要写任何代码 只是需要简单配置
### 缺点：目前只支持druid连接池

1. 环境
````
    a库：
    CREATE TABLE a (
    id int(11) unsigned NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
    
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
5. 格式化插件 eclipse

1. db : 
1. mapper : 
1. util : 
1. call service : https://docs.spring.io/spring-cloud-openfeign/docs/2.2.5.RELEASE/reference/html/  

1. configuration : https://spring.io/projects/spring-cloud-config 
1. trace : https://spring.io/projects/spring-cloud-sleuth
2. testing : https://spring.io/guides/gs/contract-rest/