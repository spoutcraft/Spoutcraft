#version 120

uniform sampler2D gcolor;
uniform sampler2D gdepth;

varying vec4 texcoord;

void main() {
	gl_FragData[0] = texture2D(gcolor, texcoord.st);
	gl_FragData[1] = texture2D(gdepth, texcoord.st);
	gl_FragData[3] = vec4(texture2D(gcolor, texcoord.st).rgb, 1.0);
}
