package net.cosmogrp.storage.bukkit;

import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.builder.LayoutModelServiceBuilder;
import net.cosmogrp.storage.bukkit.codec.YamlCodec;
import net.cosmogrp.storage.bukkit.codec.YamlModelParser;
import net.cosmogrp.storage.dist.DelegatedCachedModelService;
import net.cosmogrp.storage.model.Model;
import org.bukkit.plugin.Plugin;

import java.io.File;

import static net.cosmogrp.storage.validate.Validate.notNull;
import static net.cosmogrp.storage.validate.Validate.state;

public class YamlModelServiceBuilder<T extends Model & YamlCodec>
        extends LayoutModelServiceBuilder<T, YamlModelServiceBuilder<T>> {

    private YamlModelParser<T> modelParser;
    private File folder;

    protected YamlModelServiceBuilder(Class<T> type) {
        super(type);
    }

    public YamlModelServiceBuilder<T> folder(File folder) {
        this.folder = folder;
        return this;
    }

    public YamlModelServiceBuilder<T> dataFolder(Plugin plugin, String child) {
        return folder(new File(plugin.getDataFolder(), child));
    }

    public YamlModelServiceBuilder<T> dataFolder(Plugin plugin) {
        return folder(plugin.getDataFolder());
    }

    public YamlModelServiceBuilder<T> modelParser(YamlModelParser<T> modelParser) {
        this.modelParser = modelParser;
        return this;
    }

    @Override
    protected YamlModelServiceBuilder<T> back() {
        return this;
    }

    @Override
    public ModelService<T> build() {
        check();
        notNull(modelParser, "modelParser");
        notNull(folder, "folder");

        if (!folder.exists()) {
            state(folder.mkdirs(),
                    "Failed to create folder: "
                            + folder.getName());
        }

        YamlModelService<T> modelService =
                new YamlModelService<>(executor, folder, modelParser);

        if (cacheModelService == null) {
            return modelService;
        } else {
            return new DelegatedCachedModelService<>(
                    executor, cacheModelService,
                    resolverRegistry, modelService
            );
        }
    }
}
