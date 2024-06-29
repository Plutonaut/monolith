#version 460 core

layout (location=0) in vec3 position;
layout (location=1) in vec2 texcoord;

out vec2 textureCoord;

uniform mat4 ortho;

void main() {
	textureCoord = texcoord;
	
	gl_Position = ortho * vec4(position, 1.0);
}