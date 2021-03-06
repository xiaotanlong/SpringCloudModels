package com.tjl.base.redis.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * 功能描述 自定义序列化
 * 2020-05-25  遇到redis存储时 使用 fastjson 字节和对象互转时报错，1.2.62版本不兼容旧的  改为 1.2.60 就好了
 * @author tjl
 * @Type FastJson2JsonRedisSerializer
 * @Version 1.0
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private Class<T> clazz;

    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        return t == null ? new byte[0] : JSON.toJSONString(t, new SerializerFeature[]{SerializerFeature.WriteClassName}).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes != null && bytes.length > 0) {
            String str = new String(bytes, DEFAULT_CHARSET);
            return JSON.parseObject(str, this.clazz);
        } else {
            return null;
        }
    }

    static {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }
}
