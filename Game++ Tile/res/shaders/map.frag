varying vec2 varyingTexCoord;
varying vec2 pos;

uniform sampler2D colorTex;
uniform sampler2D normalTex;
uniform vec2 offset;
uniform float scale;
uniform int lightsCount;
uniform float random;

uniform float lightsX[100];
uniform float lightsY[100];
uniform float lightsSize[100];
uniform float lightsIntensity[100];

vec3 nightColor=vec3(0.3, 0.6, 1.0);
vec3 torchColor=vec3(1.2, 0.6, 0.3);

float rand(vec2 co){
	co*=.000001+.0000002*co.y;
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

float light(vec2 lightPos, float size, float intensity){
	float distMult = max((1.0 - pow(distance(pos, lightPos - offset)/size, 3.0)), 0.0);
	float dirMult = max(dot((normalize(texture2D(normalTex, varyingTexCoord.xy).xyz - vec3(0.5, 0.5, 0.5))), normalize(vec3(pos-(lightPos - offset), 1.0+0.07*(rand(lightPos+vec2(random, 0.0)))))), 0.0);

	return dirMult * distMult * intensity;
}

vec3 lightColor(float bright){
	return mix(vec3(0.3, 0.6, 1.0), vec3(1.2, 0.6, 0.3), bright);
}

void main(){
	vec4 color = texture2D(colorTex, varyingTexCoord.xy);
	float bright = 0.0;

	for(int i=0;i<lightsCount;i++){
		bright+=light(vec2(lightsX[i], lightsY[i]), lightsSize[i], lightsIntensity[i]);
	}

	bright = clamp(bright, 0.2, 1.5);

	vec3 lightColor = lightColor(min(bright, 1.0)) * bright;

	gl_FragColor = vec4(color.rgb * lightColor, color.a);
}