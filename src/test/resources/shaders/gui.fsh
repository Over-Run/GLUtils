#version 330

in vec4 out_color;
in vec2 out_tex;
out vec4 fragColor;
uniform int textured;
uniform sampler2D texSampler;

void main() {
    fragColor = out_color;
    if (textured != 0) {
        fragColor *= texture(texSampler, out_tex);
    }
}