varying vec2 varyingTexCoord;
varying vec2 pos;

uniform float scale;

void main()
{
	varyingTexCoord = gl_MultiTexCoord0.xy;

	vec4 verpos = gl_ModelViewMatrix * gl_Vertex;
	pos = gl_Vertex.xy / scale;
	gl_Position = gl_ProjectionMatrix * verpos;
}