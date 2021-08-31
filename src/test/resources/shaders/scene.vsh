#version 330

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int textured;
    float reflectance;
};

layout (location=0) in vec3 vert;
layout (location=1) in vec2 in_tex;
layout (location=2) in vec3 in_vertexNormal;
out vec2 out_tex;
out vec3 mvVertexNormal;
out vec3 mvVertexPos;
uniform Material material;
uniform mat4 proj, modelv;
uniform int textured;

void main() {
    vec4 mvPos = modelv * vec4(vert, 1.0);
    gl_Position = proj * mvPos;
    if (material.textured != 0) {
        out_tex = in_tex;
    }
    mvVertexNormal = normalize(modelv * vec4(in_vertexNormal, 0.0)).xyz;
    mvVertexPos = mvPos.xyz;
}