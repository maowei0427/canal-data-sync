package com.vlinklink.sync.redis.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author r GuoJiaChen
 * @date 2020/3/11 下午3:16
 */
@Slf4j
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> myRedisTemplate;

    /***
     * Hash结构保存数据
     * @param key hash唯一性
     * @param hashKey 业务实体ID
     * @param value 业务实体值
     */
    public void hashSave(String key, String hashKey, String value) {
        myRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    /***
     * Hash结构获取数据
     * @param key hash唯一性
     * @param hashKey 业务实体ID
     * @return
     */
    public Object hashGet(String key, String hashKey) {
        return myRedisTemplate.opsForHash().get(key, hashKey);
    }

    /***
     * Hash结构转换为Map
     * @param key hash唯一性
     * @return
     */
    public Map hashToMap(String key) {
        return myRedisTemplate.opsForHash().entries(key);
    }

    /***
     * Hash结构删除数据
     * @param key hash唯一性
     * @param hashKey 业务实体ID
     */
    public void hashDelete(String key, String hashKey) {
        myRedisTemplate.opsForHash().delete(key, hashKey);
    }

    /***
     * 根据key删除
     * @param key
     */
    public void deleteKey(String key) {
        myRedisTemplate.delete(key);
    }

    /***
     * 保存字符串信息
     * @param key
     * @param value
     */
    public void stringSave(String key, String value) {
        myRedisTemplate.opsForValue().set(key, value);
    }

    /***
     * 获取字符串
     * @param key
     * @return
     */
    public String stringGet(String key) {
        return myRedisTemplate.opsForValue().get(key);
    }

    /***
     * 是否存在该Key
     * @param key
     * @return
     */
    public boolean haskey(String key) {
        return myRedisTemplate.hasKey(key);
    }

    /***
     * 将value存储到List中
     * @param key
     * @param value
     */
    public void listLeftPush(String key, String value) {
        myRedisTemplate.opsForList().leftPush(key, value);
    }

    /***
     * 从List的右侧弹出vlaue
     * @param key
     * @return
     */
    public String listRightPop(String key) {
        return myRedisTemplate.opsForList().rightPop(key);
    }

    /***
     * list中个数
     * @param key
     * @return
     */
    public long llen(String key) {
        return myRedisTemplate.opsForList().size(key);
    }

    /***
     * 从List集合左侧弹出
     * @param key
     * @return
     */
    public String lpop(String key) {
        return myRedisTemplate.opsForList().leftPop(key);
    }

    /***
     * 存储一个或多个元素到Set集合中
     * @param key
     * @param value
     */
    public void setAdd(String key, String... value) {
        myRedisTemplate.opsForSet().add(key, value);
    }

    /***
     * 判断元素是否存在Set集合中
     * @param key
     * @param value
     * @return
     */
    public boolean setIsMember(String key, String value) {
        return myRedisTemplate.opsForSet().isMember(key ,value);
    }


    /**
     * 保存复杂类型数据到缓存
     * @param key
     * @param obj
     * @return
     */
    public void saveBean(String key, Object obj) {
        myRedisTemplate.opsForValue().set(key, JSON.toJSONString(obj));
    }

    /**
     * 保存复杂类型数据到缓存（并设置失效时间）
     * @param key
     * @param obj
     * @param seconds
     */
    public void saveBean(String key, Object obj, int seconds) {
        myRedisTemplate.opsForValue().set(key, JSON.toJSONString(obj), seconds, TimeUnit.SECONDS);
    }

    /**
     * 取得复杂JSON数据
     *
     * @param key
     * @param clazz
     * @param clazz
     * @return
     */
    public <T> T getBean(String key, Class<T> clazz) {
        String value = myRedisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return JSON.parseObject(value, clazz);
    }

    public  JSONObject getBean(String key) {
        String value = myRedisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return JSON.parseObject(value);
    }

    public JSONArray getArrBean(String key) {
        String value = myRedisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return JSONArray.parseArray(value);
    }

}
