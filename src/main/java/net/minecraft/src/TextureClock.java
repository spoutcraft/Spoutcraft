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

	public void func_94219_l() {
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

			for (var6 = (int)((this.field_94239_h + 1.0D) * (double)this.field_94226_b.size()) % this.field_94226_b.size(); var6 < 0; var6 = (var6 + this.field_94226_b.size()) % this.field_94226_b.size()) {
				;
			}

			if (var6 != this.field_94222_f) {
				this.field_94222_f = var6;
				this.field_94228_a.func_94281_a(this.field_94224_d, this.field_94225_e, (Texture)this.field_94226_b.get(this.field_94222_f), this.field_94227_c);
			}
		}
	}
}