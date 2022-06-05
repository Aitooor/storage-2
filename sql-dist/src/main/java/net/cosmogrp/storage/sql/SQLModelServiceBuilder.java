package net.cosmogrp.storage.sql;

import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.builder.LayoutModelServiceBuilder;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.sql.connection.SQLClient;
import net.cosmogrp.storage.sql.identity.MapSerializer;
import net.cosmogrp.storage.sql.identity.Table;
import org.jdbi.v3.core.mapper.RowMapper;

import static net.cosmogrp.storage.validate.Validate.notNull;

public class SQLModelServiceBuilder<T extends Model & MapSerializer>
        extends LayoutModelServiceBuilder<T, SQLModelServiceBuilder<T>> {

    private SQLClient sqlClient;
    private RowMapper<T> rowMapper;
    private Table table;

    protected SQLModelServiceBuilder(Class<T> type) {
        super(type);
    }

    public SQLModelServiceBuilder<T> client(SQLClient sqlClient) {
        this.sqlClient = sqlClient;
        return this;
    }

    public SQLModelServiceBuilder<T> rowMapper(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
        return this;
    }

    public SQLModelServiceBuilder<T> table(Table table) {
        this.table = table;
        return this;
    }

    @Override
    protected SQLModelServiceBuilder<T> back() {
        return this;
    }

    @Override
    public ModelService<T> build() {
        check();
        notNull(sqlClient, "sqlClient");
        notNull(rowMapper, "rowMapper");
        notNull(table, "table");

        if (cacheModelService == null) {
            return new SQLModelService<>(
                    executor, sqlClient,
                    rowMapper, table
            );
        } else {
            return new CachedSQLModelService<>(
                    executor, cacheModelService,
                    resolverRegistry, sqlClient,
                    rowMapper, table
            );
        }
    }
}
