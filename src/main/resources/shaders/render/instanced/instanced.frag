#version 460 core

out vec4 fragColor;

in vec3 vColor;
in vec2 vTexCoord;

uniform sampler2D textureSampler;
uniform int hasTexture;

void main() {
	fragColor = vec4(vColor, 1.0); 
	if (hasTexture > 0) {
		fragColor = texture(textureSampler, vTexCoord);
	}
}