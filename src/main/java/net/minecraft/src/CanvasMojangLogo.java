package net.minecraft.src;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
// MCPatcher Start
//import javax.imageio.ImageIO;
import com.pclewis.mcpatcher.TexturePackAPI;
// MCPatcher End

class CanvasMojangLogo extends Canvas {

	/** BufferedImage containing the Mojang logo. */
	private BufferedImage logo;

	public CanvasMojangLogo() {
		try {
			// MCPatcher Start
			this.logo = TexturePackAPI.getImage(PanelCrashReport.class, "/gui/crash_logo.png");
			// MCPatcher End
		// Spout Start
		} catch (Exception var2) {
		// Spout End
			;
		}

		byte var1 = 100;
		this.setPreferredSize(new Dimension(var1, var1));
		this.setMinimumSize(new Dimension(var1, var1));
	}

	public void paint(Graphics par1Graphics) {
		super.paint(par1Graphics);
		par1Graphics.drawImage(this.logo, this.getWidth() / 2 - this.logo.getWidth() / 2, 32, (ImageObserver)null);
	}
}
