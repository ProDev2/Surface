package com.prodev.surfacelib.frame;

import com.prodev.surfacelib.layer.Layer;

import java.util.HashMap;

public class Timeline {
    private Layer layer;
    private HashMap<Integer, Keyframe> keyframes;

    public Timeline(Layer layer) {
        this.layer = layer;
        this.keyframes = new HashMap<>();
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }
}
