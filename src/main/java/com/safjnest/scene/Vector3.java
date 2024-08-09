package com.safjnest.scene;

import lombok.Value;

@Value
public class Vector3 {
    float x;
    float y;
    float z;

    public Vector3 plus(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    public Vector3 minus(Vector3 other) {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    public Vector3 times(float k) {
        return new Vector3(k * x, k * y, k * z);
    }

    public Vector3 dividedBy(float k) {
        return new Vector3(x / k, y / k, z / k);
    }

    public Vector3 invert() {
        return this.times(-1);
    }

    public Vector3 normalize() {
        return this.dividedBy(norm());
    }

    //linear interpolation
    public static Vector3 lerp(Vector3 v1, Vector3 v2, float t) {
        return v1.times(1 - t).plus(v2.times(t));
    }

    public float dot(Vector3 other) {
        return (x * other.x) + (y * other.y) + (z * other.z);
    }

    public float norm() {
        return (float) Math.sqrt(x*x + y*y + z*z);
    }


}
