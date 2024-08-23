#version 460 core

const int MAX_BONES = 150;
const int MAX_WEIGHTS = 4;

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 texcoord;
layout (location=3) in vec3 tangent;
layout (location=4) in vec3 bitangent;
layout (location=5) in vec4 boneweights;
layout (location=6) in ivec4 boneindices;
layout (location=7) in mat4 instancematrices;

out vec3 vNormal;
out vec2 vTextureCoord;
out vec3 vTangent;
out vec3 vBitangent;
out vec3 vViewPosition;
out mat4 mView;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
uniform mat4 boneMatrices[MAX_BONES];
uniform int isInstanced;

void main() {
	vec4 initPos 			= vec4(position, 1.0);
	vec4 initNormal 		= vec4(normal, 0.0);
	vec4 initTangent 		= vec4(tangent, 0.0);
	vec4 initBitangent 		= vec4(bitangent, 0.0);
    mat4 modelMat           = model;
    if (isInstanced > 0) {
        modelMat = model * instancematrices;
    }
    mat4 modelview          = view * modelMat;
    vec4 mvPosition 		= modelview * initPos;
    gl_Position 			= projection * mvPosition;
    vNormal 				= normalize(modelview * initNormal).xyz;
    vTangent 				= normalize(modelview * initTangent).xyz;
    vBitangent 				= normalize(modelview * initBitangent).xyz;
    vTextureCoord 			= texcoord;    
    vViewPosition  			= mvPosition.xyz;
    mView 					= modelview;
}