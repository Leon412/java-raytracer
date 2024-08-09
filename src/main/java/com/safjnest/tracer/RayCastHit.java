package com.safjnest.tracer;


import com.safjnest.scene.SceneObject;
import com.safjnest.scene.Vector3;
import lombok.Value;

@Value
class RayCastHit {
    SceneObject object;
    float t;
    Vector3 normal;
}
