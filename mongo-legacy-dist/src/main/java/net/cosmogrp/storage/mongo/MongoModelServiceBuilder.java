package net.cosmogrp.storage.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.builder.LayoutModelServiceBuilder;
import net.cosmogrp.storage.dist.DelegatedCachedModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.mongo.codec.DocumentCodec;
import net.cosmogrp.storage.mongo.codec.MongoModelParser;
import org.bson.Document;

import static net.cosmogrp.storage.Validate.notNull;

public class MongoModelServiceBuilder<T extends Model & DocumentCodec>
        extends LayoutModelServiceBuilder<T, MongoModelServiceBuilder<T>> {

    private MongoDatabase database;
    private String collectionName;
    private MongoModelParser<T> modelParser;

    protected MongoModelServiceBuilder(Class<T> type) {
        super(type);
    }

    public MongoModelServiceBuilder<T> database(MongoDatabase database) {
        this.database = database;
        return this;
    }

    public MongoModelServiceBuilder<T> modelParser(MongoModelParser<T> modelParser) {
        this.modelParser = modelParser;
        return this;
    }

    public MongoModelServiceBuilder<T> collection(String collection) {
        this.collectionName = collection;
        return this;
    }

    @Override
    public ModelService<T> build() {
        check();
        notNull(modelParser, "modelParser");
        notNull(database, "database");
        notNull(collectionName, "collectionName");

        MongoCollection<Document> collection =
                database.getCollection(collectionName);

        MongoModelService<T> modelService =
                new MongoModelService<>(executor, collection, modelParser);

        if (cacheModelService == null) {
            return modelService;
        } else {
            return new DelegatedCachedModelService<>(
                    executor, cacheModelService,
                    resolverRegistry, modelService
            );
        }
    }

    @Override
    protected MongoModelServiceBuilder<T> back() {
        return this;
    }
}
