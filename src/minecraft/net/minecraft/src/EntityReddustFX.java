package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer; //Spout

public class EntityReddustFX extends EntityFX {
	float reddustParticleScale;

	public EntityReddustFX(World par1World, double par2, double par4, double par6, float par8, float par9, float par10) {
		this(par1World, par2, par4, par6, 1.0F, par8, par9, par10);
	}

	public EntityReddustFX(World par1World, double par2, double par4, double par6, float par8, float par9, float par10, float par11) {
		super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);
		this.motionX *= 0.1D;
		this.motionY *= 0.1D;
		this.motionZ *= 0.1D;
		if (par9 == 0.0F) {
			//Spout HD start
			if (Colorizer.computeRedstoneWireColor(15)) {
				par9 = Colorizer.setColor[0];
				par10 = Colorizer.setColor[1];
				par11 = Colorizer.setColor[2];
			}
			//Spout HD end
		}

		float var12 = (float)Math.random() * 0.4F + 0.6F;
		this.particleRed = ((float)(Math.random() * 0.2D) + 0.8F) * par9 * var12;
		this.particleGreen = ((float)(Math.random() * 0.2D) + 0.8F) * par10 * var12;
		this.particleBlue = ((float)(Math.random() * 0.2D) + 0.8F) * par11 * var12;
		this.particleScale *= 0.75F;
		this.particleScale *= par8;
		this.reddustParticleScale = this.particleScale;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
		this.particleMaxAge = (int)((float)this.particleMaxAge * par8);
		this.noClip = false;
	}

	public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {
		float var8 = ((float)this.particleAge + par2) / (float)this.particleMaxAge * 32.0F;
		if (var8 < 0.0F) {
			var8 = 0.0F;
		}

		if (var8 > 1.0F) {
			var8 = 1.0F;
		}

		this.particleScale = this.reddustParticleScale * var8;
		super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if (this.particleAge++ >= this.particleMaxAge) {
			this.setEntityDead();
		}

		this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		if (this.posY == this.prevPosY) {
			this.motionX *= 1.1D;
			this.motionZ *= 1.1D;
		}

		this.motionX *= 0.96D;
		this.motionY *= 0.96D;
		this.motionZ *= 0.96D;
		if (this.onGround) {
			this.motionX *= 0.7D;
			this.motionZ *= 0.7D;
		}
	}
}
