package net.cosmogrp.storage.redis.messenger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.cosmogrp.storage.redis.channel.Channel;
import net.cosmogrp.storage.redis.channel.RedisChannel;
import net.cosmogrp.storage.redis.messenger.pubsub.RedisSubChannelPubsub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class RedisMessenger implements Messenger {

    private final String parentChannel;
    private final String serverId;
    private final Gson gson;

    private final JedisPool messengerPool;

    private final Map<String, RedisChannel<?>> channels;

    private final JedisPubSub pubSub;

    public RedisMessenger(
            String parentChannel, String serverId,
            Executor executor, Gson gson,
            JedisPool messengerPool,
            Jedis listenerConnection
    ) {
        this.parentChannel = parentChannel;
        this.serverId = serverId;
        this.gson = gson;
        this.messengerPool = messengerPool;

        this.channels = new ConcurrentHashMap<>();
        pubSub = new RedisSubChannelPubsub(parentChannel, serverId, gson, channels);

        executor.execute(() ->
                listenerConnection.subscribe(
                        pubSub, parentChannel
                ));
    }

    @Override
    public <T> Channel<T> getChannel(String name, TypeToken<T> type) {
        @SuppressWarnings("unchecked")
        RedisChannel<T> channel = (RedisChannel<T>) channels.get(name);

        if (channel == null) {
            channel = new RedisChannel<>(
                    parentChannel, serverId, name, type,
                    this, messengerPool, gson
            );

            channels.put(name, channel);
        } else {
            if (!channel.getType().equals(type)) {
                throw new IllegalArgumentException(
                        "Channel type mismatch"
                );
            }
        }

        return channel;
    }

    @Override
    public void close() {
        channels.clear();

        if (pubSub.isSubscribed()) {
            pubSub.unsubscribe();
        }
    }

}
