package net.cosmogrp.storage.builder;

import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.resolve.ResolverRegistry;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class LayoutModelServiceBuilder
        <T extends Model, O extends ModelServiceBuilder<T>>
        implements ModelServiceBuilder<T> {

    protected final Class<T> type;
    protected Executor executor;
    protected ModelService<T> cacheModelService;
    protected ResolverRegistry<T> resolverRegistry;

    public LayoutModelServiceBuilder(Class<T> type) {
        this.type = type;
    }

    @Override
    public O executor(Executor executor) {
        this.executor = executor;
        return back();
    }

    @Override
    public O cachedService(ModelService<T> cachedService) {
        this.cacheModelService = cachedService;
        return back();
    }

    @Override
    public O resolverRegistry(ResolverRegistry<T> resolverRegistry) {
        this.resolverRegistry = resolverRegistry;
        return back();
    }

    protected void check() {
        if (executor == null) {
            executor = Executors.newSingleThreadExecutor();
        }
        if (cacheModelService != null && resolverRegistry == null) {
            resolverRegistry = ResolverRegistry.empty();
        }
    }

    protected abstract O back();

}
