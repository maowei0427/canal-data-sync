package com.vlinklink.sync.app;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.vlinklink.sync.conf.CanalConfig;
import com.vlinklink.sync.event.EventInfo;
import com.vlinklink.sync.service.EventHandleContext;
import com.vlinklink.sync.service.IEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据同步
 * @author maowei
 * @package_name com.vlinklink.sync.app
 * @date 2020/9/7
 * @time 14:45
 */
@Slf4j
@Component
public abstract class DataSyncApp implements ActionByEventType{

    @Autowired
    private CanalConnector canalConnector;

    @Autowired
    private CanalConfig canalConfig;

    //状态位
    private boolean RUNNING = true;

    /**
     * 启动
     */
    public void work(){
        while (RUNNING){
            Message message = canalConnector.getWithoutAck(canalConfig.getBatchSize());
            long batchId = message.getId();
            int size = message.getEntries().size();
            if (!(batchId == -1 || size == 0)) {
                log.info("处理 batchId 为 {} 的数据",batchId);
                List<EventHandleContext> handlers = createHandlers(createEventInfos(message.getEntries()));
                if (handlers != null) {
                    for (EventHandleContext handler : handlers) {
                        //同步处理, 处理完后ack(也可以放在一个Queue里按个顺序ack, 处理耗时不大,场景适合,如遇到处理耗时多的场景, 可以考虑放入MQ队列里,削峰消费处理,),
                        // 放到线程池异步处理时, 可能会出现CanalClientException: deserializer failed, ack error , clientId:1001 batchId:680 is not exist , please check
                        //出现的原因是异步处理, 可能先提交了大的batchId, 导致另一个batchId小的
                        // 线程处理完回来ack的时候, 发现batchId不存在了,因为被大的batchId覆盖了
                        try {
                            handler.handle();
                        }catch (Exception e){
                            log.error("处理 batchId 为 {} 的数据发生异常 ",batchId);
                            e.printStackTrace();
                            canalConnector.rollback();
                        }
                    }
                    //处理完一个批次的数据,再ack
                    canalConnector.ack(batchId);
                }
            }
        }
    }

    /**
     * 产生事件信息和数据
     * @param entrys
     * @return
     */
    private List<EventInfo> createEventInfos(List<CanalEntry.Entry> entrys) {
        //事件信息集合
        List<EventInfo> eventInfoList = new ArrayList<>(entrys.size());
        for (CanalEntry.Entry entry : entrys) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }
            CanalEntry.RowChange rowChage = null;
            try {
                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }
            //事件类型
            CanalEntry.EventType eventType = rowChage.getEventType();
            //数据库
            String schemaName = entry.getHeader().getSchemaName();
            //表名
            String tableName = entry.getHeader().getTableName();
            log.info("===========binlog[{} : {}] ,tableName[{} : {}] , eventType : {}",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    schemaName, tableName, eventType);

            for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
                eventInfoList.add(new EventInfo(rowData,schemaName,tableName,eventType));
            }
        }
        return eventInfoList;
    }


    private List<EventHandleContext> createHandlers(List<EventInfo> eventInfos){
        if (eventInfos == null || eventInfos.size() == 0){
            log.info("没有事件产生, 无需处理 .................................");
            return null;
        }
        //事件处理的集合
        List<EventHandleContext> eventHandlerList = new ArrayList<>(eventInfos.size());
        for (EventInfo eventInfo : eventInfos) {
            IEventHandler handlerByEventType = createHandlerByEventType(eventInfo);
            if (handlerByEventType != null){
                eventHandlerList.add(new EventHandleContext(handlerByEventType,eventInfo));
            }
        }
        return eventHandlerList;
    }

    @Override
    public abstract IEventHandler createHandlerByEventType(EventInfo eventInfo);


}
