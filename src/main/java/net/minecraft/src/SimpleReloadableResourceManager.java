package net.minecraft.src;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleReloadableResourceManager implements ReloadableResourceManager {
	private static final Joiner field_130074_a = Joiner.on(", ");
	public final Map field_110548_a = Maps.newHashMap();
	private final List field_110546_b = Lists.newArrayList();
	private final Set field_135057_d = Sets.newLinkedHashSet();
	private final MetadataSerializer field_110547_c;

	public SimpleReloadableResourceManager(MetadataSerializer par1MetadataSerializer) {
		this.field_110547_c = par1MetadataSerializer;
	}

	public void func_110545_a(ResourcePack par1ResourcePack) {
		FallbackResourceManager var4;

		for (Iterator var2 = par1ResourcePack.func_110587_b().iterator(); var2.hasNext(); var4.func_110538_a(par1ResourcePack)) {
			String var3 = (String)var2.next();
			this.field_135057_d.add(var3);
			var4 = (FallbackResourceManager)this.field_110548_a.get(var3);

			if (var4 == null) {
				var4 = new FallbackResourceManager(this.field_110547_c);
				this.field_110548_a.put(var3, var4);
			}
		}
	}

	public Set func_135055_a() {
		return this.field_135057_d;
	}

	public Resource func_110536_a(ResourceLocation par1ResourceLocation) throws IOException {
		ResourceManager var2 = (ResourceManager)this.field_110548_a.get(par1ResourceLocation.func_110624_b());

		if (var2 != null) {
			return var2.func_110536_a(par1ResourceLocation);
		} else {
			throw new FileNotFoundException(par1ResourceLocation.toString());
		}
	}

	public List func_135056_b(ResourceLocation par1ResourceLocation) throws IOException {
		ResourceManager var2 = (ResourceManager)this.field_110548_a.get(par1ResourceLocation.func_110624_b());

		if (var2 != null) {
			return var2.func_135056_b(par1ResourceLocation);
		} else {
			throw new FileNotFoundException(par1ResourceLocation.toString());
		}
	}

	private void func_110543_a() {
		this.field_110548_a.clear();
		this.field_135057_d.clear();
	}

	public void func_110541_a(List par1List) {
		this.func_110543_a();
		Minecraft.getMinecraft().getLogAgent().logInfo("Reloading ResourceManager: " + field_130074_a.join(Iterables.transform(par1List, new SimpleReloadableResourceManagerINNER1(this))));
		Iterator var2 = par1List.iterator();

		while (var2.hasNext()) {
			ResourcePack var3 = (ResourcePack)var2.next();
			this.func_110545_a(var3);
		}

		this.func_110544_b();
	}

	public void func_110542_a(ResourceManagerReloadListener par1ResourceManagerReloadListener) {
		this.field_110546_b.add(par1ResourceManagerReloadListener);
		par1ResourceManagerReloadListener.func_110549_a(this);
	}

	private void func_110544_b() {
		TexturePackChangeHandler.beforeChange1(false);
		Iterator var1 = this.field_110546_b.iterator();

		while (var1.hasNext()) {
			ResourceManagerReloadListener var2 = (ResourceManagerReloadListener)var1.next();
			var2.func_110549_a(this);
		}

		TexturePackChangeHandler.afterChange1(false);
	}
}
