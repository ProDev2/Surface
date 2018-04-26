package com.prodev.surfacelib.frame;

import java.util.HashMap;

public class Keyframe {
    private HashMap<String, Integer> values;
    private HashMap<String, Integer> colors;

    public Keyframe() {
        this.values = new HashMap<>();
        this.colors = new HashMap<>();
    }

    public boolean hasValue(String key) {
        return values.containsKey(key);
    }

    public boolean hasColor(int key) {
        return colors.containsKey(key);
    }

    public void setValue(String key, int value) {
        values.put(key, value);
    }

    public void setColor(String key, int color) {
        colors.put(key, color);
    }

    public int getValue(String key) {
        return values.get(key);
    }

    public int getColor(String key) {
        return values.get(key);
    }
}
