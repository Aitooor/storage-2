package net.cosmogrp.storage.codec;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public interface ModelWriter<R> {
    
    ModelWriter<R> write(String field, UUID uuid);

    default ModelWriter<R> write(String field, Date date) {
        return write(field, date.getTime());
    }
    
    ModelWriter<R> write(String field, String value);

    default ModelWriter<R> write(String field, Enum<?> value) {
        return write(field, value.name());
    }
    
    ModelWriter<R> write(String field, int value);
    
    ModelWriter<R> write(String field, long value);
    
    ModelWriter<R> write(String field, double value);
    
    ModelWriter<R> write(String field, boolean value);

    ModelWriter<R> writeObject(String field, Object value);
    
    ModelWriter<R> write(
            String field, 
            ModelCodec<R> child
    );
    
    ModelWriter<R> write(
            String field, 
            Collection<? extends ModelCodec<R>> children
    );

    default ModelWriter<R> write(
            String field,
            Map<?, ? extends ModelCodec<R>> children
    ) {
        return write(field, children.values());
    }

    R end();
    
}
