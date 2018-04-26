package com.prodev.surfacelib.interfaces;

import com.prodev.surfacelib.frame.Keyframe;

public interface Saveable {
    void save(Keyframe keyframe);
    void load(Keyframe keyframe);
}
