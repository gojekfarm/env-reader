package com.gojek.de;

public class ConfigFactory {
    public static Config get(String fileName){
        return new Config(fileName);
    }

    public static Config get(){
        return new Config();
    }
}
