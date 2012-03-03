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
package org.spoutcraft.client.gui;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.FoodStats;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Item;
import net.minecraft.src.Material;
import net.minecraft.src.Potion;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderManager;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Tessellator;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
import org.spoutcraft.spoutcraftapi.gui.ArmorBar;
import org.spoutcraft.spoutcraftapi.gui.BubbleBar;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.Color;
import org.spoutcraft.spoutcraftapi.gui.Control;
import org.spoutcraft.spoutcraftapi.gui.ExpBar;
import org.spoutcraft.spoutcraftapi.gui.GenericBitmap;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;
import org.spoutcraft.spoutcraftapi.gui.GenericComboBox;
import org.spoutcraft.spoutcraftapi.gui.GenericEntityWidget;
import org.spoutcraft.spoutcraftapi.gui.GenericGradient;
import org.spoutcraft.spoutcraftapi.gui.GenericItemWidget;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericListWidget;
import org.spoutcraft.spoutcraftapi.gui.GenericListWidgetItem;
import org.spoutcraft.spoutcraftapi.gui.GenericRadioButton;
import org.spoutcraft.spoutcraftapi.gui.GenericScrollArea;
import org.spoutcraft.spoutcraftapi.gui.GenericScrollable;
import org.spoutcraft.spoutcraftapi.gui.GenericSlider;
import org.spoutcraft.spoutcraftapi.gui.GenericTextField;
import org.spoutcraft.spoutcraftapi.gui.GenericTexture;
import org.spoutcraft.spoutcraftapi.gui.HealthBar;
import org.spoutcraft.spoutcraftapi.gui.HungerBar;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;
import org.spoutcraft.spoutcraftapi.gui.MinecraftFont;
import org.spoutcraft.spoutcraftapi.gui.MinecraftTessellator;
import org.spoutcraft.spoutcraftapi.gui.Orientation;
import org.spoutcraft.spoutcraftapi.gui.RenderDelegate;
import org.spoutcraft.spoutcraftapi.gui.RenderPriority;
import org.spoutcraft.spoutcraftapi.gui.RenderUtil;
import org.spoutcraft.spoutcraftapi.gui.Widget;
import org.spoutcraft.spoutcraftapi.gui.WidgetAnchor;

public class MCRenderDelegate implements RenderDelegate {
	private Color scrollBarColor = new Color(0.26F, 0.26F, 0.26F, 0.33F);
	private Color scrollBarColor2 = new Color(0.1F, 0.1F, 0.1F, 0.38F);
	public static boolean shouldRenderCursor = false;
	protected final RenderItemCustom renderer;
	protected HashMap<UUID, GuiButton> customFields = new HashMap<UUID, GuiButton>();
	protected TObjectIntMap<GenericBitmap> bitmapId = new TObjectIntHashMap<GenericBitmap>();
	MinecraftFont font = new MinecraftFontWrapper();
	MinecraftTessellator tessellator = new MinecraftTessellatorWrapper();
	TIntObjectHashMap<String> optimalWidth = new TIntObjectHashMap<String>();

	public MCRenderDelegate() {
		renderer = new RenderItemCustom();
		renderer.setRenderManager(RenderManager.instance);
	}

	public void downloadTexture(String plugin, String url) {
		CustomTextureManager.downloadTexture(plugin, url);
	}

	public int getScreenHeight() {
		ScaledResolution resolution = new ScaledResolution(SpoutClient.getHandle().gameSettings, SpoutClient.getHandle().displayWidth, SpoutClient.getHandle().displayHeight);
		return resolution.getScaledHeight();
	}

	public int getScreenWidth() {
		ScaledResolution resolution = new ScaledResolution(SpoutClient.getHandle().gameSettings, SpoutClient.getHandle().displayWidth, SpoutClient.getHandle().displayHeight);
		return resolution.getScaledWidth();
	}

	public int getTextWidth(String text) {
		return Minecraft.theMinecraft.fontRenderer.getStringWidth(text);
	}

	public void render(ArmorBar bar) {
		float armorPercent = Minecraft.theMinecraft.thePlayer.inventory.getTotalArmorValue() / 0.2f;
		if (bar.isVisible() && bar.getMaxNumShields() > 0) {
			int y = (int) bar.getScreenY();
			float armorPercentPerIcon = 100f / bar.getMaxNumShields();
			for (int icon = 0; icon < bar.getMaxNumShields(); icon++) {
				if (armorPercent > 0 || bar.isAlwaysVisible()) {
					int x = (int) bar.getScreenX() + icon * bar.getIconOffset();
					boolean full = (icon + 1) * armorPercentPerIcon <= armorPercent;
					boolean half = (icon + 1) * armorPercentPerIcon < armorPercent + armorPercentPerIcon;
					if (full) { // white armor (filled in)
						RenderUtil.drawTexturedModalRectangle(x, y, 34, 9, 9, 9, 0f);
					} else if (half) { // half filled in
						RenderUtil.drawTexturedModalRectangle(x, y, 25, 9, 9, 9, 0f);
					} else {
						RenderUtil.drawTexturedModalRectangle(x, y, 16, 9, 9, 9, 0f);
					}
				}
			}
		}
	}

