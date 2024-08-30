#version 430

struct Sphere {
    vec3 center;
    float radius;
    vec3 color;
};

struct Camera {
    vec3 position;
    vec3 forwards;
    vec3 right;
    vec3 up;
};

struct Ray {
    vec3 origin;
    vec3 direction;
};

struct RenderState {
    float t;
    vec3 color;
    bool hit;
};

layout (local_size_x = 8, local_size_y = 4) in;
layout (rgba32f, binding = 0) uniform image2D img_output;
layout (std430, binding = 1) buffer sceneData {
  Sphere[] spheres;
};

uniform Camera viewer;
uniform int sphereCount;

vec3 rayColor(Ray ray);
void hit(Ray ray, Sphere sphere, float tMin, float tMax, inout RenderState renderstate);

void main() {

    ivec2 pixel_coords = ivec2(gl_GlobalInvocationID.xy);
    ivec2 screen_size = imageSize(img_output);
    float horizontalCoefficient = ((float(pixel_coords.x) * 2 - screen_size.x) / screen_size.x);
    float verticalCoefficient = ((float(pixel_coords.y) * 2 - screen_size.y) / screen_size.x);

    Ray ray;
    ray.origin = viewer.position;
    ray.direction = viewer.forwards + horizontalCoefficient * viewer.right + verticalCoefficient * viewer.up;

    vec3 pixel = rayColor(ray);

    imageStore(img_output, pixel_coords, vec4(pixel,1.0));
}

vec3 rayColor(Ray ray) {

    vec3 color = vec3(0.0);

    float nearestHit = 999999999;
    bool hitSomething = false;
    RenderState renderState;

    for (int i = 0; i < sphereCount; i++) {

        hit(ray, spheres[i], 0.001, nearestHit, renderState);

        if (renderState.hit) {
            nearestHit = renderState.t;
            hitSomething = true;
        }
    }

    if (hitSomething) {
        color = renderState.color;
    }

    return color;
}

void hit(Ray ray, Sphere sphere, float tMin, float tMax, inout RenderState renderState) {
    vec3 co = ray.origin - sphere.center;
    float a = dot(ray.direction, ray.direction);
    float b = 2 * dot(ray.direction, co);
    float c = dot(co, co) - sphere.radius * sphere.radius;
    float discriminant = b * b - (4 * a * c);

    if (discriminant > 0.0) {

        float t = (-b - sqrt(discriminant)) / (2 * a);

        if (t > tMin && t < tMax) {
            renderState.t = t;
            renderState.color = sphere.color;
            renderState.hit = true;
            return;
        }
    }

    renderState.hit = false;
}