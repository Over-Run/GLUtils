#version 330

layout (location=0) in vec3 vert;
layout (location=1) in vec4 in_color;
layout (location=2) in vec2 in_tex;
layout (location=3) in vec3 in_vertexNormal;
out vec4 out_color;
out vec2 out_tex;
out vec3 mvVertexNormal;
out vec3 mvVertexPos;
uniform mat4 proj, modelv;
uniform int textured;

void main() {
    vec4 mvPos = modelv * vec4(vert, 1.0);
    gl_Position = proj * mvPos;
    out_color = in_color;
    if (textured != 0) {
        out_tex = in_tex;
    }
    mvVertexNormal = normalize(modelv * vec4(in_vertexNormal, 0.0)).xyz;
    mvVertexPos = mvPos.xyz;
}