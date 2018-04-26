package com.prodev.surfacelib.interfaces;

import android.view.MotionEvent;

import com.prodev.surfacelib.layer.Layer;

public interface Interactable {
    void onClick(Layer layer);

    void onDrag(Layer layer, float xDrag, float yDrag);

    void onPress(Layer layer);

    void onRelease(Layer layer);

    void onTouch(Layer layer, MotionEvent event);
}
