#version 460 core

out vec4 fragColor;

in vec3 vColor;
in vec2 vTexCoord;

struct Material
{
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    float reflectance;
    int hasTexture;
    int hasNormalMap;
};

struct AmbientLight
{
	float factor;
	vec3 color;
};

uniform sampler2D textureSampler;
uniform Material material;

uniform AmbientLight ambientLight;

vec4 _calc_ambient_light(AmbientLight ambientLight, vec4 materialAmbientColor) {
	return vec4(ambientLight.factor * ambientLight.color, 1) * materialAmbientColor;
}

void main() {
    vec4 instanceColor = vec4(vColor, 1.0);
    if (material.hasTexture > 0) {
		instanceColor = texture(textureSampler, vTexCoord);
    }

	vec4 ambientColor = _calc_ambient_light(ambientLight, instanceColor + material.ambient);
//	vec4 diffuseColor = instanceColor + material.diffuse;
//	vec4 specularColor = instanceColor + material.specular;

	fragColor = instanceColor + ambientColor;
//    fragColor = specularColor;
}