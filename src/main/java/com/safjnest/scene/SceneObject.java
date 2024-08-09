package com.safjnest.scene;

import java.util.Optional;

import com.safjnest.tracer.Ray;

public interface SceneObject {
    Material getMaterial();
    Optional<Float> earliestIntersection(Ray ray);
    Vector3 normalAt(Vector3 point);
}
