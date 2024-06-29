#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 color;
layout (location=2) in vec2 texcoord;

out vec3 vColor;
out vec2 vTextureCoord;

uniform mat4 projection;

void main() {
	vTextureCoord = texcoord;
	vColor = color;
	gl_Position = projection * vec4(position, 1.0);
}	