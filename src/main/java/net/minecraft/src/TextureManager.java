package net.minecraft.src;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.prupe.mcpatcher.hd.CustomAnimation;

public class TextureManager implements Tickable, ResourceManagerReloadListener {
	public final Map field_110585_a = Maps.newHashMap();
	private final Map field_130089_b = Maps.newHashMap();
	private final List field_110583_b = Lists.newArrayList();
	private final Map field_110584_c = Maps.newHashMap();
	private ResourceManager field_110582_d;

	public TextureManager(ResourceManager par1ResourceManager) {
		this.field_110582_d = par1ResourceManager;
	}

	public void func_110577_a(ResourceLocation par1ResourceLocation) {
		Object var2 = (TextureObject)this.field_110585_a.get(par1ResourceLocation);

		if (var2 == null) {
			var2 = new SimpleTexture(par1ResourceLocation);
			this.func_110579_a(par1ResourceLocation, (TextureObject)var2);
		}

		TextureUtil.bindTexture(((TextureObject)var2).func_110552_b());
	}

	public ResourceLocation func_130087_a(int par1) {
		return (ResourceLocation)this.field_130089_b.get(Integer.valueOf(par1));
	}

	public boolean func_130088_a(ResourceLocation par1ResourceLocation, TextureMap par2TextureMap) {
		if (this.func_110580_a(par1ResourceLocation, par2TextureMap)) {
			this.field_130089_b.put(Integer.valueOf(par2TextureMap.func_130086_a()), par1ResourceLocation);
			return true;
		} else {
			return false;
		}
	}

	public boolean func_110580_a(ResourceLocation par1ResourceLocation, TickableTextureObject par2TickableTextureObject) {
		if (this.func_110579_a(par1ResourceLocation, par2TickableTextureObject)) {
			this.field_110583_b.add(par2TickableTextureObject);
			return true;
		} else {
			return false;
		}
	}

	public boolean func_110579_a(ResourceLocation par1ResourceLocation, TextureObject par2TextureObject) {
		boolean var3 = true;

		try {
			((TextureObject)par2TextureObject).func_110551_a(this.field_110582_d);
		} catch (IOException var8) {
			Minecraft.getMinecraft().getLogAgent().logWarningException("Failed to load texture: " + par1ResourceLocation, var8);
			par2TextureObject = TextureUtil.field_111001_a;
			this.field_110585_a.put(par1ResourceLocation, par2TextureObject);
			var3 = false;
		} catch (Throwable var9) {
			CrashReport var5 = CrashReport.makeCrashReport(var9, "Registering texture");
			CrashReportCategory var6 = var5.makeCategory("Resource location being registered");
			var6.addCrashSection("Resource location", par1ResourceLocation);
			var6.addCrashSectionCallable("Texture object class", new TextureManagerINNER1(this, (TextureObject)par2TextureObject));
			throw new ReportedException(var5);
		}

		this.field_110585_a.put(par1ResourceLocation, par2TextureObject);
		return var3;
	}

	public TextureObject func_110581_b(ResourceLocation par1ResourceLocation) {
		return (TextureObject)this.field_110585_a.get(par1ResourceLocation);
	}

	public ResourceLocation func_110578_a(String par1Str, DynamicTexture par2DynamicTexture) {
		Integer var3 = (Integer)this.field_110584_c.get(par1Str);

		if (var3 == null) {
			var3 = Integer.valueOf(1);
		} else {
			var3 = Integer.valueOf(var3.intValue() + 1);
		}

		this.field_110584_c.put(par1Str, var3);
		ResourceLocation var4 = new ResourceLocation(String.format("dynamic/%s_%d", new Object[] {par1Str, var3}));
		this.func_110579_a(var4, par2DynamicTexture);
		return var4;
	}

	public void func_110550_d() {
		Iterator var1 = this.field_110583_b.iterator();

		while (var1.hasNext()) {
			Tickable var2 = (Tickable)var1.next();
			var2.func_110550_d();
		}

		CustomAnimation.updateAll();
	}

	public void func_110549_a(ResourceManager par1ResourceManager) {
		Iterator var2 = this.field_110585_a.entrySet().iterator();

		while (var2.hasNext()) {
			Entry var3 = (Entry)var2.next();
			this.func_110579_a((ResourceLocation)var3.getKey(), (TextureObject)var3.getValue());
		}
	}
}
