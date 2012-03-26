#version 120

varying vec4 color;
varying vec4 texcoord;
varying vec4 lmcoord;

attribute vec4 mc_Entity;

uniform int worldTime;

void main() {

	texcoord = gl_TextureMatrix[0] * gl_MultiTexCoord0;
	
	vec4 position = gl_Vertex;

	if (mc_Entity.x == 31.0 && texcoord.t < 0.15) {
		float magnitude = sin(worldTime * 3.14159265358979323846264 / 172.0) * 0.2;
		position.x += sin(worldTime * 3.14159265358979323846264 / 86.0) * magnitude;
		position.z += sin(worldTime * 3.14159265358979323846264 / 72.0) * magnitude;
	}

	gl_Position = gl_ProjectionMatrix * (gl_ModelViewMatrix * position);
		
	color = gl_Color;
	
	lmcoord = gl_TextureMatrix[1] * gl_MultiTexCoord1;
	
	gl_FogFragCoord = gl_Position.z;
}