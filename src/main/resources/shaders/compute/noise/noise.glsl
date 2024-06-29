#version 460 core

#define M_PI 3.14159265358979323846

uniform float zoom;
uniform float timestep;

layout(rgba32f, binding = 0) writeonly uniform image2D outimage;

layout (local_size_x = 8, local_size_y = 4) in;

// 2D Random
float random (in vec2 p) {
	return fract(sin(dot(p.xy, vec2(12.9898, 78.233))) * 43578.5453123);
}

// Cubic Hermine Curve. (smoothstep())
vec2 hermine (in vec2 p) {
	return p * p * (3.0 - 2.0 * p);
}

vec2 quintic (in vec2 p) {
	return p * p * p * (p * (p * 6. - 15.) + 10.);
}

vec2 skew (in vec2 p) {
	vec2 r = vec2(0.0);
	r.x = 1.1547 * p.x;
	r.y = p.y + 0.5 * r.x;

	return r;
}

vec3 simplex (in vec2 st) {
	vec3 xyz = vec3(0.0);

	vec2 p = fract(skew(st));
	if (p.x > p.y) {
		xyz.xy = 1.0 - vec2(p.x, p.y - p.x);
		xyz.z = p.y;
	} else {
		xyz.yz = 1.0 - vec2(p.x - p.y, p.y);
		xyz.x = p.x;
	}

	return fract(xyz);
}

float noise (in vec2 p) {
	// Four corners of a 2D tile.
	float a = random(p);
	float b = random(p + vec2(1.0, 0.0));
	float c = random(p + vec2(0.0, 1.0));
	float d = random(p + vec2(1.0, 1.0));

	// Smooth interpolation

	vec2 u = p * p * (3.0 - 2.0 * p);

	// Mix 4 corners percentages
	return mix(a, b, u.x) + (c - a) * u.y * (1.0 - u.x) + (d - b) * u.x * u.y;
}

void main() {
	ivec2 pix = ivec2(gl_GlobalInvocationID.xy);
	ivec2 size = imageSize(outimage);

	vec2 p = (vec2(pix) + vec2(0.5, 0.5)) / vec2(size.x, size.y);
	float z = max(zoom, 0.01);

	p *= z;
	vec3 s = simplex(p);

	vec4 color = vec4(s.xy, 1.0, 1.0);

	imageStore(outimage, pix, color);
}
