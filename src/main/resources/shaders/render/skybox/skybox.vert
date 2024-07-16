#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texcoord;

out vec2 vTexCoord;

uniform mat4 projection;
uniform mat4 modelview;

void main()
{
    gl_Position = projection * modelview * vec4(position, 1.0);
    vTexCoord = texcoord;
}