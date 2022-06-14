package net.cosmogrp.storage.bukkit;

import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.bukkit.codec.YamlCodec;
import net.cosmogrp.storage.bukkit.codec.YamlModelParser;
import net.cosmogrp.storage.bukkit.codec.YamlReader;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.dist.RemoteModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.resolve.ResolverRegistry;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class YamlModelService<T extends Model & YamlCodec>
        extends CachedRemoteModelService<T> {

    private final File folder;
    private final YamlModelParser<T> modelParser;

    protected YamlModelService(
            Executor executor, File folder,
            YamlModelParser<T> modelParser,
            ModelService<T> cacheModelService,
            ResolverRegistry<T> resolverRegistry
    ) {
        super(executor, cacheModelService, resolverRegistry);
        this.folder = folder;
        this.modelParser = modelParser;
    }

    public static <T extends Model & YamlCodec>
    YamlModelServiceBuilder<T> builder(Class<T> type) {
        return new YamlModelServiceBuilder<>(type);
    }

    @Override
    public List<T> findSync(String field, String value) {
        if (field.equals(ID_FIELD)) {
            return Collections.singletonList(findSync(value));
        }

        return Collections.emptyList();
    }

    private File createFile(String id, boolean create) {
        File file = new File(folder, id + ".yml");

        if (create && !file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return file;
    }

    private T parse(File file) {
        YamlConfiguration configuration = YamlConfiguration
                .loadConfiguration(file);

        return modelParser.parse(YamlReader.create(configuration));
    }

    @Override
    protected void internalSave(T model) {
        File file = createFile(model.getId(), true);

        YamlConfiguration configuration = new YamlConfiguration();
        configuration.addDefaults(model.serialize());
        configuration.options().copyDefaults(true);

        try {
            configuration.save(file);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not save model", e);
        }
    }

    @Override
    protected void internalDelete(T model) {
        File file = createFile(model.getId(), false);

        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    protected @Nullable T internalFind(String id) {
        File file = createFile(id, false);

        if (!file.exists()) {
            return null;
        }

        return parse(file);
    }

    @Override
    protected List<T> internalFindAll() {
        File[] files = folder.listFiles();

        if (files == null) {
            return Collections.emptyList();
        }

        List<T> models = new ArrayList<>(files.length);

        for (File file : files) {
            if (!file.getName().endsWith(".yml")) {
                continue;
            }

            models.add(parse(file));
        }

        return models;
    }
}
