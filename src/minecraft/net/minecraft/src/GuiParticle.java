package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiParticle extends Gui {
	private List particles;
	private Minecraft mc;

	public GuiParticle(Minecraft minecraft) {
		particles = new ArrayList();
		mc = minecraft;
	}

	public void update() {
		for (int i = 0; i < particles.size(); i++) {
			Particle particle = (Particle)particles.get(i);
			particle.preUpdate();
			particle.update(this);
			if (particle.isDead) {
				particles.remove(i--);
			}
		}
	}

	public void draw(float f) {
		mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/particles.png"));
		for (int i = 0; i < particles.size(); i++) {
			Particle particle = (Particle)particles.get(i);
			int j = (int)((particle.prevPosX + (particle.posX - particle.prevPosX) * (double)f) - 4D);
			int k = (int)((particle.prevPosY + (particle.posY - particle.prevPosY) * (double)f) - 4D);
			float f1 = (float)(particle.prevTintAlpha + (particle.tintAlpha - particle.prevTintAlpha) * (double)f);
			float f2 = (float)(particle.prevTintRed + (particle.tintRed - particle.prevTintRed) * (double)f);
			float f3 = (float)(particle.prevTintGreen + (particle.tintGreen - particle.prevTintGreen) * (double)f);
			float f4 = (float)(particle.prevTintBlue + (particle.tintBlue - particle.prevTintBlue) * (double)f);
			GL11.glColor4f(f2, f3, f4, f1);
			drawTexturedModalRect(j, k, 40, 0, 8, 8);
		}
	}
}
