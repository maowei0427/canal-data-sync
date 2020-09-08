package com.vlinklink.sync.app;

import com.vlinklink.sync.event.EventInfo;
import com.vlinklink.sync.service.IEventHandler;

/**
 * 根据数据库+表名+事件类型 产生handler 抽象接口层
 * @author maowei
 * @package_name com.vlinklink.sync.app
 * @date 2020/9/7
 * @time 15:14
 */
public interface ActionByEventType {

    IEventHandler createHandlerByEventType(EventInfo info);

}
