package net.cosmogrp.storage.resolve;

import net.cosmogrp.storage.model.Model;

import java.util.HashMap;
import java.util.Map;

public class ResolverRegistryBuilder<T extends Model> {

    private final Map<String, RelationalResolver<T>> resolvers;

    protected ResolverRegistryBuilder() {
        this.resolvers = new HashMap<>();
    }

    public ResolverRegistryBuilder<T> register(
            String name,
            FieldExtractor<T> extractor
    ) {
        return register(name, new RelationalResolver<>(extractor));
    }

    public ResolverRegistryBuilder<T> register(
            String name,
            RelationalResolver<T> resolver
    ) {
        resolvers.put(name, resolver);
        return this;
    }

    public ResolverRegistry<T> build() {
        return new ResolverRegistry<>(resolvers);
    }

}
