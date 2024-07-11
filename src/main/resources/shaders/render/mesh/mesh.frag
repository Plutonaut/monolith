#version 460 core

struct Material
{
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	float reflectance;
	int hasTexture;
	int hasNormalMap;
};

in vec4 vColor;
in vec2 vTextureCoord;

uniform int hasTexture;
uniform sampler2D textureSampler;
uniform Material material;

out vec4 fragColor;

void main() {
	vec4 color = vColor + material.ambient;
	if (hasTexture == 1) color = texture(textureSampler, vTextureCoord);
	fragColor = color;
}