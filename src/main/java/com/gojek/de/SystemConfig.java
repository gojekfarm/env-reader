package com.gojek.de;

import java.util.Map;

class SystemConfig implements IConfig {

    private Map<String, String> systemEnv;

    @Override
    public void load() {
        if (systemEnv == null)
            systemEnv = System.getenv();
    }

    @Override
    public String get(String key) {
        load();
        return systemEnv.get(key);
    }

    @Override
    public Map<String, String> getAll() {
        load();
        return systemEnv;
    }
}
