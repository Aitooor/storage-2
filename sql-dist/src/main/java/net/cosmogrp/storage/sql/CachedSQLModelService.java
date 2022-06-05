package net.cosmogrp.storage.sql;

import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.resolve.ResolverRegistry;
import net.cosmogrp.storage.sql.connection.SQLClient;
import net.cosmogrp.storage.sql.identity.MapSerializer;
import net.cosmogrp.storage.sql.identity.Table;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executor;

public class CachedSQLModelService<T extends Model & MapSerializer>
        extends CachedRemoteModelService<T> {

    private final SQLModelService<T> delegate;

    protected CachedSQLModelService(
            Executor executor,
            ModelService<T> cacheModelService,
            ResolverRegistry<T> resolverRegistry,
            SQLClient client,
            RowMapper<T> mapper,
            Table table
    ) {
        super(executor, cacheModelService, resolverRegistry);
        this.delegate = new SQLModelService<>(
                executor, client,
                mapper, table
        );
    }

    @Override
    protected void internalSave(T model) {
        delegate.saveSync(model);
    }

    @Override
    protected void internalDelete(T model) {
        delegate.deleteSync(model);
    }

    @Override
    protected @Nullable T internalFind(String id) {
        return delegate.findSync(id);
    }

    @Override
    protected List<T> internalFindAll() {
        return delegate.findAllSync();
    }

    @Override
    public List<T> findSync(String field, String value) {
        return delegate.findSync(field, value);
    }
}
