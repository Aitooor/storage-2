package net.cosmogrp.storage.mongo.model;

import net.cosmogrp.storage.codec.ModelReader;
import net.cosmogrp.storage.model.AbstractModel;
import net.cosmogrp.storage.mongo.codec.DocumentWriter;
import net.cosmogrp.storage.mongo.codec.DocumentCodec;
import org.bson.Document;

public class DummyModel extends AbstractModel
        implements DocumentCodec {

    private final String someValue;

    public DummyModel(String id, String someValue) {
        super(id);
        this.someValue = someValue;
    }

    public static DummyModel create(String id, String someValue) {
        return new DummyModel(id, someValue);
    }

    public static DummyModel fromDocument(ModelReader<Document> reader) {
        return new DummyModel(
                reader.readString("_id"),
                reader.readString("someValue")
        );
    }

    @Override
    public String toString() {
        return "DummyModel{" +
                "someValue='" + someValue + '\'' +
                '}';
    }

    public String getSomeValue() {
        return someValue;
    }

    @Override
    public Document serialize() {
        return DocumentWriter.create(this)
                .write("someValue", someValue)
                .end();
    }
}
