#version 120

/*

Settings by Sonic Ether
Bokeh Depth-of-Field by Sonic Ether
God Rays by Blizzard
Bloom shader by CosmicSpore (Modified from original source: http://myheroics.wordpress.com/2008/09/04/glsl-bloom-shader/)
Cross-Processing by Sonic Ether.
High Desaturation effect by Sonic Ether
HDR by Sonic Ether
Glare by Sonic Ether
Shaders 2.0 port of Yourself's Cell Shader, port by an anonymous user.
Bug Fixes by Kool_Kat.

*/

// Place two leading Slashes in front of the following '#define' lines in order to disable an option.
// MOTIONBLUR, HDR, and BOKEH_DOF are very beta shaders. Use at risk of weird results.
// MOTIONBLUR and BOKEH_DOF are not compatable with eachother. Shaders break when you enable both.
// GLARE is still a work in progress.
// BLOOM is currently broken.

//#define HDR
//#define HDR_RANGE 1.0					// Higher value means that HDR will modulate screen brightness more. Default = 1.0
//#define BOKEH_DOF
//#define GODRAYS
//#define GODRAYS_EXPOSURE 0.8
//#define GODRAYS_SAMPLES 16
//#define GODRAYS_DECAY 0.9
//#define GODRAYS_DENSITY 0.5
//#define GLARE
//#define GLARE_AMOUNT 0.25
//#define GLARE_RANGE 3.0
//#define BLOOM
//#define BLOOM_AMOUNT 1.0
//#define BLOOM_RANGE 3
//#define CEL_SHADING
//#define CEL_SHADING_THRESHOLD 0.4
//#define CEL_SHADING_THICKNESS 0.004
//#define USE_HIGH_QUALITY_BLUR

#define CROSSPROCESS
#define BRIGHTMULT 1.10                 	// 1.0 = default brightness. Higher values mean brighter. 0 would be black.
#define DARKMULT 0.08						// 0.0 = normal image. Higher values will darken dark colors.
#define COLOR_BOOST	0.1					// 0.0 = normal saturation. Higher values mean more saturated image.
#define MOTIONBLUR
#define MOTIONBLUR_AMOUNT 0.3
#define HIGHDESATURATE
#define GAMMA 0.88							//1.0 is default brightness. lower values will brighten image, higher values will darken image	





// DOF Constants - DO NOT CHANGE
// HYPERFOCAL = (Focal Distance ^ 2)/(Circle of Confusion * F Stop) + Focal Distance
#ifdef USE_DOF
const float HYPERFOCAL = 3.132;
const float PICONSTANT = 3.14159;
#endif


//uniform sampler2D texture;
uniform sampler2D gcolor;
uniform sampler2D gdepth;
uniform sampler2D composite;
uniform sampler2D gnormal;
uniform sampler2D gaux1; // red is our motion blur mask. If red == 1, don't blur

uniform mat4 gbufferProjectionInverse;
uniform mat4 gbufferPreviousProjection;

uniform mat4 gbufferModelViewInverse;
uniform mat4 gbufferPreviousModelView;

uniform vec3 cameraPosition;
uniform vec3 previousCameraPosition;

uniform vec3 sunPosition;

uniform float worldTime;
uniform float aspectRatio;
uniform float near;
uniform float far;

varying vec4 texcoord;




// Standard depth function.
float getDepth(vec2 coord) {
    return 2.0 * near * far / (far + near - (2.0 * texture2D(gdepth, coord).x - 1.0) * (far - near));
}
float eDepth(vec2 coord) {
	return texture2D(gdepth, coord).x;
}
float realcolor(vec2 coord) {
	return ((texture2D(gcolor, coord).r + texture2D(gcolor, coord).g + texture2D(gcolor, coord).b)/3.0);
}

float prevavgclr;
float prevavgclrb;


#ifdef BOKEH_DOF

const float blurclamp = 10.0;  // max blur amount
const float bias = 0.3;	//aperture - bigger values for shallower depth of field

#endif

