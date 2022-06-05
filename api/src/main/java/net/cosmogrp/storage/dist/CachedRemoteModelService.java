package net.cosmogrp.storage.dist;

import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.resolve.ResolverRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public abstract class CachedRemoteModelService<T extends Model>
        extends CachedAsyncModelService<T> {

    protected final ModelService<T> cacheModelService;
    protected final ResolverRegistry<T> resolverRegistry;

    public CachedRemoteModelService(
            Executor executor,
            ModelService<T> cacheModelService,
            ResolverRegistry<T> resolverRegistry
    ) {
        super(executor);
        this.cacheModelService = cacheModelService;
        this.resolverRegistry = resolverRegistry;
    }

    @Override
    public @Nullable T findSync(String id) {
        T model = internalFind(id);

        if (model != null) {
            // add to cache
            cacheModelService.saveSync(model);
        }

        return model;
    }

    @Override
    public @Nullable T getSync(String id) {
        return cacheModelService.findSync(id);
    }

    @Override
    public List<T> getSync(String field, String value) {
        Iterable<String> ids = resolverRegistry.resolve(field, value);
        List<T> models = new ArrayList<>();

        for (String id : ids) {
            T model = getOrFindSync(id);
            if (model != null) {
                models.add(model);
            }
        }

        return models;
    }

    @Override
    public @Nullable T getOrFindSync(String id) {
        T model = getSync(id);

        if (model != null) {
            return model;
        }

        return findSync(id);
    }

    @Override
    public List<T> getOrFindSync(String field, String value) {
        List<T> models = getSync(field, value);

        if (models.isEmpty()) {
            models = findSync(field, value);
        }

        return models;
    }

    @Override
    public List<T> getAllSync() {
        return cacheModelService.findAllSync();
    }

    @Override
    public List<T> findAllSync() {
        List<T> loadedModels = internalFindAll();

        for (T model : loadedModels) {
            cacheModelService.saveSync(model);
        }

        return loadedModels;
    }

    @Override
    public void saveSync(T model) {
        saveInCache(model);
        internalSave(model);
    }

    @Override
    public void uploadSync(T model) {
        deleteInCache(model);
        internalSave(model);
    }

    @Override
    public void uploadAllSync(Consumer<T> preUploadAction) {
        List<T> models = cacheModelService.findAllSync();
        for (T model : models) {
            preUploadAction.accept(model);
            uploadSync(model);
        }
    }

    @Override
    public void deleteSync(T model) {
        deleteInCache(model);
        internalDelete(model);
    }

    @Override
    public T deleteSync(String id) {
        T model = getOrFindSync(id);

        if (model != null) {
            deleteSync(model);
        }

        return model;
    }

    public void saveInCache(T model) {
        resolverRegistry.bind(model);
        cacheModelService.saveSync(model);
    }

    public void deleteInCache(T model) {
        cacheModelService.deleteSync(model);
        resolverRegistry.unbind(model);
    }

    protected abstract void internalSave(T model);

    protected abstract void internalDelete(T model);

    protected abstract @Nullable T internalFind(String id);

    protected abstract List<T> internalFindAll();

}
