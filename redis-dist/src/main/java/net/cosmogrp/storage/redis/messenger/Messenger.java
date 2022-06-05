package net.cosmogrp.storage.redis.messenger;

import com.google.gson.reflect.TypeToken;
import net.cosmogrp.storage.redis.channel.Channel;

import java.io.Closeable;

public interface Messenger extends Closeable {

    <T> Channel<T> getChannel(String name, TypeToken<T> type);

    default <T> Channel<T> getChannel(String name, Class<T> type) {
        return getChannel(name, TypeToken.get(type));
    }

}
