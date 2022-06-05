package net.cosmogrp.storage.dist;

import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.model.Model;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class AsyncModelService<T extends Model>
        implements ModelService<T> {

    protected final Executor executor;

    public AsyncModelService(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<T> find(String id) {
        return CompletableFuture.supplyAsync(() -> findSync(id), executor);
    }

    public CompletableFuture<List<T>> find(String field, String value) {
        return CompletableFuture.supplyAsync(() -> findSync(field, value), executor);
    }

    public CompletableFuture<List<T>> findAll() {
        return CompletableFuture.supplyAsync(this::findAllSync, executor);
    }

    public CompletableFuture<Void> save(T model) {
        return CompletableFuture.runAsync(() -> saveSync(model), executor);
    }

    public CompletableFuture<Void> delete(T model) {
        return CompletableFuture.runAsync(() -> deleteSync(model), executor);
    }

    public CompletableFuture<T> delete(String id) {
        return CompletableFuture.supplyAsync(() -> deleteSync(id), executor);
    }

}
