#version 330

layout (location=0) in vec3 vert;
layout (location=1) in vec3 in_color;
layout (location=2) in vec2 in_tex;
out vec3 out_color;
out vec2 out_tex;
uniform mat4 proj, modelv;
uniform int textured;

void main() {
    gl_Position = proj * modelv * vec4(vert, 1.0);
    out_color = in_color;
    if (textured != 0) {
        out_tex = in_tex;
    }
}