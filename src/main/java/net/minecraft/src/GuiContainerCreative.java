package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
// Spout Start
import org.spoutcraft.api.material.CustomItem;
import org.spoutcraft.api.material.MaterialData;
// Spout End

public class GuiContainerCreative extends InventoryEffectRenderer {
	private static InventoryBasic inventory = new InventoryBasic("tmp", true, 45);

	/** Currently selected creative inventory tab index. */
	private static int selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();

	/** Amount scrolled in Creative mode inventory (0 = top, 1 = bottom) */
	private float currentScroll = 0.0F;

	/** True if the scrollbar is being dragged */
	private boolean isScrolling = false;

	/**
	 * True if the left mouse button was held down last time drawScreen was called.
	 */
	private boolean wasClicking;
	private GuiTextField searchField;

	/**
	 * Used to back up the ContainerCreative's inventory slots before filling it with the player's inventory slots for the
	 * inventory tab.
	 */
	private List backupContainerSlots;
	private Slot field_74235_v = null;
	private boolean field_74234_w = false;
	private CreativeCrafting field_82324_x;

	public GuiContainerCreative(EntityPlayer par1EntityPlayer) {
		super(new ContainerCreative(par1EntityPlayer));
		par1EntityPlayer.openContainer = this.inventorySlots;
		this.allowUserInput = true;
		par1EntityPlayer.addStat(AchievementList.openInventory, 1);
		this.ySize = 136;
		this.xSize = 195;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		if (!this.mc.playerController.isInCreativeMode()) {
			this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
		}
	}

