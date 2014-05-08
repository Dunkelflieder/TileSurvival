varying vec2 varyingTexCoord;
varying vec2 pos;

uniform sampler2D colorTex;
uniform sampler2D normalTex;
uniform vec2 offset;
uniform float scale;


float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

float light(vec2 lightPos, float size){
	float distMult = max((1-pow(distance(pos, lightPos - offset)/size, 3.0)), 0.0);
	float dirMult = dot((normalize(texture2D(normalTex, varyingTexCoord.xy).xyz - vec3(0.5, 0.5, 0.5))), normalize(vec3(pos-(lightPos - offset), 1.0)));

	return dirMult * distMult;
}

vec3 lightColor(float bright){
	return mix(vec3(0.3, 0.6, 1.0), vec3(1.2, 0.6, 0.3), bright);
}

void main(){
	vec4 color = texture2D(colorTex, varyingTexCoord.xy);
	float bright = 0.0;

	bright = clamp(bright, 0.2, 1.5);

	bright = clamp({light}, 0.2, 1.5);
	gl_FragColor = vec4(color.rgb * lightColor(min(bright, 1.0)) * bright, color.a);
	//gl_FragColor = vec4(color.rgb, 1.0) * color.a;
	//gl_FragColor = vec4(lightColor(min(bright, 1.0)) * bright, color.a);
}