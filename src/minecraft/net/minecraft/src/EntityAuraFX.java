package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer; //Spout

public class EntityAuraFX extends EntityFX {
	public EntityAuraFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
		super(par1World, par2, par4, par6, par8, par10, par12);
		float var14 = this.rand.nextFloat() * 0.1F + 0.2F;
		this.particleRed = var14;
		this.particleGreen = var14;
		this.particleBlue = var14;
		this.setParticleTextureIndex(0);
		this.setSize(0.02F, 0.02F);
		this.particleScale *= this.rand.nextFloat() * 0.6F + 0.5F;
		this.motionX *= 0.02D;
		this.motionY *= 0.02D;
		this.motionZ *= 0.02D;
		this.particleMaxAge = (int)(20.0D / (Math.random() * 0.8D + 0.2D));
		this.noClip = true;
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.99D;
		this.motionY *= 0.99D;
		this.motionZ *= 0.99D;
		if (this.particleMaxAge-- <= 0) {
			this.setEntityDead();
		}
	}
//Spout start
	public EntityAuraFX colorize() {
		if (Colorizer.computeMyceliumParticleColor()) {
			this.particleRed = Colorizer.setColor[0];
			this.particleGreen = Colorizer.setColor[1];
			this.particleBlue = Colorizer.setColor[2];
		}

		return this;
	}
//Spout end
}
