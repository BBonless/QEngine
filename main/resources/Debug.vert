#version 400 core

layout(location = 0) in vec3 vertPosition;
layout(location = 1) in vec3 vertNormal;
layout(location = 2) in vec2 vertTexCoord;
layout(location = 3) in vec3 vertInstanceOffset;

uniform mat4 MVP;
uniform mat4 Model;
uniform mat3 Normal;

out vec3 Color;

void main() {
    gl_Position = MVP * vec4(vertPosition + vertInstanceOffset, 1.0);

    Color = vertPosition;
}