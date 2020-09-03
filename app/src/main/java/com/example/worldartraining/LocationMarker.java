package com.example.worldartraining;

import com.google.ar.core.Anchor;

public class LocationMarker {
    // Location in real-world terms
    public double longitude;
    public double latitude;

    // Location in AR terms
    public Anchor anchor;

    //Renderer
    public Renderer renderer;

    public LocationMarker(double longitude, double latitude, Renderer renderer) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.renderer = renderer;
    }

    public void setOnTouchListener(Runnable touchEvent) {
        this.touchEvent = touchEvent;
    }

    public Runnable getTouchEvent() {
        return touchEvent;
    }
    private Runnable touchEvent;
    private int touchableSize;

    public int getTouchableSize() {
        return touchableSize;
    }

    public void setTouchableSize(int touchableSize) {
        this.touchableSize = touchableSize;
    }
}
