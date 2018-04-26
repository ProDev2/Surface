package com.prodev.surfacelib.interfaces;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.simplelib.math.Vector2;

public interface Renderable {
    Bitmap export(Vector2 size);

    void render(Canvas canvas);
}
