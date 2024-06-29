#version 460 core

const int MAX_WEIGHTS = 4;
const int MAX_BONES = 150;

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec3 tangent;
layout (location=3) in vec3 bitangent;
layout (location=4) in vec2 texcoord;
layout (location=5) in vec4 boneweights;
layout (location=6) in ivec4 boneindices;

out vec3 outPosition;
out vec3 outNormal;
out vec3 outTangent;
out vec3 outBitangent;
out vec2 outTexCoord;
out vec3 outViewPosition;
out vec4 outWorldPosition;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform mat4 bonesMatrices[MAX_BONES];

void main()
{
	vec4 initPos 		= vec4(0, 0, 0, 0);
	vec4 initNormal 	= vec4(0, 0, 0, 0);
	vec4 initTangent 	= vec4(0, 0, 0, 0);
	vec4 initBitangent 	= vec4(0, 0, 0, 0);
	
	int count = 0;
	// for i = 0; i < MAX_WEIGHTS; i++
	
	if (count == 0) {
		initPos 		= vec4(position, 1.0);
		initNormal 		= vec4(normal, 0.0);
		initTangent 	= vec4(tangent, 0.0);
		initBitangent 	= vec4(bitangent, 0.0);
	}
	
	mat4 modelViewMatrix 		= viewMatrix * modelMatrix;
	vec4 modelViewPosition 		= modelViewMatrix * initPos;
	gl_Position 				= projectionMatrix * modelViewPosition;
	outPosition 				= modelViewPosition.xyz;
	outNormal 					= normalize(modelViewMatrix * initNormal).xyz;
	outTangent					= normalize(modelViewMatrix * initTangent).xyz;
	outBitangent 				= normalize(modelViewMatrix * initBitangent).xyz;
	outTexCoord					= texcoord;
	outViewPosition				= modelViewPosition.xyz;
	outWorldPosition 			= modelMatrix * initPos;
}