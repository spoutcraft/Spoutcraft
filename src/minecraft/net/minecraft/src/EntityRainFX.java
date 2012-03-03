package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer; //Spout

public class EntityRainFX extends EntityFX {
	public EntityRainFX(World par1World, double par2, double par4, double par6) {
		super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);
		this.motionX *= 0.3D;
		this.motionY = (double)((float)Math.random() * 0.2F + 0.1F);
		this.motionZ *= 0.3D;
		//Spout HD start
		if (Colorizer.computeWaterColor(this.posX, this.posY, this.posZ)) {
			this.particleRed = Colorizer.waterColor[0];
			this.particleGreen = Colorizer.waterColor[1];
			this.particleBlue = Colorizer.waterColor[2];
		} else {
			this.particleRed = 0.2F;
			this.particleGreen = 0.3F;
			this.particleBlue = 1.0F;
		}
		//Spout HD end

		this.setParticleTextureIndex(19 + this.rand.nextInt(4));
		this.setSize(0.01F, 0.01F);
		this.particleGravity = 0.06F;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
	}

	public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {
		super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= (double)this.particleGravity;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.98D;
		this.motionY *= 0.98D;
		this.motionZ *= 0.98D;
		if (this.particleMaxAge-- <= 0) {
			this.setEntityDead();
		}

		if (this.onGround) {
			if (Math.random() < 0.5D) {
				this.setEntityDead();
			}

			this.motionX *= 0.7D;
			this.motionZ *= 0.7D;
		}

		Material var1 = this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
		if (var1.isLiquid() || var1.isSolid()) {
			double var2 = (double)((float)(MathHelper.floor_double(this.posY) + 1) - BlockFluid.getFluidHeightPercent(this.worldObj.getBlockMetadata(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))));
			if (this.posY < var2) {
				this.setEntityDead();
			}
		}
	}
}
