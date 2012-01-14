package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer;//Spout
import net.minecraft.src.EntityFX;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

public class EntityReddustFX extends EntityFX {

	float reddustParticleScale;

	public EntityReddustFX(World var1, double var2, double var4, double var6, float var8, float var9, float var10) {
		this(var1, var2, var4, var6, 1.0F, var8, var9, var10);
	}

	public EntityReddustFX(World var1, double var2, double var4, double var6, float var8, float var9, float var10, float var11) {
		super(var1, var2, var4, var6, 0.0D, 0.0D, 0.0D);
		this.motionX *= 0.10000000149011612D;
		this.motionY *= 0.10000000149011612D;
		this.motionZ *= 0.10000000149011612D;
		if (var9 == 0.0F) {
			var9 = 1.0F;
//Spout HD start
			if (Colorizer.computeRedstoneWireColor(15)) {
				var9 = Colorizer.redstoneWireRed;
				var10 = Colorizer.redstoneWireGreen;
				var11 = Colorizer.redstoneWireBlue;
			}
//Spout HD end
		}

		float var12 = (float)Math.random() * 0.4F + 0.6F;
		this.particleRed = ((float)(Math.random() * 0.20000000298023224D) + 0.8F) * var9 * var12;
		this.particleGreen = ((float)(Math.random() * 0.20000000298023224D) + 0.8F) * var10 * var12;
		this.particleBlue = ((float)(Math.random() * 0.20000000298023224D) + 0.8F) * var11 * var12;
		this.particleScale *= 0.75F;
		this.particleScale *= var8;
		this.reddustParticleScale = this.particleScale;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
		this.particleMaxAge = (int)((float)this.particleMaxAge * var8);
		this.noClip = false;
	}

	public void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		float var8 = ((float)this.particleAge + var2) / (float)this.particleMaxAge * 32.0F;
		if (var8 < 0.0F) {
			var8 = 0.0F;
		}

		if (var8 > 1.0F) {
			var8 = 1.0F;
		}

		this.particleScale = this.reddustParticleScale * var8;
		super.renderParticle(var1, var2, var3, var4, var5, var6, var7);
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

		this.motionX *= 0.9599999785423279D;
		this.motionY *= 0.9599999785423279D;
		this.motionZ *= 0.9599999785423279D;
		if (this.onGround) {
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}

	}
}
