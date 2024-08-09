package com.safjnest.tracer;

import java.util.Optional;

import com.safjnest.scene.*;

import lombok.Value;

@Value
public class RayTracer {
    private static final int NUM_BOUNCES = 2;
    private static final int NUM_SAMPLES_PER_PIXEL_PER_DIRECTION = 2;
    private static final int NUM_SAMPLES_PER_PIXEL = NUM_SAMPLES_PER_PIXEL_PER_DIRECTION * NUM_SAMPLES_PER_PIXEL_PER_DIRECTION;

    private Scene scene;
    private int w;
    private int h;

    public Color tracedValueAtPixel(int x, int y) {
        float xt = ((float) x) / w;
        float yt = ((float) h - y - 1) / h;

        float dx = 1.0f / (w * NUM_SAMPLES_PER_PIXEL_PER_DIRECTION);
        float dy = 1.0f / (h * NUM_SAMPLES_PER_PIXEL_PER_DIRECTION);

        Color color = Color.BLACK;
        for (int xi = 0; xi < NUM_SAMPLES_PER_PIXEL_PER_DIRECTION; xi++) {
            for (int yi = 0; yi < NUM_SAMPLES_PER_PIXEL_PER_DIRECTION; yi++) {
                color = color.plus(tracedValueAtPositionOnImagePlane(xt + dx * xi, yt + dy * yi));
            }
        }

        return color.divide(NUM_SAMPLES_PER_PIXEL).clamp();
    }

    private Color tracedValueAtPositionOnImagePlane(float xt, float yt) {
        Vector3 top = Vector3.lerp(scene.getImagePlane().getTopLeft(), scene.getImagePlane().getTopRight(), xt);
        Vector3 bottom = Vector3.lerp(scene.getImagePlane().getBottomLeft(), scene.getImagePlane().getBottomRight(), xt);
        Vector3 point = Vector3.lerp(bottom, top, yt);

        Ray ray = new Ray(point, point.minus(scene.getCamera()));

        return colorFromFirstObjectHit(ray, 0).clamp();
    }

    private Color colorFromFirstObjectHit(Ray ray, int depth) {
        RayCastHit hit = null;
        for(SceneObject object : scene.getObjects()) {
            Optional<Float> f = object.earliestIntersection(ray);
            if(f.isPresent() && (hit == null || f.get() < hit.getT())) {
                hit = new RayCastHit(object, f.get(), object.normalAt(ray.at(f.get())));
            }
        }

        //if(hit == null) hit = rayCastHitLight(ray);

        if(hit != null) {
            Vector3 p = ray.at(hit.getT());
            Color color = phongLightingAtPoint(hit, p);
            if((depth < NUM_BOUNCES)) {
                Vector3 n = hit.getNormal();
                Vector3 v = ray.getDirection().invert().normalize();
                Vector3 r = n.times(2.0f * n.dot(v)).minus(v);
                Color reflection = colorFromFirstObjectHit(new Ray(p, r), depth + 1);
                color = color.plus(reflection.times(hit.getObject().getMaterial().getKReflection()));
            }
            return color;
        }

        return Color.BLACK;
    }
    
    private Color phongLightingAtPoint(RayCastHit hit, Vector3 p) {
        Material material = hit.getObject().getMaterial();
        Vector3 n = hit.getNormal();
        
        Color ambient = material.getKAmbient().times(scene.getAmbientLight());

        Color diffuse = Color.BLACK;
        Color specular = Color.BLACK;

        for(Light light : scene.getLights()) {
            Vector3 l = light.getPosition().minus(p).normalize();
            float nl = n.dot(l);

            if(nl < 0 || isPointInShadowFromLight(p, light, hit.getObject())) {
                continue;
            }

            Vector3 r = n.times(2.0f * nl).minus(l).normalize();
            Vector3 v = scene.getCamera().minus(p).normalize();
            float vra = (float) Math.pow(v.dot(r), material.getAlpha());

            diffuse = diffuse.plus(material.getKDiffuse().times(light.getIntensityDiffuse()).times(nl));
            specular = specular.plus(material.getKSpecular().times(light.getIntensitySpecular()).times(vra));
        }

        return ambient.plus(diffuse).plus(specular).clamp();
    }

    private boolean isPointInShadowFromLight(Vector3 p, Light l, SceneObject currentObject) {
        Ray shadowRay = new Ray(p, l.getPosition().minus(p));
        for(SceneObject object : scene.getObjects()) {
            if(!object.equals(currentObject)) {
                Optional<Float> f = object.earliestIntersection(shadowRay);
                if(f.isPresent() && f.get() <= 1) {
                    return true;
                }
            }
        }
        return false;
    }


    //this method is just to visualize where the lights are
    private RayCastHit rayCastHitLight(Ray ray) {
        RayCastHit hit = null;
        for(Light light : scene.getLights()) {
            Sphere lightSphere = new Sphere(light.getPosition(), 0.05f, new Material(
                new Color(0.9f, 0.9f, 0.9f),
                new Color(0.9f, 0.9f, 0.9f),
                new Color(0.9f, 0.9f, 0.9f),
                new Color(0.9f, 0.9f, 0.9f),
                25));
            Optional<Float> f = lightSphere.earliestIntersection(ray);
            if(f.isPresent() && (hit == null || f.get() < hit.getT()) && f.get() <= 1) {
                hit = new RayCastHit(lightSphere, f.get(), lightSphere.normalAt(ray.at(f.get())));
            }
        }
        return hit;
    }
}
