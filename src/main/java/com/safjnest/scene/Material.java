package com.safjnest.scene;

import lombok.Value;

@Value
public class Material {
    Color kAmbient;
    Color kDiffuse;
    Color kSpecular;
    Color kReflection;
    int alpha;
}
