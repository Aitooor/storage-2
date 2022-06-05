package net.cosmogrp.storage.resolve;

import net.cosmogrp.storage.model.Model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RelationalResolver<T extends Model> {

    private final FieldExtractor<T> extractor;
    private final Map<String, Set<String>> binds;

    public RelationalResolver(FieldExtractor<T> extractor) {
        this.extractor = extractor;
        this.binds = new HashMap<>();
    }

    public Iterable<String> resolve(String value) {
        return binds.get(value);
    }

    public void bind(String value, String id) {
        Set<String> ids = binds.get(value);
        if (ids == null) {
            ids = new HashSet<>(1);
            ids.add(id);
            binds.put(value, ids);
        } else {
            ids.add(id);
        }
    }

    public void bind(T model) {
        bind(extractor.apply(model), model.getId());
    }

    public void unbind(String value, String id) {
        Set<String> ids = binds.get(value);
        if (ids != null) {
            if (ids.remove(id)) {
                if (ids.isEmpty()) {
                    binds.remove(value);
                }
            }
        }
    }

    public void unbind(T model) {
        unbind(extractor.apply(model), model.getId());
    }

    public void unbind(String value) {
        binds.remove(value);
    }
}