#ifdef GODRAYS
	vec4 addGodRays(vec4 nc, vec2 tx) {
		float threshold = 0.99 * far;
//		bool foreground = false;
		float depthGD = getDepth(tx);
		if ( (worldTime < 14000 || worldTime > 22000) && (sunPosition.z < 0) && (depthGD < threshold) ) {
			vec2 lightPos = sunPosition.xy / -sunPosition.z;
			lightPos.y *= aspectRatio;
			lightPos = (lightPos + 1.0)/2.0;
			//vec2 coord = tx;
			vec2 delta = (tx - lightPos) * GODRAYS_DENSITY / float(GODRAYS_SAMPLES);
			float decay = -sunPosition.z / 100.0;
			vec3 colorGD = vec3(0.0);
			
			for (int i = 0; i < GODRAYS_SAMPLES; i++) {
				tx -= delta;
				if (tx.x < 0.0 || tx.x > 1.0) {
					if (tx.y < 0.0 || tx.y > 1.0) {
						break;
					}
				}
				vec3 sample = vec3(0.0);
				if (getDepth(tx) > threshold) {
					sample = texture2D(composite, tx).rgb;
				}
				sample *= vec3(decay);
				if (distance(tx, lightPos) > 0.05) {
					sample *= 0.2;
				}
					colorGD += sample;
					decay *= GODRAYS_DECAY;
			}
			return (nc + GODRAYS_EXPOSURE * vec4(colorGD, 0.0));
        } else {
			return nc;
		}
	}
#endif 

/*
#ifdef BLOOM
	vec4 addBloom(vec4 c, vec2 t) {
		int j;
		int i;
		vec4 bloom = vec4(0.0);
		vec2 loc = vec2(0.0);
		float count = 0.0;
		
		for( i= -BLOOM_RANGE ; i < BLOOM_RANGE; i++ ) {
			for ( j = -BLOOM_RANGE; j < BLOOM_RANGE; j++ ) {
				loc = t + vec2(j, i)*0.004;
				
				// Only add to bloom texture if loc is on-screen.
				if(loc.x > 0 && loc.x < 1 && loc.y > 0 && loc.y < 1) {
					bloom += texture2D(composite, loc) * BLOOM_AMOUNT;
					count += 1;
				}
			}
		}
		bloom /= vec4(count);
		
		if (c.r < 0.3)
		{
			return bloom*bloom*0.012;
		}
		else
		{
			if (c.r < 0.5)
			{
				return bloom*bloom*0.009;
			}
			else
			{
				return bloom*bloom*0.0075;
			}
		}
	}
#endif
*/

#ifdef CEL_SHADING
	float getCellShaderFactor(vec2 coord) {
    float d = getDepth(coord);
    vec3 n = normalize(vec3(getDepth(coord+vec2(CEL_SHADING_THICKNESS,0.0))-d,getDepth(coord+vec2(0.0,CEL_SHADING_THICKNESS))-d , CEL_SHADING_THRESHOLD));
    //clamp(n.z*3.0,0.0,1.0);
    return n.z; 
	}
#endif


