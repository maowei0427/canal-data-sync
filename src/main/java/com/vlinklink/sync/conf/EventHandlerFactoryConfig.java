package com.vlinklink.sync.conf;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.vlinklink.sync.event.TableInfo;
import com.vlinklink.sync.service.EventHandlerFactory;
import com.vlinklink.sync.service.RedisAddHandler;
import com.vlinklink.sync.service.RedisDeleteHandler;
import com.vlinklink.sync.service.RedisUpdateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 事件处理 工厂
 * 配置注入
 * @author maowei
 * @package_name com.vlinklink.sync.conf
 * @date 2020/9/7
 * @time 17:13
 */
@Configuration
public class EventHandlerFactoryConfig {

    @Autowired
    private RedisAddHandler redisAddHandler;

    @Autowired
    private RedisDeleteHandler redisDeleteHandler;

    @Autowired
    private RedisUpdateHandler redisUpdateHandler;
    
    @Autowired
    private CanalConfig canalConfig;

    @Bean
    public EventHandlerFactory register(){
        EventHandlerFactory eventHandlerFactory = new EventHandlerFactory();
        //是否是自定义表处理
        Boolean custom = canalConfig.getCustom();
        if (custom){
            //多过滤器分隔
            String[] filterSplit = canalConfig.getFilter().split(CanalConstants.DEFAULT_FILTER_SPLIT);
            List<String> filterList = new ArrayList<>(Arrays.asList(filterSplit));
            //表信息
            List<TableInfo> tableInfos = new ArrayList<>(filterList.size());
            if (filterList != null) {
                for (String filter : filterList) {
                    String[] split = filter.split(CanalConstants.DEFAULT_TABLE_SPLIT);
                    if (split.length>1){
                        tableInfos.add(new TableInfo(split[0],split[1]));
                    }else{
                        throw new RuntimeException("filter格式解析发生错误");
                    }
                }
            }
            //给每个数据库的不同表添加业务处理逻辑
            for (TableInfo tableInfo : tableInfos) {
                //可以使用if判断, 不同表添加不同的逻辑
                eventHandlerFactory.setHandler(EventHandlerFactory.createUnionKey(tableInfo, CanalEntry.EventType.DELETE),redisDeleteHandler);
                eventHandlerFactory.setHandler(EventHandlerFactory.createUnionKey(tableInfo, CanalEntry.EventType.INSERT),redisAddHandler);
                eventHandlerFactory.setHandler(EventHandlerFactory.createUnionKey(tableInfo, CanalEntry.EventType.UPDATE),redisUpdateHandler);
            }
        }else{
            //统一处理器 TODO
            //CanalConstants.UNITY_TABLE CanalConstants.UNITY_SCHEMA

        }
        return eventHandlerFactory;
    }


}
