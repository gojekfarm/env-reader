package com.gojek.de;

import com.gojek.de.exception.ConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class FileConfig implements IConfig {
    private Properties properties;

    FileConfig(String appConfigFile) {
        try {
            properties = new Properties();
            InputStream resourceAsStream = getClass().getResourceAsStream(appConfigFile);
            properties.load(resourceAsStream);
        } catch (RuntimeException | IOException e) {
            throw new ConfigException("Something went wrong when reading the file " + appConfigFile);
        }
    }

    @Override
    public String get(String key) {
        return properties.getProperty(key);
    }

    @Override
    public Map<String, String> getAll() {
        return (Map) properties;
    }
}
