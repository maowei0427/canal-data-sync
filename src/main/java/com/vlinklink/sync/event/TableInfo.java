package com.vlinklink.sync.event;

import lombok.Getter;

/**
 * 数据库信息
 * @author maowei
 * @package_name com.vlinklink.sync.event
 * @date 2020/9/8
 * @time 14:00
 */
@Getter
public class TableInfo {

    /**
     * 数据库
     */
    private String schemaName;

    /**
     * 表名
     */
    private String tableName;

    public TableInfo(String schemaName, String tableName) {
        this.schemaName = schemaName;
        this.tableName = tableName;
    }
}
