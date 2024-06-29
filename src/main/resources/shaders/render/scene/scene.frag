#version 460 core

const int MAX_LIGHTS = 10;
const float SPECULAR_POWER = 10.0;

// Vertex Data -----------------------------------------------------------------------
in vec3 vViewPosition;
in vec3 vNormal;
in vec2 vTextureCoord;
in vec3 vTangent;
in vec3 vBitangent;
in mat4 mView;

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
// Light Structs -----------------------------------------------------------------------
struct Attenuation 
{
	float constant;
	float linear;
	float exponent;
};
struct AmbientLight
{
    float factor;
    vec3 color;
};
struct PointLight 
{
    vec3 position;
    vec3 color;
    float intensity;
    Attenuation att;
};
struct SpotLight
{
    PointLight pl;
    vec3 conedir;
    float cutoff;
};
struct DirLight
{
    vec3 color;
    vec3 direction;
    float intensity;
};

uniform int isSelected;
uniform int isAnimated;

// Texture Uniforms -----------------------------------------------------------------------
uniform sampler2D textureSampler;
uniform sampler2D normalSampler;

// Material Uniform -----------------------------------------------------------------------
uniform Material material;

// Light Uniforms -----------------------------------------------------------------------
uniform AmbientLight ambientLight;
uniform DirLight dirLight;
uniform PointLight pointLights[MAX_LIGHTS];
uniform SpotLight spotLights[MAX_LIGHTS];

// Light Calculation Functions -----------------------------------------------------------
vec4 _calc_light_color(vec4 diffuse, vec4 specular, vec3 lightColor, float lightIntensity, vec3 position, vec3 toLightDirection, vec3 normal, float materialReflectance) {
	vec4 diffuseColor = vec4(0, 0, 0, 1);
	vec4 specularColor = vec4(0, 0, 0, 1);
	
	// Diffuse Light
	float diffuseFactor = max(dot(normal, toLightDirection), 0.0);
	diffuseColor = diffuse * vec4(lightColor, 1.0) * lightIntensity * diffuseFactor;
	
	// Specular Light
	vec3 cameraDirection = normalize(-position);
	vec3 fromLightDirection = -toLightDirection;
	vec3 reflectedLight = normalize(reflect(fromLightDirection, normal));
	
	// Specular Factor
	float specularFactor = max(dot(cameraDirection, reflectedLight), 0.0);

	specularFactor = pow(specularFactor, SPECULAR_POWER);
	specularColor = specular * lightIntensity * specularFactor * materialReflectance * vec4(lightColor, 1.0);
	
	return (diffuseColor + specularColor);
}

// Ambient Light ---------------------------------------------------------------------------------
vec4 _calc_ambient_light(AmbientLight ambientLight, vec4 materialAmbientColor) {
	return vec4(ambientLight.factor * ambientLight.color, 1) * materialAmbientColor;
}

// Directional Light ---------------------------------------------------------------------------------
vec4 calcDirLight(DirLight light, vec4 diffuse, vec4 specular, vec3 position, vec3 normal, float materialReflectance) {
	return _calc_light_color(diffuse, specular, light.color, light.intensity, position, normalize(light.direction), normal, materialReflectance);
}

// Point Lights ---------------------------------------------------------------------------------
vec4 _calc_point_light(PointLight light, vec4 diffuse, vec4 specular, vec3 position, vec3 normal, float materialReflectance) {
	vec3 lightDirection = light.position - position;
	vec3 toLightDirection = normalize(lightDirection);
	vec4 lightColor = _calc_light_color(diffuse, specular, light.color, light.intensity, position, toLightDirection, normal, materialReflectance);
	
	// Apply Attenuation
	float lightDistance = length(lightDirection);
	float attenuationInv = light.att.constant + light.att.linear * lightDistance + light.att.exponent * lightDistance * lightDistance;
	
	return lightColor / attenuationInv;
}

// Spot Lights ----------------------------------------------------------------------
vec4 _calc_spot_light(SpotLight light, vec4 diffuse, vec4 specular, vec3 position, vec3 normal, float materialReflectance) {
	vec3 lightDirection = light.pl.position - position;
	vec3 toLightDirection = normalize(lightDirection);
	vec3 fromLightDirection = -toLightDirection;
	
	float spotAlpha = dot(fromLightDirection, normalize(light.conedir));
	
	vec4 color = vec4(0, 0, 0, 0);
	
	if (spotAlpha > light.cutoff) {
		color = _calc_point_light(light.pl, diffuse, specular, position, normal, materialReflectance);
		color *= (1.0 - (1.0 - spotAlpha) / (1.0 - light.cutoff));
	}
	
	return color;
}

// Normal Map Calculations ----------------------------------------------------------------------
vec3 _calc_normal(vec3 normal, vec3 tangent, vec3 bitangent, vec2 textureCoords, mat4 modelViewMatrix) {
	vec3 newNormal = texture(normalSampler, textureCoords).rgb;
	newNormal = normalize(newNormal * 2.0 - 1.0);
	
	if (isAnimated > 0) {
		mat3 TBN = mat3(tangent, bitangent, normal);
		newNormal = normalize(TBN * newNormal);
	} else {
		newNormal = normalize(modelViewMatrix * vec4(newNormal, 0.0)).xyz;
	}
	
	return newNormal;
}

// FRAGMENT SHADER -----------------------------------------------------------
void main() {
	// Texture color
	vec4 textureColor = vec4(vec3(0.1), 1.0);
	
	if (material.hasTexture > 0) {
		textureColor = texture(textureSampler, vTextureCoord);
	}
	
	// Material colors
	vec4 ambient = _calc_ambient_light(ambientLight, textureColor + material.ambient);
	vec4 diffuse = textureColor + material.diffuse;
	vec4 specular = textureColor + material.specular;
	vec3 normal = vNormal;
	float reflectance = material.reflectance;
	
	if (material.hasNormalMap > 0) {
		normal = _calc_normal(vNormal, vTangent, vBitangent, vTextureCoord, mView);
	}
	
	vec4 diffuseSpecularComp = calcDirLight(dirLight, diffuse, specular, vViewPosition, normal, reflectance);
	
	for (int i = 0; i < MAX_LIGHTS; i++) {
		if (pointLights[i].intensity > 0.0) {
			diffuseSpecularComp += _calc_point_light(pointLights[i], diffuse, specular, vViewPosition, normal, reflectance);
		}
	}
	
	for (int i = 0; i < MAX_LIGHTS; i++) {
		if (spotLights[i].pl.intensity > 0.0) {
			diffuseSpecularComp += _calc_spot_light(spotLights[i], diffuse, specular, vViewPosition, normal, reflectance);
		}
	}
	
	fragColor = ambient + diffuseSpecularComp;
	if (isSelected > 0) {
		fragColor = vec4(fragColor.x, fragColor.y, 1, 1);
	}
}