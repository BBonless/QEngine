#version 400 core

layout(location = 0) in vec3 vertPosition;
layout(location = 1) in vec3 vertNormal;
layout(location = 2) in vec2 vertTexCoord;
layout(location = 3) in vec3 vertInstanceOffset;

uniform mat4 MVP;
uniform mat4 Model;

uniform vec3 CamUp;
uniform vec3 CamRight;

out vec3 fragPosition;
out vec2 fragTexCoord;

void main() {
    vec3 WorldspaceVertexPosition =
        vertInstanceOffset
        + CamRight * vertPosition.x
        + CamUp * vertPosition.y;
    gl_Position = MVP * vec4(WorldspaceVertexPosition, 1.0);

    fragPosition = vec3(Model * vec4(WorldspaceVertexPosition, 1.0));
    fragTexCoord = vertTexCoord;
}