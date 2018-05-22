package com.gojek.de;

import java.util.Map;

class SystemConfig implements IConfig {

    private Map<String, String> systemEnv;

    SystemConfig() {
        systemEnv = System.getenv();
    }

    @Override
    public String get(String key) {
        return systemEnv.get(key);
    }

    @Override
    public Map<String, String> getAll() {
        return systemEnv;
    }
}
