package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer; //Spout HD
import net.minecraft.src.EntityFX;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class EntityBubbleFX extends EntityFX {

	public EntityBubbleFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
		super(var1, var2, var4, var6, var8, var10, var12);
//Spout HD start
		if (Colorizer.computeWaterColor(this.worldObj.getWorldChunkManager(), this.posX, this.posY, this.posZ)) {
			this.particleRed = Colorizer.waterColor[0];
			this.particleGreen = Colorizer.waterColor[1];
			this.particleBlue = Colorizer.waterColor[2];
		}
		else {
			this.particleRed = 1.0F;
			this.particleGreen = 1.0F;
			this.particleBlue = 1.0F;
		}
 //Spout HD end
		this.setParticleTextureIndex(32);
		this.setSize(0.02F, 0.02F);
		this.particleScale *= this.rand.nextFloat() * 0.6F + 0.2F;
		this.motionX = var8 * 0.20000000298023224D + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.02F);
		this.motionY = var10 * 0.20000000298023224D + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.02F);
		this.motionZ = var12 * 0.20000000298023224D + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.02F);
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY += 0.0020D;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.8500000238418579D;
		this.motionY *= 0.8500000238418579D;
		this.motionZ *= 0.8500000238418579D;
		if (this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) != Material.water) {
			this.setEntityDead();
		}

		if (this.particleMaxAge-- <= 0) {
			this.setEntityDead();
		}

	}
}
