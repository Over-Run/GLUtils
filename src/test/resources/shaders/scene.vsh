#version 330

layout (location=0) in vec3 vert;
layout (location=1) in vec3 in_color;
out vec3 out_color;
uniform mat4 proj, modelv;

void main() {
    gl_Position = proj * modelv * vec4(vert, 1.0);
    out_color = in_color;
}