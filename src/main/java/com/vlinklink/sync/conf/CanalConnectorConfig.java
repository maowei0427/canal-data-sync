package com.vlinklink.sync.conf;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

/**
 * canal链接
 * @author maowei
 * @package_name com.vlinklink.sync.conf
 * @date 2020/9/7
 * @time 14:47
 */
@Configuration
public class CanalConnectorConfig {

    @Autowired
    private CanalConfig canalConfig;

    @Bean
    public CanalConnector initConnector(){
        //目前canal server上的一个instance只能有一个client消费, 当有多个client消费时,会有bug
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(canalConfig.getHost(),
                canalConfig.getPort()), canalConfig.getDestination(), canalConfig.getUserName(), canalConfig.getPassword());
        connector.connect();
        connector.subscribe(canalConfig.getFilter());
        connector.rollback();
        return connector;
    }

}