	public void render(BubbleBar bar) {
		if (Minecraft.theMinecraft.thePlayer.isInsideOfMaterial(Material.water)) {
			int bubbles = (int) Math.ceil(((double) (Minecraft.theMinecraft.thePlayer.getAir() - 2) * bar.getMaxNumBubbles()) / (Minecraft.theMinecraft.thePlayer.maxAir));
			int poppingBubbles = (int) Math.ceil(((double) Minecraft.theMinecraft.thePlayer.getAir() * bar.getMaxNumBubbles()) / (Minecraft.theMinecraft.thePlayer.maxAir)) - bubbles;
			if (bar.isVisible()) {
				for (int bubble = 0; bubble < bubbles + poppingBubbles; bubble++) {
					if (bubble < bubbles) {
						RenderUtil.drawTexturedModalRectangle((int) bar.getScreenX() - bubble * bar.getIconOffset(), (int) bar.getScreenY(), 16, 18, 9, 9, 0f);
					} else {
						RenderUtil.drawTexturedModalRectangle((int) bar.getScreenX() - bubble * bar.getIconOffset(), (int) bar.getScreenY(), 25, 18, 9, 9, 0f);
					}
				}
			}
		}
	}

	public void render(GenericButton button) {
		if (button.isVisible()) {
			FontRenderer font = Minecraft.theMinecraft.fontRenderer;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, Minecraft.theMinecraft.renderEngine.getTexture("/gui/gui.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef((float) Math.floor(button.getScreenX()), (float) Math.floor(button.getScreenY()), 0);
			float width = (float) (button.getWidth() < 200 ? button.getWidth() : 200);
			GL11.glScalef((float) button.getWidth() / width, (float) button.getHeight() / 20f, 1);

			String text = getFittingText(button.getText(), (int) button.getInnerWidth());

			int hoverState = getHoverState(button, isHovering(button));
			RenderUtil.drawTexturedModalRectangle(0, 0, 0, 46 + hoverState * 20, (int) Math.ceil(width / 2), 20, 0f);
			RenderUtil.drawTexturedModalRectangle((int) Math.floor(width / 2), 0, 200 - (int) Math.ceil(width / 2), 46 + hoverState * 20, (int) Math.ceil(width / 2), 20, 0f);
			Color color = getColor(button);

			int left = 5;
			WidgetAnchor align = button.getAlign();
			if (align == WidgetAnchor.TOP_CENTER || align == WidgetAnchor.CENTER_CENTER || align == WidgetAnchor.BOTTOM_CENTER) {
				left = (int) ((width / 2) - (font.getStringWidth(text) / 2));
			} else if (align == WidgetAnchor.TOP_RIGHT || align == WidgetAnchor.CENTER_RIGHT || align == WidgetAnchor.BOTTOM_RIGHT) {
				left = (int) (width - font.getStringWidth(text)) - 5;
			}

			GL11.glPushMatrix();
			float scale = button.getScale();
			GL11.glScalef(scale, scale, scale);
			font.drawStringWithShadow(text, left, 6, color.toInt());
			GL11.glPopMatrix();
		}
	}

	protected boolean isHovering(Widget widget) {
		double mouseX = widget.getScreen().getMouseX();
		double mouseY = widget.getScreen().getMouseY();

		boolean hovering = mouseX >= widget.getActualX() && mouseY >= widget.getActualY() && mouseX < widget.getActualX() + widget.getWidth() && mouseY < widget.getActualY() + widget.getHeight();
		return hovering;
	}

	protected int getHoverState(Control control, boolean hover) {
		int state = 1;
		if (!control.isEnabled()) {
			state = 0;
		} else if (hover) {
			state = 2;
		}

		return state;
	}

	public void render(GenericGradient gradient) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(770, 771);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		boolean VERT = gradient.getOrientation() == Orientation.VERTICAL;
		tessellator.setColorRGBA_F(gradient.getTopColor().getRedF(), gradient.getTopColor().getGreenF(), gradient.getTopColor().getBlueF(), gradient.getTopColor().getAlphaF());
		tessellator.addVertex((VERT ? gradient.getWidth() : 0) + gradient.getScreenX(), (VERT ? 0 : gradient.getHeight()) + gradient.getScreenY(), 0.0D);
		tessellator.addVertex(gradient.getScreenX(), gradient.getScreenY(), 0.0D);
		tessellator.setColorRGBA_F(gradient.getBottomColor().getRedF(), gradient.getBottomColor().getGreenF(), gradient.getBottomColor().getBlueF(), gradient.getBottomColor().getAlphaF());
		tessellator.addVertex((VERT ? 0 : gradient.getWidth()) + gradient.getScreenX(), (VERT ? gradient.getHeight() : 0) + gradient.getScreenY(), 0.0D);
		tessellator.addVertex(gradient.getWidth() + gradient.getScreenX(), gradient.getHeight() + gradient.getScreenY(), 0.0D);
		tessellator.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void render(GenericItemWidget item) {
		GL11.glDepthFunc(515);
		RenderHelper.enableStandardItemLighting();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glDisable(GL11.GL_LIGHTING);
		double oldX = 1;
		double oldY = 1;
		double oldZ = 1;
		Block block = null;
		if (item.getTypeId() < 255 && RenderBlocks.renderItemIn3d(Block.blocksList[item.getTypeId()].getRenderType())) {
			block = Block.blocksList[item.getTypeId()];
			oldX = block.maxX;
			oldY = block.maxY;
			oldZ = block.maxZ;
			block.maxX = block.maxX * item.getWidth() / 8;
			block.maxY = block.maxY * item.getHeight() / 8;
			block.maxZ = block.maxZ * item.getDepth() / 8;
		} else {
			renderer.setScale((item.getWidth() / 8D), (item.getHeight() / 8D), 1);
		}
		GL11.glPushMatrix();
		GL11.glTranslatef((float) item.getScreenX(), (float) item.getScreenY(), 0);
		if (item.getAnchor() == WidgetAnchor.SCALE) {
			GL11.glScalef((float) (item.getScreen().getWidth() / 427f), (float) (item.getScreen().getHeight() / 240f), 1);
		}
		int id = item.getTypeId();
		int data = item.getData();
		if (MaterialData.getCustomItem(id) != null) {
			int temp = id;
			id = 318;
			data = temp;
		}
		renderer.drawItemIntoGui(SpoutClient.getHandle().fontRenderer, SpoutClient.getHandle().renderEngine, id, data, Item.itemsList[id].getIconFromDamage(item.getData()), 0, 0);
		GL11.glPopMatrix();
		if (item.getTypeId() < 255 && RenderBlocks.renderItemIn3d(Block.blocksList[item.getTypeId()].getRenderType())) {
			block.maxX = oldX;
			block.maxY = oldY;
			block.maxZ = oldZ;
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		RenderHelper.disableStandardItemLighting();
	}

	public void render(GenericLabel label) {
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		String lines[] = label.getLines();

		double swidth = label.getTextWidth();
		double sheight = label.getTextHeight();

		GL11.glPushMatrix();

		double top = label.getScreenY();

		WidgetAnchor align = label.getAlign();
		if (align == WidgetAnchor.CENTER_LEFT || align == WidgetAnchor.CENTER_CENTER || align == WidgetAnchor.CENTER_RIGHT) {
			top -= (int) (label.isAuto() ? label.getActualHeight() : label.getHeight()) / 2;
		} else if (align == WidgetAnchor.BOTTOM_LEFT || align == WidgetAnchor.BOTTOM_CENTER || align == WidgetAnchor.BOTTOM_RIGHT) {
			top -= (int) (label.isAuto() ? label.getActualHeight() : label.getHeight());
		}

		double aleft = label.getScreenX();
		if (align == WidgetAnchor.TOP_CENTER || align == WidgetAnchor.CENTER_CENTER || align == WidgetAnchor.BOTTOM_CENTER) {
			aleft -= (int) (label.isAuto() ? label.getActualWidth() : label.getWidth()) / 2;
		} else if (align == WidgetAnchor.TOP_RIGHT || align == WidgetAnchor.CENTER_RIGHT || align == WidgetAnchor.BOTTOM_RIGHT) {
			aleft -= (int) (label.isAuto() ? label.getActualWidth() : label.getWidth());
		}

		GL11.glTranslatef((float) Math.floor(aleft), (float) Math.floor(top), 0);
		if (!label.isAuto()) {
			GL11.glScalef((float) (label.getWidth() / swidth), (float) (label.getHeight() / sheight), 1);
		} else if (label.getAnchor() == WidgetAnchor.SCALE) {
			GL11.glScalef((float) (label.getScreen().getWidth() / 427f), (float) (label.getScreen().getHeight() / 240f), 1);
		}

		for (int i = 0; i < lines.length; i++) {
			double left = 0;

			if (align == WidgetAnchor.TOP_CENTER || align == WidgetAnchor.CENTER_CENTER || align == WidgetAnchor.BOTTOM_CENTER) {
				left = (swidth / 2) - (font.getStringWidth(lines[i]) / 2);
			} else if (align == WidgetAnchor.TOP_RIGHT || align == WidgetAnchor.CENTER_RIGHT || align == WidgetAnchor.BOTTOM_RIGHT) {
				left = swidth - font.getStringWidth(lines[i]);
			}

			float scale = label.getScale();
			float reset = 1 / scale;
			GL11.glScalef(scale, scale, scale);
			font.drawStringWithShadow(lines[i], (int) left, i * 10, label.getTextColor().toInt());
			GL11.glScalef(reset, reset, reset);
		}
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public void render(GenericSlider slider) {
		if (slider.isVisible()) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, Minecraft.theMinecraft.renderEngine.getTexture("/gui/gui.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			float width = (float) (slider.getWidth() < 200 ? slider.getWidth() : 200);
			GL11.glTranslatef((float) slider.getScreenX(), (float) slider.getScreenY(), 0);
			GL11.glScalef((float) slider.getWidth() / width, (float) slider.getHeight() / 20f, 1);

			double mouseX = slider.getScreen().getMouseX();

			RenderUtil.drawTexturedModalRectangle(0, 0, 0, 46, (int) Math.ceil(width / 2), 20, 0f);
			RenderUtil.drawTexturedModalRectangle((int) Math.floor(width / 2), 0, 200 - (int) Math.ceil(width / 2), 46, (int) Math.ceil(width / 2), 20, 0f);

			if (slider.isDragging()) {
				slider.setSliderPosition((float) (mouseX - (slider.getScreenX() + 4)) / (float) (slider.getWidth() - 8));
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			width -= 8;
			RenderUtil.drawTexturedModalRectangle((int) (slider.getSliderPosition() * width), 0, 0, 66, 4, 20, 0f);
			RenderUtil.drawTexturedModalRectangle((int) (slider.getSliderPosition() * width) + 4, 0, 196, 66, 4, 20, 0f);

			Color color = slider.getTextColor();
			if (!slider.isEnabled()) {
				color = slider.getDisabledColor();
			}

			int left = 5;
			WidgetAnchor align = slider.getAlign();
			if (align == WidgetAnchor.TOP_CENTER || align == WidgetAnchor.CENTER_CENTER || align == WidgetAnchor.BOTTOM_CENTER) {
				left = (int) ((width / 2) - (font.getTextWidth(slider.getText()) / 2));
			} else if (align == WidgetAnchor.TOP_RIGHT || align == WidgetAnchor.CENTER_RIGHT || align == WidgetAnchor.BOTTOM_RIGHT) {
				left = (int) (width - font.getTextWidth(slider.getText())) - 5;
			}

			GL11.glPushMatrix();
			float scale = slider.getScale();
			GL11.glScalef(scale, scale, scale);
			font.drawString(slider.getText(), left, 6, color.toInt());
			GL11.glPopMatrix();
		}
	}

	public void render(GenericTextField textField) {
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		RenderUtil.drawRectangle((int) (textField.getScreenX() - 1), (int) (textField.getScreenY() - 1), (int) (textField.getScreenX() + textField.getWidth() + 1), (int) (textField.getScreenY() + textField.getHeight() + 1), textField.getBorderColor().toInt());
		RenderUtil.drawRectangle((int) textField.getScreenX(), (int) textField.getScreenY(), (int) (textField.getScreenX() + textField.getWidth()), (int) (textField.getScreenY() + textField.getHeight()), textField.getFieldColor().toInt());

		int x = (int) (textField.getScreenX() + GenericTextField.PADDING);
		int y = (int) (textField.getScreenY() + GenericTextField.PADDING);
		int color = textField.isEnabled() ? textField.getColor().toInt() : textField.getDisabledColor().toInt();
		int[] cursor = textField.getTextProcessor().getCursor2D();
		int lineNum = 0;
		int cursorOffset = 0;
		if (textField.getText().length() != 0) {
			String line;
			Iterator<String> iter = textField.getTextProcessor().iterator();

			while (iter.hasNext()) {
				line = iter.next();
				if (lineNum == cursor[0]) {
					cursorOffset = font.getStringWidth(line.substring(0, cursor[1]));
				}
				font.drawStringWithShadow(line, x, y + (GenericTextField.LINE_HEIGHT + GenericTextField.LINE_SPACING) * lineNum++, color);
			}
		} else if (!textField.isFocus()) {
			font.drawStringWithShadow(textField.getPlaceholder(), x, y, color);
		}
		boolean showCursor = textField.isEnabled() && textField.isFocus() && shouldRenderCursor;
		if (showCursor) {
			font.drawStringWithShadow("_", x + cursorOffset, y + (GenericTextField.LINE_HEIGHT + GenericTextField.LINE_SPACING) * cursor[0] + 1, color);
		}

	}

	public void render(GenericTexture texture) {
		String addon = texture.getAddon().getDescription().getName();
		String url = texture.getUrl();
		org.newdawn.slick.opengl.Texture textureBinding = CustomTextureManager.getTextureFromUrl(addon, url);

		if (textureBinding != null) {
			if (texture.getOriginalHeight() == -1 && texture.getOriginalWidth() == -1) {
				texture.setOriginalWidth(textureBinding.getImageWidth());
				texture.setOriginalHeight(textureBinding.getImageHeight());
			}
			if (texture.getFinishDelegate() != null) {
				boolean oldLock = SpoutClient.enableSandbox();
				try {
					texture.getFinishDelegate().run();
				} finally {
					SpoutClient.enableSandbox(oldLock);
				}
				texture.setFinishDelegate(null);
			}

			GL11.glTranslatef((float) texture.getScreenX(), (float) texture.getScreenY(), 0); // moves texture into place
			drawTexture(textureBinding, (int) texture.getWidth(), (int) texture.getHeight(), texture.isDrawingAlphaChannel(), texture.getLeft(), texture.getTop());
		}
	}

	public void render(GenericBitmap bitmap) {
		int textureId;
		if (bitmapId.containsKey(bitmap)) {
			textureId = bitmapId.get(bitmap);
		} else {
			IntBuffer tmp = IntBuffer.allocate(1);
			GL11.glGenTextures(tmp);
			textureId = tmp.get(0);
			bitmapId.put(bitmap, textureId);
		}
		int width = (int) bitmap.getActualWidth();
		int height = (int) bitmap.getActualHeight();
		int left = bitmap.getLeft();
		int top = bitmap.getTop();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, bitmap.getRawWidth(), bitmap.getRawHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bitmap.getBuffer());
		GL11.glTranslatef((float) bitmap.getScreenX(), (float) bitmap.getScreenY(), 0); // moves texture into place
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glDepthMask(false);
		bindColor(new Color(1.0F, 1.0F, 1.0F));
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		double tLeft = 0, tTop = 0, rWidth = bitmap.getWidth(), rHeight = bitmap.getHeight(), tWidth = rWidth, tHeight = rHeight;
		if (top >= 0 && left >= 0) {
			tWidth = Math.min(tWidth, width);
			tHeight = Math.min(tHeight, height);
			tLeft = Math.min(Math.max(0, left), rWidth);
			tTop = Math.min(Math.max(0, top), rHeight);
		}
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.0D, height, -90, tLeft, tTop); // draw corners
		tessellator.addVertexWithUV(width, height, -90, tWidth, tTop);
		tessellator.addVertexWithUV(width, 0.0D, -90, tWidth, tHeight);
		tessellator.addVertexWithUV(0.0D, 0.0D, -90, tLeft, tHeight);
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void render(HealthBar bar) {
		int health = Minecraft.theMinecraft.thePlayer.health;
		boolean whiteOutlinedHearts = Minecraft.theMinecraft.thePlayer.heartsLife / 3 % 2 == 1;
		if (Minecraft.theMinecraft.thePlayer.heartsLife < 10) {
			whiteOutlinedHearts = false;
		}
		float healthPercent = health / 0.2f;
		int y = (int) bar.getScreenY();
		if (healthPercent <= bar.getDangerPercent()) {
			y += GuiIngame.rand.nextInt(2);
		}
		float healthPercentPerIcon = 100f / bar.getMaxNumHearts();
		if (bar.isVisible() && bar.getMaxNumHearts() > 0) {
			for (int icon = 0; icon < bar.getMaxNumHearts(); ++icon) {
				boolean full = (icon + 1) * healthPercentPerIcon <= healthPercent;
				boolean half = (icon + 1) * healthPercentPerIcon < healthPercent + healthPercentPerIcon;
				int x = (int) bar.getScreenX() + icon * bar.getIconOffset();

				int iconType = 16;

				if (Minecraft.theMinecraft.thePlayer.isPotionActive(Potion.poison)) {
					iconType += 36;
				}

				byte hardcore = 0;
				if (Minecraft.theMinecraft.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
					hardcore = 5 * 9;
				}

				RenderUtil.drawTexturedModalRectangle(x, y, 16 + (whiteOutlinedHearts ? 1 : 0) * 9, hardcore, 9, 9, 0f);
				if (whiteOutlinedHearts) {
					if (full) {
						RenderUtil.drawTexturedModalRectangle(x, y, iconType + 54, hardcore, 9, 9, 0f);
					} else if (half) {
						RenderUtil.drawTexturedModalRectangle(x, y, iconType + 63, hardcore, 9, 9, 0f);
					}
				}

				if (full) {
					RenderUtil.drawTexturedModalRectangle(x, y, iconType + 36, hardcore, 9, 9, 0f);
				} else if (half) {
					RenderUtil.drawTexturedModalRectangle(x, y, iconType + 45, hardcore, 9, 9, 0f);
				}

			}
		}
	}

	public void render(GenericEntityWidget entityWidget) {
		Entity entity = SpoutClient.getInstance().getEntityFromId(entityWidget.getEntityId());
		if (entity != null) {
			GL11.glEnable(32826);
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glPushMatrix();
			GL11.glTranslated(entityWidget.getX() + entityWidget.getWidth() / 2, entityWidget.getY() + entityWidget.getHeight(), 50F);
			RenderHelper.enableStandardItemLighting();
			float f1 = (float) Math.min(entityWidget.getWidth(), entityWidget.getHeight());
			GL11.glScalef(-f1, f1, f1);
			GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(entity.prevRotationYaw, 0, 1.0F, 0);
			RenderHelper.enableStandardItemLighting();
			RenderManager.instance.playerViewY = 180F;
			RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
			GL11.glPopMatrix();
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(32826);
		}
	}

	public MinecraftFont getMinecraftFont() {
		return font;
	}

	public MinecraftTessellator getTessellator() {
		return tessellator;
	}

	public void render(HungerBar bar) {
		FoodStats foodStats = Minecraft.theMinecraft.thePlayer.getFoodStats();

		int foodLevel = foodStats.getFoodLevel();
		float foodPercent = foodLevel / 0.2f;
		float foodPercentPerIcon = 100f / bar.getNumOfIcons();

		if (bar.isVisible() && bar.getNumOfIcons() > 0) {

			int foodIcon = 16;
			byte foodOutline = 0;

			if (Minecraft.theMinecraft.thePlayer.isPotionActive(Potion.hunger)) {
				foodIcon += 36;
				foodOutline = 13;
			}

			for (int icon = 0; icon < bar.getNumOfIcons(); icon++) {
				int x = (int) bar.getScreenX() - icon * bar.getIconOffset();
				int y = (int) bar.getScreenY();

				if (Minecraft.theMinecraft.thePlayer.getFoodStats().getSaturationLevel() <= 0.0F && bar.getUpdateCounter() % (foodLevel * 3 + 1) == 0) {
					y += GuiIngame.rand.nextInt(3) - 1;
				}

				RenderUtil.drawTexturedModalRectangle(x, y, 16 + foodOutline * 9, 27, 9, 9, 0f);

				if ((icon + 1) * foodPercentPerIcon <= foodPercent) {
					RenderUtil.drawTexturedModalRectangle(x, y, foodIcon + 36, 27, 9, 9, 0f);
				} else if ((icon + 1) * foodPercentPerIcon < foodPercent + foodPercentPerIcon) {
					RenderUtil.drawTexturedModalRectangle(x, y, foodIcon + 45, 27, 9, 9, 0f);
				}
			}
		}

	}

	public void render(ExpBar bar) {
		if (bar.isVisible()) {
			int expCap = Minecraft.theMinecraft.thePlayer.xpBarCap();
			if (expCap > 0) {
				int x = (int) bar.getScreenX();
				int y = (int) bar.getScreenY();
				int exp = (int) (Minecraft.theMinecraft.thePlayer.experience * 183.0F);
				RenderUtil.drawTexturedModalRectangle(x, y, 0, 64, 182, 5, 0f);
				if (exp > 0) {
					RenderUtil.drawTexturedModalRectangle(x, y, 0, 69, exp, 5, 0f);
				}
			}

			if (Minecraft.theMinecraft.playerController.func_35642_f() && Minecraft.theMinecraft.thePlayer.experienceLevel > 0) {
				int color = 8453920;
				String level = "" + Minecraft.theMinecraft.thePlayer.experienceLevel;
				FontRenderer font = SpoutClient.getHandle().fontRenderer;
				int x = (int) (bar.getScreenX() + (183 / 2) - (font.getStringWidth(level) / 2));
				int y = (int) bar.getScreenY() - 6;
				font.drawString(level, x + 1, y, 0);
				font.drawString(level, x - 1, y, 0);
				font.drawString(level, x, y + 1, 0);
				font.drawString(level, x, y - 1, 0);
				font.drawString(level, x, y, color);
			}
		}
	}

	public void render(GenericCheckBox checkBox) {
		if (checkBox.isVisible()) {
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F);
			Texture checkBoxCross = CustomTextureManager.getTextureFromJar("/res/check.png");
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef((float) Math.floor(checkBox.getScreenX()), (float) Math.floor(checkBox.getScreenY()), 0);
			renderBaseBox(checkBox, true);
			FontRenderer font = SpoutClient.getHandle().fontRenderer;
			Color color = getColor(checkBox);
			if (!checkBox.isChecked()) {
				color.setAlpha(0.2F);
			} else {
				color.setRed(0).setGreen(1).setBlue(0);
			}
			drawTexture(checkBoxCross, 20, 20, color, true);
			font.drawString(checkBox.getText(), 22, 7, getColor(checkBox).toInt());
		}
	}

	public void render(GenericRadioButton radioButton) {
		if (radioButton.isVisible()) {
			Texture radio = CustomTextureManager.getTextureFromJar("/res/radio.png");
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef((float) Math.floor(radioButton.getScreenX()), (float) Math.floor(radioButton.getScreenY()), 0);
			renderBaseBox(radioButton, true);
			FontRenderer font = SpoutClient.getHandle().fontRenderer;
			Color color = getColor(radioButton);
			if (!radioButton.isSelected()) {
				color.setAlpha(0.2F);
			}
			drawTexture(radio, 20, 20, color, true);
			font.drawString(radioButton.getText(), 22, 7, getColor(radioButton).toInt());
		}
	}

	public void renderBaseBox(Control box) {
		renderBaseBox(box, false);
	}

	public void renderBaseBox(Control box, boolean blend) {
		Texture usedTexture = null;
		if (box.isEnabled() && isHovering(box)) {
			usedTexture = CustomTextureManager.getTextureFromJar("/res/boxHover.png");
		} else if (box.isEnabled()) {
			usedTexture = CustomTextureManager.getTextureFromJar("/res/boxNormal.png");
		} else {
			usedTexture = CustomTextureManager.getTextureFromJar("/res/boxDisabled.png");
		}
		drawTexture(usedTexture, 20, 20, blend);
	}

	public void drawTexture(Texture textureBinding, int width, int height) {
		drawTexture(textureBinding, width, height, new Color(1.0F, 1.0F, 1.0F), false, -1, -1);
	}

	public void drawTexture(Texture textureBinding, int width, int height, int left, int top) {
		drawTexture(textureBinding, width, height, new Color(1.0F, 1.0F, 1.0F), false, left, top);
	}

	public void drawTexture(Texture textureBinding, int width, int height, boolean blend) {
		drawTexture(textureBinding, width, height, new Color(1.0F, 1.0F, 1.0F), blend, -1, -1);
	}

	public void drawTexture(Texture textureBinding, int width, int height, boolean blend, int left, int top) {
		drawTexture(textureBinding, width, height, new Color(1.0F, 1.0F, 1.0F), blend, left, top);
	}

	public void drawTexture(Texture textureBinding, int width, int height, Color color) {
		drawTexture(textureBinding, width, height, color, false, -1, -1);
	}

	public void drawTexture(Texture textureBinding, int width, int height, Color color, int left, int top) {
		drawTexture(textureBinding, width, height, color, false, left, top);
	}

	public void drawTexture(Texture textureBinding, int width, int height, Color color, boolean blend) {
		drawTexture(textureBinding, width, height, color, blend, -1, -1);
	}

	public void drawTexture(Texture textureBinding, int width, int height, Color color, boolean blend, int left, int top) {
		if (textureBinding == null) {
			return;
		}
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		if (blend) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(770, 771);
		}
		GL11.glDepthMask(false);
		bindColor(color);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureBinding.getTextureID());
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		double tLeft = 0, tTop = 0, rWidth = textureBinding.getWidth(), rHeight = textureBinding.getHeight(), tWidth = rWidth, tHeight = rHeight;
		if (top >= 0 && left >= 0) {
			tWidth = Math.min(tWidth, width);
			tHeight = Math.min(tHeight, height);
			tLeft = Math.min(Math.max(0, left), rWidth);
			tTop = Math.min(Math.max(0, top), rHeight);
		}
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.0D, height, -90, tLeft, tTop); // draw corners
		tessellator.addVertexWithUV(width, height, -90, tLeft + tWidth, tTop);
		tessellator.addVertexWithUV(width, 0.0D, -90, tLeft + tWidth, tTop + tHeight);
		tessellator.addVertexWithUV(0.0D, 0.0D, -90, tLeft, tTop + tHeight);

		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
		if (blend) {
			GL11.glDisable(GL11.GL_BLEND);
		}
	}

	public Color getColor(Button c) {
		if (c.isEnabled() && isHovering(c)) {
			return c.getHoverColor().clone();
		} else if (c.isEnabled()) {
			return c.getColor().clone();
		} else {
			return c.getDisabledColor().clone();
		}
	}

	protected void bindColor(Color c) {
		GL11.glColor4f(c.getRedF(), c.getGreenF(), c.getBlueF(), c.getAlphaF());
	}

	public void render(GenericListWidgetItem lwi, int x, int y, int width, int height) {
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		font.drawString(lwi.getTitle(), x + 2, y + 2, new Color(1.0F, 1.0F, 1.0F).toInt());
		font.drawString(lwi.getText(), x + 2, y + 2 + 8, new Color(0.8F, 0.8F, 0.8F).toInt());
	}

	private void scissorWidget(Widget widget) {
		double x = widget.getActualX() + widget.getWidth(), y = widget.getActualY() + widget.getHeight(), width = widget.getWidth(), height = widget.getHeight();
		double screenHeight;
		GuiScreen screen = SpoutClient.getHandle().currentScreen;
		screenHeight = screen.height;
		int windowWidth = SpoutClient.getHandle().displayWidth, windowHeight = SpoutClient.getHandle().displayHeight;
		ScaledResolution scale = new ScaledResolution(SpoutClient.getHandle().gameSettings, windowWidth, windowHeight);
		double scaleFactor = scale.scaleFactor;
		height = height * scaleFactor;
		width = width * scaleFactor;
		x *= scaleFactor;
		y *= scaleFactor;
		screenHeight *= scaleFactor;
		x = x - width;
		y = screenHeight - y;
		GL11.glScissor((int) x, (int) y, (int) width, (int) height);
	}

	public void render(GenericScrollable gs) {
		int scrollTop = gs.getScrollPosition(Orientation.VERTICAL);
		int scrollLeft = gs.getScrollPosition(Orientation.HORIZONTAL);
		GL11.glTranslated(gs.getScreenX(), gs.getScreenY(), 0);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		scissorWidget(gs);
		RenderUtil.drawRectangle(0, 0, (int) gs.getWidth(), (int) gs.getHeight(), gs.getBackgroundColor().toInt());
		GL11.glPushMatrix();
		GL11.glTranslated(-scrollLeft, -scrollTop, 0);
		GL11.glPushMatrix();

		//Render scrollarea contents
		gs.renderContents();

		GL11.glPopMatrix();
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		GL11.glDisable(2896 /*GL_LIGHTING*/);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		//Draw scrollbars
		if (gs.needsScrollBar(Orientation.HORIZONTAL)) {
			Minecraft mc = SpoutClient.getHandle();
			int texture = mc.renderEngine.getTexture("/gui/allitems.png");
			mc.renderEngine.bindTexture(texture);
			double scrollX = 0;
			double p = (double) scrollLeft / (double) gs.getMaximumScrollPosition(Orientation.HORIZONTAL);
			scrollX = 3 + p * (gs.getViewportSize(Orientation.HORIZONTAL) - 16.0 - 6);
			RenderUtil.drawGradientRectangle(0, (int) gs.getHeight() - 16, (int) gs.getWidth(), (int) gs.getHeight(), scrollBarColor.toInt(), scrollBarColor2.toInt());
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			RenderUtil.drawTexturedModalRectangle((int) scrollX, (int) (gs.getHeight() - 16), 0, 208, 16, 16, 0f);
		}
		if (gs.needsScrollBar(Orientation.VERTICAL)) {
			Minecraft mc = SpoutClient.getHandle();
			int texture = mc.renderEngine.getTexture("/gui/allitems.png");
			mc.renderEngine.bindTexture(texture);
			double scrollY = 0;
			double p = (double) scrollTop / (double) gs.getMaximumScrollPosition(Orientation.VERTICAL);
			scrollY = 3 + p * (gs.getViewportSize(Orientation.VERTICAL) - 16.0 - 6);
			RenderUtil.drawGradientRectangle((int) gs.getWidth() - 16, 0, (int) gs.getWidth(), (int) gs.getHeight(), scrollBarColor.toInt(), scrollBarColor2.toInt());
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			RenderUtil.drawTexturedModalRectangle((int) (gs.getWidth() - 16), (int) scrollY, 0, 208, 16, 16, 0f);

			RenderUtil.drawGradientRectangle(0, -1, (int) gs.getWidth(), 5, new Color(0.0F, 0.0F, 0.0F, 1.0F).toInt(), new Color(0.0F, 0.0F, 0.0F, 0.0F).toInt());
			RenderUtil.drawGradientRectangle(0, (int) gs.getHeight() - 5, (int) gs.getWidth() + 1, (int) gs.getHeight(), new Color(0.0F, 0.0F, 0.0F, 0.0F).toInt(), new Color(0.0F, 0.0F, 0.0F, 1.0F).toInt());
		}
	}

	public void renderContents(GenericListWidget lw) {
		int scrollTop = lw.getScrollPosition(Orientation.VERTICAL);
		int scrollBottom = (int) (scrollTop + lw.getHeight() + 5);
		GL11.glTranslated(0, 5, 0);
		int currentHeight = 0;
		for (ListWidgetItem item : lw.getItems()) {
			//Only render visible items
			if (currentHeight >= scrollTop - item.getHeight() && currentHeight <= scrollBottom) {

				//Draw selection border
				if (lw.isSelected(item)) {
					RenderUtil.drawRectangle(4, currentHeight - 1, lw.getViewportSize(Orientation.HORIZONTAL) - 3, currentHeight - 1 + item.getHeight() + 2, new Color(1.0F, 1.0F, 1.0F).toInt());
					RenderUtil.drawRectangle(5, currentHeight, lw.getViewportSize(Orientation.HORIZONTAL) - 4, currentHeight + item.getHeight(), new Color(0.0F, 0.0F, 0.0F).toInt());
				}

				//Render actual item
				GL11.glPushMatrix();
				item.render(5, currentHeight, lw.getViewportSize(Orientation.HORIZONTAL) - 9, item.getHeight());
				GL11.glPopMatrix();
			}

			currentHeight += item.getHeight();
		}
	}

	public void renderContents(GenericScrollArea genericScrollArea) {
		for (RenderPriority priority : RenderPriority.values()) {
			for (Widget w : genericScrollArea.getAttachedWidgets()) {
				if (w.getPriority() == priority) {
					GL11.glPushMatrix();
					w.render();
					GL11.glPopMatrix();
				}
			}
		}
	}

	public String getFittingText(String text, int width) {
		if (width <= 1) {
			return text;
		}
		int hash = (new HashCodeBuilder()).append(text).append(width).toHashCode();
		if (optimalWidth.contains(hash)) {
			return optimalWidth.get(hash);
		}
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		String t = new String(text);
		int remove = 0;
		while (font.getStringWidth(t) > width) {
			remove++;
			t = text.substring(0, text.length() - 1 - remove) + "...";
		}
		optimalWidth.put(hash, t);
		return t;
	}

	public void render(GenericComboBox comboBox) {
		if (comboBox.isVisible()) {
			comboBox.setInnerWidth((int) comboBox.getWidth() - 16);
			render((GenericButton) comboBox);
			Texture text;
			if (comboBox.isOpen()) {
				text = CustomTextureManager.getTextureFromJar("/res/ascending.png");
			} else {
				text = CustomTextureManager.getTextureFromJar("/res/descending.png");
			}
			GL11.glTranslated(comboBox.getWidth() - 16, 3, 0);
			RenderUtil.drawRectangle(0, -3, 16, (int) comboBox.getHeight(), 0x33000000);
			drawTexture(text, 16, 16, getColor(comboBox), true);
		}
	}
	//	public void render(GenericListWidget lw) {
	//		int scrollTop = lw.getScrollPosition();
	//		int scrollBottom = (int) (scrollTop + lw.getHeight() - 5);
	//		GL11.glTranslated(lw.getScreenX(), lw.getScreenY(), 0);
	//		GL11.glEnable(GL11.GL_SCISSOR_TEST);
	//		scissorWidget(lw);
	//		int currentHeight = 0;
	//		RenderUtil.drawRectangle(0, 0, (int)lw.getWidth(), (int)lw.getHeight(), new Color(0.0F,0.0F,0.0F,0.6F).toInt());
	//		GL11.glTranslated(0, -scrollTop + 5, 0);
	//		for (ListWidgetItem item:lw.getItems()) {
	//
	//			//Only render visible items
	//			if (currentHeight >= scrollTop - item.getHeight() && currentHeight <= scrollBottom) {
	//
	//				//Draw selection border
	//				if (lw.isSelected(item)) {
	//					RenderUtil.drawRectangle(4, currentHeight-1, (int) (lw.getWidth() - 13), currentHeight-1+item.getHeight()+2, new Color(1.0F,1.0F,1.0F).toInt());
	//					RenderUtil.drawRectangle(5, currentHeight, (int) (lw.getWidth() - 14), currentHeight+item.getHeight(), new Color(0.0F,0.0F,0.0F).toInt());
	//				}
	//
	//				//Render actual item
	//				GL11.glPushMatrix();
	//				item.render(5, currentHeight, (int) (lw.getWidth() - 15), item.getHeight());
	//				GL11.glPopMatrix();
	//			}
	//
	//			currentHeight += item.getHeight();
	//		}
	//		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	//		GL11.glTranslatef(0, scrollTop - 5, 0);
	//		GL11.glDisable(2896 /*GL_LIGHTING*/);
	//		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	//
	//		RenderUtil.drawGradientRectangle(0, 0, (int)lw.getWidth(), 5, new Color(0.0F,0.0F,0.0F,1.0F).toInt(), new Color(0.0F,0.0F,0.0F,0.0F).toInt());
	//		RenderUtil.drawGradientRectangle(0, (int)lw.getHeight() - 5, (int)lw.getWidth(), (int)lw.getHeight(), new Color(0.0F,0.0F,0.0F,0.0F).toInt(), new Color(0.0F,0.0F,0.0F,1.0F).toInt());
	//
	//		Minecraft mc = SpoutClient.getHandle();
	//		int texture = mc.renderEngine.getTexture("/gui/allitems.png");
	//		mc.renderEngine.bindTexture(texture);
	//		double scrollY = 0;
	//		double p = (double)scrollTop / (double)lw.getMaxScrollPosition();
	//		scrollY = 3 + p * (lw.getHeight() - 16.0 - 6);
	//		RenderUtil.drawTexturedModalRectangle((int) (lw.getWidth() - 14), (int) scrollY, 0, 208, 16, 16, 0f);
	//	}
}
