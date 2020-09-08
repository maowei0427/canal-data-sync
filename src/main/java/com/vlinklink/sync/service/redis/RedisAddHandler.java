package com.vlinklink.sync.service.redis;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.vlinklink.sync.service.IEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * mysql数据新增, redis新增
 * @author maowei
 * @package_name com.vlinklink.sync.service
 * @date 2020/9/7
 * @time 15:34
 */
@Service
public class RedisAddHandler implements IEventHandler {

    @Autowired
    private RedisHandler redisHandler;

    @Override
    public void handle(CanalEntry.RowData rowData) {
        JSONObject dataJson = redisHandler.dataParse(rowData,true);
        redisHandler.addFromList(dataJson);
    }


}
