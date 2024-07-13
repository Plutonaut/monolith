#version 460 core

layout (location=0) in vec3 position;
layout (location=1) in vec3 color;
layout (location=2) in vec2 texcoord;

out vec4 vColor;
out vec2 vTextureCoord;

uniform mat4 projection;
uniform mat4 modelview;

void main() {
	vColor 			= vec4(color, 1.0);
	vTextureCoord 	= texcoord;
	gl_Position 	= projection * modelview * vec4(position, 1.0);
}