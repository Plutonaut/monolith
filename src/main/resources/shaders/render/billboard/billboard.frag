#version 460 core

uniform sampler2D textureSampler;

in vec2 vTexCoord;
out vec4 fragColor;

void main() {
    fragColor = texture2D(textureSampler, vTexCoord);

    if (all(equal(fragColor.xyz, vec3(0)))) {
        discard;
    }

    if (fragColor.w == 0.0) {
        discard;
    }
}