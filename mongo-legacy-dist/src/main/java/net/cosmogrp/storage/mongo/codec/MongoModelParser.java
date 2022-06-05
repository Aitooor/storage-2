package net.cosmogrp.storage.mongo.codec;

import net.cosmogrp.storage.codec.ModelParser;
import net.cosmogrp.storage.model.Model;
import org.bson.Document;

public interface MongoModelParser<T extends Model>
        extends ModelParser<T, Document> {
}
