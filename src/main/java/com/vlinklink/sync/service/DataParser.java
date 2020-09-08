package com.vlinklink.sync.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * rowData解析成json
 * @author maowei
 * @package_name com.vlinklink.sync.service
 * @date 2020/9/7
 * @time 15:51
 */
@Component
public class DataParser {

    /**
     * rowData解析成json , 加上boolean, 解析修改前或者后的列
     * @param rowData
     * @param after true为修改后的列, 如修改,新增 false为修改前的列, 如删除
     * @return
     */
    public JSONObject parse(CanalEntry.RowData rowData,boolean after){
        List<CanalEntry.Column> columnsList ;
        if (after){
            //修改后的列, 如修改,新增
            columnsList = rowData.getAfterColumnsList();
        }else{
            //false为修改前的列, 如删除
            columnsList = rowData.getBeforeColumnsList();
        }
        JSONObject dataJson = new JSONObject(columnsList.size());
        for (CanalEntry.Column column : columnsList) {
            dataJson.put(column.getName(),column.getValue());
        }
        return dataJson;
    }


}
