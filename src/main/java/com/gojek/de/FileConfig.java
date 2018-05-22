package com.gojek.de;

import com.gojek.de.exception.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

public class FileConfig implements IConfig {
    private Properties properties;

    FileConfig(String appConfigFile) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            properties = new Properties();
            URL resource = classLoader.getResource(appConfigFile);
            String file = resource.getFile();
            FileInputStream fileInputStream = new FileInputStream(file);
            properties.load(fileInputStream);
        }
        catch (RuntimeException|IOException e) {
            throw new ConfigException("Something went wrong when reading the file " + appConfigFile);
        }
    }

    @Override
    public String get(String key) {
        return properties.getProperty(key);
    }

    @Override
    public Map<String, String> getAll() {
        return (Map)properties;
    }
}
