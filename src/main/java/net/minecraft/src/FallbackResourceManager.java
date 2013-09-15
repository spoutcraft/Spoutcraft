package net.minecraft.src;

import com.google.common.collect.Lists;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FallbackResourceManager implements ResourceManager {
	public final List field_110540_a = new ArrayList();
	private final MetadataSerializer field_110539_b;

	public FallbackResourceManager(MetadataSerializer par1MetadataSerializer) {
		this.field_110539_b = par1MetadataSerializer;
	}

	public void func_110538_a(ResourcePack par1ResourcePack) {
		this.field_110540_a.add(par1ResourcePack);
	}

	public Set func_135055_a() {
		return null;
	}

	public Resource func_110536_a(ResourceLocation par1ResourceLocation) throws IOException {
		ResourcePack var2 = null;
		ResourceLocation var3 = func_110537_b(par1ResourceLocation);

		for (int var4 = this.field_110540_a.size() - 1; var4 >= 0; --var4) {
			ResourcePack var5 = (ResourcePack)this.field_110540_a.get(var4);

			if (var2 == null && var5.func_110589_b(var3)) {
				var2 = var5;
			}

			if (var5.func_110589_b(par1ResourceLocation)) {
				InputStream var6 = null;

				if (var2 != null) {
					var6 = var2.func_110590_a(var3);
				}

				return new SimpleResource(par1ResourceLocation, var5.func_110590_a(par1ResourceLocation), var6, this.field_110539_b);
			}
		}

		throw new FileNotFoundException(par1ResourceLocation.toString());
	}

	public List func_135056_b(ResourceLocation par1ResourceLocation) throws IOException {
		ArrayList var2 = Lists.newArrayList();
		ResourceLocation var3 = func_110537_b(par1ResourceLocation);
		Iterator var4 = this.field_110540_a.iterator();

		while (var4.hasNext()) {
			ResourcePack var5 = (ResourcePack)var4.next();

			if (var5.func_110589_b(par1ResourceLocation)) {
				InputStream var6 = var5.func_110589_b(var3) ? var5.func_110590_a(var3) : null;
				var2.add(new SimpleResource(par1ResourceLocation, var5.func_110590_a(par1ResourceLocation), var6, this.field_110539_b));
			}
		}

		if (var2.isEmpty()) {
			throw new FileNotFoundException(par1ResourceLocation.toString());
		} else {
			return var2;
		}
	}

	static ResourceLocation func_110537_b(ResourceLocation par0ResourceLocation) {
		return new ResourceLocation(par0ResourceLocation.func_110624_b(), par0ResourceLocation.func_110623_a() + ".mcmeta");
	}
}
