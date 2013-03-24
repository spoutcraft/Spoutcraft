package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.mod.SkyRenderer$Layer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import net.minecraft.src.Tessellator;

class SkyRenderer$WorldEntry {
	private final int worldType;
	private final ArrayList skies;
	private final HashMap objects;
	private final HashSet textures;

	SkyRenderer$WorldEntry(int var1) {
		this.worldType = var1;
		this.skies = new ArrayList();
		this.objects = new HashMap();
		this.textures = new HashSet();
		this.loadSkies();
		this.loadCelestialObject("sun", "/environment/sun.png");
		this.loadCelestialObject("moon", "/environment/moon_phases.png");
	}

	private void loadSkies() {
		int var1 = -1;

		while (true) {
			String var2 = "/environment/sky" + this.worldType + "/sky" + (var1 < 0 ? "" : "" + var1);
			SkyRenderer$Layer var3 = SkyRenderer$Layer.create(var2);

			if (var3 == null) {
				if (var1 > 0) {
					return;
				}
			} else if (var3.valid) {
				this.skies.add(var3);
				this.textures.add(SkyRenderer$Layer.access$300(var3));
			}

			++var1;
		}
	}

	private void loadCelestialObject(String var1, String var2) {
		String var3 = "/environment/sky" + this.worldType + "/" + var1;
		Properties var4 = TexturePackAPI.getProperties(var3 + ".properties");

		if (var4 != null) {
			var4.setProperty("fade", "false");
			var4.setProperty("rotate", "true");
			SkyRenderer$Layer var5 = new SkyRenderer$Layer(var3, var4);

			if (var5.valid) {
				this.objects.put(var2, var5);
			}
		}
	}

	boolean active() {
		return !this.skies.isEmpty() || !this.objects.isEmpty();
	}

	void renderAll(Tessellator var1) {
		HashSet var2 = new HashSet();
		Iterator var3 = this.skies.iterator();

		while (var3.hasNext()) {
			SkyRenderer$Layer var4 = (SkyRenderer$Layer)var3.next();

			if (var4.prepare()) {
				var2.add(SkyRenderer$Layer.access$300(var4));
			}
		}

		HashSet var6 = new HashSet();
		var6.addAll(this.textures);
		var6.removeAll(var2);
		Iterator var7 = var6.iterator();

		while (var7.hasNext()) {
			String var5 = (String)var7.next();
			TexturePackAPI.unloadTexture(var5);
		}

		var7 = this.skies.iterator();

		while (var7.hasNext()) {
			SkyRenderer$Layer var8 = (SkyRenderer$Layer)var7.next();

			if (var8.brightness > 0.0F) {
				var8.render(var1);
				SkyRenderer$Layer.clearBlendingMethod();
			}
		}
	}

	SkyRenderer$Layer getCelestialObject(String var1) {
		return (SkyRenderer$Layer)this.objects.get(var1);
	}

	void unloadTextures() {
		Iterator var1 = this.skies.iterator();

		while (var1.hasNext()) {
			SkyRenderer$Layer var2 = (SkyRenderer$Layer)var1.next();
			TexturePackAPI.unloadTexture(SkyRenderer$Layer.access$300(var2));
		}
	}
}
