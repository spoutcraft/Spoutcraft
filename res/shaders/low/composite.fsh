#version 120





//to increase shadow draw distance, edit SHADOWDISTANCE and SHADOWHPL below. Both should be equal. Needs decimal point.
//disabling is done by adding "//" to the beginning of a line.





//ADJUSTABLE VARIABLES

//#define HQ              //high quality. Only enable HQ or LQ, not both
#define LQ            //low quality. Only enable HQ or LQ, not both
#define BLURFACTOR 3.5
#define SHADOW_DARKNESS 1.10   // 1.0 Is defualt darkness. 2.0 is black shadows. 0.0 is no shadows.
#define SHADOWDISTANCE 64.0 
//#define SHADOW_FILTER

/* SHADOWRES:1024 */
/* SHADOWHPL:64.0 */

//#define SSAO
#define SSAO_LUMINANCE 0.0				// At what luminance will SSAO's shadows become highlights.
#define SSAO_STRENGTH 1.60               // Too much strength causes white highlights on extruding edges and behind objects
#define SSAO_LOOP 1						// Integer affecting samples that are taken to calculate SSAO. Higher values mean more accurate shadowing but bigger performance impact
#define SSAO_NOISE false					// Randomize SSAO sample gathering. With noise enabled and SSAO_LOOP set to 1, you will see higher performance at the cost of fuzzy dots in shaded areas.
#define SSAO_NOISE_AMP 0.5					// Multiplier of noise. Higher values mean SSAO takes random samples from a larger radius. Big performance hit at higher values.
#define SSAO_MAX_DEPTH 0.5				// View distance of SSAO
#define SSAO_SAMPLE_DELTA 0.4			// Radius of SSAO shadows. Higher values cause more performance hit.
#define CORRECTSHADOWCOLORS				// Colors sunlight and ambient light correctly according to real-life.
#define SHADOWOFFSET 0.4				// Shadow offset multiplier. Values that are too low will cause artefacts.
//#define FXAA							// FXAA shader. Broken, but you can give it a try if you want.

//END OF ADJUSTABLE VARIABLES




uniform sampler2D gcolor;
uniform sampler2D gdepth;
uniform sampler2D composite;
uniform sampler2D shadow;
uniform sampler2D gaux1;

varying vec4 texcoord;
varying vec4 lmcoord;

uniform int worldTime;

uniform mat4 gbufferProjectionInverse;
uniform mat4 gbufferModelViewInverse;
uniform mat4 shadowProjection;
uniform mat4 shadowModelView;

uniform float near;
uniform float far;
uniform float viewWidth;
uniform float viewHeight;
uniform float rainStrength;

// Standard depth function.
float getDepth(vec2 coord) {
    return 2.0 * near * far / (far + near - (2.0 * texture2D(gdepth, coord).x - 1.0) * (far - near));
}


#ifdef SSAO


// Alternate projected depth (used by SSAO, probably AA too)
float getProDepth( vec2 coord ) {
	float depth = texture2D(gdepth, coord).x;
	return ( 2.0 * near ) / ( far + near - depth * ( far - near ) );
}

float znear = near; //Z-near
float zfar = far; //Z-far

float diffarea = 0.6; //self-shadowing reduction
float gdisplace = 0.30; //gauss bell center

bool noise = SSAO_NOISE; //use noise instead of pattern for sample dithering?
bool onlyAO = false; //use only ambient occlusion pass?

vec2 texCoord = texcoord.st;


vec2 rand(vec2 coord) { //generating noise/pattern texture for dithering
  float width = 1.0;
  float height = 1.0;
  float noiseX = ((fract(1.0-coord.s*(width/2.0))*0.25)+(fract(coord.t*(height/2.0))*0.75))*2.0-1.0;
  float noiseY = ((fract(1.0-coord.s*(width/2.0))*0.75)+(fract(coord.t*(height/2.0))*0.25))*2.0-1.0;

  if (noise) {
    noiseX = clamp(fract(sin(dot(coord ,vec2(12.9898,78.233))) * 43758.5453),0.0,1.0)*2.0-1.0;
    noiseY = clamp(fract(sin(dot(coord ,vec2(12.9898,78.233)*2.0)) * 43758.5453),0.0,1.0)*2.0-1.0;
  }
  return vec2(noiseX,noiseY)*0.002*SSAO_NOISE_AMP;
}


float compareDepths(float depth1, float depth2, int zfar) {  
  float garea = 1.5; //gauss bell width    
  float diff = (depth1 - depth2) * 100.0; //depth difference (0-100)
  //reduce left bell width to avoid self-shadowing 
  if (diff < gdisplace) {
    garea = diffarea;
  } else {
    zfar = 1;
  }

  float gauss = pow(2.7182,-2.0*(diff-gdisplace)*(diff-gdisplace)/(garea*garea));
  return gauss;
} 

float calAO(float depth, float dw, float dh) {  
  float temp = 0;
  float temp2 = 0;
  float coordw = texCoord.x + dw/depth;
  float coordh = texCoord.y + dh/depth;
  float coordw2 = texCoord.x - dw/depth;
  float coordh2 = texCoord.y - dh/depth;

  if (coordw  < 1.0 && coordw  > 0.0 && coordh < 1.0 && coordh  > 0.0){
    vec2 coord = vec2(coordw , coordh);
    vec2 coord2 = vec2(coordw2, coordh2);
    int zfar = 0;
    temp = compareDepths(depth, getProDepth(coord),zfar);

    //DEPTH EXTRAPOLATION:
    if (zfar > 0){
      temp2 = compareDepths(getProDepth(coord2),depth,zfar);
      temp += (1.0-temp)*temp2; 
    }
  }

  return temp;  
}  






