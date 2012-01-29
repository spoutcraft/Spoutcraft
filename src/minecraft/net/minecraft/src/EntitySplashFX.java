package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer; //Spout HD
import net.minecraft.src.EntityRainFX;
import net.minecraft.src.World;

public class EntitySplashFX extends EntityRainFX {

	public EntitySplashFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
		super(var1, var2, var4, var6);
		this.particleGravity = 0.04F;
		this.setParticleTextureIndex(this.getParticleTextureIndex() + 1);
		if (var10 == 0.0D && (var8 != 0.0D || var12 != 0.0D)) {
			this.motionX = var8;
			this.motionY = var10 + 0.1D;
			this.motionZ = var12;
		}
 //Spout HD start
		if (Colorizer.computeWaterColor(this.worldObj.getWorldChunkManager(), this.posX, this.posY, this.posZ)) {
			this.particleRed = Colorizer.waterColor[0];
			this.particleGreen = Colorizer.waterColor[1];
			this.particleBlue = Colorizer.waterColor[2];
		}
 //Spout HD end
	}
}
