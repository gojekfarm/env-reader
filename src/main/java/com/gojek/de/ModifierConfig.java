package com.gojek.de;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ModifierConfig implements IConfig {

    private Map<String, String> configs;

    public ModifierConfig(Config config, ModifierSet modifier) {
        configs = new HashMap<>();
        Map<String, String> newConfigs = config.getAll().entrySet()
                .stream()
                .filter(a -> a.getKey().matches(modifier.getRegex()))
                .collect(Collectors.toMap(
                        a -> modifier.getKeyModifier().apply(a.getKey()),
                        a -> modifier.getValueModifier().apply(a.getValue())));
        configs.putAll(newConfigs);
    }

    @Override
    public String get(String key) {
        return configs.get(key);
    }

    @Override
    public Map<String, String> getAll() {
        return configs;
    }
}
