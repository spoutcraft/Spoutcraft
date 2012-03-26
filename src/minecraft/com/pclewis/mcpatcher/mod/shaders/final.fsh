#version 120

// More realistic depth-of-field by Azraeil.
// This is a modification of Daxnitro's depth-of-field shader.

// If you want a higher quality blur, remove the forward slashes from the following line:
//#define USE_HIGH_QUALITY_BLUR

uniform sampler2D gcolor;
uniform sampler2D gdepth;
uniform sampler2D composite;

varying vec4 texcoord;

uniform float aspectRatio;
uniform float near;
uniform float far;

// HYPERFOCAL = (Focal Distance ^ 2)/(Circle of Confusion * F Stop) + Focal Distance
const float HYPERFOCAL = 3.132;
const float PICONSTANT = 3.14159;

float getDepth(vec2 coord);
vec4 getBlurredColor();
vec4 getSample(vec2 coord, vec2 aspectCorrection);
vec4 getSampleWithBoundsCheck(vec2 offset);

float samples = 0.0;
vec2 space;

void main() {
	vec4 baseColor = texture2D(composite, texcoord.st);

	float depth = getDepth(texcoord.st);
	    
	float cursorDepth = getDepth(vec2(0.5, 0.5));
    
    // foreground blur = 1/2 background blur. Blur should follow exponential pattern until cursor = hyperfocal -- Cursor before hyperfocal
    // Blur should go from 0 to 1/2 hyperfocal then clear to infinity -- Cursor @ hyperfocal.
    // hyperfocal to inifity is clear though dof extends from 1/2 hyper to hyper -- Cursor beyond hyperfocal
    
    float mixAmount = 0.0;
    
    if (depth < cursorDepth) {
    	mixAmount = clamp(2.0 * ((clamp(cursorDepth, 0.0, HYPERFOCAL) - depth) / (clamp(cursorDepth, 0.0, HYPERFOCAL))), 0.0, 1.0);
	} else if (cursorDepth == HYPERFOCAL) {
		mixAmount = 0.0;
	} else {
		mixAmount =  1.0 - clamp((((cursorDepth * HYPERFOCAL) / (HYPERFOCAL - cursorDepth)) - (depth - cursorDepth)) / ((cursorDepth * HYPERFOCAL) / (HYPERFOCAL - cursorDepth)), 0.0, 1.0);
	}
    
    if (mixAmount != 0.0) {
		gl_FragColor = mix(baseColor, getBlurredColor(), mixAmount);
   	} else {
   		gl_FragColor = baseColor;
   	}
}

float getDepth(vec2 coord) {
    return 2.0 * near * far / (far + near - (2.0 * texture2D(gdepth, coord).x - 1.0) * (far - near));
}

