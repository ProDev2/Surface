package com.prodev.surfacelib;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.prodev.surfacelib.layer.Layer;
import com.prodev.surfacelib.render.Renderer;
import com.simplelib.math.Vector2;
import com.simplelib.math.Vector4;
import com.simplelib.tools.Tools;

public class Surface extends View implements Runnable {
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 50;

    private Renderer renderer;
    private Vector4 bounds;

    private Layer touchedLayer;

    public Surface(Context context) {
        super(context);
    }

    public Surface(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Surface(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
        this.renderer.addUpdateListener(this);

        run();
    }

    @Override
    public void run() {
        if (renderer != null)
            invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (renderer != null && bounds != null) {
            Layer mainLayer = renderer.getMainLayer();

            if (event.getAction() == MotionEvent.ACTION_DOWN && touchedLayer == null) {
                float xP = (event.getX() - bounds.getX()) / bounds.getWidth();
                float yP = (event.getY() - bounds.getY()) / bounds.getHeight();

                float relX = mainLayer.getWidth() * xP;
                float relY = mainLayer.getHeight() * yP;

                if (mainLayer.containsPoint(new Vector2(relX, relY))) {
                    touchedLayer = renderer.findAtPos(new Vector2(relX, relY));
                    handleTouch(event);
                    return true;
                }
            }

            if (touchedLayer != null) {
                handleTouch(event);
            }

            if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                touchedLayer = null;
        }

        return super.onTouchEvent(event);
    }

    private void handleTouch(MotionEvent event) {
        if (touchedLayer != null)
            touchedLayer.handleTouch(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int desiredWidth = Tools.dpToPx(DEFAULT_WIDTH);
        int desiredHeight = Tools.dpToPx(DEFAULT_HEIGHT);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = widthSize;
        int height = heightSize;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (renderer != null) {
            Layer mainLayer = renderer.getMainLayer();

            Bitmap bitmap = mainLayer.export(new Vector2(getWidth(), getHeight()));

            float x = (getWidth() / 2) - (bitmap.getWidth() / 2);
            float y = (getHeight() / 2) - (bitmap.getHeight() / 2);

            if (bounds == null)
                bounds = new Vector4(new Vector2(), new Vector2());

            bounds.setPos(x, y);
            bounds.setSize(bitmap.getWidth(), bitmap.getHeight());

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setAlpha(mainLayer.getAlpha());

            canvas.drawBitmap(bitmap, bounds.getAsMatix(), paint);
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        run();
    }
}