// Main ---------------------------------------------------------------------------------------------------
void main() {

	vec4 color = texture2D(composite, texcoord.st);


#ifdef BOKEH_DOF

	float depth = eDepth(texcoord.xy);
	
	if (depth > 0.9999) {
		depth = 1.0;
	}
	

	float cursorDepth = eDepth(vec2(0.5, 0.5));
	
	if (cursorDepth > 0.9999) {
		cursorDepth = 1.0;
	}
	
	
	vec2 aspectcorrect = vec2(1.0, aspectRatio) * 1.5;
	
	float factor = (depth - cursorDepth);
	 
	vec2 dofblur = (vec2 (clamp( factor * bias, -blurclamp, blurclamp )))*0.4;
	
	

	vec4 col = vec4(0.0);
	col += texture2D(composite, texcoord.st);
	
	col += texture2D(composite, texcoord.st + (vec2( 0.0,0.4 )*aspectcorrect) * dofblur);
	col += texture2D(composite, texcoord.st + (vec2( 0.15,0.37 )*aspectcorrect) * dofblur);
	col += texture2D(composite, texcoord.st + (vec2( 0.29,0.29 )*aspectcorrect) * dofblur);
	col += texture2D(composite, texcoord.st + (vec2( -0.37,0.15 )*aspectcorrect) * dofblur);	
	col += texture2D(composite, texcoord.st + (vec2( 0.4,0.0 )*aspectcorrect) * dofblur);	
	col += texture2D(composite, texcoord.st + (vec2( 0.37,-0.15 )*aspectcorrect) * dofblur);	
	col += texture2D(composite, texcoord.st + (vec2( 0.29,-0.29 )*aspectcorrect) * dofblur);	
	col += texture2D(composite, texcoord.st + (vec2( -0.15,-0.37 )*aspectcorrect) * dofblur);
	col += texture2D(composite, texcoord.st + (vec2( 0.0,-0.4 )*aspectcorrect) * dofblur);	
	col += texture2D(composite, texcoord.st + (vec2( -0.15,0.37 )*aspectcorrect) * dofblur);
	col += texture2D(composite, texcoord.st + (vec2( -0.29,0.29 )*aspectcorrect) * dofblur);
	col += texture2D(composite, texcoord.st + (vec2( 0.37,0.15 )*aspectcorrect) * dofblur);	
	col += texture2D(composite, texcoord.st + (vec2( -0.4,0.0 )*aspectcorrect) * dofblur);	
	col += texture2D(composite, texcoord.st + (vec2( -0.37,-0.15 )*aspectcorrect) * dofblur);	
	col += texture2D(composite, texcoord.st + (vec2( -0.29,-0.29 )*aspectcorrect) * dofblur);	
	col += texture2D(composite, texcoord.st + (vec2( 0.15,-0.37 )*aspectcorrect) * dofblur);
	
	col += texture2D(composite, texcoord.st + (vec2( 0.15,0.37 )*aspectcorrect) * dofblur*0.9);
	col += texture2D(composite, texcoord.st + (vec2( -0.37,0.15 )*aspectcorrect) * dofblur*0.9);		
	col += texture2D(composite, texcoord.st + (vec2( 0.37,-0.15 )*aspectcorrect) * dofblur*0.9);		
	col += texture2D(composite, texcoord.st + (vec2( -0.15,-0.37 )*aspectcorrect) * dofblur*0.9);
	col += texture2D(composite, texcoord.st + (vec2( -0.15,0.37 )*aspectcorrect) * dofblur*0.9);
	col += texture2D(composite, texcoord.st + (vec2( 0.37,0.15 )*aspectcorrect) * dofblur*0.9);		
	col += texture2D(composite, texcoord.st + (vec2( -0.37,-0.15 )*aspectcorrect) * dofblur*0.9);	
	col += texture2D(composite, texcoord.st + (vec2( 0.15,-0.37 )*aspectcorrect) * dofblur*0.9);	
	
	col += texture2D(composite, texcoord.st + (vec2( 0.29,0.29 )*aspectcorrect) * dofblur*0.7);
	col += texture2D(composite, texcoord.st + (vec2( 0.4,0.0 )*aspectcorrect) * dofblur*0.7);	
	col += texture2D(composite, texcoord.st + (vec2( 0.29,-0.29 )*aspectcorrect) * dofblur*0.7);	
	col += texture2D(composite, texcoord.st + (vec2( 0.0,-0.4 )*aspectcorrect) * dofblur*0.7);	
	col += texture2D(composite, texcoord.st + (vec2( -0.29,0.29 )*aspectcorrect) * dofblur*0.7);
	col += texture2D(composite, texcoord.st + (vec2( -0.4,0.0 )*aspectcorrect) * dofblur*0.7);	
	col += texture2D(composite, texcoord.st + (vec2( -0.29,-0.29 )*aspectcorrect) * dofblur*0.7);	
	col += texture2D(composite, texcoord.st + (vec2( 0.0,0.4 )*aspectcorrect) * dofblur*0.7);
			 
	col += texture2D(composite, texcoord.st + (vec2( 0.29,0.29 )*aspectcorrect) * dofblur*0.4);
	col += texture2D(composite, texcoord.st + (vec2( 0.4,0.0 )*aspectcorrect) * dofblur*0.4);	
	col += texture2D(composite, texcoord.st + (vec2( 0.29,-0.29 )*aspectcorrect) * dofblur*0.4);	
	col += texture2D(composite, texcoord.st + (vec2( 0.0,-0.4 )*aspectcorrect) * dofblur*0.4);	
	col += texture2D(composite, texcoord.st + (vec2( -0.29,0.29 )*aspectcorrect) * dofblur*0.4);
	col += texture2D(composite, texcoord.st + (vec2( -0.4,0.0 )*aspectcorrect) * dofblur*0.4);	
	col += texture2D(composite, texcoord.st + (vec2( -0.29,-0.29 )*aspectcorrect) * dofblur*0.4);	
	col += texture2D(composite, texcoord.st + (vec2( 0.0,0.4 )*aspectcorrect) * dofblur*0.4);	

	color = col/41;
	

#endif

/*
#ifdef USE_DOF
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
		color = mix(color, getBlurredColor(), mixAmount);
   	}
#endif
*/






#ifdef MOTIONBLUR

	float depth = texture2D(gdepth, texcoord.st).x;
	
	float noblur = texture2D(gaux1, texcoord.st).r;

	
		if (depth > 0.9999999) {
		depth = 1;
		}
		
		
	
	
		if (depth < 1.9999999) {
		vec4 currentPosition = vec4(texcoord.x * 2.0 - 1.0, texcoord.y * 2.0 - 1.0, 2.0 * depth - 1.0, 1.0);
	
		vec4 fragposition = gbufferProjectionInverse * currentPosition;
		fragposition = gbufferModelViewInverse * fragposition;
		fragposition /= fragposition.w;
		fragposition.xyz += cameraPosition;
	
		vec4 previousPosition = fragposition;
		previousPosition.xyz -= previousCameraPosition;
		previousPosition = gbufferPreviousModelView * previousPosition;
		previousPosition = gbufferPreviousProjection * previousPosition;
		previousPosition /= previousPosition.w;
	
		vec2 velocity = (currentPosition - previousPosition).st * 0.007 * MOTIONBLUR_AMOUNT;
		velocity = velocity;
	
		int samples = 1;
		
		if (noblur > 0.9) {
			velocity = vec2(0,0);
		}
		
		
		vec2 coord = texcoord.st + velocity;


		for (int i = 0; i < 18; ++i, coord += velocity) {
			if (coord.s > 1.0 || coord.t > 1.0 || coord.s < 0.0 || coord.t < 0.0) {
				break;
			}


			color += texture2D(composite, coord);
			++samples;
		
		}	
			color = (color/1.0)/samples;
		}
		

	
#endif

#ifdef GODRAYS
	color.r = addGodRays(color, texcoord.st).r;
	color.g = addGodRays(color, texcoord.st).g;
	color.b = addGodRays(color, texcoord.st).b;
#endif

/*
#ifdef BLOOM
	color = color * 0.8;
	color += addBloom(color, texcoord.st);
#endif
*/


#ifdef BLOOM

	color = color * 0.8;
	
	float radius = 0.002;
	float blm_amount = 0.02*BLOOM_AMOUNT;
	float sc = 20.0;
	
	int i = 0;
	int samples = 1;
	
	vec4 clr = vec4(0.0);
	
	for (i = -10; i < 10; i++) {
	clr += texture2D(composite, texcoord.st + (vec2(i,i))*radius)*sc;
	clr += texture2D(composite, texcoord.st + (vec2(i,-i))*radius)*sc;
	clr += texture2D(composite, texcoord.st + (vec2(-i,i))*radius)*sc;
	clr += texture2D(composite, texcoord.st + (vec2(-i,-i))*radius)*sc;
	
	clr += texture2D(composite, texcoord.st + (vec2(0.0,i))*radius)*sc;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,-i))*radius)*sc;
	clr += texture2D(composite, texcoord.st + (vec2(-i,0.0))*radius)*sc;
	clr += texture2D(composite, texcoord.st + (vec2(i,0.0))*radius)*sc;
	
	++samples;
	sc = sc - 1.0;
	}
	
	clr = (clr/8.0)/samples;
	
	color += clr*blm_amount;

