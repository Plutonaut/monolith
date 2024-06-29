#version 460 core

#define M_PI 3.14159265358979323846

layout(rgba8, binding = 0) writeonly uniform image2D outimage;

layout (local_size_x = 8, local_size_y = 4) in;

float rand(vec2 c) {
	return fract(sin(dot(c.xy, vec2(12.9898, 78.233))) * 43758.5453);
}

float noise(vec2 p, int screenwidth, float freq) {
	float unit = screenwidth / freq;
	vec2 ij = floor(p / unit);
	vec2 xy = mod(p, unit) / unit;
	//xy = 3.*xy*xy-2.*xy*xy*xy;
	xy = .5 * (1. - cos(M_PI * xy));
	float a = rand((ij + vec2(0., 0.)));
	float b = rand((ij + vec2(1., 0.)));
	float c = rand((ij + vec2(0., 1.)));
	float d = rand((ij + vec2(1., 1.)));
	float x1 = mix(a, b, xy.x);
	float x2 = mix(c, d, xy.x);
	return mix(x1, x2, xy.y);
}

float pNoise(vec2 p, int octaves, int screenwidth) {
	float n = 0.;
	float normK = 0.;
	float f = 4.;
	float amp = 1.;
	int iCount = 0;
	for (int i = 0; i < 50; i++) {
		n += amp * noise(p, screenwidth, f);
		f *= 2.;
		normK += amp;
		amp *= 0.5;
		if (iCount == octaves)
			break;
		iCount++;
	}
	float nf = n / normK;
	return nf * nf * nf * nf;
}

void main() {
	ivec2 pix = ivec2(gl_GlobalInvocationID.xy);
	ivec2 size = imageSize(outimage);

	if (pix.x >= size.x || pix.y >= size.y) {
		return;
	}

	vec2 p = (vec2(pix) + vec2(0.5, 0.5)) / vec2(size.x, size.y);
	float v = pNoise(p, 2, size.x);

	vec4 color = vec4(vec3(v), 1.);

	imageStore(outimage, pix, color);
}
