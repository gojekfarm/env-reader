package com.gojek.de;

import com.gojek.de.exception.ConfigException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private final ArrayList<IConfig> configs =new ArrayList<>();

    public Config(String fileName) {
        configs.add(new SystemConfig());
        configs.add(new FileConfig(fileName));
    }

    public Config(){
        configs.add(new SystemConfig());
    }

    public boolean has(String key) {
        return get(key, null) != null;
    }

    public String get(String key) {
        String ret = get(key, null);
        if (ret == null)
            throw new ConfigException("No config found");
        return ret;
    }

    public String get(String key, String defaultValue) {
        for (IConfig config: configs) {
            String value = config.get(key);
            if(!StringUtils.isBlank(value))
                return value;
        }
        return defaultValue;
    }



    public Map<String, String> getAll(){
        HashMap<String, String> allConfigs = new HashMap<>();
        ArrayList<IConfig> configsInReverse = new ArrayList<>(configs);
        Collections.reverse(configsInReverse);
        for (IConfig reverseConfig :
                configsInReverse) {
            Map<String, String> all = reverseConfig.getAll();
            allConfigs.putAll(all);
        }
        return allConfigs;
    }

    public int getInt(String key) {
        try {
            String value = get(key);
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ConfigException(String.format("Config value is not a number"));
        }
    }

    public int getInt(String key, Integer defaultValue) {
        try {
            String value = get(key, String.valueOf(defaultValue));
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ConfigException(String.format("Config value is not a number"));
        }
    }
}
