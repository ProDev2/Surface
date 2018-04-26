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

        ImageLayer image = new ImageLayer(new Vector2(1920, 1080));
        image.setImage(ImageTools.createImage(1000, 1000, 0xff00ff00));
        image.setId("image");

        renderer = new Renderer(image);
        surface.setRenderer(renderer);
    }
}
