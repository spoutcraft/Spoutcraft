/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.client.player.accessories;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.RenderPlayer;
import org.apache.commons.lang3.tuple.Pair;
import org.spoutcraft.client.HDImageBufferDownload;
import org.spoutcraft.client.special.VIP;

public class AccessoryHandler {

	private Set<Pair<Accessory, String>> acs;
	private static Set<String> downloaded = new HashSet<String>();
	
	public void addAccessory(Accessory n, String url) {
		if(!(downloaded.contains(url))) {
				Minecraft.theMinecraft.renderEngine.obtainImageData(url, new HDImageBufferDownload());
				downloaded.add(url);
		}
		if(acs == null) {
			acs = new HashSet<Pair<Accessory, String>>();
		}
		acs.add(Pair.of(n, url));
	}

	public void renderAllAccessories(EntityPlayer player, RenderPlayer renderer, float f, float par2) {
		for(Pair<Accessory, String> a : acs) {
			if(renderer.loadDownloadableImageTexture(a.getRight(), null))
				a.getLeft().render(player, f, par2);
		}
	}
	
	public void addAccessoriesFor(ModelBiped modelBipedMain, EntityPlayer par1EntityPlayer) {
		VIP vip = par1EntityPlayer.vip;
		if(vip == null)
			return;
		Map<String, String> vAcs = vip.Accessories();
		String that = vAcs.get("tophat");
		String nhat = vAcs.get("notchhat");
		String brace = vAcs.get("bracelet");
		String wings = vAcs.get("wings");
		String ears = vAcs.get("ears");
		String glasses = vAcs.get("sunglasses");
		String tail = vAcs.get("tail");
		if(that!=null) addAccessory(new TopHat(modelBipedMain), that);
		if(nhat!=null) addAccessory(new NotchHat(modelBipedMain), that);
		if(brace!=null) addAccessory(new Bracelet(modelBipedMain), that);
		if(wings!=null) addAccessory(new Wings(modelBipedMain), that);
		if(ears!=null) addAccessory(new Ears(modelBipedMain), that);
		if(glasses!=null) addAccessory(new Sunglasses(modelBipedMain), that);
		if(tail!=null) addAccessory(new Tail(modelBipedMain), that);
	}
}