float getSSAOFactor() {
	vec2 noise = rand(texCoord); 
	float depth = getProDepth(texCoord);
  if (depth > SSAO_MAX_DEPTH) {
    return 1.0;
  }
  float cdepth = texture2D(gdepth,texCoord).g;
	
	float ao;
	float s;
	
  float incx = 1.0 / viewWidth * SSAO_SAMPLE_DELTA;
  float incy = 1.0 / viewHeight * SSAO_SAMPLE_DELTA;
  float pw = incx;
  float ph = incy;
  float aoMult = SSAO_STRENGTH;
  int aaLoop = SSAO_LOOP;
  float aaDiff = (1.0 + 2.0 / aaLoop);
  for (int i = 0; i < aaLoop ; i++) {
    float npw  = (pw + 0.2 * noise.x) / cdepth;
    float nph  = (ph + 0.2 * noise.y) / cdepth;

    ao += calAO(depth, npw, nph) * aoMult;
    ao += calAO(depth, npw, -nph) * aoMult;
    ao += calAO(depth, -npw, nph) * aoMult;
    ao += calAO(depth, -npw, -nph) * aoMult;
	
	 ao += calAO(depth, 2.0*npw, 2.0*nph) * aoMult/2.0;
    ao += calAO(depth, 2.0*npw, -2.0*nph) * aoMult/2.0;
    ao += calAO(depth, -2.0*npw, 2.0*nph) * aoMult/2.0;
    ao += calAO(depth, -2.0*npw, -2.0*nph) * aoMult/2.0;
	
	 ao += calAO(depth, 3.0*npw, 3.0*nph) * aoMult/3.0;
    ao += calAO(depth, 3.0*npw, -3.0*nph) * aoMult/3.0;
    ao += calAO(depth, -3.0*npw, 3.0*nph) * aoMult/3.0;
    ao += calAO(depth, -3.0*npw, -3.0*nph) * aoMult/3.0;
	
	 ao += calAO(depth, 4.0*npw, 4.0*nph) * aoMult/4.0;
    ao += calAO(depth, 4.0*npw, -4.0*nph) * aoMult/4.0;
    ao += calAO(depth, -4.0*npw, 4.0*nph) * aoMult/4.0;
    ao += calAO(depth, -4.0*npw, -4.0*nph) * aoMult/4.0;
	
    pw += incx*4.0;
    ph += incy*4.0;
    aoMult /= aaDiff; 
    s += 16.0;
  }
	
	ao /= s;
	ao = 1.0-ao;	
  ao = clamp(ao, 0.0, 0.5) * 2.0;
	
  return ao;
}

#endif



#ifdef FXAA

vec4 fxaa() { 
	float pw = 1.0 / viewWidth;
	float ph = 1.0 / viewHeight;
	
	vec3 rgbNW = texture2D(gcolor, texcoord.xy + vec2(-pw,-ph)).xyz;
	vec3 rgbNE = texture2D(gcolor, texcoord.xy + vec2(pw,-ph)).xyz;
	vec3 rgbSW = texture2D(gcolor, texcoord.xy + vec2(-pw,ph)).xyz;
	vec3 rgbSE = texture2D(gcolor, texcoord.xy + vec2(pw,ph)).xyz;
	vec3 rgbM =  texture2D(gcolor, texcoord.xy).xyz;
	
	vec3 luma = vec3(0.299, 0.587, 0.114);
	float lumaNW = dot(rgbNW, luma);
	float lumaNE = dot(rgbNE, luma);
	float lumaSW = dot(rgbSW, luma);
	float lumaSE = dot(rgbSE, luma);
	float lumaM = dot(rgbM, luma);
	
	float lumaMin = min(lumaM, min(min(lumaNW,lumaNE),min(lumaSW,lumaSE)));
	float lumaMax = max(lumaM, max(max(lumaNW,lumaNE),max(lumaSW,lumaSE)));
	
	vec2 dir;
	dir.x = -((lumaNW + lumaNE) - (lumaSW + lumaSE));
	dir.y = ((lumaNW + lumaSW) - (lumaNE + lumaSE));
	
	float dirReduce = max((lumaNW + lumaNE + lumaSW + lumaSE) * (0.25 * 1.0/8.0), 1.0/128.0);
	
	float rcpDirMin = 1.0 /(min(abs(dir.x), abs(dir.y)) + dirReduce);
	
	dir = min(vec2(4.0,4.0),max(vec2(-4.0,-4.0),dir * rcpDirMin)) * vec2(pw,ph);
	
	vec3 rgbA = (1.0/2.0) * (texture2D(gcolor, texcoord.xy + dir * (1.0/3,0 - 0.5)).xyz + 
							texture2D(gcolor, texcoord.xy + dir * (2.0/3.0 - 0.5)).xyz);
							
	vec3 rgbB = rgbA * (1.0/2.0) + (1.0/4.0) * (texture2D(gcolor, texcoord.xy + dir * (0.0/3.0 - 0.5)).xyz + 
												texture2D(gcolor, texcoord.xy + dir * (3.0/3.0 - 0.5)).xyz);
	
	float lumaB = dot(rgbB, luma);
	
	if((lumaB < lumaMin) || (lumaB > lumaMax)) {
		return vec4(rgbA , 1.0);
	}
	return vec4(rgbB, 1.0);
}



#endif











