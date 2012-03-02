package org.spoutcraft.client.special;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
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
			if (member.hasTitle()) {
				who.displayName = member.getTitle();
			}
			if (member.hasCape()) {
				who.updateCloak(member.getCape());
			}
			if (member.hasParticles()) {
				String particles = member.getParticles();
				who.particles = particles;
			}
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
					if (internal.containsKey("particles")) {
						Object parts = internal.get("particles");
						String realParts;
						if (parts instanceof Boolean && !((Boolean) parts)) {
							continue;
						} else if (parts instanceof String) {
							realParts = (String) parts;
						} else {
							realParts = "reddust";
						}
						member.setParticles(realParts);
					}
					vips.put(key, member);
				}
			}
		}
	}
}
