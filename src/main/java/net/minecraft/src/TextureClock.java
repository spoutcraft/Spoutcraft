package net.minecraft.src;

import com.prupe.mcpatcher.mod.FancyDial;
import net.minecraft.client.Minecraft;

public class TextureClock extends TextureStitched {
	public double field_94239_h;
	private double field_94240_i;

	public TextureClock() {
		super("clock");
		FancyDial.setup(this);
	}

	public void updateAnimation() {
		Minecraft var1 = Minecraft.getMinecraft();
		double var2 = 0.0D;

		if (var1.theWorld != null && var1.thePlayer != null) {
			float var4 = var1.theWorld.getCelestialAngle(1.0F);
			var2 = (double)var4;

			if (!var1.theWorld.provider.isSurfaceWorld()) {
				var2 = Math.random();
			}
		}

		double var7;

		for (var7 = var2 - this.field_94239_h; var7 < -0.5D; ++var7) {
			;
		}

		while (var7 >= 0.5D) {
			--var7;
		}

		if (var7 < -1.0D) {
			var7 = -1.0D;
		}

		if (var7 > 1.0D) {
			var7 = 1.0D;
		}

		this.field_94240_i += var7 * 0.1D;
		this.field_94240_i *= 0.8D;
		this.field_94239_h += this.field_94240_i;

		if (!FancyDial.update(this)) {
			int var6;

			for (var6 = (int)((this.field_94239_h + 1.0D) * (double)this.textureList.size()) % this.textureList.size(); var6 < 0; var6 = (var6 + this.textureList.size()) % this.textureList.size()) {
				;
			}

			if (var6 != this.frameCounter) {
				this.frameCounter = var6;
				this.textureSheet.copyFrom(this.originX, this.originY, (Texture)this.textureList.get(this.frameCounter), this.rotated);
			}
		}
	}
}
