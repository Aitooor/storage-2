package net.cosmogrp.storage.codec;

import net.cosmogrp.storage.model.Model;

public interface ModelParser<T extends Model, R> {

    T parse(ModelReader<R> reader);

}
