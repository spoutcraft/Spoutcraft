/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */

package org.spoutcraft.client.special;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

public class VIPs {

	private static final Map<String, VIPMember> vips = new HashMap<String, VIPMember>();
	private static final File file = new File(Minecraft.getMinecraftDir(), "spoutcraft" + File.separator + "vip.yml");
	private static final Yaml yaml = new Yaml(new SafeConstructor());

	public static void newPlayer(EntityPlayer who) {
		if (who == null) {
			return;
		}
		if (who.username == null) {
			return;
		}
		if (vips.containsKey(who.username)) {
			VIPMember member = vips.get(who.username);
			System.out.println("VIP detected: "+who.username+"! Setting cloak to: "+member.getCape());
			if (member.hasTitle()) {
				who.displayName = member.getTitle();
			}
			if (member.hasCape()) {
				who.updateCloak(member.getCape());
			}
			who.worldObj.releaseEntitySkin(who);
			who.worldObj.obtainEntitySkin(who);
			//TODO set particles
		}
	}

	static {
		if (file.exists()) {
			Map<String, Object> map = null;
			try {
				map = (Map<String, Object>) yaml.load(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (map != null) {
				for (String key : map.keySet()) {
					VIPMember member = new VIPMember(key);
					Map<String, Object> internal = (Map<String, Object>) map.get(key);
					if (internal.containsKey("title")) {
						member.setTitle((String) internal.get("title"));
					}
					if (internal.containsKey("cape")) {
						member.setCape((String) internal.get("cape"));
					}
					vips.put(key, member);
				}
			}
		} else {
			try {
				file.createNewFile();
			} catch (IOException ex) {
				Logger.getLogger(VIPs.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
