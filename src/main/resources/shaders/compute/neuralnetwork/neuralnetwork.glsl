#version 460 core

uniform float forecast;
uniform int outCount;

struct Perceptron
{

};

layout (local_size_x = 8, local_size_y = 4) in;

layout(std140, binding = 0) buffer inBuffer {
	float inputs[];
}

layout(std140, binding = 1) buffer outBuffer {
	float outputs[];
}

void main() {

}
