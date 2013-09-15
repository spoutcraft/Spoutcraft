package net.minecraft.src;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.prupe.mcpatcher.hd.CustomAnimation;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TextureManager implements Tickable, ResourceManagerReloadListener {
	public final Map mapTextureObjects = Maps.newHashMap();
	private final Map mapResourceLocations = Maps.newHashMap();
	private final List listTickables = Lists.newArrayList();
	private final Map mapTextureCounters = Maps.newHashMap();
	private ResourceManager theResourceManager;

	public TextureManager(ResourceManager par1ResourceManager) {
		this.theResourceManager = par1ResourceManager;
	}

	public void bindTexture(ResourceLocation par1ResourceLocation) {
		Object var2 = (TextureObject)this.mapTextureObjects.get(par1ResourceLocation);

		if (var2 == null) {
			var2 = new SimpleTexture(par1ResourceLocation);
			this.loadTexture(par1ResourceLocation, (TextureObject)var2);
		}

		TextureUtil.bindTexture(((TextureObject)var2).getGlTextureId());
	}

	public ResourceLocation getResourceLocation(int par1) {
		return (ResourceLocation)this.mapResourceLocations.get(Integer.valueOf(par1));
	}

	public boolean loadTextureMap(ResourceLocation par1ResourceLocation, TextureMap par2TextureMap) {
		if (this.loadTickableTexture(par1ResourceLocation, par2TextureMap)) {
			this.mapResourceLocations.put(Integer.valueOf(par2TextureMap.getTextureType()), par1ResourceLocation);
			return true;
		} else {
			return false;
		}
	}

	public boolean loadTickableTexture(ResourceLocation par1ResourceLocation, TickableTextureObject par2TickableTextureObject) {
		if (this.loadTexture(par1ResourceLocation, par2TickableTextureObject)) {
			this.listTickables.add(par2TickableTextureObject);
			return true;
		} else {
			return false;
		}
	}

	public boolean loadTexture(ResourceLocation par1ResourceLocation, TextureObject par2TextureObject) {
		boolean var3 = true;

		try {
			((TextureObject)par2TextureObject).loadTexture(this.theResourceManager);
		} catch (IOException var8) {
			Minecraft.getMinecraft().getLogAgent().logWarningException("Failed to load texture: " + par1ResourceLocation, var8);
			par2TextureObject = TextureUtil.missingTexture;
			this.mapTextureObjects.put(par1ResourceLocation, par2TextureObject);
			var3 = false;
		} catch (Throwable var9) {
			CrashReport var5 = CrashReport.makeCrashReport(var9, "Registering texture");
			CrashReportCategory var6 = var5.makeCategory("Resource location being registered");
			var6.addCrashSection("Resource location", par1ResourceLocation);
			var6.addCrashSectionCallable("Texture object class", new TextureManagerINNER1(this, (TextureObject)par2TextureObject));
			throw new ReportedException(var5);
		}

		this.mapTextureObjects.put(par1ResourceLocation, par2TextureObject);
		return var3;
	}

	public TextureObject getTexture(ResourceLocation par1ResourceLocation) {
		return (TextureObject)this.mapTextureObjects.get(par1ResourceLocation);
	}

	public ResourceLocation getDynamicTextureLocation(String par1Str, DynamicTexture par2DynamicTexture) {
		Integer var3 = (Integer)this.mapTextureCounters.get(par1Str);

		if (var3 == null) {
			var3 = Integer.valueOf(1);
		} else {
			var3 = Integer.valueOf(var3.intValue() + 1);
		}

		this.mapTextureCounters.put(par1Str, var3);
		ResourceLocation var4 = new ResourceLocation(String.format("dynamic/%s_%d", new Object[] {par1Str, var3}));
		this.loadTexture(var4, par2DynamicTexture);
		return var4;
	}

	public void tick() {
		Iterator var1 = this.listTickables.iterator();

		while (var1.hasNext()) {
			Tickable var2 = (Tickable)var1.next();
			var2.tick();
		}

		CustomAnimation.updateAll();
	}

	public void onResourceManagerReload(ResourceManager par1ResourceManager) {
		Iterator var2 = this.mapTextureObjects.entrySet().iterator();

		while (var2.hasNext()) {
			Entry var3 = (Entry)var2.next();
			this.loadTexture((ResourceLocation)var3.getKey(), (TextureObject)var3.getValue());
		}
	}
	
	// Spout Start
	public void bindTexture(int texture) {
		TextureUtil.bindTexture(texture);
	}
}