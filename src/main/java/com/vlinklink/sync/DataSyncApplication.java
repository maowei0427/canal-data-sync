package com.vlinklink.sync;

import com.vlinklink.sync.app.redis.RedisSyncApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 利用canal实现数据同步
 * @author maowei
 * @package_name com.vlinklink.sync
 * @date 2020/9/7
 * @time 10:03
 */
@ComponentScan(basePackages = {"com.vlinklink"})
@SpringBootApplication
public class DataSyncApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DataSyncApplication.class, args);
        RedisSyncApp redisSyncApp = context.getBean(RedisSyncApp.class);
        redisSyncApp.work();
    }


}
