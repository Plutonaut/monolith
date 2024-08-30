#version 430 core

layout (location = 0) in vec2 texcoord;

out vec2 fragmentTextureCoordinate;

void main()
{
    gl_Position = vec4(texcoord, 0.0, 1.0);
    fragmentTextureCoordinate = 0.5 * (texcoord + vec2(1.0));
    fragmentTextureCoordinate.y *= -1;
}