vec4 getBlurredColor() {
	vec4 blurredColor = vec4(0.0);
	float depth = getDepth(texcoord.xy);
	vec2 aspectCorrection = vec2(1.0, aspectRatio) * 0.005;

	vec2 ac0_4 = 0.4 * aspectCorrection;	// 0.4
#ifdef USE_HIGH_QUALITY_BLUR
	vec2 ac0_4x0_4 = 0.4 * ac0_4;			// 0.16
	vec2 ac0_4x0_7 = 0.7 * ac0_4;			// 0.28
#endif
	
	vec2 ac0_29 = 0.29 * aspectCorrection;	// 0.29
#ifdef USE_HIGH_QUALITY_BLUR
	vec2 ac0_29x0_7 = 0.7 * ac0_29;			// 0.203
	vec2 ac0_29x0_4 = 0.4 * ac0_29;			// 0.116
#endif
	
	vec2 ac0_15 = 0.15 * aspectCorrection;	// 0.15
	vec2 ac0_37 = 0.37 * aspectCorrection;	// 0.37
#ifdef USE_HIGH_QUALITY_BLUR
	vec2 ac0_15x0_9 = 0.9 * ac0_15;			// 0.135
	vec2 ac0_37x0_9 = 0.37 * ac0_37;		// 0.1369
#endif
	
	vec2 lowSpace = texcoord.st;
	vec2 highSpace = 1.0 - lowSpace;
	space = vec2(min(lowSpace.s, highSpace.s), min(lowSpace.t, highSpace.t));
		
	if (space.s >= ac0_4.s && space.t >= ac0_4.t) {

		blurredColor += texture2D(composite, texcoord.st + vec2(0.0, ac0_4.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(ac0_4.s, 0.0));   
		blurredColor += texture2D(composite, texcoord.st + vec2(0.0, -ac0_4.t)); 
		blurredColor += texture2D(composite, texcoord.st + vec2(-ac0_4.s, 0.0)); 
		
#ifdef USE_HIGH_QUALITY_BLUR
		blurredColor += texture2D(composite, texcoord.st + vec2(ac0_4x0_7.s, 0.0));       
		blurredColor += texture2D(composite, texcoord.st + vec2(0.0, -ac0_4x0_7.t));     
		blurredColor += texture2D(composite, texcoord.st + vec2(-ac0_4x0_7.s, 0.0));     
		blurredColor += texture2D(composite, texcoord.st + vec2(0.0, ac0_4x0_7.t));
	
		blurredColor += texture2D(composite, texcoord.st + vec2(ac0_4x0_4.s, 0.0));
		blurredColor += texture2D(composite, texcoord.st + vec2(0.0, -ac0_4x0_4.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(-ac0_4x0_4.s, 0.0));
		blurredColor += texture2D(composite, texcoord.st + vec2(0.0, ac0_4x0_4.t));
#endif

		blurredColor += texture2D(composite, texcoord.st + vec2(ac0_29.s, -ac0_29.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(ac0_29.s, ac0_29.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(-ac0_29.s, ac0_29.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(-ac0_29.s, -ac0_29.t));
	
#ifdef USE_HIGH_QUALITY_BLUR
		blurredColor += texture2D(composite, texcoord.st + vec2(ac0_29x0_7.s, ac0_29x0_7.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(ac0_29x0_7.s, -ac0_29x0_7.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(-ac0_29x0_7.s, ac0_29x0_7.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(-ac0_29x0_7.s, -ac0_29x0_7.t));
		
		blurredColor += texture2D(composite, texcoord.st + vec2(ac0_29x0_4.s, ac0_29x0_4.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(ac0_29x0_4.s, -ac0_29x0_4.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(-ac0_29x0_4.s, ac0_29x0_4.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(-ac0_29x0_4.s, -ac0_29x0_4.t));
#endif		
		
		blurredColor += texture2D(composite, texcoord.st + vec2(ac0_15.s, ac0_37.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(-ac0_37.s, ac0_15.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(ac0_37.s, -ac0_15.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(-ac0_15.s, -ac0_37.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(-ac0_15.s, ac0_37.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(ac0_37.s, ac0_15.t)); 
		blurredColor += texture2D(composite, texcoord.st + vec2(-ac0_37.s, -ac0_15.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(ac0_15.s, -ac0_37.t));

#ifdef USE_HIGH_QUALITY_BLUR
		blurredColor += texture2D(composite, texcoord.st + vec2(ac0_15x0_9.s, ac0_37x0_9.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(-ac0_37x0_9.s, ac0_15x0_9.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(ac0_37x0_9.s, -ac0_15x0_9.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(-ac0_15x0_9.s, -ac0_37x0_9.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(-ac0_15x0_9.s, ac0_37x0_9.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(ac0_37x0_9.s, ac0_15x0_9.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(-ac0_37x0_9.s, -ac0_15x0_9.t));
		blurredColor += texture2D(composite, texcoord.st + vec2(ac0_15x0_9.s, -ac0_37x0_9.t));
#endif

#ifdef USE_HIGH_QUALITY_BLUR
	    blurredColor /= 41.0;
#else
	    blurredColor /= 16.0;
#endif
	    
	} else {
		
		blurredColor += getSampleWithBoundsCheck(vec2(0.0, ac0_4.t));
		blurredColor += getSampleWithBoundsCheck(vec2(ac0_4.s, 0.0));   
		blurredColor += getSampleWithBoundsCheck(vec2(0.0, -ac0_4.t)); 
		blurredColor += getSampleWithBoundsCheck(vec2(-ac0_4.s, 0.0)); 
		
#ifdef USE_HIGH_QUALITY_BLUR
		blurredColor += getSampleWithBoundsCheck(vec2(ac0_4x0_7.s, 0.0));       
		blurredColor += getSampleWithBoundsCheck(vec2(0.0, -ac0_4x0_7.t));     
		blurredColor += getSampleWithBoundsCheck(vec2(-ac0_4x0_7.s, 0.0));     
		blurredColor += getSampleWithBoundsCheck(vec2(0.0, ac0_4x0_7.t));
	
		blurredColor += getSampleWithBoundsCheck(vec2(ac0_4x0_4.s, 0.0));
		blurredColor += getSampleWithBoundsCheck(vec2(0.0, -ac0_4x0_4.t));
		blurredColor += getSampleWithBoundsCheck(vec2(-ac0_4x0_4.s, 0.0));
		blurredColor += getSampleWithBoundsCheck(vec2(0.0, ac0_4x0_4.t));
#endif

		blurredColor += getSampleWithBoundsCheck(vec2(ac0_29.s, -ac0_29.t));
		blurredColor += getSampleWithBoundsCheck(vec2(ac0_29.s, ac0_29.t));
		blurredColor += getSampleWithBoundsCheck(vec2(-ac0_29.s, ac0_29.t));
		blurredColor += getSampleWithBoundsCheck(vec2(-ac0_29.s, -ac0_29.t));
	
#ifdef USE_HIGH_QUALITY_BLUR
		blurredColor += getSampleWithBoundsCheck(vec2(ac0_29x0_7.s, ac0_29x0_7.t));
		blurredColor += getSampleWithBoundsCheck(vec2(ac0_29x0_7.s, -ac0_29x0_7.t));
		blurredColor += getSampleWithBoundsCheck(vec2(-ac0_29x0_7.s, ac0_29x0_7.t));
		blurredColor += getSampleWithBoundsCheck(vec2(-ac0_29x0_7.s, -ac0_29x0_7.t));
		
		blurredColor += getSampleWithBoundsCheck(vec2(ac0_29x0_4.s, ac0_29x0_4.t));
		blurredColor += getSampleWithBoundsCheck(vec2(ac0_29x0_4.s, -ac0_29x0_4.t));
		blurredColor += getSampleWithBoundsCheck(vec2(-ac0_29x0_4.s, ac0_29x0_4.t));
		blurredColor += getSampleWithBoundsCheck(vec2(-ac0_29x0_4.s, -ac0_29x0_4.t));
#endif
				
		blurredColor += getSampleWithBoundsCheck(vec2(ac0_15.s, ac0_37.t));
		blurredColor += getSampleWithBoundsCheck(vec2(-ac0_37.s, ac0_15.t));
		blurredColor += getSampleWithBoundsCheck(vec2(ac0_37.s, -ac0_15.t));
		blurredColor += getSampleWithBoundsCheck(vec2(-ac0_15.s, -ac0_37.t));
		blurredColor += getSampleWithBoundsCheck(vec2(-ac0_15.s, ac0_37.t));
		blurredColor += getSampleWithBoundsCheck(vec2(ac0_37.s, ac0_15.t)); 
		blurredColor += getSampleWithBoundsCheck(vec2(-ac0_37.s, -ac0_15.t));
		blurredColor += getSampleWithBoundsCheck(vec2(ac0_15.s, -ac0_37.t));
		
#ifdef USE_HIGH_QUALITY_BLUR
		blurredColor += getSampleWithBoundsCheck(vec2(ac0_15x0_9.s, ac0_37x0_9.t));
		blurredColor += getSampleWithBoundsCheck(vec2(-ac0_37x0_9.s, ac0_15x0_9.t));
		blurredColor += getSampleWithBoundsCheck(vec2(ac0_37x0_9.s, -ac0_15x0_9.t));
		blurredColor += getSampleWithBoundsCheck(vec2(-ac0_15x0_9.s, -ac0_37x0_9.t));
		blurredColor += getSampleWithBoundsCheck(vec2(-ac0_15x0_9.s, ac0_37x0_9.t));
		blurredColor += getSampleWithBoundsCheck(vec2(ac0_37x0_9.s, ac0_15x0_9.t));
		blurredColor += getSampleWithBoundsCheck(vec2(-ac0_37x0_9.s, -ac0_15x0_9.t));
		blurredColor += getSampleWithBoundsCheck(vec2(ac0_15x0_9.s, -ac0_37x0_9.t));
#endif
	
	    blurredColor /= samples;
	    
	}

    return blurredColor;
}

vec4 getSampleWithBoundsCheck(vec2 offset) {
	vec2 coord = texcoord.st + offset;
	if (coord.s <= 1.0 && coord.s >= 0.0 && coord.t <= 1.0 && coord.t >= 0.0) {
		samples += 1.0;
		return texture2D(composite, coord);
	} else {
		return vec4(0.0);
	}
}