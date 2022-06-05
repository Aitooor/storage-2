package net.cosmogrp.storage.redis.connection;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisInstance {

    private final Jedis listenerConnection;
    private final JedisPool jedisPool;

    public JedisInstance(Jedis listenerConnection, JedisPool jedisPool) {
        this.listenerConnection = listenerConnection;
        this.jedisPool = jedisPool;
    }

    public Jedis getListenerConnection() {
        return listenerConnection;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }
}
