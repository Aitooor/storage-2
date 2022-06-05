package net.cosmogrp.storage.dist;

import net.cosmogrp.storage.CachedModelService;
import net.cosmogrp.storage.model.Model;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public abstract class CachedAsyncModelService<T extends Model>
        extends AsyncModelService<T>
        implements CachedModelService<T> {
    public CachedAsyncModelService(Executor executor) {
        super(executor);
    }

    public CompletableFuture<T> get(String id) {
        return CompletableFuture.supplyAsync(() -> getSync(id), executor);
    }

    public CompletableFuture<List<T>> get(String field, String value) {
        return CompletableFuture.supplyAsync(() -> getSync(field, value), executor);
    }

    public CompletableFuture<T> getOrFind(String id) {
        return CompletableFuture.supplyAsync(() -> getOrFindSync(id), executor);
    }

    public CompletableFuture<List<T>> getOrFind(String field, String value) {
        return CompletableFuture.supplyAsync(() -> getOrFindSync(field, value), executor);
    }

    public CompletableFuture<List<T>> getAll() {
        return CompletableFuture.supplyAsync(this::getAllSync, executor);
    }

    public CompletableFuture<Void> upload(T model) {
        return CompletableFuture.runAsync(() -> uploadSync(model), executor);
    }

    public CompletableFuture<Void> uploadAll(Consumer<T> preUploadAction) {
        return CompletableFuture.runAsync(() -> uploadAllSync(preUploadAction), executor);
    }
}
