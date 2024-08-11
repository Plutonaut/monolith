#version 460 core

layout (location=0) in vec3 position;
layout (location=1) in vec2 texcoord;

out vec2 textureCoord;

uniform mat4 model;
uniform mat4 projection;

void main() {
	textureCoord = texcoord;
	gl_Position = projection * model * vec4(position, 1.0);
}