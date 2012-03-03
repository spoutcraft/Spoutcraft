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
package org.spoutcraft.client.gui.singleplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.src.GuiScreen;
import net.minecraft.src.MathHelper;
import net.minecraft.src.PlayerControllerCreative;
import net.minecraft.src.PlayerControllerSP;
import net.minecraft.src.WorldSettings;
import net.minecraft.src.WorldType;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.GuiSpoutScreen;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;
import org.spoutcraft.spoutcraftapi.gui.GenericComboBox;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericScrollArea;
import org.spoutcraft.spoutcraftapi.gui.GenericTextField;

public class GuiCreateWorld extends GuiSpoutScreen {
	
	private GenericButton buttonDone, buttonCancel, buttonNewSeed;
	private GenericComboBox comboGameType, comboWorldType, comboWorldHeight;
	private GenericCheckBox checkGenerateStructures;
	private GenericTextField textName, textSeed;
	private GenericLabel labelTitle, labelName, labelSeed, labelGameType, labelWorldType, labelWorldHeight, labelFilePreview;
	private GenericScrollArea scrollArea;
	
	private GuiScreen parent;
	
	private static List<String> listGameTypes = new ArrayList<String>();
	private static List<String> listWorldTypes = new ArrayList<String>();
	private static List<String> listWorldHeights = new ArrayList<String>();
	
	Random seed = new Random();
	private boolean createClicked;
	
	static {
		listGameTypes.add("Survival");
		listGameTypes.add("Creative");
		listGameTypes.add("Hardcore");
		
		listWorldTypes.add("Normal");
		listWorldTypes.add("Superflat");
		
		for(int i = 6; i <= 11; i++) {
			listWorldHeights.add(""+(int) Math.pow(2, i));
		}
	}
	
	public GuiCreateWorld(GuiScreen parent) {
		this.parent = parent;
	}
	
	@Override
	protected void createInstances() {
		buttonDone = new GenericButton("Done");
		
		buttonCancel = new GenericButton("Cancel");
		
		comboGameType = new GenericComboBox();
		comboGameType.setItems(listGameTypes);
		
		labelGameType = new GenericLabel("Game Type");
		
		comboWorldType = new GenericComboBox();
		comboWorldType.setItems(listWorldTypes);
		
		labelWorldType = new GenericLabel("World Type");
		
		comboWorldHeight = new GenericComboBox();
		comboWorldHeight.setItems(listWorldHeights);
		comboWorldHeight.setSelection(2); //Set to 128 by default.
		comboWorldHeight.setEnabled(false);
		comboWorldHeight.setTooltip("Broken as of Minecraft 1.2 :(");
		
		labelWorldHeight = new GenericLabel("World Height");
		
		checkGenerateStructures = new GenericCheckBox("Generate Structures");
		checkGenerateStructures.setTooltip("Villages, Dungeons, Strongholds, etc.");
		checkGenerateStructures.setChecked(true);
		
		textName = new GenericTextField();
		textName.setMaximumCharacters(0);
		textName.setWidth(200);
		textName.setText("World");
		
		textSeed = new GenericTextField();
		textSeed.setTooltip("Insert start-value for the random generator here,\nor leave blank for random seed.");
		textSeed.setMaximumCharacters(0);
		textSeed.setWidth(200);
		updateSeed();
		
		buttonNewSeed = new GenericButton("New Seed");
		
		labelTitle = new GenericLabel("Create New world");
		
		labelName = new GenericLabel("Name");
		labelFilePreview = new GenericLabel("Will be saved as World");
		updateSavePreview();
		
		labelSeed = new GenericLabel("Seed");
		
		scrollArea = new GenericScrollArea();
		
		Addon spoutcraft = SpoutClient.getInstance().getAddonManager().getAddon("Spoutcraft");
		getScreen().attachWidgets(spoutcraft, labelTitle, scrollArea, buttonDone, buttonCancel);
		scrollArea.attachWidgets(spoutcraft, comboGameType, comboWorldHeight, comboWorldType, checkGenerateStructures, textName, textSeed, labelName, labelSeed, labelGameType, labelWorldType, labelWorldHeight, labelFilePreview, buttonNewSeed);
	}

