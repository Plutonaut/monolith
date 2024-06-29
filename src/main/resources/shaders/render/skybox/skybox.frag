#version 330

in vec2 vTexCoord;
out vec4 fragColor;

// Material Struct Data -----------------------------------------------------------------------
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
// Material Uniform -----------------------------------------------------------------------
uniform Material material;
// Light Uniforms -----------------------------------------------------------------------
uniform AmbientLight ambientLight;

// Ambient Light ---------------------------------------------------------------------------------
vec4 _calc_ambient_light(AmbientLight ambientLight, vec4 materialAmbientColor) {
	return vec4(ambientLight.factor * ambientLight.color, 1) * materialAmbientColor;
}
void main()
{
	vec4 textureColor = texture(textureSampler, vTexCoord);
	vec4 ambient = _calc_ambient_light(ambientLight, textureColor + material.ambient);
	fragColor = ambient;
}