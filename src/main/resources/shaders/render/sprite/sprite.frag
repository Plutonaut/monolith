#version 330

in vec2 vTextureCoord;
in vec3 vColor;

out vec4 fragColor;

uniform sampler2D textureSampler;

void main() {
	fragColor = texture(textureSampler, vTextureCoord) * vec4(vColor, 1.0);
}