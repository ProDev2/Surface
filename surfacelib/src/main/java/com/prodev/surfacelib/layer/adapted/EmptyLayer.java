package com.prodev.surfacelib.layer.adapted;

import android.graphics.Canvas;

import com.prodev.surfacelib.layer.Layer;
import com.simplelib.math.Vector2;

public class EmptyLayer extends Layer {
    public EmptyLayer(int width, int height) {
        super(width, height);
    }

    public EmptyLayer(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public EmptyLayer(Vector2 size) {
        super(size);
    }

    public EmptyLayer(Vector2 pos, Vector2 size) {
        super(pos, size);
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
