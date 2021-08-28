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

in vec3 fragNormal;
in vec3 fragPosition;
in vec2 fragTexCoord;

uniform Material Mat;
uniform LightSource Light;

uniform vec3 viewPos;

uniform sampler2D Texture;

out vec4 outColor;

void main() {
    //Ambient
    vec3 AmbientColor = Light.Color * Mat.Ambient;

    //Diffuse
    vec3 Normal = normalize(fragNormal);
    vec3 LightDir = normalize(Light.Position - fragPosition);
    float Diff = max(dot(Normal, LightDir), 0.0);
    vec3 DiffuseColor = Light.Color * (Diff * Mat.Diffuse);

    //Specular
    vec3 ViewDir = normalize(viewPos - fragPosition);
    vec3 ReflectDir = reflect(-LightDir, Normal);
    float Spec = pow(max(dot(ViewDir, ReflectDir), 0.0), Mat.Shininess);
    vec3 SpecularColor = Light.Color * (Spec * Mat.Specular);

    outColor = texture(Texture, fragTexCoord) * vec4(AmbientColor + DiffuseColor + SpecularColor, 1.0);
}