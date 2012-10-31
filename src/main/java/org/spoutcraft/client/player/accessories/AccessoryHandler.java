/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.RenderManager;
import net.minecraft.src.RenderPlayer;

import org.spoutcraft.client.HDImageBufferDownload;
import org.spoutcraft.client.special.VIP;

public class AccessoryHandler {
	private static Map<String, Set<Pair<Accessory, String>>> sacs = new HashMap<String, Set<Pair<Accessory, String>>>();
	private static Set<String> downloaded = new HashSet<String>();
	private static RenderPlayer renderer = (RenderPlayer) RenderManager.instance.entityRenderMap.get(EntityPlayer.class);
	private static ModelBiped modelBipedMain = ((RenderPlayer) RenderManager.instance.entityRenderMap.get(EntityPlayer.class)).modelBipedMain;

	private AccessoryHandler() {
	}

	public static void addAccessory(String player, Accessory n, String url) {
		if (!(downloaded.contains(url))) {
			Minecraft.theMinecraft.renderEngine.obtainImageData(url, new HDImageBufferDownload());
			downloaded.add(url);
		}
		Set<Pair<Accessory, String>> acs = sacs.get(player);
		if (acs == null) {
			acs = new HashSet<Pair<Accessory, String>>();
		}

		Set<Pair<Accessory, String>> toRemove = new HashSet<Pair<Accessory, String>>();
		for (Pair<Accessory, String> pr : acs) {
			if (pr.getLeft().getType().equals(n.getType())) {
				toRemove.add(pr);
			}
		}

		acs.removeAll(toRemove);
		acs.add(Pair.of(n, url));
		sacs.put(player, acs);
	}

	public static void renderAllAccessories(EntityPlayer player, float f, float par2) {
		Set<Pair<Accessory, String>> acs = sacs.get(player.username);
		if (acs == null) {
			return;
		}
		for (Pair<Accessory, String> a : acs) {
			if (renderer.loadDownloadableImageTexture(a.getRight(), null)) {
				a.getLeft().render(player, f, par2);
			}
		}
	}

	public static void addVIPAccessoriesFor(EntityPlayer player) {
		VIP vip = player.vip;
		if (vip == null) {
			return;
		}
		Map<String, String> vAcs = vip.Accessories();
		if (vAcs == null) {
			return;
		}
		String that = vAcs.get("tophat");
		String nhat = vAcs.get("notchhat");
		String brace = vAcs.get("bracelet");
		String wings = vAcs.get("wings");
		String ears = vAcs.get("ears");
		String glasses = vAcs.get("sunglasses");
		String tail = vAcs.get("tail");
		if (that != null) {
			addAccessory(player.username, new TopHat(modelBipedMain), that);
		}
		if (nhat != null) {
			addAccessory(player.username, new NotchHat(modelBipedMain), nhat);
		}
		if (brace != null) {
			addAccessory(player.username, new Bracelet(modelBipedMain), brace);
		}
		if (wings != null) {
			addAccessory(player.username, new Wings(modelBipedMain), wings);
		}
		if (ears != null) {
			addAccessory(player.username, new Ears(modelBipedMain), ears);
		}
		if (glasses != null) {
			addAccessory(player.username, new Sunglasses(modelBipedMain), glasses);
		}
		if (tail != null) {
			addAccessory(player.username, new Tail(modelBipedMain), tail);
		}
	}

	public static void addAccessoryType(String player, AccessoryType type, String url) {
		Set<Pair<Accessory, String>> acs = sacs.get(player);
		if (acs == null) {
			acs = new HashSet<Pair<Accessory, String>>();
			sacs.put(player, acs);
		}
		Accessory toCreate;
		switch (type) {
			case BRACELET:
				toCreate = new Bracelet(modelBipedMain);
				break;
			case EARS:
				toCreate = new Ears(modelBipedMain);
				break;
			case NOTCHHAT:
				toCreate = new NotchHat(modelBipedMain);
				break;
			case SUNGLASSES:
				toCreate = new Sunglasses(modelBipedMain);
				break;
			case TAIL:
				toCreate = new Tail(modelBipedMain);
				break;
			case TOPHAT:
				toCreate = new TopHat(modelBipedMain);
				break;
			case WINGS:
				toCreate = new Wings(modelBipedMain);
				break;
			default:
				toCreate = null;
				break;
		}
		if (toCreate != null) {
			addAccessory(player, toCreate, url);
		}
	}

	public static void removeAccessoryType(String player, AccessoryType type) {
		Set<Pair<Accessory, String>> acs = sacs.get(player);
		if (acs == null) {
			return;
		}
		Pair<Accessory, String> toRemove = null;
		for (Pair<Accessory, String> accessory : acs) {
			if (accessory.getLeft().getType().equals(type)) {
				toRemove = accessory;
				break;
			}
		}
		acs.remove(toRemove);
	}

	public static boolean isHandled(String username) {
		return sacs.containsKey(username);
	}

	public static boolean hasAccessory(String username, AccessoryType type) {
		if (!sacs.containsKey(username)) {
			return false;
		}

		for (Pair<Accessory, String> accessory : sacs.get(username)) {
			if (accessory.getLeft().getType().equals(type)) {
				return true;
			}
		}

		return false;
	}
}
