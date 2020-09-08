package com.vlinklink.sync.service;

import com.vlinklink.sync.event.EventInfo;

/**
 * 事件处理上下文对象 -- 策略模式
 * @author maowei
 * @package_name com.vlinklink.sync.service
 * @date 2020/9/7
 * @time 14:08
 */
public class EventHandleContext {

    private IEventHandler handler;

    private EventInfo eventInfo;

    public EventHandleContext(IEventHandler handler, EventInfo eventInfo) {
        this.handler = handler;
        this.eventInfo = eventInfo;
    }

    public void setHandler(IEventHandler handler) {
        this.handler = handler;
    }

    public void handle(){
        handler.handle(eventInfo.getRowData());
    }

}
