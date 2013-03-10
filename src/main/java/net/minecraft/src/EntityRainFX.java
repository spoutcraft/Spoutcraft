package net.minecraft.src;

// MCPatcher Start
import com.prupe.mcpatcher.mod.ColorizeBlock;
// MCPatcher End

public class EntityRainFX extends EntityFX {
	public EntityRainFX(World par1World, double par2, double par4, double par6) {
		super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);
		this.motionX *= 0.30000001192092896D;
		this.motionY = (double)((float)Math.random() * 0.2F + 0.1F);
		this.motionZ *= 0.30000001192092896D;

		// MCPatcher Start
		if (ColorizeBlock.computeWaterColor(this.posX, this.posY, this.posZ)) {
			this.particleRed = ColorizeBlock.waterColor[0];
			this.particleGreen = ColorizeBlock.waterColor[1];
			this.particleBlue = ColorizeBlock.waterColor[2];
		} else {
			this.particleRed = 0.2F;
			this.particleGreen = 0.3F;
			this.particleBlue = 1.0F;
		}
		// MCPatcher End

		this.setParticleTextureIndex(19 + this.rand.nextInt(4));
		this.setSize(0.01F, 0.01F);
		this.particleGravity = 0.06F;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= (double)this.particleGravity;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;

		if (this.particleMaxAge-- <= 0) {
			this.setDead();
		}

		if (this.onGround) {
			if (Math.random() < 0.5D) {
				this.setDead();
			}

			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}

		Material var1 = this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

		if (var1.isLiquid() || var1.isSolid()) {
			double var2 = (double)((float)(MathHelper.floor_double(this.posY) + 1) - BlockFluid.getFluidHeightPercent(this.worldObj.getBlockMetadata(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))));

			if (this.posY < var2) {
				this.setDead();
			}
		}
	}
}
