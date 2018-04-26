package com.prodev.surfacelib.layer.adapted;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.prodev.surfacelib.layer.Layer;
import com.simplelib.math.Vector2;

public class ImageLayer extends Layer {
    private Bitmap image;

    public ImageLayer(int width, int height) {
        super(width, height);
    }

    public ImageLayer(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public ImageLayer(Vector2 size) {
        super(size);
    }

    public ImageLayer(Vector2 pos, Vector2 size) {
        super(pos, size);
    }

    public ImageLayer setImage(Bitmap image) {
        this.image = image;
        return this;
    }

    @Override
    public void draw(Canvas canvas) {
        if (image != null) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);

            Rect rect = new Rect(0, 0, (int) getWidth(), (int) getHeight());
            canvas.drawBitmap(image, null, rect, paint);
        }
    }
}
