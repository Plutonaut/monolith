#version 460 core

// set local_size coordinates so that they multiply to GPU "warp" size;
// AMD -> 64
// NVIDA -> 32
layout (local_size_x = 8, local_size_y = 4, local_size_z = 1) in;

layout(rgba32f, binding = 0) uniform image2D outimage;

// ----- BASE INPUTS -----
// uvec3 - sizes of the work groups in this compute shader
// gl_NumWorkGroups;

// uvec3 - the ID of the current work group we are in
// gl_WorkGroupID;

// uvec3 - the ID of the current invocation we are in with respect to the work group we are in
// gl_LocalInvocationID;

// ----- SHORTHAND INPUTS -----
// uvec3 - the ID of the current invocation we are in with respect to the whole compute shader
// gl_GlobalInvocationID = gl_WorkGroupID * gl_WorkGroupSize + gl_LocalInvocationID
// gl_GlobalInvocationID

// uint - the index of the current invocation we are in with respect to the work group we are in
// gl_LocalInvocationIndex =
//		gl_LocalInvocationID.z * gl_WorkGroupSize.x * gl_WorkGroupSize.y +
//		gl_LocalInvocationID.y * gl_WorkGroupSize.x +
//		gl_LocalInvocationID.x
// gl_LocalInvocationIndex;

void main() {
	vec4 value = vec4(0.0, 0.0, 0.0, 1.0);
	ivec2 texelCoord = ivec2(gl_GlobalInvocationID.xy);

	value.x = float(texelCoord.x) / (gl_NumWorkGroups.x);
	value.y = float(texelCoord.y) / (gl_NumWorkGroups.y);

	imageStore(outimage, texelCoord, value);
}
