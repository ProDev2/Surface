package com.prodev.surfacelib.layer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.prodev.surfacelib.interfaces.RenderModifier;
import com.prodev.surfacelib.interfaces.Renderable;
import com.prodev.surfacelib.interfaces.Updatable;
import com.prodev.surfacelib.render.Renderer;
import com.simplelib.math.Vector2;
import com.simplelib.math.Vector4;
import com.simplelib.tools.ImageTools;

import java.util.ArrayList;

public abstract class Layer extends Vector4 implements Renderable, Updatable {
    private Renderer renderer;

    private String id;

    private Layer parentLayer;
    private ArrayList<Layer> subLayers;

    private ArrayList<RenderModifier> modifiers;

    private boolean drawLayer;

    private int alpha;
    private int backgroundColor;

    public Layer(int width, int height) {
        this(0, 0, width, height);
    }

    public Layer(int x, int y, int width, int height) {
        this(new Vector2(x, y), new Vector2(width, height));
    }

    public Layer(Vector2 size) {
        this(new Vector2(), size);
    }

    public Layer(Vector2 pos, Vector2 size) {
        super(pos, size);

        this.subLayers = new ArrayList<>();
        this.modifiers = new ArrayList<>();

        init();
    }

    private void init() {
        this.drawLayer = true;

        this.alpha = 255;
        this.backgroundColor = 0x00000000;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;

        updateSubLayers();
    }

    public void setParentLayer(Layer parentLayer) {
        this.parentLayer = parentLayer;

        updateSubLayers();
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public Layer getParentLayer() {
        return parentLayer;
    }

    public void callUpdate() {
        if (renderer != null)
            renderer.callUpdate();
    }

    public void updateSubLayers() {
        for (Layer subLayer : subLayers) {
            subLayer.setRenderer(renderer);
            subLayer.setParentLayer(this);
        }
    }

    public boolean hasParentLayer() {
        return parentLayer != null;
    }

    public void addSubLayer(Layer subLayer) {
        if (subLayer != null && !subLayers.contains(subLayer)) {
            subLayer.setRenderer(renderer);
            subLayer.setParentLayer(this);
            subLayers.add(subLayer);
        }
    }

    public void removeSubLayer(Layer subLayer) {
        if (subLayer != null && subLayers.contains(subLayer)) {
            subLayers.remove(subLayer);
        }
    }

    public void addRenderModifier(RenderModifier modifier) {
        if (modifier != null && !modifiers.contains(modifier)) {
            modifier.setLayer(this);
            modifiers.add(modifier);
        }
    }

    public void removeRenderModifier(RenderModifier modifier) {
        if (modifier != null && modifiers.contains(modifier)) {
            modifiers.remove(modifier);
        }
    }

    public void setDrawLayer(boolean drawLayer) {
        this.drawLayer = drawLayer;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isDrawLayer() {
        return drawLayer;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public Layer findLayerById(String id) {
        if (this.id.equals(id))
            return this;

        for (Layer subLayer : subLayers) {
            Layer foundLayer = subLayer.findLayerById(id);
            if (foundLayer != null)
                return foundLayer;
        }
        return null;
    }

    public Layer findAtPos(Vector2 pos) {
        if (containsPoint(pos)) {
            pos = getRelativePos(pos);
            for (Layer subLayer : subLayers) {
                if (subLayer.containsPoint(pos)) {
                    Layer foundLayer = subLayer.findAtPos(pos);
                    if (foundLayer != null)
                        return foundLayer;
                }
            }
            return this;
        }
        return null;
    }

    public Vector2 getAbsolutePosOf(Vector2 pos) {
        pos = getAbsolutePos(pos);
        if (getParentLayer() != null)
            return getParentLayer().getAbsolutePosOf(pos);
        else
            return pos;
    }

    public float getAbsoluteRotation() {
        if (getParentLayer() != null)
            return getParentLayer().getAbsoluteRotation() + rotation;
        else
            return rotation;
    }

    @Override
    public void update() {
        callUpdate();
    }

    @Override
    public Bitmap export(Vector2 size) {
        float oversizeX = getWidth() - size.getX();
        float oversizeY = getHeight() - size.getY();

        float width = getWidth() - Math.max(oversizeX, oversizeY);
        float height = getHeight() - Math.max(oversizeX, oversizeY);

        Bitmap bitmap = render();
        if (oversizeX != 0 || oversizeY != 0)
            return ImageTools.resizedBitmap(bitmap, (int) width, (int) height);

        return bitmap;
    }

    @Override
    public void render(Canvas canvas) {
        if (size.getX() > 0 && size.getY() > 0) {
            Bitmap bitmap = render();

            //Apply to canvas
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setAlpha(alpha);

            canvas.drawBitmap(bitmap, getAsMatix(), paint);
        }
    }

    public Bitmap render() {
        if (size.getX() > 0 && size.getY() > 0) {
            Bitmap.Config config = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = Bitmap.createBitmap((int) size.getX(), (int) size.getY(), config);
            Canvas subCanvas = new Canvas(bitmap);

            //Draw background
            if (backgroundColor != 0)
                subCanvas.drawColor(backgroundColor);

            //Normal rendering
            for (RenderModifier modifier : modifiers)
                modifier.onSubdraw(subCanvas);

            if (drawLayer)
                draw(subCanvas);

            for (RenderModifier modifier : modifiers)
                modifier.onOverdraw(subCanvas);

            //SubLayers
            for (RenderModifier modifier : modifiers)
                modifier.onSubdrawSubLayers(subCanvas);

            for (Layer subLayer : subLayers) {
                subLayer.setRenderer(renderer);
                subLayer.setParentLayer(this);
                subLayer.render(subCanvas);
            }

            for (RenderModifier modifier : modifiers)
                modifier.onOverdrawSubLayers(subCanvas);

            return bitmap;
        }
        return null;
    }

    public abstract void draw(Canvas canvas);
}
