package org.getspout.spout.gl;

import org.lwjgl.opengl.GL11;
import org.spoutcraft.spoutcraftapi.gl.SafeGL;

public class SpoutGL implements SafeGL{

	@Override
	public void popMatrix() {
		GL11.glPopMatrix();
	}

	@Override
	public void pushMatrix() {
		GL11.glPushMatrix();
	}

}
