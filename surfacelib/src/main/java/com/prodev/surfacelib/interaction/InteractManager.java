package com.prodev.surfacelib.interaction;

import android.view.MotionEvent;

import com.prodev.surfacelib.interfaces.Interactable;
import com.prodev.surfacelib.layer.Layer;
import com.simplelib.tools.Tools;

import java.util.ArrayList;

public class InteractManager {
    private static final float DEFAULT_MAX_MOVEMENT = 5;

    private Layer layer;
    private ArrayList<Interactable> interactListeners;

    private boolean pressed;
    private boolean moved;

    private float touchX, touchY;
    private float moveX, moveY;

    public InteractManager(Layer layer) {
        this.layer = layer;
        this.interactListeners = new ArrayList<>();
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public void addListener(Interactable interactListener) {
        if (interactListener != null && !interactListeners.contains(interactListener))
            interactListeners.add(interactListener);
    }

    public void removeListener(Interactable interactListener) {
        if (interactListener != null && interactListeners.contains(interactListener))
            interactListeners.remove(interactListener);
    }

    public void handleTouch(MotionEvent event) {
        for (Interactable interactable : interactListeners) {
            interactable.onTouch(layer, event);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!pressed) {
                        pressed = true;
                        interactable.onPress(layer);
                    }

                    touchX = event.getX();
                    touchY = event.getY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    moveX = event.getX() - touchX;
                    moveY = event.getY() - touchY;

                    float maxMovementDp = Tools.dpToPx(DEFAULT_MAX_MOVEMENT);
                    if (moveX < -maxMovementDp || moveY < -maxMovementDp || moveX > maxMovementDp || moveY > maxMovementDp)
                        moved = true;

                    if (moved)
                        interactable.onDrag(layer, moveX, moveY);
                    break;

                case MotionEvent.ACTION_UP:
                    if (pressed) {
                        pressed = false;
                        interactable.onRelease(layer);

                        if (!moved)
                            interactable.onClick(layer);
                        moved = false;
                    }
            }
        }
    }
}
