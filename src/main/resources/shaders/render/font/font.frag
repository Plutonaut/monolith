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

in vec2 textureCoord;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform Material material;

void main() {
    vec4 textureColor = texture(textureSampler, textureCoord);

    if (all(equal(textureColor.xyz, vec3(0)))) {
        textureColor.w = 0;
    }

    fragColor = material.diffuse * textureColor;
}