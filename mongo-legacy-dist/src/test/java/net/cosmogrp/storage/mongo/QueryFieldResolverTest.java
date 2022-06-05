package net.cosmogrp.storage.mongo;

import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.mongo.model.DummyModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

public class QueryFieldResolverTest {

    @Test
    public void test() {
        CachedRemoteModelService<DummyModel> modelService =
                TestHelper.create();

        for (int i = 0; i < 5; i++) {
            UUID id = UUID.randomUUID();

            modelService.saveSync(DummyModel
                    .create(id.toString(), "a"));
        }

        List<DummyModel> models = modelService
                .getSync("someValue", "a");

        Assertions.assertEquals(5, models.size());
    }

}