	protected void handleMouseClick(Slot par1Slot, int par2, int par3, int par4) {
		this.field_74234_w = true;
		boolean var5 = par4 == 1;
		par4 = par2 == -999 && par4 == 0 ? 4 : par4;
		ItemStack var7;
		InventoryPlayer var10;

		if (par1Slot == null && selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && par4 != 5) {
			var10 = this.mc.thePlayer.inventory;

			if (var10.getItemStack() != null) {
				if (par3 == 0) {
					this.mc.thePlayer.dropPlayerItem(var10.getItemStack());
					this.mc.playerController.func_78752_a(var10.getItemStack());
					var10.setItemStack((ItemStack)null);
				}

				if (par3 == 1) {
					var7 = var10.getItemStack().splitStack(1);
					this.mc.thePlayer.dropPlayerItem(var7);
					this.mc.playerController.func_78752_a(var7);

					if (var10.getItemStack().stackSize == 0) {
						var10.setItemStack((ItemStack)null);
					}
				}
			}
		} else {
			int var11;

			if (par1Slot == this.field_74235_v && var5) {
				for (var11 = 0; var11 < this.mc.thePlayer.inventoryContainer.getInventory().size(); ++var11) {
					this.mc.playerController.sendSlotPacket((ItemStack)null, var11);
				}
			} else {
				ItemStack var6;

				if (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex()) {
					if (par1Slot == this.field_74235_v) {
						this.mc.thePlayer.inventory.setItemStack((ItemStack)null);
					} else if (par4 == 4 && par1Slot != null && par1Slot.getHasStack()) {
						var6 = par1Slot.decrStackSize(par3 == 0 ? 1 : par1Slot.getStack().getMaxStackSize());
						this.mc.thePlayer.dropPlayerItem(var6);
						this.mc.playerController.func_78752_a(var6);
					} else if (par4 == 4 && this.mc.thePlayer.inventory.getItemStack() != null) {
						this.mc.thePlayer.dropPlayerItem(this.mc.thePlayer.inventory.getItemStack());
						this.mc.playerController.func_78752_a(this.mc.thePlayer.inventory.getItemStack());
						this.mc.thePlayer.inventory.setItemStack((ItemStack)null);
					} else {
						this.mc.thePlayer.inventoryContainer.slotClick(par1Slot == null ? par2 : SlotCreativeInventory.func_75240_a((SlotCreativeInventory)par1Slot).slotNumber, par3, par4, this.mc.thePlayer);
						this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
					}
				} else if (par4 != 5 && par1Slot.inventory == inventory) {
					var10 = this.mc.thePlayer.inventory;
					var7 = var10.getItemStack();
					ItemStack var8 = par1Slot.getStack();
					ItemStack var9;

					if (par4 == 2) {
						if (var8 != null && par3 >= 0 && par3 < 9) {
							var9 = var8.copy();
							var9.stackSize = var9.getMaxStackSize();
							this.mc.thePlayer.inventory.setInventorySlotContents(par3, var9);
							this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
						}

						return;
					}

					if (par4 == 3) {
						if (var10.getItemStack() == null && par1Slot.getHasStack()) {
							var9 = par1Slot.getStack().copy();
							var9.stackSize = var9.getMaxStackSize();
							var10.setItemStack(var9);
						}

						return;
					}

					if (par4 == 4) {
						if (var8 != null) {
							var9 = var8.copy();
							var9.stackSize = par3 == 0 ? 1 : var9.getMaxStackSize();
							this.mc.thePlayer.dropPlayerItem(var9);
							this.mc.playerController.func_78752_a(var9);
						}

						return;
					}

					if (var7 != null && var8 != null && var7.isItemEqual(var8)) {
						if (par3 == 0) {
							if (var5) {
								var7.stackSize = var7.getMaxStackSize();
							} else if (var7.stackSize < var7.getMaxStackSize()) {
								++var7.stackSize;
							}
						} else if (var7.stackSize <= 1) {
							var10.setItemStack((ItemStack)null);
						} else {
							--var7.stackSize;
						}
					} else if (var8 != null && var7 == null) {
						var10.setItemStack(ItemStack.copyItemStack(var8));
						var7 = var10.getItemStack();

						if (var5) {
							var7.stackSize = var7.getMaxStackSize();
						}
					} else {
						var10.setItemStack((ItemStack)null);
					}
				} else {
					this.inventorySlots.slotClick(par1Slot == null ? par2 : par1Slot.slotNumber, par3, par4, this.mc.thePlayer);

					if (Container.func_94532_c(par3) == 2) {
						for (var11 = 0; var11 < 9; ++var11) {
							this.mc.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + var11).getStack(), 36 + var11);
						}
					} else if (par1Slot != null) {
						var6 = this.inventorySlots.getSlot(par1Slot.slotNumber).getStack();
						this.mc.playerController.sendSlotPacket(var6, par1Slot.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
					}
				}
			}
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		if (this.mc.playerController.isInCreativeMode()) {
			super.initGui();
			this.buttonList.clear();
			Keyboard.enableRepeatEvents(true);
			this.searchField = new GuiTextField(this.fontRenderer, this.guiLeft + 82, this.guiTop + 6, 89, this.fontRenderer.FONT_HEIGHT);
			this.searchField.setMaxStringLength(15);
			this.searchField.setEnableBackgroundDrawing(false);
			this.searchField.setVisible(false);
			this.searchField.setTextColor(16777215);
			int var1 = selectedTabIndex;
			selectedTabIndex = -1;
			this.setCurrentCreativeTab(CreativeTabs.creativeTabArray[var1]);
			this.field_82324_x = new CreativeCrafting(this.mc);
			this.mc.thePlayer.inventoryContainer.addCraftingToCrafters(this.field_82324_x);
		} else {
			this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
		}
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed() {
		super.onGuiClosed();

		if (this.mc.thePlayer != null && this.mc.thePlayer.inventory != null) {
			this.mc.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.field_82324_x);
		}

		Keyboard.enableRepeatEvents(false);
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		if (selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex()) {
			if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat)) {
				this.setCurrentCreativeTab(CreativeTabs.tabAllSearch);
			} else {
				super.keyTyped(par1, par2);
			}
		} else {
			if (this.field_74234_w) {
				this.field_74234_w = false;
				this.searchField.setText("");
			}

			if (!this.checkHotbarKeys(par2)) {
				if (this.searchField.textboxKeyTyped(par1, par2)) {
					this.updateCreativeSearch();
				} else {
					super.keyTyped(par1, par2);
				}
			}
		}
	}

	private void updateCreativeSearch() {
		ContainerCreative var1 = (ContainerCreative)this.inventorySlots;
		var1.itemList.clear();
		Item[] var2 = Item.itemsList;
		int var3 = var2.length;
		int var4;

		for (var4 = 0; var4 < var3; ++var4) {
			Item var5 = var2[var4];

			if (var5 != null && var5.getCreativeTab() != null) {
				var5.getSubItems(var5.itemID, (CreativeTabs)null, var1.itemList);
			}
		}

		Enchantment[] var8 = Enchantment.enchantmentsList;
		var3 = var8.length;

		for (var4 = 0; var4 < var3; ++var4) {
			Enchantment var12 = var8[var4];

			if (var12 != null && var12.type != null) {
				Item.enchantedBook.func_92113_a(var12, var1.itemList);
			}
		}

		Iterator var9 = var1.itemList.iterator();
		String var10 = this.searchField.getText().toLowerCase();

		while (var9.hasNext()) {
			ItemStack var11 = (ItemStack)var9.next();
			boolean var13 = false;
			Iterator var6 = var11.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips).iterator();

			while (true) {
				if (var6.hasNext()) {
					String var7 = (String)var6.next();

					if (!var7.toLowerCase().contains(var10)) {
						continue;
					}

					var13 = true;
				}

				if (!var13) {
					var9.remove();
				}

				break;
			}
		}

		this.currentScroll = 0.0F;
		var1.scrollTo(0.0F);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		CreativeTabs var3 = CreativeTabs.creativeTabArray[selectedTabIndex];

		if (var3.drawInForegroundOfTab()) {
			this.fontRenderer.drawString(var3.getTranslatedTabLabel(), 8, 6, 4210752);
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3) {
		if (par3 == 0) {
			int var4 = par1 - this.guiLeft;
			int var5 = par2 - this.guiTop;
			CreativeTabs[] var6 = CreativeTabs.creativeTabArray;
			int var7 = var6.length;

			for (int var8 = 0; var8 < var7; ++var8) {
				CreativeTabs var9 = var6[var8];

				if (this.func_74232_a(var9, var4, var5)) {
					//this.setCurrentCreativeTab(var9); Might not be needed in 1.5
					return;
				}
			}

			// Spout Start
			// TODO: This might not be needed
			handleSpoutMouse(par1, par2, true);
			// Spout End
		}

		super.mouseClicked(par1, par2, par3);
	}

	/**
	 * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
	 * mouseMove, which==0 or which==1 is mouseUp
	 */
	protected void mouseMovedOrUp(int par1, int par2, int par3) {
		if (par3 == 0) {
			int var4 = par1 - this.guiLeft;
			int var5 = par2 - this.guiTop;
			CreativeTabs[] var6 = CreativeTabs.creativeTabArray;
			int var7 = var6.length;

			for (int var8 = 0; var8 < var7; ++var8) {
				CreativeTabs var9 = var6[var8];

				if (this.func_74232_a(var9, var4, var5)) {
					this.setCurrentCreativeTab(var9);
					return;
				}
			}
		}

		super.mouseMovedOrUp(par1, par2, par3);
	}

	/**
	 * returns (if you are not on the inventoryTab) and (the flag isn't set) and( you have more than 1 page of items)
	 */
	private boolean needsScrollBars() {
		return selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory() && ((ContainerCreative)this.inventorySlots).hasMoreThan1PageOfItemsInList();
	}

	private void setCurrentCreativeTab(CreativeTabs par1CreativeTabs) {
		int var2 = selectedTabIndex;
		selectedTabIndex = par1CreativeTabs.getTabIndex();
		ContainerCreative var3 = (ContainerCreative)this.inventorySlots;
		this.field_94077_p.clear();
		var3.itemList.clear();
		par1CreativeTabs.displayAllReleventItems(var3.itemList);

		if (par1CreativeTabs == CreativeTabs.tabInventory) {
			Container var4 = this.mc.thePlayer.inventoryContainer;

			if (this.backupContainerSlots == null) {
				this.backupContainerSlots = var3.inventorySlots;
			}

			var3.inventorySlots = new ArrayList();

			for (int var5 = 0; var5 < var4.inventorySlots.size(); ++var5) {
				SlotCreativeInventory var6 = new SlotCreativeInventory(this, (Slot)var4.inventorySlots.get(var5), var5);
				var3.inventorySlots.add(var6);
				int var7;
				int var8;
				int var9;

				if (var5 >= 5 && var5 < 9) {
					var7 = var5 - 5;
					var8 = var7 / 2;
					var9 = var7 % 2;
					var6.xDisplayPosition = 9 + var8 * 54;
					var6.yDisplayPosition = 6 + var9 * 27;
				} else if (var5 >= 0 && var5 < 5) {
					var6.yDisplayPosition = -2000;
					var6.xDisplayPosition = -2000;
				} else if (var5 < var4.inventorySlots.size()) {
					var7 = var5 - 9;
					var8 = var7 % 9;
					var9 = var7 / 9;
					var6.xDisplayPosition = 9 + var8 * 18;

					if (var5 >= 36) {
						var6.yDisplayPosition = 112;
					} else {
						var6.yDisplayPosition = 54 + var9 * 18;
					}
				}
			}

			this.field_74235_v = new Slot(inventory, 0, 173, 112);
			var3.inventorySlots.add(this.field_74235_v);
		} else if (var2 == CreativeTabs.tabInventory.getTabIndex()) {
			var3.inventorySlots = this.backupContainerSlots;
			this.backupContainerSlots = null;
		}

		if (this.searchField != null) {
			if (par1CreativeTabs == CreativeTabs.tabAllSearch) {
				this.searchField.setVisible(true);
				this.searchField.setCanLoseFocus(false);
				this.searchField.setFocused(true);
				this.searchField.setText("");
				this.updateCreativeSearch();
			} else {
				this.searchField.setVisible(false);
				this.searchField.setCanLoseFocus(true);
				this.searchField.setFocused(false);
			}
		}

		this.currentScroll = 0.0F;
		var3.scrollTo(0.0F);
	}

	/**
	 * Handles mouse input.
	 */
	public void handleMouseInput() {
		super.handleMouseInput();
		int var1 = Mouse.getEventDWheel();

		if (var1 != 0 && this.needsScrollBars()) {
			int var2 = ((ContainerCreative)this.inventorySlots).itemList.size() / 9 - 5 + 1;

			if (var1 > 0) {
				var1 = 1;
			}

			if (var1 < 0) {
				var1 = -1;
			}

			this.currentScroll = (float)((double)this.currentScroll - (double)var1 / (double)var2);

			if (this.currentScroll < 0.0F) {
				this.currentScroll = 0.0F;
			}

			if (this.currentScroll > 1.0F) {
				this.currentScroll = 1.0F;
			}

			((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		boolean var4 = Mouse.isButtonDown(0);
		int var5 = this.guiLeft;
		int var6 = this.guiTop;
		int var7 = var5 + 175;
		int var8 = var6 + 18;
		int var9 = var7 + 14;
		int var10 = var8 + 112;

		if (!this.wasClicking && var4 && par1 >= var7 && par2 >= var8 && par1 < var9 && par2 < var10) {
			this.isScrolling = this.needsScrollBars();
		}

		if (!var4) {
			this.isScrolling = false;
		}

		this.wasClicking = var4;

		if (this.isScrolling) {
			this.currentScroll = ((float)(par2 - var8) - 7.5F) / ((float)(var10 - var8) - 15.0F);

			if (this.currentScroll < 0.0F) {
				this.currentScroll = 0.0F;
			}

			if (this.currentScroll > 1.0F) {
				this.currentScroll = 1.0F;
			}

			((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
		}

		super.drawScreen(par1, par2, par3);
		CreativeTabs[] var11 = CreativeTabs.creativeTabArray;
		int var12 = var11.length;

		for (int var13 = 0; var13 < var12; ++var13) {
			CreativeTabs var14 = var11[var13];

			if (this.renderCreativeInventoryHoveringText(var14, par1, par2)) {
				break;
			}
		}

		// Spout Start
		handleSpoutMouse(par1, par2, false);
		// Spout End

		if (this.field_74235_v != null && selectedTabIndex == CreativeTabs.tabInventory.getTabIndex() && this.isPointInRegion(this.field_74235_v.xDisplayPosition, this.field_74235_v.yDisplayPosition, 16, 16, par1, par2)) {
			this.drawCreativeTabHoveringText(StringTranslate.getInstance().translateKey("inventory.binSlot"), par1, par2);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.enableGUIStandardItemLighting();
		CreativeTabs var4 = CreativeTabs.creativeTabArray[selectedTabIndex];
		CreativeTabs[] var5 = CreativeTabs.creativeTabArray;
		int var6 = var5.length;
		int var7;

		for (var7 = 0; var7 < var6 - 1; ++var7) { //Spout don't render the last tab
			CreativeTabs var8 = var5[var7];
			this.mc.renderEngine.bindTexture("/gui/allitems.png");

			if (var8.getTabIndex() != selectedTabIndex) {
				this.renderCreativeTab(var8);
			}
		}

		// Spout Start - Render ours separately
		renderInInventory(true);
		// Spout End

		this.mc.renderEngine.bindTexture("/gui/creative_inv/" + var4.getBackgroundImageName());
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		this.searchField.drawTextBox();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int var9 = this.guiLeft + 175;
		var6 = this.guiTop + 18;
		var7 = var6 + 112;
		this.mc.renderEngine.bindTexture("/gui/allitems.png");

		if (var4.shouldHidePlayerInventory()) {
			this.drawTexturedModalRect(var9, var6 + (int)((float)(var7 - var6 - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
		}

		// Spout Start
		renderInInventory(false); // Render ours separately
		if (selectedTabIndex == 12) { // Return, useless code now
			return;
		}
		// Spout End

		this.renderCreativeTab(var4);

		if (var4 == CreativeTabs.tabInventory) {
			GuiInventory.drawPlayerOnGui(this.mc, this.guiLeft + 43, this.guiTop + 45, 20, (float)(this.guiLeft + 43 - par2), (float)(this.guiTop + 45 - 30 - par3));
		}
	}

	protected boolean func_74232_a(CreativeTabs par1CreativeTabs, int par2, int par3) {
		int var4 = par1CreativeTabs.getTabColumn();
		int var5 = 28 * var4;
		byte var6 = 0;

		if (var4 == 5) {
			var5 = this.xSize - 28 + 2;
		} else if (var4 > 0) {
			var5 += var4;
		}

		int var7;

		if (par1CreativeTabs.isTabInFirstRow()) {
			var7 = var6 - 32;
		} else {
			var7 = var6 + this.ySize;
		}

		return par2 >= var5 && par2 <= var5 + 28 && par3 >= var7 && par3 <= var7 + 32;
	}

	/**
	 * Renders the creative inventory hovering text if mouse is over it. Returns true if did render or false otherwise.
	 * Params: current creative tab to be checked, current mouse x position, current mouse y position.
	 */
	protected boolean renderCreativeInventoryHoveringText(CreativeTabs par1CreativeTabs, int par2, int par3) {
		int var4 = par1CreativeTabs.getTabColumn();
		int var5 = 28 * var4;
		byte var6 = 0;

		if (var4 == 5) {
			var5 = this.xSize - 28 + 2;
		} else if (var4 > 0) {
			var5 += var4;
		}

		int var7;

		if (par1CreativeTabs.isTabInFirstRow()) {
			var7 = var6 - 32;
		} else {
			var7 = var6 + this.ySize;
		}

		if (this.isPointInRegion(var5 + 3, var7 + 3, 23, 27, par2, par3)) {
			this.drawCreativeTabHoveringText(par1CreativeTabs.getTranslatedTabLabel(), par2, par3);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Renders passed creative inventory tab into the screen.
	 */
	protected void renderCreativeTab(CreativeTabs par1CreativeTabs) {
		boolean var2 = par1CreativeTabs.getTabIndex() == selectedTabIndex;
		boolean var3 = par1CreativeTabs.isTabInFirstRow();
		int var4 = par1CreativeTabs.getTabColumn();
		int var5 = var4 * 28;
		int var6 = 0;
		int var7 = this.guiLeft + 28 * var4;
		int var8 = this.guiTop;
		byte var9 = 32;

		if (var2) {
			var6 += 32;
		}

		if (var4 == 5) {
			var7 = this.guiLeft + this.xSize - 28;
		} else if (var4 > 0) {
			var7 += var4;
		}

		if (var3) {
			var8 -= 28;
		} else {
			var6 += 64;
			var8 += this.ySize - 4;
		}

		GL11.glDisable(GL11.GL_LIGHTING);
		this.drawTexturedModalRect(var7, var8, var5, var6, 28, var9);
		this.zLevel = 100.0F;
		itemRenderer.zLevel = 100.0F;
		var7 += 6;
		var8 += 8 + (var3 ? 1 : -1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		ItemStack var10 = new ItemStack(par1CreativeTabs.getTabIconItem());
		itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, var10, var7, var8);
		itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, var10, var7, var8);
		GL11.glDisable(GL11.GL_LIGHTING);
		itemRenderer.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 0) {
			this.mc.displayGuiScreen(new GuiAchievements(this.mc.statFileWriter));
		}

		if (par1GuiButton.id == 1) {
			this.mc.displayGuiScreen(new GuiStats(this, this.mc.statFileWriter));
		}
	}

	public int func_74230_h() {
		return selectedTabIndex;
	}

	/**
	 * Returns the creative inventory
	 */
	static InventoryBasic getInventory() {
		return inventory;
	}

	// Spout Start
	public void handleSpoutMouse(int x, int y, boolean isClicked) {
		int var7 = guiTop;
		int var5 = guiLeft - 26;
		boolean inside = x >= var5 && x <= var5 + 28 && y >= var7 && y <= var7 + 32;
		if (inside) {
			drawCreativeTabHoveringText("Custom Items", x, y);
			if (isClicked) {
				selectedTabIndex = CreativeTabs.tabSpout.getTabIndex();
				ContainerCreative var3 = (ContainerCreative)this.inventorySlots;
				var3.itemList.clear();
				CreativeTabs.tabSpout.displayAllReleventItems(var3.itemList);
				this.searchField.setVisible(false);
				this.searchField.setCanLoseFocus(true);
				this.searchField.setFocused(false);
				this.currentScroll = 0.0F;
				var3.scrollTo(0.0F);
			}
		}
	}

	public void renderInInventory(boolean beforeBackground) {
		if (!beforeBackground && selectedTabIndex != 12) {
			return;
		}
		this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/allitems.png"));
		byte var9 = 32;

		int startY = 0;
		int diff = 75;
		if (!beforeBackground) {
			startY = 32;
			diff=79;
		}

		int var8 = this.guiTop;
		int var7 = this.guiLeft-21;

		GL11.glDisable(GL11.GL_LIGHTING);
		//this.drawTexturedModalRect(var7, var8, 56, 0, 28, var9);
		this.drawTexturedModalRect(var7, var8, 56, startY, 25, var9/2);
		this.drawTexturedModalRect(var7, var8 + var9/2, 56, startY+diff, 25, var9/2);
		this.zLevel = 100.0F;
		itemRenderer.zLevel = 100.0F;
		var7 += 6;
		var8 += 7;
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		// Bypass stupidity
		ItemStack var10;
		if (MaterialData.getCustomItems().length == 0) {
			var10 = new ItemStack(Item.flint);
		} else {
			CustomItem cItem = MaterialData.getCustomItems()[0];
			var10 = new ItemStack(cItem.getRawId(), 1, cItem.getRawData());
		}
		// End of stupidity
		itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, var10, var7, var8);
		itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, var10, var7, var8);
		GL11.glDisable(GL11.GL_LIGHTING);
		itemRenderer.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}
	// Spout End
}