void main() {
	vec4 fragposition = gbufferProjectionInverse * vec4(texcoord.s * 2.0 - 1.0, texcoord.t * 2.0 - 1.0, 2.0 * texture2D(gdepth, texcoord.st).x - 1.0, 1.0);
	fragposition /= fragposition.w;
	
	#ifdef SHADOWDISTANCE
	float drawdistance = SHADOWDISTANCE;
	float drawdistancesquared = pow(drawdistance, 2);
	#endif
	
	float land = texture2D(gaux1, texcoord.st).b;
	float distance = sqrt(fragposition.x * fragposition.x + fragposition.y * fragposition.y + fragposition.z * fragposition.z);

	float shading = 1.0;
//
	if (distance < drawdistance && distance > 0.1) {
		// shadows
		vec4 worldposition = gbufferModelViewInverse * fragposition;

		float xzDistanceSquared = worldposition.x * worldposition.x + worldposition.z * worldposition.z;
		float yDistanceSquared  = worldposition.y * worldposition.y;
		
		if (yDistanceSquared < drawdistancesquared) {
			
			worldposition = shadowModelView * worldposition;
			float comparedepth = -worldposition.z;
			worldposition = shadowProjection * worldposition;
			worldposition /= worldposition.w;
			
			worldposition.st = worldposition.st * 0.5 + 0.5;
				
			if (comparedepth > 0.0 && worldposition.s < 1.0 && worldposition.s > 0.0 && worldposition.t < 1.0 && worldposition.t > 0.0){
				float shadowMult = min(1.0 - xzDistanceSquared / drawdistancesquared, 1.0) * min(1.0 - yDistanceSquared / drawdistancesquared, 1.0);
				float sampleDistance = 1.0 / 2048.0;
					
					
					
				#ifdef HQ
				
					
					float zoffset = 0.0;
					float offsetx = 0.0000*BLURFACTOR*SHADOWOFFSET;
					float offsety = 0.0004*BLURFACTOR*SHADOWOFFSET;
					
					float shadowdarkness = 0.50*SHADOW_DARKNESS;
					float diffthresh = 0.9;
					float bluramount = 0.000055*BLURFACTOR;
					
					
					float xfade = shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0.0*bluramount+offsetx, 0.0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset);
							//xfade = min(xfade, shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1.0*bluramount+offsetx, 1.0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset));
							//xfade = min(xfade, shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1.0*bluramount+offsetx, 1.0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset));
							//xfade = min(xfade, shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1.0*bluramount+offsetx, -1.0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset));
							//xfade = min(xfade, shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1.0*bluramount+offsetx, -1.0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset));
							
							float bluramount2 = 0.0009;
							
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1.0*bluramount2+offsetx, 1.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1.0*bluramount2+offsetx, 1.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1.0*bluramount2+offsetx, -1.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1.0*bluramount2+offsetx, -1.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0.0*bluramount2+offsetx, 1.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0.0*bluramount2+offsetx, -1.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1.0*bluramount2+offsetx, 0.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1.0*bluramount2+offsetx, 0.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							
							/*
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2.0*bluramount2+offsetx, 2.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-2.0*bluramount2+offsetx, 2.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2.0*bluramount2+offsetx, -2.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-2.0*bluramount2+offsetx, -2.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);

							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0.0*bluramount2+offsetx, 2.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0.0*bluramount2+offsetx, -2.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2.0*bluramount2+offsetx, 0.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-2.0*bluramount2+offsetx, 0.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							*/
							
							xfade = pow(xfade, 1.0);
							xfade = clamp((xfade - 0.007), 0.0, 1.0);
						
						
							//xfade /= 16.0;
							//xfade = clamp(xfade, 0.0, 1.0);
							
														//xfade = pow(xfade, 0.75);
							
							float xfadescale = xfade * 15.0;
							

							
							//if (xfade < 0.04) {
							//	xfade = 3.0;
							//}
							
					bluramount *= xfadescale;
					
					
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1*bluramount+offsetx, 0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1*bluramount+offsetx, 1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0*bluramount+offsetx, 1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0*bluramount+offsetx, 0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2*bluramount+offsetx, 0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2*bluramount+offsetx, 1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2*bluramount+offsetx, 2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1*bluramount+offsetx, 2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0*bluramount+offsetx, 2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1*bluramount+offsetx, 2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1*bluramount+offsetx, 1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1*bluramount+offsetx, 0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1*bluramount+offsetx, -1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0*bluramount+offsetx, -1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1*bluramount+offsetx, -1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2*bluramount+offsetx, -1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(3*bluramount+offsetx, -1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(3*bluramount+offsetx, 0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(3*bluramount+offsetx, 1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(3*bluramount+offsetx, 2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(3*bluramount+offsetx, 3*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2*bluramount+offsetx, 3*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1*bluramount+offsetx, 3*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0*bluramount+offsetx, 3*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1*bluramount+offsetx, 3*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-2*bluramount+offsetx, 3*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-2*bluramount+offsetx, 2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-2*bluramount+offsetx, 1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-2*bluramount+offsetx, 0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-2*bluramount+offsetx, -1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-2*bluramount+offsetx, -2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1*bluramount+offsetx, -2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0*bluramount+offsetx, -2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1*bluramount+offsetx, -2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2*bluramount+offsetx, -2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(3*bluramount+offsetx, -2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(4*bluramount+offsetx, -2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(4*bluramount+offsetx, -1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(4*bluramount+offsetx, 0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(4*bluramount+offsetx, 1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(4*bluramount+offsetx, 2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(4*bluramount+offsetx, 3*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(4*bluramount+offsetx, 4*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(3*bluramount+offsetx, 4*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2*bluramount+offsetx, 4*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1*bluramount+offsetx, 4*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0*bluramount+offsetx, 4*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1*bluramount+offsetx, 4*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-2*bluramount+offsetx, 4*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-3*bluramount+offsetx, 4*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-3*bluramount+offsetx, 3*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-3*bluramount+offsetx, 2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-3*bluramount+offsetx, 1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-3*bluramount+offsetx, 0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-3*bluramount+offsetx, -1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-3*bluramount+offsetx, -2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-3*bluramount+offsetx, -3*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-2*bluramount+offsetx, -3*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1*bluramount+offsetx, -3*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0*bluramount+offsetx, -3*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1*bluramount+offsetx, -3*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2*bluramount+offsetx, -3*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(3*bluramount+offsetx, -3*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(4*bluramount+offsetx, -3*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(5*bluramount+offsetx, -1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(5*bluramount+offsetx, 0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(5*bluramount+offsetx, 1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(5*bluramount+offsetx, 2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2*bluramount+offsetx, 5*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1*bluramount+offsetx, 5*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0*bluramount+offsetx, 5*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1*bluramount+offsetx, 5*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-4*bluramount+offsetx, 2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-4*bluramount+offsetx, 1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-4*bluramount+offsetx, 0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-4*bluramount+offsetx, -1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1*bluramount+offsetx, -4*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0*bluramount+offsetx, -4*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);

					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1*bluramount+offsetx, -4*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/80 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2*bluramount+offsetx, -4*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);

					shading = (shading/80 + 0.97);
					
					shading = clamp((shading), (1.0 - 0.5*SHADOW_DARKNESS), 1.0);
					
					
					//shading = shading2;
					
					if (land < 0.5) {
					shading = 1.0;
					}
					
					
					
				#endif
					
				#ifdef LQ
				
					float zoffset = 0.00;
					float offsetx = 0.0000*BLURFACTOR*SHADOWOFFSET;
					float offsety = 0.0004*BLURFACTOR*SHADOWOFFSET;
				
					
					//shadow filtering
					
					float step = 1.0/2048.0;
					float shadowdarkness = 0.5*SHADOW_DARKNESS;
					float diffthresh = 0.95;
					float bluramount = 0.00009*BLURFACTOR;
					
					
					
					
					
					
					shading += -0.02 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1*bluramount+offsetx, 1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);

				#ifdef SHADOW_FILTER
				
						shading = 1.0;
				
						vec2 Texcoord2 = texcoord.st;
					
					
						float width2 = 1.0;
						float height2 = 1.0;
						float noiseX2 = ((fract(1.0-Texcoord2.s*(width2/2.0))*0.25)+(fract(Texcoord2.t*(height2/2.0))*0.75))*2.0-1.0;
						float noiseY2 = ((fract(1.0-Texcoord2.s*(width2/2.0))*0.75)+(fract(Texcoord2.t*(height2/2.0))*0.25))*2.0-1.0;

						
							noiseX2 = clamp(fract(sin(dot(Texcoord2 ,vec2(12.9898,78.233))) * 43758.5453),0.0,1.0)*2.0-1.0;
							noiseY2 = clamp(fract(sin(dot(Texcoord2 ,vec2(12.9898,78.233)*2.0)) * 43758.5453),0.0,1.0)*2.0-1.0;
						
						noiseX2 *= 0.002;
						noiseY2 *= 0.002;
						
						float width3 = 2.0;
						float height3 = 2.0;
						float noiseX3 = ((fract(1.0-Texcoord2.s*(width3/2.0))*0.25)+(fract(Texcoord2.t*(height3/2.0))*0.75))*2.0-1.0;
						float noiseY3 = ((fract(1.0-Texcoord2.s*(width3/2.0))*0.75)+(fract(Texcoord2.t*(height3/2.0))*0.25))*2.0-1.0;

						
							noiseX3 = clamp(fract(sin(dot(Texcoord2 ,vec2(18.9898,28.633))) * 4378.5453),0.0,1.0)*2.0-1.0;
							noiseY3 = clamp(fract(sin(dot(Texcoord2 ,vec2(11.9898,59.233)*2.0)) * 3758.5453),0.0,1.0)*2.0-1.0;
						
						noiseX3 *= 0.002;
						noiseY3 *= 0.002;
						
						float width4 = 3.0;
						float height4 = 3.0;
						float noiseX4 = ((fract(1.0-Texcoord2.s*(width4/2.0))*0.25)+(fract(Texcoord2.t*(height4/2.0))*0.75))*2.0-1.0;
						float noiseY4 = ((fract(1.0-Texcoord2.s*(width4/2.0))*0.75)+(fract(Texcoord2.t*(height4/2.0))*0.25))*2.0-1.0;

						
							noiseX4 = clamp(fract(sin(dot(Texcoord2 ,vec2(16.9898,38.633))) * 41178.5453),0.0,1.0)*2.0-1.0;
							noiseY4 = clamp(fract(sin(dot(Texcoord2 ,vec2(21.9898,66.233)*2.0)) * 9758.5453),0.0,1.0)*2.0-1.0;
						
						noiseX4 *= 0.002;
						noiseY4 *= 0.002;
						
						float width5 = 4.0;
						float height5 = 4.0;
						float noiseX5 = ((fract(1.0-Texcoord2.s*(width5/2.0))*0.25)+(fract(Texcoord2.t*(height5/2.0))*0.75))*2.0-1.0;
						float noiseY5 = ((fract(1.0-Texcoord2.s*(width5/2.0))*0.75)+(fract(Texcoord2.t*(height5/2.0))*0.25))*2.0-1.0;

						
							noiseX5 = clamp(fract(sin(dot(Texcoord2 ,vec2(11.9898,68.633))) * 21178.5453),0.0,1.0)*2.0-1.0;
							noiseY5 = clamp(fract(sin(dot(Texcoord2 ,vec2(26.9898,71.233)*2.0)) * 6958.5453),0.0,1.0)*2.0-1.0;
						
						noiseX5 *= 0.002;
						noiseY5 *= 0.002;
						
						float width6 = 5.0;
						float height6 = 5.0;
						float noiseX6 = ((fract(1.0-Texcoord2.s*(width6/2.0))*0.25)+(fract(Texcoord2.t*(height6/2.0))*0.75))*2.0-1.0;
						float noiseY6 = ((fract(1.0-Texcoord2.s*(width6/2.0))*0.75)+(fract(Texcoord2.t*(height6/2.0))*0.25))*2.0-1.0;

						
							noiseX6 = clamp(fract(sin(dot(Texcoord2 ,vec2(15.9898,24.633))) * 31178.5453),0.0,1.0)*2.0-1.0;
							noiseY6 = clamp(fract(sin(dot(Texcoord2 ,vec2(23.9898,51.233)*2.0)) * 8958.5453),0.0,1.0)*2.0-1.0;
						
						noiseX6 *= 0.002;
						noiseY6 *= 0.002;
						
						float width7 = 6.0;
						float height7 = 6.0;
						float noiseX7 = ((fract(1.0-Texcoord2.s*(width7/2.0))*0.25)+(fract(Texcoord2.t*(height7/2.0))*0.75))*2.0-1.0;
						float noiseY7 = ((fract(1.0-Texcoord2.s*(width7/2.0))*0.75)+(fract(Texcoord2.t*(height7/2.0))*0.25))*2.0-1.0;

						
							noiseX7 = clamp(fract(sin(dot(Texcoord2 ,vec2(12.9898,44.633))) * 51178.5453),0.0,1.0)*2.0-1.0;
							noiseY7 = clamp(fract(sin(dot(Texcoord2 ,vec2(43.9898,61.233)*2.0)) * 9958.5453),0.0,1.0)*2.0-1.0;
						
						noiseX7 *= 0.002;
						noiseY7 *= 0.002;
						
						float width8 = 7.0;
						float height8 = 7.0;
						float noiseX8 = ((fract(1.0-Texcoord2.s*(width8/2.0))*0.25)+(fract(Texcoord2.t*(height8/2.0))*0.75))*2.0-1.0;
						float noiseY8 = ((fract(1.0-Texcoord2.s*(width8/2.0))*0.75)+(fract(Texcoord2.t*(height8/2.0))*0.25))*2.0-1.0;

						
							noiseX8 = clamp(fract(sin(dot(Texcoord2 ,vec2(14.9898,47.633))) * 51468.5453),0.0,1.0)*2.0-1.0;
							noiseY8 = clamp(fract(sin(dot(Texcoord2 ,vec2(13.9898,81.233)*2.0)) * 6388.5453),0.0,1.0)*2.0-1.0;
						
						noiseX8 *= 0.002;
						noiseY8 *= 0.002;
						
						float width9 = 8.0;
						float height9 = 8.0;
						float noiseX9 = ((fract(1.0-Texcoord2.s*(width9/2.0))*0.25)+(fract(Texcoord2.t*(height9/2.0))*0.75))*2.0-1.0;
						float noiseY9 = ((fract(1.0-Texcoord2.s*(width9/2.0))*0.75)+(fract(Texcoord2.t*(height9/2.0))*0.25))*2.0-1.0;

						
							noiseX9 = clamp(fract(sin(dot(Texcoord2 ,vec2(24.9898,59.633))) * 55468.5453),0.0,1.0)*2.0-1.0;
							noiseY9 = clamp(fract(sin(dot(Texcoord2 ,vec2(23.9898,95.233)*2.0)) * 16388.5453),0.0,1.0)*2.0-1.0;
						
						noiseX9 *= 0.002;
						noiseY9 *= 0.002;
						
						float width10 = 9.0;
						float height10 = 9.0;
						float noiseX10 = ((fract(1.0-Texcoord2.s*(width10/2.0))*0.25)+(fract(Texcoord2.t*(height10/2.0))*0.75))*2.0-1.0;
						float noiseY10 = ((fract(1.0-Texcoord2.s*(width10/2.0))*0.75)+(fract(Texcoord2.t*(height10/2.0))*0.25))*2.0-1.0;

						
							noiseX10 = clamp(fract(sin(dot(Texcoord2 ,vec2(26.9898,59.633))) * 57468.5453),0.0,1.0)*2.0-1.0;
							noiseY10 = clamp(fract(sin(dot(Texcoord2 ,vec2(25.9898,95.233)*2.0)) * 18388.5453),0.0,1.0)*2.0-1.0;
						
						noiseX10 *= 0.002;
						noiseY10 *= 0.002;
					
						float width11 = 10.0;
						float height11 = 10.0;
						float noiseX11 = ((fract(1.0-Texcoord2.s*(width11/2.0))*0.25)+(fract(Texcoord2.t*(height11/2.0))*0.75))*2.0-1.0;
						float noiseY11 = ((fract(1.0-Texcoord2.s*(width11/2.0))*0.75)+(fract(Texcoord2.t*(height11/2.0))*0.25))*2.0-1.0;

						
							noiseX11 = clamp(fract(sin(dot(Texcoord2 ,vec2(28.9898,61.633))) * 59468.5453),0.0,1.0)*2.0-1.0;
							noiseY11 = clamp(fract(sin(dot(Texcoord2 ,vec2(26.9898,97.233)*2.0)) * 21388.5453),0.0,1.0)*2.0-1.0;
						
						noiseX11 *= 0.002;
						noiseY11 *= 0.002;
						
						float width12 = 11.0;
						float height12 = 11.0;
						float noiseX12 = ((fract(1.0-Texcoord2.s*(width12/2.0))*0.25)+(fract(Texcoord2.t*(height12/2.0))*0.75))*2.0-1.0;
						float noiseY12 = ((fract(1.0-Texcoord2.s*(width12/2.0))*0.75)+(fract(Texcoord2.t*(height12/2.0))*0.25))*2.0-1.0;

						
							noiseX12 = clamp(fract(sin(dot(Texcoord2 ,vec2(30.9898,64.633))) * 61468.5453),0.0,1.0)*2.0-1.0;
							noiseY12 = clamp(fract(sin(dot(Texcoord2 ,vec2(34.9898,99.233)*2.0)) * 23388.5453),0.0,1.0)*2.0-1.0;
						
						noiseX12 *= 0.002;
						noiseY12 *= 0.002;		

						float width13 = 12.0;
						float height13 = 12.0;
						float noiseX13 = ((fract(1.0-Texcoord2.s*(width13/2.0))*0.25)+(fract(Texcoord2.t*(height13/2.0))*0.75))*2.0-1.0;
						float noiseY13 = ((fract(1.0-Texcoord2.s*(width13/2.0))*0.75)+(fract(Texcoord2.t*(height13/2.0))*0.25))*2.0-1.0;

						
							noiseX13 = clamp(fract(sin(dot(Texcoord2 ,vec2(32.9898,66.633))) * 63468.5453),0.0,1.0)*2.0-1.0;
							noiseY13 = clamp(fract(sin(dot(Texcoord2 ,vec2(36.9898,101.233)*2.0)) * 25388.5453),0.0,1.0)*2.0-1.0;
						
						noiseX13 *= 0.002;
						noiseY13 *= 0.002;		

						float width14 = 13.0;
						float height14 = 13.0;
						float noiseX14 = ((fract(1.0-Texcoord2.s*(width14/2.0))*0.25)+(fract(Texcoord2.t*(height14/2.0))*0.75))*2.0-1.0;
						float noiseY14 = ((fract(1.0-Texcoord2.s*(width14/2.0))*0.75)+(fract(Texcoord2.t*(height14/2.0))*0.25))*2.0-1.0;

						
							noiseX14 = clamp(fract(sin(dot(Texcoord2 ,vec2(34.9898,68.633))) * 65468.5453),0.0,1.0)*2.0-1.0;
							noiseY14 = clamp(fract(sin(dot(Texcoord2 ,vec2(38.9898,103.233)*2.0)) * 27388.5453),0.0,1.0)*2.0-1.0;
						
						noiseX14 *= 0.002;
						noiseY14 *= 0.002;	

						float width15 = 14.0;
						float height15 = 14.0;
						float noiseX15 = ((fract(1.0-Texcoord2.s*(width15/2.0))*0.25)+(fract(Texcoord2.t*(height15/2.0))*0.75))*2.0-1.0;
						float noiseY15 = ((fract(1.0-Texcoord2.s*(width15/2.0))*0.75)+(fract(Texcoord2.t*(height15/2.0))*0.25))*2.0-1.0;

						
							noiseX15 = clamp(fract(sin(dot(Texcoord2 ,vec2(36.9898,70.633))) * 67468.5453),0.0,1.0)*2.0-1.0;
							noiseY15 = clamp(fract(sin(dot(Texcoord2 ,vec2(40.9898,105.233)*2.0)) * 29388.5453),0.0,1.0)*2.0-1.0;
						
						noiseX15 *= 0.002;
						noiseY15 *= 0.002;	

						float width16 = 15.0;
						float height16 = 15.0;
						float noiseX16 = ((fract(1.0-Texcoord2.s*(width16/2.0))*0.25)+(fract(Texcoord2.t*(height16/2.0))*0.75))*2.0-1.0;
						float noiseY16 = ((fract(1.0-Texcoord2.s*(width16/2.0))*0.75)+(fract(Texcoord2.t*(height16/2.0))*0.25))*2.0-1.0;

						
							noiseX16 = clamp(fract(sin(dot(Texcoord2 ,vec2(38.9898,72.633))) * 69468.5453),0.0,1.0)*2.0-1.0;
							noiseY16 = clamp(fract(sin(dot(Texcoord2 ,vec2(42.9898,107.233)*2.0)) * 31388.5453),0.0,1.0)*2.0-1.0;
						
						noiseX16 *= 0.002;
						noiseY16 *= 0.002;		

						float width17 = 16.0;
						float height17 = 16.0;
						float noiseX17 = ((fract(1.0-Texcoord2.s*(width17/2.0))*0.25)+(fract(Texcoord2.t*(height17/2.0))*0.75))*2.0-1.0;
						float noiseY17 = ((fract(1.0-Texcoord2.s*(width17/2.0))*0.75)+(fract(Texcoord2.t*(height17/2.0))*0.25))*2.0-1.0;

						
							noiseX17 = clamp(fract(sin(dot(Texcoord2 ,vec2(40.9898,74.633))) * 70468.5453),0.0,1.0)*2.0-1.0;
							noiseY17 = clamp(fract(sin(dot(Texcoord2 ,vec2(44.9898,109.233)*2.0)) * 33388.5453),0.0,1.0)*2.0-1.0;
						
						noiseX17 *= 0.002;
						noiseY17 *= 0.002;

						float width18 = 17.0;
						float height18 = 17.0;
						float noiseX18 = ((fract(1.0-Texcoord2.s*(width18/2.0))*0.25)+(fract(Texcoord2.t*(height18/2.0))*0.75))*2.0-1.0;
						float noiseY18 = ((fract(1.0-Texcoord2.s*(width18/2.0))*0.75)+(fract(Texcoord2.t*(height18/2.0))*0.25))*2.0-1.0;

						
							noiseX18 = clamp(fract(sin(dot(Texcoord2 ,vec2(42.9898,76.633))) * 72468.5453),0.0,1.0)*2.0-1.0;
							noiseY18 = clamp(fract(sin(dot(Texcoord2 ,vec2(46.9898,111.233)*2.0)) * 35388.5453),0.0,1.0)*2.0-1.0;
						
						noiseX18 *= 0.002;
						noiseY18 *= 0.002;	

						float width19 = 18.0;
						float height19 = 18.0;
						float noiseX19 = ((fract(1.0-Texcoord2.s*(width19/2.0))*0.25)+(fract(Texcoord2.t*(height19/2.0))*0.75))*2.0-1.0;
						float noiseY19 = ((fract(1.0-Texcoord2.s*(width19/2.0))*0.75)+(fract(Texcoord2.t*(height19/2.0))*0.25))*2.0-1.0;

						
							noiseX19 = clamp(fract(sin(dot(Texcoord2 ,vec2(44.9898,78.633))) * 75468.5453),0.0,1.0)*2.0-1.0;
							noiseY19 = clamp(fract(sin(dot(Texcoord2 ,vec2(48.9898,115.233)*2.0)) * 38388.5453),0.0,1.0)*2.0-1.0;
						
						noiseX19 *= 0.002;
						noiseY19 *= 0.002;		

						float width20 = 19.0;
						float height20 = 19.0;
						float noiseX20 = ((fract(1.0-Texcoord2.s*(width20/2.0))*0.25)+(fract(Texcoord2.t*(height20/2.0))*0.75))*2.0-1.0;
						float noiseY20 = ((fract(1.0-Texcoord2.s*(width20/2.0))*0.75)+(fract(Texcoord2.t*(height20/2.0))*0.25))*2.0-1.0;

						
							noiseX20 = clamp(fract(sin(dot(Texcoord2 ,vec2(46.9898,81.633))) * 77468.5453),0.0,1.0)*2.0-1.0;
							noiseY20 = clamp(fract(sin(dot(Texcoord2 ,vec2(51.9898,118.233)*2.0)) * 41188.5453),0.0,1.0)*2.0-1.0;
						
						noiseX20 *= 0.002;
						noiseY20 *= 0.002;		

						float width21 = 20.0;
						float height21 = 20.0;
						float noiseX21 = ((fract(1.0-Texcoord2.s*(width21/2.0))*0.25)+(fract(Texcoord2.t*(height21/2.0))*0.75))*2.0-1.0;
						float noiseY21 = ((fract(1.0-Texcoord2.s*(width21/2.0))*0.75)+(fract(Texcoord2.t*(height21/2.0))*0.25))*2.0-1.0;

						
							noiseX21 = clamp(fract(sin(dot(Texcoord2 ,vec2(48.9898,83.633))) * 79468.5453),0.0,1.0)*2.0-1.0;
							noiseY21 = clamp(fract(sin(dot(Texcoord2 ,vec2(53.9898,120.233)*2.0)) * 43188.5453),0.0,1.0)*2.0-1.0;
						
						noiseX21 *= 0.002;
						noiseY21 *= 0.002;	

						float width22 = 21.0;
						float height22 = 21.0;
						float noiseX22 = ((fract(1.0-Texcoord2.s*(width22/2.0))*0.25)+(fract(Texcoord2.t*(height22/2.0))*0.75))*2.0-1.0;
						float noiseY22 = ((fract(1.0-Texcoord2.s*(width22/2.0))*0.75)+(fract(Texcoord2.t*(height22/2.0))*0.25))*2.0-1.0;

						
							noiseX22 = clamp(fract(sin(dot(Texcoord2 ,vec2(51.9898,83.633))) * 81468.5453),0.0,1.0)*2.0-1.0;
							noiseY22 = clamp(fract(sin(dot(Texcoord2 ,vec2(56.9898,120.233)*2.0)) * 48188.5453),0.0,1.0)*2.0-1.0;
						
						noiseX22 *= 0.002;
						noiseY22 *= 0.002;						

					
					
					
					
					
					float xfade = shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0.0*bluramount+offsetx, 0.0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset);
							//xfade = min(xfade, shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1.0*bluramount+offsetx, 1.0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset));
							//xfade = min(xfade, shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1.0*bluramount+offsetx, 1.0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset));
							//xfade = min(xfade, shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1.0*bluramount+offsetx, -1.0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset));
							//xfade = min(xfade, shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1.0*bluramount+offsetx, -1.0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset));
							
							float bluramount2 = 0.0019;
							
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1.0*bluramount2+offsetx, 1.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1.0*bluramount2+offsetx, 1.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1.0*bluramount2+offsetx, -1.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1.0*bluramount2+offsetx, -1.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0.0*bluramount2+offsetx, 1.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0.0*bluramount2+offsetx, -1.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1.0*bluramount2+offsetx, 0.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1.0*bluramount2+offsetx, 0.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							
							/*
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2.0*bluramount2+offsetx, 2.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-2.0*bluramount2+offsetx, 2.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2.0*bluramount2+offsetx, -2.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-2.0*bluramount2+offsetx, -2.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);

							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0.0*bluramount2+offsetx, 2.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0.0*bluramount2+offsetx, -2.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2.0*bluramount2+offsetx, 0.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							xfade = max(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-2.0*bluramount2+offsetx, 0.0*bluramount2+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, 100.0)/100.0 * shadowdarkness - zoffset), xfade);
							*/
							
							xfade = pow(xfade, 0.9);
							xfade = clamp((xfade - 0.01), 0.0, 1.0);
						
						
							//xfade /= 16.0;
							//xfade = clamp(xfade, 0.0, 1.0);
							
														//xfade = pow(xfade, 0.75);
							
							float xfadescale = xfade * 5.0;
							
							if (rainStrength > 0.1) {
								xfadescale = 0.0;
							}
							

							
							//if (xfade < 0.04) {
							//	xfade = 3.0;
							//}
							
							
							
					bluramount *= xfadescale;
					
				
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX2*xfadescale + offsetx, noiseY2*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX3*xfadescale + offsetx, noiseY3*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX4*xfadescale + offsetx, noiseY4*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX5*xfadescale + offsetx, noiseY5*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					
					
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX6*xfadescale + offsetx, noiseY6*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX7*xfadescale + offsetx, noiseY7*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX8*xfadescale + offsetx, noiseY8*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX9*xfadescale + offsetx, noiseY9*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX10*xfadescale + offsetx, noiseY10*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX11*xfadescale + offsetx, noiseY11*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX12*xfadescale + offsetx, noiseY12*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX13*xfadescale + offsetx, noiseY13*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX14*xfadescale + offsetx, noiseY14*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX15*xfadescale + offsetx, noiseY15*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX16*xfadescale + offsetx, noiseY16*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX17*xfadescale + offsetx, noiseY17*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX18*xfadescale + offsetx, noiseY18*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX19*xfadescale + offsetx, noiseY19*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX20*xfadescale + offsetx, noiseY20*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX21*xfadescale + offsetx, noiseY21*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(noiseX22*xfadescale + offsetx, noiseY22*xfadescale + offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);

					shading = shading/21.0 + 0.95;
				
				/*
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1*bluramount+offsetx, 1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0*bluramount+offsetx, 1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0*bluramount+offsetx, 0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2*bluramount+offsetx, 0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2*bluramount+offsetx, 1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2*bluramount+offsetx, 2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1*bluramount+offsetx, 2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0*bluramount+offsetx, 2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1*bluramount+offsetx, 2*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1*bluramount+offsetx, 1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1*bluramount+offsetx, 0*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(-1*bluramount+offsetx, -1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(0*bluramount+offsetx, -1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(1*bluramount+offsetx, -1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
					shading += 1.0/16 - shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2(2*bluramount+offsetx, -1*bluramount+offsety)).z) * (256.0 - 0.05)) - zoffset, 0.0, diffthresh)/diffthresh * shadowdarkness - zoffset);
				
				
					shading = shading/16 + 0.86;
					
					*/
				#endif
					
					if (land < 0.5) {
					shading = 1.0;
					}
					

					
					
				#endif
			
			}
		}
	}
	
	if (rainStrength > 0.1) {
		shading = 1.0 - (0.4 * SHADOW_DARKNESS);
	}

	vec4 color = texture2D(gcolor, texcoord.st);
	
	
	
//Determine what is being illuminated by the sun. 1 = sunlight. 0 = shadow.
float sun_amb = clamp(((shading-0.1)*3.0-1.1), 0.0, 1.0);
	
	
#ifdef FXAA

color = fxaa();

#endif
	
	
#ifdef SSAO
	
  float lum = dot(color.rgb, vec3(1.0));
  vec3 luminance = vec3(lum);
  vec3 color_hold = vec3(color.rgb);
  float AO = 1.0;
  AO *= getSSAOFactor();
  
  AO = mix(AO, AO * 0.5 + 0.5, sun_amb);
  
  if (land < 0.5) {
	AO = 1.0;
  }

  color.r *= AO;
  color.g *= AO;
  color.b *= AO;
  
  color.r *= (AO*0.5 + 0.5);
  color.g *= (AO*0.5 + 0.5);
  color.b *= (AO*0.5 + 0.5);
  

  
  //color.rgb *= (clamp((AO*1.0), 0.0, 1.0));


  //color.rgb = mix((color.rgb*0.8 + ((color.r + color.g + color.b)/3.0)*0.2), (color.rgb + color_hold)/2.0, sun_amb);
  
  
#endif






#ifdef CORRECTSHADOWCOLORS

	float timefract = worldTime;

	float TimeSunrise  = ((clamp(timefract, 23000.0, 24000.0) - 23000.0) / 1000.0) + (1.0 - (clamp(timefract, 0.0, 4000.0)/4000.0));
	float TimeNoon     = ((clamp(timefract, 0.0, 4000.0)) / 4000.0) - ((clamp(timefract, 8000.0, 12000.0) - 8000.0) / 4000.0);
	float TimeSunset   = ((clamp(timefract, 8000.0, 12000.0) - 8000.0) / 4000.0) - ((clamp(timefract, 12000.0, 12750.0) - 12000.0) / 750.0);
	float TimeMidnight = ((clamp(timefract, 12000.0, 12750.0) - 12000.0) / 750.0) - ((clamp(timefract, 23000.0, 24000.0) - 23000.0) / 1000.0);
	
	
	float sunrise_sun_r = 0.9 * TimeSunrise;
	float sunrise_sun_g = 0.73 * TimeSunrise;
	float sunrise_sun_b = 0.40 * TimeSunrise;
	
	float sunrise_amb_r = 0.9 * TimeSunrise;
	float sunrise_amb_g = 0.9 * TimeSunrise;
	float sunrise_amb_b = 0.9 * TimeSunrise;
	
	
	float noon_sun_r = 1.0 * TimeNoon;
	float noon_sun_g = 0.95 * TimeNoon;
	float noon_sun_b = 0.8 * TimeNoon;
	
	float noon_amb_r = 0.8 * TimeNoon;
	float noon_amb_g = 0.93 * TimeNoon;
	float noon_amb_b = 1.0 * TimeNoon;
	
	
	float sunset_sun_r = 0.9 * TimeSunset;
	float sunset_sun_g = 0.73 * TimeSunset;
	float sunset_sun_b = 0.40 * TimeSunset;
	
	float sunset_amb_r = 0.9 * TimeSunset;
	float sunset_amb_g = 0.9 * TimeSunset;
	float sunset_amb_b = 0.9 * TimeSunset;
	
	
	float midnight_sun_r = 0.6 * TimeMidnight;
	float midnight_sun_g = 0.65 * TimeMidnight;
	float midnight_sun_b = 0.8 * TimeMidnight;
	
	float midnight_amb_r = 0.8 * TimeMidnight;
	float midnight_amb_g = 0.85 * TimeMidnight;
	float midnight_amb_b = 1.0 * TimeMidnight;


	float sunlight_r = sunrise_sun_r + noon_sun_r + sunset_sun_r + midnight_sun_r;
	float sunlight_g = sunrise_sun_g + noon_sun_g + sunset_sun_g + midnight_sun_g;
	float sunlight_b = sunrise_sun_b + noon_sun_b + sunset_sun_b + midnight_sun_b;
	
	float ambient_r = sunrise_amb_r + noon_amb_r + sunset_amb_r + midnight_amb_r;
	float ambient_g = sunrise_amb_g + noon_amb_g + sunset_amb_g + midnight_amb_g;
	float ambient_b = sunrise_amb_b + noon_amb_b + sunset_amb_b + midnight_amb_b;
	
	
	color.r = mix(color.r * ambient_r, color.r * sunlight_r, sun_amb);
	color.g = mix(color.g * ambient_g, color.g * sunlight_g, sun_amb);
	color.b = mix(color.b * ambient_b, color.b * sunlight_b, sun_amb);
	/*
	*/
	

	
	if (land < 0.5) {
		color.r = ((color.r * 1.4 - 0.2) * (TimeSunrise))   +   ((color.r * 1.4 - 0.5) * (TimeNoon))   +   ((color.r * 1.4 - 0.2) * (TimeSunset))   +   (color.r * TimeMidnight);
		color.g = ((color.g * 1.4 - 0.2) * (TimeSunrise))   +   ((color.g * 1.4 - 0.5) * (TimeNoon))   +   ((color.g * 1.4 - 0.2) * (TimeSunset))   +   (color.g * TimeMidnight);
		color.b = ((color.b * 1.4 - 0.2) * (TimeSunrise))   +   ((color.b * 1.4 - 0.5) * (TimeNoon))   +   ((color.b * 1.4 - 0.2) * (TimeSunset))   +   (color.b * TimeMidnight);
		
		if (rainStrength > 0.1) {
			color.r = ((color.r * 0.8 + 0.2) * (TimeSunrise))   +   ((color.r * 0.8 + 1.2) * (TimeNoon))   +   ((color.r * 0.8 + 0.2) * (TimeSunset))   +   (color.r * TimeMidnight);
			color.g = ((color.g * 0.8 + 0.2) * (TimeSunrise))   +   ((color.g * 0.8 + 1.2) * (TimeNoon))   +   ((color.g * 0.8 + 0.2) * (TimeSunset))   +   (color.g * TimeMidnight);
			color.b = ((color.b * 0.8 + 0.2) * (TimeSunrise))   +   ((color.b * 0.8 + 1.2) * (TimeNoon))   +   ((color.b * 0.8 + 0.2) * (TimeSunset))   +   (color.b * TimeMidnight);
		}
	}
	

#endif


	float noblur = texture2D(gaux1, texcoord.st).r;



	gl_FragData[0] = texture2D(gcolor, texcoord.st);
	gl_FragData[1] = texture2D(gdepth, texcoord.st);
	gl_FragData[3] = vec4(color.rgb * (shading), 1.0);
	gl_FragData[4] = vec4(noblur, 0.0, 0.0, 1.0);
}
