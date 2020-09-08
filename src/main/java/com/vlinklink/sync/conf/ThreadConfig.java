package com.vlinklink.sync.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author maowei
 * @package_name com.vlinklink.sync.conf
 * @date 2020/9/7
 * @time 15:20
 */
@org.springframework.context.annotation.Configuration
public class ThreadConfig {

    @Bean
    public ThreadPoolTaskExecutor defaultThreadPool(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        //核心线程数量
        threadPoolTaskExecutor.setCorePoolSize(2);
        //最大线程数量
        threadPoolTaskExecutor.setMaxPoolSize(5);
        //队列中最大任务数
        threadPoolTaskExecutor.setQueueCapacity(2);
        //线程名称前缀
        threadPoolTaskExecutor.setThreadNamePrefix("ThreadPool-");
        //当达到最大线程数时如何处理新任务
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //线程空闲后最大存活时间
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        //初始化线程池
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

}
