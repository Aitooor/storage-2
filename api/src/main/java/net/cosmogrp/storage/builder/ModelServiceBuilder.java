package net.cosmogrp.storage.builder;

import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.resolve.ResolverRegistry;

import java.util.concurrent.Executor;

public interface ModelServiceBuilder<T extends Model> {

    ModelServiceBuilder<T> executor(Executor executor);

    ModelServiceBuilder<T> cachedService(ModelService<T> cachedService);

    ModelServiceBuilder<T> resolverRegistry(ResolverRegistry<T> resolverRegistry);

    ModelService<T> build();

}
