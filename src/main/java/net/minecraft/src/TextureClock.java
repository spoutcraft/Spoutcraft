package net.minecraft.src;

import com.prupe.mcpatcher.hd.FancyDial;

public class TextureClock extends TextureAtlasSprite {
	public double field_94239_h;
	private double field_94240_i;

	public TextureClock(String par1Str) {
		super(par1Str);
		FancyDial.setup(this);
	}

	public void updateAnimation() {
		if (!this.field_110976_a.isEmpty()) {
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

				for (var6 = (int)((this.field_94239_h + 1.0D) * (double)this.field_110976_a.size()) % this.field_110976_a.size(); var6 < 0; var6 = (var6 + this.field_110976_a.size()) % this.field_110976_a.size()) {
					;
				}

				if (var6 != this.field_110973_g) {
					this.field_110973_g = var6;
					TextureUtil.func_110998_a((int[])this.field_110976_a.get(this.field_110973_g), this.field_130223_c, this.field_130224_d, this.field_110975_c, this.field_110974_d, false, false);
				}
			}
		}
	}
}

