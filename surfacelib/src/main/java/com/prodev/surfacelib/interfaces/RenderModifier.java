package com.prodev.surfacelib.interfaces;

import android.graphics.Canvas;

import com.prodev.surfacelib.layer.Layer;

public interface RenderModifier {
    void setLayer(Layer layer);

    void onSubdraw(Canvas canvas);

    void onOverdraw(Canvas canvas);

    void onSubdrawSubLayers(Canvas canvas);

    void onOverdrawSubLayers(Canvas canvas);

    boolean needsToBeRedrawn();
}
