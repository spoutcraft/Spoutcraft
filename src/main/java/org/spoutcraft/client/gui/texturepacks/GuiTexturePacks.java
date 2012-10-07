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
package org.spoutcraft.client.gui.texturepacks;

import java.io.File;

import com.pclewis.mcpatcher.mod.TextureUtils;
import org.apache.commons.io.FileUtils;
import org.lwjgl.Sys;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.TexturePackBase;
import net.minecraft.src.TexturePackCustom;

import org.bukkit.ChatColor;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericListView;
import org.spoutcraft.api.gui.Label;
import org.spoutcraft.client.SpoutClient;

public class GuiTexturePacks extends GuiScreen {
	private GenericListView view;
	private Label screenTitle;
	private Label loadingTexture;
	private Button buttonDone, buttonOpenFolder, buttonSelect, buttonReservoir, buttonDelete, buttonInfo;
	private boolean instancesCreated = false;
	private TexturePacksModel model = SpoutClient.getInstance().getTexturePacksModel();

	private void createInstances() {
		model.setCurrentGui(this);
		if (instancesCreated) {
			return;
		}

		StringTranslate t = StringTranslate.getInstance();

		model.update();
		screenTitle = new GenericLabel(t.translateKey("texturePack.title", "Texture Packs"));
		loadingTexture = new GenericLabel(ChatColor.GREEN + t.translateKey("spout.texturepack.loading", "Loading texture..."));
		view = new GenericListView(model);
		buttonDone = new GenericButton(t.translateKey("gui.done", "Main Menu"));
		buttonOpenFolder = new GenericButton(t.translateKey("texturePack.openFolder", "Open Folder"));
		buttonSelect = new GenericButton(t.translateKey("spout.texturepack.select", "Select"));
		buttonReservoir = new GenericButton(t.translateKey("spout.texturepack.database", "Database"));
		buttonReservoir.setEnabled(false);
		buttonReservoir.setTooltip(t.translateKey("spout.texturepack.tip.database", "Disabled until further notice"));
		buttonDelete = new DeleteTexturepackButton(this, t.translateKey("spout.texturepack.delete", "Delete"));
		buttonInfo = new GenericButton(t.translateKey("spout.texturepack.info", "Info"));
	}

	public void initGui() {
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");

		createInstances();

		int top = 10;

		int swidth = mc.fontRenderer.getStringWidth(screenTitle.getText());
		screenTitle.setY(top).setX(width / 2 - swidth / 2).setHeight(11).setWidth(swidth);
		getScreen().attachWidget(spoutcraft, screenTitle);

		swidth = mc.fontRenderer.getStringWidth(loadingTexture.getText());
		loadingTexture.setVisible(false);
		loadingTexture.setY(top).setX(width / 2 + swidth).setHeight(11).setWidth(swidth);
		getScreen().attachWidget(spoutcraft, loadingTexture);

		top+=15;

		view.setX(5).setY(top).setWidth(width - 10).setHeight(height - top - 55);
		getScreen().attachWidget(spoutcraft, view);

		top += 5 + view.getHeight();

		int totalWidth = Math.min(width - 10, 200 * 3 + 10);
		int cellWidth = (totalWidth - 10) / 3;
		int left = width / 2 - totalWidth / 2;
		int center = left + 5 + cellWidth;
		int right = center + 5 + cellWidth;

		buttonSelect.setX(right).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonSelect);

		buttonInfo.setX(center).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonInfo);

		buttonDelete.setX(left).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonDelete);

		top += 25;

		buttonOpenFolder.setX(left).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonOpenFolder);

		buttonReservoir.setX(center).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonReservoir);

		buttonDone.setX(right).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonDone);

		if (!instancesCreated) {
			int selected;
			selected = model.getTextures().indexOf(TextureUtils.getSelectedTexturePack());
			view.setSelection(selected);
		}

		instancesCreated = true;
		updateButtons();
	}

	public void drawScreen(int x, int y, float f) {
		drawDefaultBackground();
	}

	@Override
	public void buttonClicked(Button btn) {
		if (btn.equals(buttonDone)) {
			SpoutClient.getHandle().displayGuiScreen(new org.spoutcraft.client.gui.mainmenu.MainMenu());
		}
		if (btn.equals(buttonOpenFolder)) {
			System.out.println(SpoutClient.getInstance().getTexturePackFolder().getAbsolutePath());
			Sys.openURL("file://" + SpoutClient.getInstance().getTexturePackFolder().getAbsolutePath());
		}
		if (btn.equals(buttonSelect) && view.getSelectedRow() != -1) {
			TexturePackItem item = model.getItem(view.getSelectedRow());
			boolean current = item.getPack() == TextureUtils.getSelectedTexturePack();
			if (!current) {
				item.select();
				updateButtons();
			} else {
				GuiPreviewTexturePack preview = new GuiPreviewTexturePack(this);
				mc.displayGuiScreen(preview);
			}
		}
		if (btn.equals(buttonReservoir)) {
			mc.displayGuiScreen(new GuiTexturePacksDatabase());
		}
		if (btn.equals(buttonInfo)) {
			try {
				TexturePackItem item = model.getItem(view.getSelectedRow());
				if (item.id != -1) {
					Sys.openURL("http://textures.spout.org/info/" + item.id);
				}
			} catch(Exception e) {
			}
		}
	}

	public void updateButtons() {
		try {
			StringTranslate t = StringTranslate.getInstance();
			TexturePackItem item = model.getItem(view.getSelectedRow());
			boolean current = item.getPack() == TextureUtils.getSelectedTexturePack();
			buttonSelect.setEnabled(true);
			if (current) {
				buttonSelect.setText(t.translateKey("spout.texturepack.preview.button", "Preview"));
			} else {
				buttonSelect.setText(t.translateKey("spout.texturepack.select", "Select"));
			}
			buttonInfo.setEnabled(item.id != -1);
			buttonDelete.setEnabled(!current && (item.getPack() instanceof TexturePackCustom));
		} catch(Exception e) {}
	}

	public void deleteCurrentTexturepack() {
		for (int tries = 0; tries < 3; tries++) {
			try {
				TexturePackBase pack = model.getItem(view.getSelectedRow()).getPack();
				if (pack instanceof TexturePackCustom) {
					TexturePackCustom custom = (TexturePackCustom) pack;
					custom.closeTexturePackFile();
					File d = new File(SpoutClient.getInstance().getTexturePackFolder(), custom.func_77538_c());
					if (!d.exists()) {
						d = new File(new File(Minecraft.getAppDir("minecraft"), "texturepacks"), custom.func_77538_c());
					}
					d.setWritable(true);
					FileUtils.forceDelete(d);
					model.update();

					if (!d.exists()) {
						break;
					}

					Thread.sleep(25);
				}
			} catch(Exception e) {
			}
		}
	}

	public void setLoading(boolean newValue) {
		loadingTexture.setVisible(newValue);
	}
}
