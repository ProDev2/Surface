package com.prodev.surface;

import android.os.Bundle;

import com.prodev.surfacelib.Surface;
import com.prodev.surfacelib.layer.adapted.ImageLayer;
import com.prodev.surfacelib.render.Renderer;
import com.simplelib.SimpleActivity;
import com.simplelib.math.Vector2;
import com.simplelib.tools.ImageTools;

public class MainActivity extends SimpleActivity {
    private Surface surface;
    private Renderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surface = (Surface) findViewById(R.id.main_surface);

        final ImageLayer image = new ImageLayer(new Vector2(1920, 1080));
        image.setImage(ImageTools.createImage(1000, 1000, 0xff00ff00));
        image.setId("image");

        renderer = new Renderer(image);
        surface.setRenderer(renderer);

        final ImageLayer image2 = new ImageLayer(image.getCenter().multiply(new Vector2(1, 0.5f)));
        image2.setImage(ImageTools.createImage(1000, 1000, 0xffff0000));
        image2.setId("image2");
        image2.setRotationPoint(image2.getWidth(), image2.getHeight());
        image2.setRotation(45);
        image2.moveBy(0, image2.getHeight() * 2f);
        renderer.addSubLayer(image2);
    }
}
