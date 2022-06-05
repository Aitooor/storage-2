package net.cosmogrp.storage.mongo.codec;

import net.cosmogrp.storage.codec.ModelCodec;
import net.cosmogrp.storage.codec.ModelReader;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

/**
 * It reads a document and converts it into a Java object
 */
public class DocumentReader implements ModelReader<Document> {

    private final Document document;

    private DocumentReader(Document document) {
        this.document = document;
    }

    @Override
    public Date readDate(String field) {
        return document.getDate(field);
    }

    @Override
    public String readString(String field) {
        return document.getString(field);
    }

    @Override
    public double readDouble(String field) {
        return document.getDouble(field);
    }

    @Override
    public long readLong(String field) {
        return document.getLong(field);
    }

    @Override
    public int readInt(String field) {
        return document.getInteger(field);
    }

    @Override
    public boolean readBoolean(String field) {
        return document.getBoolean(field);
    }

    @Override
    public <T> List<T> readList(String field, Class<T> clazz) {
        return document.getList(field, clazz);
    }

    @Override
    public <T extends ModelCodec<Document>> @Nullable T readChild(
            String field,
            Function<ModelReader<Document>, T> parser
    ) {
        Document child = document.get(field, Document.class);

        if (child == null) {
            return null;
        }

        return parser.apply(DocumentReader.create(child));
    }

    @Override
    public <K, V extends ModelCodec<Document>> Map<K, V> readMap(
            String field, Function<V, K> keyParser,
            Function<ModelReader<Document>, V> valueParser
    ) {
        List<Document> documents = readList(field, Document.class);
        Map<K, V> map = new HashMap<>(documents.size());

        for (Document document : documents) {
            V value = valueParser.apply(DocumentReader.create(document));

            map.put(keyParser.apply(value), value);
        }

        return map;
    }

    @Override
    public <T extends ModelCodec<Document>> Set<T> readChildren(
            String field,
            Function<ModelReader<Document>, T> parser
    ) {
        Set<T> children = new HashSet<>();
        List<Document> documents = readList(field, Document.class);

        for (Document document : documents) {
            children.add(parser.apply(
                    DocumentReader.create(document)
            ));
        }

        return children;
    }

    public static DocumentReader create(Document document) {
        return new DocumentReader(document);
    }
}
