#version 460 core

layout (points) in;
layout (triangle_strip) out;
layout (max_vertices = 4) out;

uniform mat4 viewprojection;
uniform vec3 cameraposition;
uniform float billboardSize;

out vec2 vTexCoord;

void main() {
    vec3 pos = gl_in[0].gl_Position.xyz;
    vec3 toCamera = normalize(cameraposition - pos);
    vec3 up = vec3(0.0, 1.0, 0.0);
    vec3 right = cross(toCamera, up) * billboardSize;

    pos -= (right * 0.5);
    gl_Position = viewprojection * vec4(pos, 1.0);
    vTexCoord = vec2(0.0, 0.0);
    EmitVertex(); // sets variables back to undefined

    pos.y += billboardSize;
    gl_Position = viewprojection * vec4(pos, 1.0);
    vTexCoord = vec2(0.0, 1.0);
    EmitVertex();

    pos.y -= billboardSize;
    pos += right;
    gl_Position = viewprojection * vec4(pos, 1.0);
    vTexCoord = vec2(1.0, 0.0);
    EmitVertex();

    pos.y += billboardSize;
    gl_Position = viewprojection * vec4(pos, 1.0);
    vTexCoord = vec2(1.0, 1.0);
    EmitVertex();

    EndPrimitive();
}