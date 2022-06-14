package net.cosmogrp.storage.redis;

import com.google.gson.Gson;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.builder.LayoutModelServiceBuilder;
import net.cosmogrp.storage.dist.DelegatedCachedModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.redis.connection.RedisCache;

import static net.cosmogrp.storage.Validate.notNull;

public class RedisModelServiceBuilder<T extends Model>
        extends LayoutModelServiceBuilder<T, RedisModelServiceBuilder<T>> {

    private Gson gson;
    private String tableName;
    private int expireAfterSave;
    private RedisCache redisCache;

    protected RedisModelServiceBuilder(Class<T> type) {
        super(type);
    }

    public RedisModelServiceBuilder<T> gson(Gson gson) {
        this.gson = gson;
        return back();
    }

    public RedisModelServiceBuilder<T> tableName(String tableName) {
        this.tableName = tableName;
        return back();
    }

    public RedisModelServiceBuilder<T> expireAfterSave(int expireAfterSave) {
        this.expireAfterSave = expireAfterSave;
        return back();
    }

    public RedisModelServiceBuilder<T> redisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
        return back();
    }

    @Override
    protected RedisModelServiceBuilder<T> back() {
        return this;
    }

    @Override
    public ModelService<T> build() {
        check();
        notNull(gson, "gson");
        notNull(tableName, "tableName");
        notNull(redisCache, "redisCache");

        if (expireAfterSave <= 0) {
            expireAfterSave = -1;
        }

        ModelService<T> modelService = new RedisModelService<>(
                executor, type, gson,
                redisCache, tableName,
                expireAfterSave
        );

        if (cacheModelService == null) {
            return modelService;
        } else {
            return new DelegatedCachedModelService<>(
                    executor, cacheModelService,
                    resolverRegistry, modelService
            );
        }
    }
}
