package net.minecraft.src;

public class EntityFX extends Entity {
	protected int particleTextureIndexX;
	protected int particleTextureIndexY; 
	protected float particleTextureJitterX;
	protected float particleTextureJitterY;
	// Spout Start - protected to public
	public int particleAge;
	public int particleMaxAge;
	public float particleScale;
	public float particleGravity;
	// Spout End

	/** The red amount of color. Used as a percentage, 1.0 = 255 and 0.0 = 0. */
	// Spout Start - protected to public
	public float particleRed;
	// Spout End

	/**
	 * The green amount of color. Used as a percentage, 1.0 = 255 and 0.0 = 0.
	 */
	// Spout Start - protected to public
	public float particleGreen;
	// Spout End

	/**
	 * The blue amount of color. Used as a percentage, 1.0 = 255 and 0.0 = 0.
	 */
	// Spout Start - protected to public
	public float particleBlue;
	// Spout End

	/** Particle alpha */
	protected float particleAlpha;
	protected Icon particleTextureIndex;
	public static double interpPosX;
	public static double interpPosY;
	public static double interpPosZ;

	// Spout Start - protected to public
	public EntityFX(World par1World, double par2, double par4, double par6) {
	// Spout End
		super(par1World);
		this.particleAge = 0;
		this.particleMaxAge = 0;
		this.particleAlpha = 1.0F;
		this.particleTextureIndex = null;
		this.setSize(0.2F, 0.2F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(par2, par4, par6);
		this.lastTickPosX = par2;
		this.lastTickPosY = par4;
		this.lastTickPosZ = par6;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleTextureJitterX = this.rand.nextFloat() * 3.0F;
		this.particleTextureJitterY = this.rand.nextFloat() * 3.0F;
		this.particleScale = (this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
		this.particleMaxAge = (int)(4.0F / (this.rand.nextFloat() * 0.9F + 0.1F));
		this.particleAge = 0;
	}

	public EntityFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
		this(par1World, par2, par4, par6);
		this.motionX = par8 + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.4F);
		this.motionY = par10 + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.4F);
		this.motionZ = par12 + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.4F);
		float var14 = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
		float var15 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
		this.motionX = this.motionX / (double)var15 * (double)var14 * 0.4000000059604645D;
		this.motionY = this.motionY / (double)var15 * (double)var14 * 0.4000000059604645D + 0.10000000149011612D;
		this.motionZ = this.motionZ / (double)var15 * (double)var14 * 0.4000000059604645D;
	}

	public EntityFX multiplyVelocity(float par1) {
		this.motionX *= (double)par1;
		this.motionY = (this.motionY - 0.10000000149011612D) * (double)par1 + 0.10000000149011612D;
		this.motionZ *= (double)par1;
		return this;
	}

	public EntityFX multipleParticleScaleBy(float par1) {
		this.setSize(0.2F * par1, 0.2F * par1);
		this.particleScale *= par1;
		return this;
	}

	public void setRBGColorF(float par1, float par2, float par3) {
		this.particleRed = par1;
		this.particleGreen = par2;
		this.particleBlue = par3;
	}

	/**
	 * Sets the particle alpha (float)
	 */
	public void setAlphaF(float par1) {
		this.particleAlpha = par1;
	}

	public float getRedColorF() {
		return this.particleRed;
	}

	public float getGreenColorF() {
		return this.particleGreen;
	}

	public float getBlueColorF() {
		return this.particleBlue;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	protected boolean canTriggerWalking() {
		return false;
	}

	protected void entityInit() {}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge) {
			this.setDead();
		}

		this.motionY -= 0.04D * (double)this.particleGravity;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;

		if (this.onGround) {
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}
	}

	public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {
		float var8 = (float)this.particleTextureIndexX / 16.0F;
		float var9 = var8 + 0.0624375F;
		float var10 = (float)this.particleTextureIndexY / 16.0F;
		float var11 = var10 + 0.0624375F;
		float var12 = 0.1F * this.particleScale;

		if (this.particleTextureIndex != null) {
			 var8 = this.particleTextureIndex.getMinU();
			 var9 = this.particleTextureIndex.getMaxU();
			 var10 = this.particleTextureIndex.getMinV();
			 var11 = this.particleTextureIndex.getMaxV(); 
		}

		float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)par2 - interpPosX);
		float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)par2 - interpPosY);
		float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)par2 - interpPosZ);
		float var16 = 1.0F;
		par1Tessellator.setColorRGBA_F(this.particleRed * var16, this.particleGreen * var16, this.particleBlue * var16, this.particleAlpha);
		par1Tessellator.addVertexWithUV((double)(var13 - par3 * var12 - par6 * var12), (double)(var14 - par4 * var12), (double)(var15 - par5 * var12 - par7 * var12), (double)var9, (double)var11);
		par1Tessellator.addVertexWithUV((double)(var13 - par3 * var12 + par6 * var12), (double)(var14 + par4 * var12), (double)(var15 - par5 * var12 + par7 * var12), (double)var9, (double)var10);
		par1Tessellator.addVertexWithUV((double)(var13 + par3 * var12 + par6 * var12), (double)(var14 + par4 * var12), (double)(var15 + par5 * var12 + par7 * var12), (double)var8, (double)var10);
		par1Tessellator.addVertexWithUV((double)(var13 + par3 * var12 - par6 * var12), (double)(var14 - par4 * var12), (double)(var15 + par5 * var12 - par7 * var12), (double)var8, (double)var11);
	}

	public int getFXLayer() {
		return 0;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {}

	public void func_94052_a(RenderEngine par1RenderEngine, Icon par2Icon) {
		if (this.getFXLayer() == 1) {
			this.particleTextureIndex = par2Icon;
		} else {
			if (this.getFXLayer() != 2) {
				throw new RuntimeException("Invalid call to Particle.setTex, use coordinate methods");
			}

			this.particleTextureIndex = par2Icon;
		}
	}

	/**
	 * Public method to set private field particleTextureIndex.
	 */
	public void setParticleTextureIndex(int par1) {
		if (this.getFXLayer() != 0) {
			throw new RuntimeException("Invalid call to Particle.setMiscTex");
		} else {
			this.particleTextureIndexX = par1 % 16;
			this.particleTextureIndexY = par1 / 16; 
		}
	}

	public void nextTextureIndexX() {
		++this.particleTextureIndexX; 
	}

	/**
	 * If returns false, the item will not inflict any damage against entities.
	 */
	public boolean canAttackWithItem() {
		return false;
	}

	public String toString() {
		return this.getClass().getSimpleName() + ", Pos (" + this.posX + "," + this.posY + "," + this.posZ + "), RGBA (" + this.particleRed + "," + this.particleGreen + "," + this.particleBlue + "," + this.particleAlpha + "), Age " + this.particleAge;
	}
}