#endif

#ifdef GLARE

	color = color * 0.8;
	
	float radius = 0.002*GLARE_RANGE;
	float radiusv = 0.002;
	float bloomintensity = 0.1*GLARE_AMOUNT;

	vec4 clr = vec4(0.0);
	
	clr += texture2D(composite, texcoord.st);
	
	//horizontal (70 taps)
	
	
	clr += texture2D(composite, texcoord.st + (vec2(10.0,0.0))*radius)*10.0;
	clr += texture2D(composite, texcoord.st + (vec2(9.0,0.0))*radius)*11.0;
	clr += texture2D(composite, texcoord.st + (vec2(8.0,0.0))*radius)*12.0;
	clr += texture2D(composite, texcoord.st + (vec2(7.0,0.0))*radius)*13.0;
	clr += texture2D(composite, texcoord.st + (vec2(6.0,0.0))*radius)*14.0;
	clr += texture2D(composite, texcoord.st + (vec2(5.0,0.0))*radius)*15.0;
	clr += texture2D(composite, texcoord.st + (vec2(4.0,0.0))*radius)*16.0;
	clr += texture2D(composite, texcoord.st + (vec2(3.0,0.0))*radius)*17.0;
	clr += texture2D(composite, texcoord.st + (vec2(2.0,0.0))*radius)*18.0;
	clr += texture2D(composite, texcoord.st + (vec2(1.0,0.0))*radius)*19.0;
	
		clr += texture2D(composite, texcoord.st + (vec2(0.0,0.0))*radius)*20.0;
		
	clr += texture2D(composite, texcoord.st + (vec2(-1.0,0.0))*radius)*19.0;
	clr += texture2D(composite, texcoord.st + (vec2(-2.0,0.0))*radius)*18.0;
	clr += texture2D(composite, texcoord.st + (vec2(-3.0,0.0))*radius)*17.0;
	clr += texture2D(composite, texcoord.st + (vec2(-4.0,0.0))*radius)*16.0;
	clr += texture2D(composite, texcoord.st + (vec2(-5.0,0.0))*radius)*15.0;
	clr += texture2D(composite, texcoord.st + (vec2(-6.0,0.0))*radius)*14.0;
	clr += texture2D(composite, texcoord.st + (vec2(-7.0,0.0))*radius)*13.0;
	clr += texture2D(composite, texcoord.st + (vec2(-8.0,0.0))*radius)*12.0;
	clr += texture2D(composite, texcoord.st + (vec2(-9.0,0.0))*radius)*11.0;
	clr += texture2D(composite, texcoord.st + (vec2(-10.0,0.0))*radius)*10.0;

	
	//vertical
	

	clr += texture2D(composite, texcoord.st + (vec2(0.0,10.0))*radius)*10.0;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,9.0))*radius)*11.0;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,8.0))*radius)*12.0;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,7.0))*radius)*13.0;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,6.0))*radius)*14.0;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,5.0))*radius)*15.0;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,4.0))*radius)*16.0;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,3.0))*radius)*17.0;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,2.0))*radius)*18.0;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,1.0))*radius)*19.0;
	
	
	clr += texture2D(composite, texcoord.st + (vec2(0.0,-10.0))*radius)*10.0;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,-9.0))*radius)*11.0;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,-8.0))*radius)*12.0;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,-7.0))*radius)*13.0;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,-6.0))*radius)*14.0;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,-5.0))*radius)*15.0;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,-4.0))*radius)*16.0;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,-3.0))*radius)*17.0;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,-2.0))*radius)*18.0;
	clr += texture2D(composite, texcoord.st + (vec2(0.0,-1.0))*radius)*19.0;
	

	
	clr = (clr/20.0)/5.0;
	clr.r = pow(clr.r, 1.2)*1.6 - (clr.g + clr.b)*0.6;
	clr.g = pow(clr.g, 1.2)*1.6 - (clr.r + clr.b)*0.6;
	clr.b = pow(clr.b, 1.2)*1.9 - (clr.r + clr.g)*0.9;
	
	clr = clamp((clr), 0.0, 1.0);
	
	color.r = color.r + (clr.r*1.5)*bloomintensity;
	color.g = color.g + (clr.g*1.5)*bloomintensity;
	color.b = color.b + (clr.b*4.0)*bloomintensity;
	color = max(color, 0.0);
	//color = color*1.05 - 0.05;
	
	

