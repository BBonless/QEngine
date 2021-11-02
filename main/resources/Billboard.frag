#version 400 core

struct Material {
    vec3 Ambient;
    vec3 Diffuse;
    vec3 Specular;
    float Shininess;
};

struct LightSource {
    vec3 Position;
    vec3 Color;
};

in vec3 fragPosition;
in vec2 fragTexCoord;

uniform Material Mat;
uniform LightSource Light;

uniform vec3 viewPos;

uniform sampler2D Texture;

out vec4 outColor;

void main() {
    //Ambient
    vec3 AmbientColor = Mat.Ambient;

    //Diffuse
    vec3 DiffuseColor = vec3(0);

    //Specular;
    vec3 SpecularColor = vec3(0);

    outColor = texture(Texture, fragTexCoord) * vec4(AmbientColor + DiffuseColor + SpecularColor, 1.0);
}