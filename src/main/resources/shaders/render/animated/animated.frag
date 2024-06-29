#version 460 core

in vec3 outPosition;
in vec3 outNormal;
in vec3 outTangent;
in vec3 outBitangent;
in vec2 outTexCoord;
in vec3 outViewPosition;
in vec4 outWorldPosition;

out vec4 fragColor;

void main()
{
	fragColor = vec4(outPosition, 1.0);
}