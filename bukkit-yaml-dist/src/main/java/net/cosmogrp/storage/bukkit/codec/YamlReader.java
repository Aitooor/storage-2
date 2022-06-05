package net.cosmogrp.storage.bukkit.codec;

import net.cosmogrp.storage.codec.ModelCodec;
import net.cosmogrp.storage.codec.ModelReader;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static net.cosmogrp.storage.validate.Validate.state;

public class YamlReader implements ModelReader<Map<String, Object>> {

    private final ConfigurationSection parentSection;

    private YamlReader(ConfigurationSection parentSection) {
        this.parentSection = parentSection;
    }

    public static YamlReader create(ConfigurationSection parentSection) {
        return new YamlReader(parentSection);
    }

    @Override
    public String readString(String field) {
        return parentSection.getString(field);
    }

    @Override
    public double readDouble(String field) {
        return parentSection.getDouble(field);
    }

    @Override
    public long readLong(String field) {
        return parentSection.getLong(field);
    }

    @Override
    public int readInt(String field) {
        return parentSection.getInt(field);
    }

    @Override
    public boolean readBoolean(String field) {
        return parentSection.getBoolean(field);
    }

    @SuppressWarnings("unchecked") @Override
    public <T> List<T> readList(String field, Class<T> clazz) {
        // only support for string list
        state(clazz == String.class, "Only support for string list");

        return (List<T>) parentSection.getStringList(field);
    }

    @Override
    public <T extends ModelCodec<Map<String, Object>>> @Nullable T readChild(
            String field,
            Function<ModelReader<Map<String, Object>>, T> parser
    ) {
        ConfigurationSection section = parentSection
                .getConfigurationSection(field);

        if (section == null) {
            return null;
        }

        return parser.apply(new YamlReader(section));
    }

    @Override
    public <K, V extends ModelCodec<Map<String, Object>>> Map<K, V> readMap(
            String field, Function<V, K> keyParser,
            Function<ModelReader<Map<String, Object>>, V> valueParser
    ) {
        ConfigurationSection section =
                parentSection.getConfigurationSection(field);

        if (section == null) {
            return Collections.emptyMap();
        }

        Set<String> keys = section.getKeys(false);
        Map<K, V> map = new HashMap<>(keys.size());
        for (String key : keys) {
            V value = valueParser.apply(new YamlReader(
                    section.getConfigurationSection(key)
            ));

            map.put(keyParser.apply(value), value);
        }

        return map;
    }

    @Override
    public <T extends ModelCodec<Map<String, Object>>> Set<T> readChildren(
            String field,
            Function<ModelReader<Map<String, Object>>, T> parser
    ) {
        ConfigurationSection section =
                parentSection.getConfigurationSection(field);

        if (section == null) {
            return Collections.emptySet();
        }

        Set<String> keys = section.getKeys(false);
        Set<T> children = new HashSet<>(keys.size());

        for (String key : keys) {
            children.add(parser.apply(new YamlReader(
                    section.getConfigurationSection(key)
            )));
        }

        return children;
    }
}
