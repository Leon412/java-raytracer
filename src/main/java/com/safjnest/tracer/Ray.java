package com.safjnest.tracer;

import com.safjnest.scene.Vector3;

import lombok.Value;

@Value
public class Ray {
    Vector3 origin;
    Vector3 direction;

    public Vector3 at(float t) {
        return origin.plus(direction.times(t));
    }
}