#endif


#ifdef CEL_SHADING
	color.rgb *= (getCellShaderFactor(texcoord.st));
#endif



#ifdef CROSSPROCESS
	//pre-gain
	color = color * (BRIGHTMULT + 0.0) + 0.03;
	
	//compensate for low-light artifacts
	color = color+0.029;

	//calculate double curve
	float dbr = -color.r + 1.4;
	float dbg = -color.g + 1.4;
	float dbb = -color.b + 1.4;
	
	//fade between simple gamma up curve and double curve
	float pr = mix(dbr, 0.65, 0.7);
	float pg = mix(dbg, 0.65, 0.7);
	float pb = mix(dbb, 0.65, 0.7);
	
	color.r = pow((color.r * 0.95 - 0.02), pr);
	color.g = pow((color.g * 0.95 - 0.015), pg);
	color.b = pow((color.b * 0.99 + 0.01), pb);
#endif

	//Color boosting
	color.r = (color.r)*(COLOR_BOOST + 1.0) + (color.g + color.b)*(-COLOR_BOOST);
	color.g = (color.g)*(COLOR_BOOST + 1.0) + (color.r + color.b)*(-COLOR_BOOST);
	color.b = (color.b)*(COLOR_BOOST + 1.0) + (color.r + color.g)*(-COLOR_BOOST);
	
	//color.r = mix(((color.r)*(COLOR_BOOST + 1.0) + (hld.g + hld.b)*(-COLOR_BOOST)), hld.r, (max(((1-rgb)*2 - 1), 0.0)));
	//color.g = mix(((color.g)*(COLOR_BOOST + 1.0) + (hld.r + hld.b)*(-COLOR_BOOST)), hld.g, (max(((1-rgb)*2 - 1), 0.0)));
	//color.b = mix(((color.b)*(COLOR_BOOST + 1.0) + (hld.r + hld.g)*(-COLOR_BOOST)), hld.b, (max(((1-rgb)*2 - 1), 0.0)));

