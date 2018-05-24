package com.gojek.de;

public class ModifierSet {
    private String regex;
    private Modifier keyModifier;
    private Modifier valueModifier;

    public ModifierSet(String regex, Modifier keyModifier, Modifier valueModifier) {
        this.regex = regex;
        this.keyModifier = keyModifier;
        this.valueModifier = valueModifier;
    }

    public String getRegex() {
        return regex;
    }

    public Modifier getKeyModifier() {
        return keyModifier;
    }

    public Modifier getValueModifier() {
        return valueModifier;
    }
}
