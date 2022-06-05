package net.cosmogrp.storage.resolve;

import net.cosmogrp.storage.model.Model;

import java.util.function.Function;

public interface FieldExtractor<T extends Model>
        extends Function<T, String> {
}
