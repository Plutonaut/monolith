#version 460 core

in vec2 fragmentTextureCoordinate;

uniform sampler2D frameBuffer;

out vec4 fragColor;

void main()
{
    fragColor = texture(frameBuffer, fragmentTextureCoordinate);
}