package com.prupe.mcpatcher.sky;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.prupe.mcpatcher.TexturePackAPI;

import net.minecraft.src.ResourceLocation;
import net.minecraft.src.Tessellator;

class SkyRenderer$WorldEntry {
	private final int worldType;
	private final List<SkyRenderer$Layer> skies = new ArrayList();
	private final Map<ResourceLocation, SkyRenderer$Layer> objects = new HashMap();
	private final Set<ResourceLocation> textures = new HashSet();

	SkyRenderer$WorldEntry(int worldType) {
		this.worldType = worldType;
		this.loadSkies();
		this.loadCelestialObject("sun");
		this.loadCelestialObject("moon_phases");
	}

	private void loadSkies() {
		int i = -1;

		while (true) {
			String path = "sky/world" + this.worldType + "/sky" + (i < 0 ? "" : String.valueOf(i)) + ".properties";
			ResourceLocation resource = TexturePackAPI.newMCPatcherResourceLocation(path);
			SkyRenderer$Layer layer = SkyRenderer$Layer.create(resource);

			if (layer == null) {
				if (i > 0) {
					return;
				}
			} else if (layer.valid) {
				SkyRenderer.access$400().fine("loaded %s", new Object[] {resource});
				this.skies.add(layer);
				this.textures.add(SkyRenderer$Layer.access$300(layer));
			}

			++i;
		}
	}

	private void loadCelestialObject(String objName) {
		ResourceLocation textureName = new ResourceLocation("textures/environment/" + objName + ".png");
		ResourceLocation resource = TexturePackAPI.newMCPatcherResourceLocation("sky/world0/" + objName + ".properties");
		Properties properties = TexturePackAPI.getProperties(resource);

		if (properties != null) {
			properties.setProperty("fade", "false");
			properties.setProperty("rotate", "true");
			SkyRenderer$Layer layer = new SkyRenderer$Layer(resource, properties);

			if (layer.valid) {
				SkyRenderer.access$400().fine("using %s (%s) for the %s", new Object[] {resource, SkyRenderer$Layer.access$300(layer), objName});
				this.objects.put(textureName, layer);
			}
		}
	}

	boolean active() {
		return !this.skies.isEmpty() || !this.objects.isEmpty();
	}

	void renderAll(Tessellator tessellator) {
		HashSet texturesNeeded = new HashSet();
		Iterator texturesToUnload = this.skies.iterator();

		while (texturesToUnload.hasNext()) {
			SkyRenderer$Layer i$ = (SkyRenderer$Layer)texturesToUnload.next();

			if (i$.prepare()) {
				texturesNeeded.add(SkyRenderer$Layer.access$300(i$));
			}
		}

		HashSet texturesToUnload1 = new HashSet();
		texturesToUnload1.addAll(this.textures);
		texturesToUnload1.removeAll(texturesNeeded);
		Iterator i$1 = texturesToUnload1.iterator();

		while (i$1.hasNext()) {
			ResourceLocation layer = (ResourceLocation)i$1.next();
			TexturePackAPI.unloadTexture(layer);
		}

		i$1 = this.skies.iterator();

		while (i$1.hasNext()) {
			SkyRenderer$Layer layer1 = (SkyRenderer$Layer)i$1.next();

			if (layer1.brightness > 0.0F) {
				layer1.render(tessellator);
				SkyRenderer$Layer.clearBlendingMethod();
			}
		}
	}

	SkyRenderer$Layer getCelestialObject(ResourceLocation defaultTexture) {
		return (SkyRenderer$Layer)this.objects.get(defaultTexture);
	}

	void unloadTextures() {
		Iterator i$ = this.skies.iterator();

		while (i$.hasNext()) {
			SkyRenderer$Layer layer = (SkyRenderer$Layer)i$.next();
			TexturePackAPI.unloadTexture(SkyRenderer$Layer.access$300(layer));
		}
	}
}
