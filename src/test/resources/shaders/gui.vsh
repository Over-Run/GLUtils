#version 330

layout (location=0) in vec3 vert;
layout (location=1) in vec3 in_color;
out vec3 out_color;
uniform mat4 view;

void main() {
    gl_Position = view * vec4(vert, 1.0);
    out_color = in_color;
}