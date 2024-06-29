#version 460 core

in vec2 textureCoord;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform vec4 textureColor;

void main() {
    fragColor = textureColor * texture(textureSampler, textureCoord);
}