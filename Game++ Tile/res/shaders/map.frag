varying vec2 varyingTexCoord;
varying vec2 pos;

uniform sampler2D colorTex;
uniform vec2 offset;
uniform float scale;


float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

float light(vec2 lightPos, float size){
	return max(size-distance(pos, lightPos - offset)/size, 0.0);
}

vec3 lightColor(float bright){
	return mix(vec3(0.3, 0.6, 1.0), vec3(1.0, 0.6, 0.3), bright+0.0);
}

void main(){
	vec4 color = texture2D(colorTex, varyingTexCoord.xy);
	float bright = 0.0;

	bright = clamp(bright, 0.2, 1.5);

	//bright = clamp(light(vec2(6.5, 2.5)) + light(vec2(7.5, 5.5)), 0.2, 1.5);
	bright = clamp({light}, 0.2, 1.5);
	gl_FragColor = vec4(color.rgb * lightColor(min(bright, 1.0)) * bright, color.a);
}