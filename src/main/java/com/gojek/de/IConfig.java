package com.gojek.de;

import java.util.Map;

public interface IConfig {
    void load();
    String get(String key);
    Map<String,String> getAll();
}
