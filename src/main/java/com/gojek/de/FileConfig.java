package com.gojek.de;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

public class FileConfig implements IConfig {
    private Properties properties;
    private String appConfigFile;
    private static final Logger logger = LoggerFactory.getLogger(FileConfig.class);

    FileConfig(String appConfigFile) {
        this.appConfigFile = appConfigFile;
    }

    @Override
    public void load() {
        if (properties != null)
            return;
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            properties = new Properties();
            URL resource = classLoader.getResource(appConfigFile);
            String file = resource.getFile();
            FileInputStream fileInputStream = new FileInputStream(file);
            properties.load(fileInputStream);
        }
        catch (RuntimeException|IOException e) {
            logger.warn("Something went wrong when reading the file {}", appConfigFile);
        }
    }

    @Override
    public String get(String key) {
        load();
        return properties.getProperty(key);
    }

    @Override
    public Map<String, String> getAll() {
        load();
        return (Map)properties;
    }
}
