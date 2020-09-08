package com.vlinklink.sync.service;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.vlinklink.sync.conf.CanalConstants;
import com.vlinklink.sync.event.TableInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件处理 工厂
 * @author maowei
 * @package_name com.vlinklink.sync.service
 * @date 2020/9/7
 * @time 17:09
 */
public class EventHandlerFactory {

    private Map<String,IEventHandler> handlerMap = new HashMap<>();

    public IEventHandler getHandler(String key){
        return handlerMap.get(key);
    }

    public void setHandler(String key,IEventHandler iEventHandler){
        handlerMap.put(key,iEventHandler);
    }

    public static String createUnionKey(TableInfo tableInfo, CanalEntry.EventType eventType){
        return createUnionKey(tableInfo.getSchemaName(), tableInfo.getTableName(), eventType);
    }

    public static String createUnionKey(String schemaName,String tableName,CanalEntry.EventType eventType){
        return schemaName + "-" + tableName + "-" + eventType.getValueDescriptor().getName();
    }

    /**
     * 统一的处理器
     * @param eventType
     * @return
     */
    public static String createUnionKey(CanalEntry.EventType eventType){
        return createUnionKey(CanalConstants.UNITY_SCHEMA,CanalConstants.UNITY_TABLE,eventType);
    }

}
