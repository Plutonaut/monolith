#version 460 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texcoord;
layout (location = 2) in mat4 instancematrices;
layout (location = 6) in vec3 color;

uniform mat4 projection;
uniform mat4 view;

out vec3 vColor;
out vec2 vTexCoord;

void main() {
    gl_Position = projection * view * instancematrices * vec4(position, 1.f);
    vColor = color;
    vTexCoord = texcoord;
}