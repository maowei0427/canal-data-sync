package com.vlinklink.sync.service.redis;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.vlinklink.sync.service.IEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * mysql数据删除, redis对应删除
 * @author maowei
 * @package_name com.vlinklink.sync.service
 * @date 2020/9/7
 * @time 15:34
 */
@Service
public class RedisDeleteHandler implements IEventHandler {

    @Autowired
    private RedisHandler redisHandler;

    @Value("${redis.sync.update.coloum}")
    private String updateColoum;

    @Override
    public void handle(CanalEntry.RowData rowData) {
        JSONObject dataJson = redisHandler.dataParse(rowData,false);
        redisHandler.deleteFromList(dataJson,updateColoum);
    }

}
