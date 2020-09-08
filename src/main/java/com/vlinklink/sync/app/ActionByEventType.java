package com.vlinklink.sync.app;

import com.vlinklink.sync.event.EventInfo;
import com.vlinklink.sync.service.IEventHandler;

/**
 * @author maowei
 * @package_name com.vlinklink.sync.app
 * @date 2020/9/7
 * @time 15:14
 */
public interface ActionByEventType {

    IEventHandler createHandlerByEventType(EventInfo info);

}
