#version 460 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texcoord;
layout (location=2) in vec3 color;

out vec3 vertexColor;
out vec2 vTexCoord;

uniform mat4 projection;

void main() {
    vertexColor = vec4(color, 0.0);
    vTexCoord = texcoord;
    gl_Position = projection * vec4(position, 1.0);
}