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
	private static final Joiner joinerResourcePacks = Joiner.on(", ");
	public final Map domainResourceManagers = Maps.newHashMap();
	private final List reloadListeners = Lists.newArrayList();
	private final Set setResourceDomains = Sets.newLinkedHashSet();
	private final MetadataSerializer rmMetadataSerializer;

	public SimpleReloadableResourceManager(MetadataSerializer par1MetadataSerializer) {
		this.rmMetadataSerializer = par1MetadataSerializer;
	}

	public void reloadResourcePack(ResourcePack par1ResourcePack) {
		FallbackResourceManager var4;

		for (Iterator var2 = par1ResourcePack.getResourceDomains().iterator(); var2.hasNext(); var4.addResourcePack(par1ResourcePack)) {
			String var3 = (String)var2.next();
			this.setResourceDomains.add(var3);
			var4 = (FallbackResourceManager)this.domainResourceManagers.get(var3);

			if (var4 == null) {
				var4 = new FallbackResourceManager(this.rmMetadataSerializer);
				this.domainResourceManagers.put(var3, var4);
			}
		}
	}

	public Set getResourceDomains() {
		return this.setResourceDomains;
	}

	public Resource getResource(ResourceLocation par1ResourceLocation) throws IOException {
		ResourceManager var2 = (ResourceManager)this.domainResourceManagers.get(par1ResourceLocation.getResourceDomain());

		if (var2 != null) {
			return var2.getResource(par1ResourceLocation);
		} else {
			throw new FileNotFoundException(par1ResourceLocation.toString());
		}
	}

	public List getAllResources(ResourceLocation par1ResourceLocation) throws IOException {
		ResourceManager var2 = (ResourceManager)this.domainResourceManagers.get(par1ResourceLocation.getResourceDomain());

		if (var2 != null) {
			return var2.getAllResources(par1ResourceLocation);
		} else {
			throw new FileNotFoundException(par1ResourceLocation.toString());
		}
	}

	private void clearResources() {
		this.domainResourceManagers.clear();
		this.setResourceDomains.clear();
	}

	public void reloadResources(List par1List) {
		this.clearResources();
		Minecraft.getMinecraft().getLogAgent().logInfo("Reloading ResourceManager: " + joinerResourcePacks.join(Iterables.transform(par1List, new SimpleReloadableResourceManagerINNER1(this))));
		Iterator var2 = par1List.iterator();

		while (var2.hasNext()) {
			ResourcePack var3 = (ResourcePack)var2.next();
			this.reloadResourcePack(var3);
		}

		this.notifyReloadListeners();
	}

	public void registerReloadListener(ResourceManagerReloadListener par1ResourceManagerReloadListener) {
		this.reloadListeners.add(par1ResourceManagerReloadListener);
		par1ResourceManagerReloadListener.onResourceManagerReload(this);
	}

	private void notifyReloadListeners() {
		TexturePackChangeHandler.beforeChange1(false);
		Iterator var1 = this.reloadListeners.iterator();

		while (var1.hasNext()) {
			ResourceManagerReloadListener var2 = (ResourceManagerReloadListener)var1.next();
			var2.onResourceManagerReload(this);
		}

		TexturePackChangeHandler.afterChange1(false);
	}
}
