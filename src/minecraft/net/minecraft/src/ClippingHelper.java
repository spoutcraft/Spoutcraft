package net.minecraft.src;


public class ClippingHelper {

	public float[][] frustum = new float[16][16];
	public float[] projectionMatrix = new float[16];
	public float[] modelviewMatrix = new float[16];
	public float[] clippingMatrix = new float[16];


	public boolean isBoxInFrustum(double var1, double var3, double var5, double var7, double var9, double var11) {
//Spout Start
		for(int var13 = 0; var13 < 6; ++var13) {
			float var14 = (float)var1;
			float var15 = (float)var3;
			float var16 = (float)var5;
			float var17 = (float)var7;
			float var18 = (float)var9;
			float var19 = (float)var11;
			if(this.frustum[var13][0] * var14 + this.frustum[var13][1] * var15 + this.frustum[var13][2] * var16 + this.frustum[var13][3] <= 0.0F && this.frustum[var13][0] * var17 + this.frustum[var13][1] * var15 + this.frustum[var13][2] * var16 + this.frustum[var13][3] <= 0.0F && this.frustum[var13][0] * var14 + this.frustum[var13][1] * var18 + this.frustum[var13][2] * var16 + this.frustum[var13][3] <= 0.0F && this.frustum[var13][0] * var17 + this.frustum[var13][1] * var18 + this.frustum[var13][2] * var16 + this.frustum[var13][3] <= 0.0F && this.frustum[var13][0] * var14 + this.frustum[var13][1] * var15 + this.frustum[var13][2] * var19 + this.frustum[var13][3] <= 0.0F && this.frustum[var13][0] * var17 + this.frustum[var13][1] * var15 + this.frustum[var13][2] * var19 + this.frustum[var13][3] <= 0.0F && this.frustum[var13][0] * var14 + this.frustum[var13][1] * var18 + this.frustum[var13][2] * var19 + this.frustum[var13][3] <= 0.0F && this.frustum[var13][0] * var17 + this.frustum[var13][1] * var18 + this.frustum[var13][2] * var19 + this.frustum[var13][3] <= 0.0F) {
				return false;
			}
		}

		return true;
	}

	public boolean isBoxInFrustumFully(double var1, double var3, double var5, double var7, double var9, double var11) {
		for(int var13 = 0; var13 < 6; ++var13) {
			float var14 = (float)var1;
			float var15 = (float)var3;
			float var16 = (float)var5;
			float var17 = (float)var7;
			float var18 = (float)var9;
			float var19 = (float)var11;
			if(var13 < 4) {
				if(this.frustum[var13][0] * var14 + this.frustum[var13][1] * var15 + this.frustum[var13][2] * var16 + this.frustum[var13][3] <= 0.0F || this.frustum[var13][0] * var17 + this.frustum[var13][1] * var15 + this.frustum[var13][2] * var16 + this.frustum[var13][3] <= 0.0F || this.frustum[var13][0] * var14 + this.frustum[var13][1] * var18 + this.frustum[var13][2] * var16 + this.frustum[var13][3] <= 0.0F || this.frustum[var13][0] * var17 + this.frustum[var13][1] * var18 + this.frustum[var13][2] * var16 + this.frustum[var13][3] <= 0.0F || this.frustum[var13][0] * var14 + this.frustum[var13][1] * var15 + this.frustum[var13][2] * var19 + this.frustum[var13][3] <= 0.0F || this.frustum[var13][0] * var17 + this.frustum[var13][1] * var15 + this.frustum[var13][2] * var19 + this.frustum[var13][3] <= 0.0F || this.frustum[var13][0] * var14 + this.frustum[var13][1] * var18 + this.frustum[var13][2] * var19 + this.frustum[var13][3] <= 0.0F || this.frustum[var13][0] * var17 + this.frustum[var13][1] * var18 + this.frustum[var13][2] * var19 + this.frustum[var13][3] <= 0.0F) {
					return false;
				}
			} else if(this.frustum[var13][0] * var14 + this.frustum[var13][1] * var15 + this.frustum[var13][2] * var16 + this.frustum[var13][3] <= 0.0F && this.frustum[var13][0] * var17 + this.frustum[var13][1] * var15 + this.frustum[var13][2] * var16 + this.frustum[var13][3] <= 0.0F && this.frustum[var13][0] * var14 + this.frustum[var13][1] * var18 + this.frustum[var13][2] * var16 + this.frustum[var13][3] <= 0.0F && this.frustum[var13][0] * var17 + this.frustum[var13][1] * var18 + this.frustum[var13][2] * var16 + this.frustum[var13][3] <= 0.0F && this.frustum[var13][0] * var14 + this.frustum[var13][1] * var15 + this.frustum[var13][2] * var19 + this.frustum[var13][3] <= 0.0F && this.frustum[var13][0] * var17 + this.frustum[var13][1] * var15 + this.frustum[var13][2] * var19 + this.frustum[var13][3] <= 0.0F && this.frustum[var13][0] * var14 + this.frustum[var13][1] * var18 + this.frustum[var13][2] * var19 + this.frustum[var13][3] <= 0.0F && this.frustum[var13][0] * var17 + this.frustum[var13][1] * var18 + this.frustum[var13][2] * var19 + this.frustum[var13][3] <= 0.0F) {
				return false;
			}
		}

		return true;
	}
//Spout End 
}
