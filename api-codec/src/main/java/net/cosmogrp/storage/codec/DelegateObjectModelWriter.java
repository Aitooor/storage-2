package net.cosmogrp.storage.codec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public abstract class DelegateObjectModelWriter<R>
        implements ModelWriter<R> {

    @Override
    public ModelWriter<R> write(String field, UUID uuid) {
        return writeObject(field, uuid.toString());
    }

    @Override
    public ModelWriter<R> write(String field, String value) {
        return writeObject(field, value);
    }

    @Override
    public ModelWriter<R> write(String field, int value) {
        return writeObject(field, value);
    }

    @Override
    public ModelWriter<R> write(String field, long value) {
        return writeObject(field, value);
    }

    @Override
    public ModelWriter<R> write(String field, double value) {
        return writeObject(field, value);
    }

    @Override
    public ModelWriter<R> write(String field, boolean value) {
        return writeObject(field, value);
    }

    @Override
    public ModelWriter<R> write(String field, ModelCodec<R> child) {
        if (child == null) {
            return writeObject(field, null);
        } else {
            return writeObject(field, child.serialize());
        }
    }

    @Override
    public ModelWriter<R> write(String field, Collection<? extends ModelCodec<R>> children) {
        List<R> documents = new ArrayList<>(children.size());
        for (ModelCodec<R> child : children) {
            documents.add(child.serialize());
        }

        return writeObject(field, documents);
    }
}
