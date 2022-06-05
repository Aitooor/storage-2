package net.cosmogrp.storage.redis.connection;

import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisCache {

    private final String name;
    private final JedisPool jedisPool;

    public RedisCache(String name, JedisPool jedisPool) {
        this.name = name;
        this.jedisPool = jedisPool;
    }

    public List<String> getAllValues(String table) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hvals(makeTable(table));
        }
    }

    public Set<String> getAllKeys(String table) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hkeys(makeTable(table));
        }
    }

    public Map<String, String> getAll(String table) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hgetAll(makeTable(table));
        }
    }
    
    public void set(
            String table, String key,
            String value, long seconds
    ) {
        try (Jedis jedis = jedisPool.getResource()) {
            String tableName = makeTable(table);
            jedis.hset(tableName, key, value);

            if (seconds > 0) {
                jedis.expire(tableName, seconds);
            }
        }
    }

    public void set(String table, String key, String value) {
        set(table, key, value, -1);
    }

    public @Nullable String get(String table, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(makeTable(table), key);
        }
    }

    public void del(String table, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hdel(makeTable(table), key);
        }
    }
    
    public String makeTable(String table) {
        return name + ":" + table;
    }
}
