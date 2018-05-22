package com.gojek.de;

import com.gojek.de.exception.ConfigException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private final ArrayList<IConfig> configs =new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(FileConfig.class);

    public Config(String ...fileNames) {
        configs.add(new SystemConfig());
        for (String fileName : fileNames) {
            try {
                configs.add(new FileConfig(fileName));
            } catch (RuntimeException e) {
                logger.warn(e.getMessage());
            }
        }
    }

    public boolean has(String key) {
        for (IConfig config: configs) {
            String value = config.get(key);
            if(!StringUtils.isBlank(value))
                return true;
        }
        return false;
    }

    public String get(String key) {
        for (IConfig config: configs) {
            String value = config.get(key);
            if(!StringUtils.isBlank(value))
                return value;
        }
        throw new ConfigException(key + " config not set");
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
        String value = null;
        try {
            value = get(key);
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ConfigException(value + " is not an int");
        }
    }

    public long getLong(String key) {
        String value = null;
        try {
            value = get(key);
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new ConfigException(String.format(value + " is not a long"));
        }
    }
}
