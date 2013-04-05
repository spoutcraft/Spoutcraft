package net.minecraft.src;

// MCPatcher Start
import com.prupe.mcpatcher.mod.ColorizeBlock;
// MCPatcher End

public class EntitySplashFX extends EntityRainFX {
	public EntitySplashFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
		super(par1World, par2, par4, par6);
		this.particleGravity = 0.04F;
		this.nextTextureIndexX();

		if (par10 == 0.0D && (par8 != 0.0D || par12 != 0.0D)) {
			this.motionX = par8;
			this.motionY = par10 + 0.1D;
			this.motionZ = par12;
		}

		// MCPatcher Start
		if (ColorizeBlock.computeWaterColor(this.posX, this.posY, this.posZ)) {
			this.particleRed = ColorizeBlock.waterColor[0];
			this.particleGreen = ColorizeBlock.waterColor[1];
			this.particleBlue = ColorizeBlock.waterColor[2];
		}
		// MCPatcher End
	}
}