#ifdef HIGHDESATURATE


	//average
	float rgb = max(color.r, max(color.g, color.b))/2 + min(color.r, min(color.g, color.b))/2;

	//adjust black and white image to be brighter
	float bw = pow(rgb, 0.7);

	//mix between per-channel analysis and average analysis
	float rgbr = mix(rgb, color.r, 0.7);
	float rgbg = mix(rgb, color.g, 0.7);
	float rgbb = mix(rgb, color.b, 0.7);

	//calculate crossfade based on lum
	float mixfactorr = max(0.0, (rgbr*2 - 1));
	float mixfactorg = max(0.0, (rgbg*2 - 1));
	float mixfactorb = max(0.0, (rgbb*2 - 1));

	//crossfade between saturated and desaturated image
	float mixr = mix(color.r, bw, mixfactorr);
	float mixg = mix(color.g, bw, mixfactorg);
	float mixb = mix(color.b, bw, mixfactorb);

	//adjust level of desaturation
	color.r = clamp((mix(mixr, color.r, 0.0)), 0.0, 1.0);
	color.g = clamp((mix(mixg, color.g, 0.0)), 0.0, 1.0);
	color.b = clamp((mix(mixb, color.b, 0.0)), 0.0, 1.0);
	
	//desaturate blue channel
	color.b = color.b*0.7 + ((color.r + color.g)/2.0)*0.3;
	

	//hold color values for color boost
	//vec4 hld = color;

	
	

	
	//undo artifact compensation
	color = max(((color*1.10) - 0.06), 0.0);
	
	color = color * BRIGHTMULT;

	color.r = pow(color.r, GAMMA);
	color.g = pow(color.g, GAMMA);
	color.b = pow(color.b, GAMMA);
	
	color = color*(1.0 + DARKMULT) - DARKMULT;
	
#endif
	gl_FragColor = color;
	
// End of Main. -----------------
}
