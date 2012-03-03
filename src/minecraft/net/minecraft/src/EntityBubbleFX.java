package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer; //Spout HD

public class EntityBubbleFX extends EntityFX {
	public EntityBubbleFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
		super(par1World, par2, par4, par6, par8, par10, par12);
		//Spout HD start
		if (Colorizer.computeWaterColor(this.posX, this.posY, this.posZ)) {
			this.particleRed = Colorizer.waterColor[0];
			this.particleGreen = Colorizer.waterColor[1];
			this.particleBlue = Colorizer.waterColor[2];
		} else {
			this.particleRed = 1.0F;
			this.particleGreen = 1.0F;
			this.particleBlue = 1.0F;
		}
 		//Spout HD end
		this.setParticleTextureIndex(32);
		this.setSize(0.02F, 0.02F);
		this.particleScale *= this.rand.nextFloat() * 0.6F + 0.2F;
		this.motionX = par8 * 0.2D + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.02F);
		this.motionY = par10 * 0.2D + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.02F);
		this.motionZ = par12 * 0.2D + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.02F);
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY += 0.002D;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.85D;
		this.motionY *= 0.85D;
		this.motionZ *= 0.85D;
		if (this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) != Material.water) {
			this.setEntityDead();
		}

		if (this.particleMaxAge-- <= 0) {
			this.setEntityDead();
		}
	}
}
