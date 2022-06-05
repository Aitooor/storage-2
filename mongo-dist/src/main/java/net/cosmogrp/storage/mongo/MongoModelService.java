package net.cosmogrp.storage.mongo;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.resolve.ResolverRegistry;
import org.jetbrains.annotations.Nullable;
import org.mongojack.JacksonMongoCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class MongoModelService<T extends Model>
        extends CachedRemoteModelService<T> {

    private final JacksonMongoCollection<T> collection;

    public MongoModelService(
            Executor executor,
            ModelService<T> cacheModelService,
            ResolverRegistry<T> resolverRegistry,
            JacksonMongoCollection<T> collection
    ) {
        super(executor, cacheModelService, resolverRegistry);
        this.collection = collection;
    }

    @Override
    protected void internalSave(T model) {
        collection.replaceOne(
                Filters.eq("_id", model.getId()),
                model, new ReplaceOptions().upsert(true)
        );
    }

    @Override
    protected void internalDelete(T model) {
        collection.deleteOne(Filters.eq("_id", model.getId()));
    }

    @Override
    protected @Nullable T internalFind(String id) {
        return collection.findOneById(id);
    }

    @Override
    protected List<T> internalFindAll() {
        return collection.find()
                .into(new ArrayList<>());
    }

    @Override
    public List<T> findSync(String field, String value) {
        return collection.find(Filters.eq(field, value))
                .into(new ArrayList<>());
    }
}
