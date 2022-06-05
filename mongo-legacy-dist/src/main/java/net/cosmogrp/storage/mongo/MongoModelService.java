package net.cosmogrp.storage.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import net.cosmogrp.storage.dist.RemoteModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.mongo.codec.DocumentCodec;
import net.cosmogrp.storage.mongo.codec.DocumentReader;
import net.cosmogrp.storage.mongo.codec.MongoModelParser;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class MongoModelService<T extends Model & DocumentCodec>
        extends RemoteModelService<T> {

    private final MongoCollection<Document> mongoCollection;
    private final MongoModelParser<T> mongoModelParser;

    protected MongoModelService(
            Executor executor,
            MongoCollection<Document> mongoCollection,
            MongoModelParser<T> mongoModelParser
    ) {
        super(executor);

        this.mongoCollection = mongoCollection;
        this.mongoModelParser = mongoModelParser;
    }

    public static <T extends Model & DocumentCodec>
    MongoModelServiceBuilder<T> builder(Class<T> type) {
        return new MongoModelServiceBuilder<>(type);
    }

    @Override
    public @Nullable T findSync(String id) {
        Document document = mongoCollection
                .find(Filters.eq("_id", id))
                .first();

        if (document == null) {
            return null;
        }

        return mongoModelParser.parse(DocumentReader.create(document));
    }

    @Override
    public List<T> findSync(String field, String value) {
        List<T> models = new ArrayList<>();

        for (Document document : mongoCollection
                .find(Filters.eq(field, value))) {
            models.add(mongoModelParser
                    .parse(DocumentReader
                            .create(document)));
        }

        return models;
    }

    @Override
    public List<T> findAllSync() {
        List<Document> documents = mongoCollection.find()
                .into(new ArrayList<>());

        List<T> models = new ArrayList<>();

        for (Document document : documents) {
            models.add(mongoModelParser.parse(DocumentReader.create(document)));
        }

        return models;
    }

    @Override
    public void saveSync(T model) {
        mongoCollection.replaceOne(
                Filters.eq("_id", model.getId()),
                model.serialize(),
                new ReplaceOptions().upsert(true)
        );
    }

    @Override
    public void deleteSync(T model) {
        mongoCollection.deleteOne(Filters.eq("_id", model.getId()));
    }
}
