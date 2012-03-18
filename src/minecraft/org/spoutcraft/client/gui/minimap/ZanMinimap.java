package org.spoutcraft.client.gui.minimap;

import net.minecraft.client.Minecraft;

import org.spoutcraft.spoutcraftapi.Spoutcraft;

/**
 * Main Zanminimap class where everything happens
 * 
 * @author lahwran
 */
public class ZanMinimap {

	/**
	 * MapCalculator instance, public for things that want to plug into the minimap
	 */
	public MapCalculator mapcalc;

	/**
	 * MapRenderer instance, public for things that want to plug into the minimap
	 */
	public MapRenderer renderer;

	public Map map;

	public TextureManager texman;

	/**
	 * Instance, mainly for things that want to plug into the minimap
	 */
	public static ZanMinimap instance;

	public ZanMinimap() {
		MinimapConfig.initialize();
		BlockColor.initDefaultColors();
		
		map = new Map();
		texman = new TextureManager();
		mapcalc = new MapCalculator(this);
		renderer = new MapRenderer(this);
		mapcalc.start();
		instance = this;
	}

	/**
	 * Heartbeat function called each render by whatever is managing the minimap.
	 * 
	 * @param mc Minecraft instance to initialize obfhub.game with
	 */
	public void onRenderTick() {
		if (Minecraft.theMinecraft.thePlayer == null)
			return;

		int scWidth = Spoutcraft.getRenderDelegate().getScreenWidth();
		int scHeight = Spoutcraft.getRenderDelegate().getScreenHeight();

		mapcalc.onRenderTick();
		renderer.onRenderTick(scWidth, scHeight);
	}

	
}
