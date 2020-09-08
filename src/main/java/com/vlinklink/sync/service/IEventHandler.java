package com.vlinklink.sync.service;

import com.alibaba.otter.canal.protocol.CanalEntry;

/**
 * 事件处理抽象类
 * @author maowei
 * @package_name com.vlinklink.sync.service
 * @date 2020/9/7
 * @time 14:06
 */
public interface IEventHandler {

    void handle(CanalEntry.RowData rowData);

}
