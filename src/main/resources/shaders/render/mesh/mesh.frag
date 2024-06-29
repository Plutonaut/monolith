#version 460 core

struct AmbientLight {
	float factor;
	vec3 color;
};
struct Material
{
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	float reflectance;
	int hasTexture;
	int hasNormalMap;
};

in vec3 vPosition;
in vec4 vColor;
in vec2 vTextureCoord;
in mat4 mView;

uniform int hasTexture;
uniform sampler2D textureSampler;
uniform AmbientLight ambientLight;
uniform Material material;

out vec4 fragColor;


// Ambient Light ---------------------------------------------------------------------------------
vec4 _calc_ambient_light(AmbientLight ambientLight, vec4 materialAmbientColor) {
	return vec4(ambientLight.factor * ambientLight.color, 1) * materialAmbientColor;
}

void main() {
	vec4 textureColor = vec4(1.0);
	if (hasTexture == 1) textureColor = texture(textureSampler, vTextureCoord);

	vec4 ambientLight = _calc_ambient_light(ambientLight, textureColor + material.ambient);

	fragColor = vColor * textureColor + ambientLight;
}