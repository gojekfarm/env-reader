package com.gojek.de;

public class ConfigFactory {
    public static Config load(String ...fileNames){
        return new Config(fileNames);
    }
}
