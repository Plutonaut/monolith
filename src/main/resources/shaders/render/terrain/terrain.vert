#version 460 core

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 texcoord;
layout (location=5) in mat4 instancematrices;

out vec3 vNormal;
out vec2 vTextureCoord;
out vec3 vViewPosition;
out mat4 mView;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
uniform mat4 terrainmodel;
uniform int isInstanced;

void main() {
    vec4 initPos 			= vec4(position, 1.0);
    vec4 initNormal 		= vec4(normal, 0.0);
    mat4 modelMat           = terrainmodel * model;
    if (isInstanced > 0) {
        modelMat = model * instancematrices;
    }
    mat4 modelview          = view * modelMat;
    vec4 mvPosition 		= modelview * initPos;
    gl_Position 			= projection * mvPosition;
    vNormal 				= normalize(modelview * initNormal).xyz;
    vTextureCoord 			= texcoord;
    vViewPosition  			= mvPosition.xyz;
    mView 					= modelview;
}