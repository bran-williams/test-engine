#version 330
in vec3 position;

out vec3 passPosition;
out vec4 passViewSpace;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main() {
    passPosition = position;
    passViewSpace = viewMatrix * vec4(position, 1.0);

    gl_Position = projectionMatrix * viewMatrix * vec4(position, 1.0);
}