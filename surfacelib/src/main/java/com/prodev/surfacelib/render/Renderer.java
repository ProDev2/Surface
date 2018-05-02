package com.prodev.surfacelib.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.prodev.surfacelib.interfaces.Renderable;
import com.prodev.surfacelib.layer.Layer;
import com.prodev.surfacelib.layer.adapted.EmptyLayer;
import com.simplelib.math.Vector2;

import java.util.ArrayList;

public class Renderer implements Renderable {
    private Layer layer;

    private ArrayList<Runnable> updates;

    public Renderer(Vector2 size) {
        this(new EmptyLayer(size));
    }

    public Renderer(Layer mainLayer) {
        this.layer = mainLayer;

        this.updates = new ArrayList<>();
    }

    public void updateData() {
        layer.setRenderer(this);
        layer.setParentLayer(null);
    }

    public void callUpdate() {
        callUpdate(false);
    }

    public void callUpdate(boolean redrawAll) {
        if (redrawAll)
            layer.redrawAll();

        for (Runnable update : updates)
            update.run();
    }

    public void addUpdateListener(Runnable update) {
        if (update != null && !updates.contains(update))
            updates.add(update);
    }

    public void setSize(int width, int height) {
        setSize(new Vector2(width, height));
    }

    public void setSize(Vector2 size) {
        layer.setSize(size.getX(), size.getY());
    }

    public Layer getMainLayer() {
        return layer;
    }

    public void addSubLayer(Layer subLayer) {
        layer.addSubLayer(subLayer);
    }

    public Layer findLayerById(String id) {
        return layer.findLayerById(id);
    }

    public Layer findAtPos(Vector2 pos) {
        return layer.findAtPos(pos);
    }

    @Override
    public Bitmap export(Vector2 size) {
        return layer.export(size);
    }

    @Override
    public void render(Canvas canvas) {
        layer.render(canvas);
    }
}
