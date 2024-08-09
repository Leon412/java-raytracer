package com.safjnest.scene;

import lombok.Value;

import java.util.Optional;

import com.safjnest.tracer.Ray;

@Value
public class Sphere implements SceneObject {
    Vector3 center;
    float radius;
    Material material;

    @Override
    public Optional<Float> earliestIntersection(Ray ray) {
        Vector3 cPrime = ray.getOrigin().minus(center);
        float a = ray.getDirection().dot(ray.getDirection());
        float b = 2 * cPrime.dot(ray.getDirection());
        float c = cPrime.dot(cPrime) - radius * radius;

        float discriminant = (b * b) - (4.0f * a * c);
        if(discriminant < 0) {
            return Optional.empty();
        }

        float sqrt = (float) Math.sqrt(discriminant);
        float t1 = (-b + sqrt) / (2.0f * a);
        float t2 = (-b - sqrt) / (2.0f * a);

        float min = Math.min(t1, t2);

        return min > 0 ?
                Optional.of(min) :
                Optional.empty();
    }

    @Override
    public Vector3 normalAt(Vector3 point) {
        return point.minus(center).normalize();
    }
}
