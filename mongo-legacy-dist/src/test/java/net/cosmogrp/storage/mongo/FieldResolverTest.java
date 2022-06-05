package net.cosmogrp.storage.mongo;

import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.mongo.model.DummyModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FieldResolverTest {

    @Test
    public void test() {
        CachedRemoteModelService<DummyModel> modelService =
                TestHelper.create();

        Map<String, UUID> someValues = new HashMap<>();

        for (int i = 0; i < 5; i++) {
            UUID id = UUID.randomUUID();
            String someValue = i + "";

            modelService.saveSync(DummyModel
                    .create(id.toString(), someValue));

            someValues.put(someValue, id);
        }

        List<DummyModel> models = modelService
                .getSync("someValue", "1");

        for (DummyModel model : models) {
            Assertions.assertEquals(
                    someValues.get("1").toString(),
                    model.getId()
            );
        }
    }

}
