package com.vlinklink.sync.event;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.vlinklink.sync.service.EventHandlerFactory;

/**
 * mysql事件变换行为
 * @author maowei
 * @package_name com.vlinklink.sync.service
 * @date 2020/9/7
 * @time 13:29
 */
public class EventInfo{

    /**
     * 行数据
     */
    private CanalEntry.RowData rowData;

    /**
     * 数据库信息
     */
    private TableInfo tableInfo;

    /**
     * mysql里事件类型
     */
    private CanalEntry.EventType eventType;

    public EventInfo(CanalEntry.RowData rowData,String schemaName,String tableName,CanalEntry.EventType eventType) {
        this.rowData = rowData;
        this.tableInfo = new TableInfo(schemaName,tableName);
        this.eventType = eventType;
    }

    public CanalEntry.RowData getRowData() {
        return rowData;
    }

    public CanalEntry.EventType getEventType() {
        return eventType;
    }

    /**
     *  数据库+表名+mysql里事件类型 组合成一个唯一的key, 可以对应为一个EventHandler
     * @return
     */
    public String getUnionKey(){
        return EventHandlerFactory.createUnionKey(tableInfo,eventType);
    }

}
