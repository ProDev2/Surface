package com.prodev.surfacelib.interaction;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.prodev.surfacelib.layer.Layer;
import com.prodev.surfacelib.render.Renderer;
import com.simplelib.math.Vector2;
import com.simplelib.math.Vector4;
import com.simplelib.tools.ImageTools;
import com.simplelib.tools.Tools;

public class Editor {
    private static final float DEFAULT_BORDER_OFFSET = 10;

    private Renderer renderer;
    private Layer layer;

    private Vector4 bounds;
    private Vector4 square;

    public float borderOffset = DEFAULT_BORDER_OFFSET;

    public Editor(Renderer renderer) {
        this.renderer = renderer;
    }

    public Editor(Renderer renderer, Layer layer) {
        this.renderer = renderer;
        this.layer = layer;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public void setBounds(Vector4 bounds) {
        this.bounds = bounds;
    }

    public void render(Canvas canvas) {
        if (layer != null && bounds != null) {
            float borderOffsetInDp = Tools.dpToPx(borderOffset);

            calculateSquare();
            
            //canvas.drawBitmap(ImageTools.createImage(square.getWidthAsInt(), square.getHeightAsInt(), 0x990000ff), square.getAsMatix(), null);
        }
    }

    private void calculateSquare() {
        Layer mainLayer = renderer.getMainLayer();

        square = layer.copy();

        float rot = layer.getRotation();
        layer.setRotation(0f);
        getAbsolutePoint(layer.getRotatedStart()).applyTo(square.getPos());
        layer.setRotation(rot);

        Vector2 resizeFactor = new Vector2(bounds.getWidth() / mainLayer.getWidth(), bounds.getHeight() / mainLayer.getHeight());

        Vector2 size = layer.getSize().copy();
        size.multiply(resizeFactor);
        size.applyTo(square.getSize());

        square.getRotationPoint().multiply(resizeFactor);
    }

    private Vector2 getAbsolutePoint(Vector2 relativePoint) {
        if (renderer == null || layer == null || bounds == null) return null;

        Layer mainLayer = renderer.getMainLayer();
        float xP = relativePoint.getX() / mainLayer.getWidth();
        float yP = relativePoint.getY() / mainLayer.getHeight();

        return bounds.getAbsolutePos(bounds.getSize().copy().multiply(new Vector2(xP, yP)));
    }

    private Vector2 getRelativePoint(Vector2 absolutePoint) {
        if (renderer == null || layer == null || bounds == null) return null;

        Vector2 relPos = bounds.getRelativePos(absolutePoint);
        float xP = relPos.getX() / bounds.getWidth();
        float yP = relPos.getY() / bounds.getHeight();

        return renderer.getMainLayer().getSize().copy().multiply(new Vector2(xP, yP));
    }
}