	@Override
	protected void layoutWidgets() {
		int top = 5;
		int swidth = mc.fontRenderer.getStringWidth(labelTitle.getText());
		labelTitle.setY(top + 7).setX(width / 2 - swidth / 2).setHeight(11).setWidth(swidth);
		
		top += 25;
		
		scrollArea.setX(5).setY(top).setHeight(height - top - 30).setWidth(width - 10);
		
		
		int ftop = 5;
		int fleft = 5;
		int fright = width / 2 - 100;
		labelName.setX(fleft).setY(ftop + 4).setHeight(16).setWidth(100);
		textName.setX(fright).setY(ftop).setHeight(16).setWidth(200);
		
		ftop += 22;
		
		labelFilePreview.setX(fright).setY(ftop).setHeight(16).setWidth(200);
		
		ftop += 13;
		
		labelSeed.setX(fleft).setY(ftop + 4).setHeight(16).setWidth(100);
		textSeed.setX(fright).setY(ftop).setHeight(16).setWidth(200);
		buttonNewSeed.setX(fright + 205).setY(ftop - 1).setHeight(20).setWidth(75);
		
		ftop += 22;
		
		labelGameType.setX(fleft).setY(ftop + 6).setWidth(200).setHeight(20);
		comboGameType.setX(fright).setY(ftop).setWidth(200).setHeight(20);
		
		ftop += 25;
		
		labelWorldHeight.setX(fleft).setY(ftop + 6).setWidth(200).setHeight(20);
		comboWorldHeight.setX(fright).setY(ftop).setWidth(200).setHeight(20);
		
		ftop += 25;
		
		labelWorldType.setX(fleft).setY(ftop + 6).setWidth(200).setHeight(20);
		comboWorldType.setX(fright).setY(ftop).setWidth(200).setHeight(20);
		
		ftop += 25;
		
		checkGenerateStructures.setX(fright).setY(ftop).setWidth(200).setHeight(20);
		
		scrollArea.updateInnerSize();
		
		top = height - 25;
		int cellWidth = Math.min(200, (width - 15) / 2);
		int left = width / 2 - 2 - cellWidth;
		int right = left + 5 + cellWidth;
		
		buttonCancel.setX(left).setY(top).setWidth(cellWidth).setHeight(20);
		buttonDone.setX(right).setY(top).setWidth(cellWidth).setHeight(20);
	}

	@Override
	protected void buttonClicked(Button btn) {
		if(btn == buttonCancel) {
			SpoutClient.getHandle().displayGuiScreen(parent);
		}
		if(btn == buttonDone) {
			this.mc.displayGuiScreen((GuiScreen)null);
			if(this.createClicked) {
				return;
			}

			this.createClicked = true;
			long seed = (new Random()).nextLong();
			String var4 = textSeed.getText();
			if(!MathHelper.stringNullOrLengthZero(var4)) {
				try {
					long var5 = Long.parseLong(var4);
					if(var5 != 0L) {
						seed = var5;
					}
				} catch (NumberFormatException var7) {
					seed = (long)var4.hashCode();
				}
			}

			byte var9 = 0;
			if(comboGameType.getSelectedItem().equals("Creative")) {
				var9 = 1;
				this.mc.playerController = new PlayerControllerCreative(this.mc);
			} else {
				this.mc.playerController = new PlayerControllerSP(this.mc);
			}

			boolean hardcore = comboGameType.getSelectedItem().equals("Hardcore");
			int height = Integer.valueOf(comboWorldHeight.getSelectedItem());
			
			this.mc.startWorld(getEffectiveSaveName(), textName.getText(), new WorldSettings(seed, var9, checkGenerateStructures.isChecked(), hardcore, WorldType.field_48637_a[comboWorldType.getSelectedRow()]));
			this.mc.displayGuiScreen((GuiScreen)null);
		}
		if(btn == buttonNewSeed) {
			updateSeed();
		}
	}

	@Override
	public void handleKeyboardInput() {
		super.handleKeyboardInput();
		updateSavePreview();
		if(textName.getText().trim().isEmpty()) {
			buttonDone.setEnabled(false);
		} else {
			buttonDone.setEnabled(true);
		}
	}
	
	private void updateSavePreview() {
		labelFilePreview.setText("Saves as '"+getEffectiveSaveName()+"'");
	}

	private void updateSeed() {
		textSeed.setText(String.valueOf(Math.abs(seed.nextLong())));
	}
	
	protected String getEffectiveSaveName() {
		String worldname = textName.getText();
		String save = worldname;
		
		save = save.replaceAll("[^A-Za-z0-9]", "-");
		String savesDir = new File(mc.mcDataDir, "saves").getAbsolutePath();
		if((new File(savesDir, save)).exists()) {
			int num = 1;
			while((new File(savesDir, save + "-("+num+")")).exists()) {
				num ++;
			}
			save = save + "-("+num+")";
		}
		
		return save;
	}

}
