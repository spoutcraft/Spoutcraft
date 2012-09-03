package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

//Spout start
import org.spoutcraft.spoutcraftapi.material.MaterialData;
import org.spoutcraft.spoutcraftapi.material.CustomItem;
//Spout end

public class GuiContainerCreative extends InventoryEffectRenderer {
	private static InventoryBasic inventory = new InventoryBasic("tmp", 45);
	private static int field_74241_p = CreativeTabs.tabBlock.getTabIndex();

	/** Amount scrolled in Creative mode inventory (0 = top, 1 = bottom) */
	private float currentScroll = 0.0F;

	/** True if the scrollbar is being dragged */
	private boolean isScrolling = false;

	/**
	 * True if the left mouse button was held down last time drawScreen was called.
	 */
	private boolean wasClicking;
	private GuiTextField field_74237_t;
	private List field_74236_u;
	private Slot field_74235_v = null;
	private boolean field_74234_w = false;

	public GuiContainerCreative(EntityPlayer par1EntityPlayer) {
		super(new ContainerCreative(par1EntityPlayer));
		par1EntityPlayer.craftingInventory = this.inventorySlots;
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

	protected void handleMouseClick(Slot par1Slot, int par2, int par3, boolean par4) {
		this.field_74234_w = true;
		ItemStack var6;
		InventoryPlayer var9;

		if (par1Slot != null) {
			int var5;

			if (par1Slot == this.field_74235_v && par4) {
				for (var5 = 0; var5 < this.mc.thePlayer.inventorySlots.getInventory().size(); ++var5) {
					this.mc.playerController.sendSlotPacket((ItemStack)null, var5);
				}
			} else {
				ItemStack var7;

				if (field_74241_p == CreativeTabs.tabInventory.getTabIndex()) {
					if (par1Slot == this.field_74235_v) {
						this.mc.thePlayer.inventory.setItemStack((ItemStack)null);
					} else {
						var5 = SlotCreativeInventory.func_75240_a((SlotCreativeInventory)par1Slot).slotNumber;
						var6 = this.mc.thePlayer.inventorySlots.getSlot(var5).getStack();

						if (par4 && (var6 == null || !(var6.getItem() instanceof ItemArmor))) {
							this.mc.playerController.sendSlotPacket((ItemStack)null, var5);
						} else {
							this.mc.thePlayer.inventorySlots.slotClick(var5, par3, par4, this.mc.thePlayer);
							var7 = this.mc.thePlayer.inventorySlots.getSlot(var5).getStack();
							this.mc.playerController.sendSlotPacket(var7, var5);
						}
					}
				} else if (par1Slot.inventory == inventory) {
					var9 = this.mc.thePlayer.inventory;
					var6 = var9.getItemStack();
					var7 = par1Slot.getStack();

					if (var6 != null && var7 != null && var6.isItemEqual(var7)) {
						if (par3 == 0) {
							if (par4) {
								var6.stackSize = var6.getMaxStackSize();
							} else if (var6.stackSize < var6.getMaxStackSize()) {
								++var6.stackSize;
							}
						} else if (var6.stackSize <= 1) {
							var9.setItemStack((ItemStack)null);
						} else {
							--var6.stackSize;
						}
					} else if (var7 != null && var6 == null) {
						boolean var8 = false;

						if (!var8) {
							var9.setItemStack(ItemStack.copyItemStack(var7));
							var6 = var9.getItemStack();

							if (par4) {
								var6.stackSize = var6.getMaxStackSize();
							}
						}
					} else {
						var9.setItemStack((ItemStack)null);
					}
				} else {
					this.inventorySlots.slotClick(par1Slot.slotNumber, par3, par4, this.mc.thePlayer);
					ItemStack var10 = this.inventorySlots.getSlot(par1Slot.slotNumber).getStack();
					this.mc.playerController.sendSlotPacket(var10, par1Slot.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
				}
			}
		} else {
			var9 = this.mc.thePlayer.inventory;

			if (var9.getItemStack() != null) {
				if (par3 == 0) {
					this.mc.thePlayer.dropPlayerItem(var9.getItemStack());
					this.mc.playerController.func_78752_a(var9.getItemStack());
					var9.setItemStack((ItemStack)null);
				}

				if (par3 == 1) {
					var6 = var9.getItemStack().splitStack(1);
					this.mc.thePlayer.dropPlayerItem(var6);
					this.mc.playerController.func_78752_a(var6);

					if (var9.getItemStack().stackSize == 0) {
						var9.setItemStack((ItemStack)null);
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
			this.controlList.clear();
			Keyboard.enableRepeatEvents(true);
			this.field_74237_t = new GuiTextField(this.fontRenderer, this.guiLeft + 82, this.guiTop + 6, 89, this.fontRenderer.FONT_HEIGHT);
			this.field_74237_t.setMaxStringLength(15);
			this.field_74237_t.setEnableBackgroundDrawing(false);
			this.field_74237_t.func_73790_e(false);
			this.field_74237_t.func_73794_g(16777215);
			int var1 = field_74241_p;
			field_74241_p = -1;
			this.func_74227_b(CreativeTabs.creativeTabArray[var1]);
		} else {
			this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
		}
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		if (field_74241_p != CreativeTabs.tabAllSearch.getTabIndex()) {
			if (Keyboard.isKeyDown(this.mc.gameSettings.keyBindChat.keyCode)) {
				this.func_74227_b(CreativeTabs.tabAllSearch);
			} else {
				super.keyTyped(par1, par2);
			}
		} else {
			if (this.field_74234_w) {
				this.field_74234_w = false;
				this.field_74237_t.setText("");
			}

			if (this.field_74237_t.textboxKeyTyped(par1, par2)) {
				this.func_74228_j();
			} else {
				super.keyTyped(par1, par2);
			}
		}
	}

	private void func_74228_j() {
		ContainerCreative var1 = (ContainerCreative)this.inventorySlots;
		var1.itemList.clear();
		Item[] var2 = Item.itemsList;
		int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			Item var5 = var2[var4];

			if (var5 != null && var5.getCreativeTab() != null) {
				var5.getSubItems(var5.shiftedIndex, (CreativeTabs)null, var1.itemList);
			}
		}

		Iterator var8 = var1.itemList.iterator();
		String var9 = this.field_74237_t.getText().toLowerCase();

		while (var8.hasNext()) {
			ItemStack var10 = (ItemStack)var8.next();
			boolean var11 = false;
			Iterator var6 = var10.getItemNameandInformation().iterator();

			while (true) {
				if (var6.hasNext()) {
					String var7 = (String)var6.next();

					if (!var7.toLowerCase().contains(var9)) {
						continue;
					}

					var11 = true;
				}

				if (!var11) {
					var8.remove();
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
	protected void drawGuiContainerForegroundLayer() {
		CreativeTabs var1 = CreativeTabs.creativeTabArray[field_74241_p];

		if (var1.drawInForegroundOfTab()) {
			this.fontRenderer.drawString(var1.getTranslatedTabLabel(), 8, 6, 4210752);
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
					this.func_74227_b(var9);
					return;
				}
			}
			
			handleSpoutMouse(par1, par2, true); //Spout
		}

		super.mouseClicked(par1, par2, par3);
	}

	/**
	 * returns (if you are not on the inventoryTab) and (the flag isn't set) and( you have more than 1 page of items)
	 */
	private boolean needsScrollBars() {
		return field_74241_p != CreativeTabs.tabInventory.getTabIndex() && CreativeTabs.creativeTabArray[field_74241_p].shouldHidePlayerInventory() && ((ContainerCreative)this.inventorySlots).hasMoreThan1PageOfItemsInList();
	}

	private void func_74227_b(CreativeTabs par1CreativeTabs) {
		int var2 = field_74241_p;
		field_74241_p = par1CreativeTabs.getTabIndex();
		ContainerCreative var3 = (ContainerCreative)this.inventorySlots;
		var3.itemList.clear();
		par1CreativeTabs.displayAllReleventItems(var3.itemList);

		if (par1CreativeTabs == CreativeTabs.tabInventory) {
			Container var4 = this.mc.thePlayer.inventorySlots;

			if (this.field_74236_u == null) {
				this.field_74236_u = var3.inventorySlots;
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
			var3.inventorySlots = this.field_74236_u;
			this.field_74236_u = null;
		}

		if (this.field_74237_t != null) {
			if (par1CreativeTabs == CreativeTabs.tabAllSearch) {
				this.field_74237_t.func_73790_e(true);
				this.field_74237_t.setCanLoseFocus(false);
				this.field_74237_t.setFocused(true);
				this.field_74237_t.setText("");
				this.func_74228_j();
			} else {
				this.field_74237_t.func_73790_e(false);
				this.field_74237_t.setCanLoseFocus(true);
				this.field_74237_t.setFocused(false);
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

			if (this.func_74231_b(var14, par1, par2)) {
				break;
			}
		}
		
		handleSpoutMouse(par1, par2, false); //Spout

		if (this.field_74235_v != null && field_74241_p == CreativeTabs.tabInventory.getTabIndex() && this.func_74188_c(this.field_74235_v.xDisplayPosition, this.field_74235_v.yDisplayPosition, 16, 16, par1, par2)) {
			this.func_74190_a(StringTranslate.getInstance().translateKey("inventory.binSlot"), par1, par2);
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
		int var4 = this.mc.renderEngine.getTexture("/gui/allitems.png");
		CreativeTabs var5 = CreativeTabs.creativeTabArray[field_74241_p];
		int var6 = this.mc.renderEngine.getTexture("/gui/creative_inv/" + var5.getBackgroundImageName());
		CreativeTabs[] var7 = CreativeTabs.creativeTabArray;
		int var8 = var7.length;
		int var9;

		for (var9 = 0; var9 < var8 - 1; ++var9) { //Spout - don't render the last one, we handle that!
			CreativeTabs var10 = var7[var9];
			this.mc.renderEngine.bindTexture(var4);

			if (var10.getTabIndex() != field_74241_p) {
				this.func_74233_a(var10);
			}
		}
		
		renderInInventory(true); //Spout - render ours separately

		this.mc.renderEngine.bindTexture(var6);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		this.field_74237_t.drawTextBox();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int var11 = this.guiLeft + 175;
		var8 = this.guiTop + 18;
		var9 = var8 + 112;
		
		//Spout start
		renderInInventory(false); //Spout - render ours separately
		if(field_74241_p == 12) { //Return, useless code now!
			return;
		}
		//Spout end
		
		this.mc.renderEngine.bindTexture(var4);

		if (var5.shouldHidePlayerInventory()) {
			this.drawTexturedModalRect(var11, var8 + (int)((float)(var9 - var8 - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
		}

		this.func_74233_a(var5);

		if (var5 == CreativeTabs.tabInventory) {
			GuiInventory.func_74223_a(this.mc, this.guiLeft + 43, this.guiTop + 45, 20, (float)(this.guiLeft + 43 - par2), (float)(this.guiTop + 45 - 30 - par3));
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

	protected boolean func_74231_b(CreativeTabs par1CreativeTabs, int par2, int par3) {
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

		if (this.func_74188_c(var5 + 3, var7 + 3, 23, 27, par2, par3)) {
			this.func_74190_a(par1CreativeTabs.getTranslatedTabLabel(), par2, par3);
			return true;
		} else {
			return false;
		}
	}

	protected void func_74233_a(CreativeTabs par1CreativeTabs) {
		boolean var2 = par1CreativeTabs.getTabIndex() == field_74241_p;
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
		ItemStack var10= new ItemStack(par1CreativeTabs.getTabIconItem());
		itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, var10, var7, var8);
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
		return field_74241_p;
	}

	/**
	 * Returns the creative inventory
	 */
	static InventoryBasic getInventory() {
		return inventory;
	}
	
	//Spout start
	public void handleSpoutMouse(int x, int y, boolean isClicked) {
		int var7 = guiTop;
		int var5 = guiLeft - 26;
		boolean inside = x >= var5 && x <= var5 + 28 && y >= var7 && y <= var7 + 32;
		if(inside) {
			func_74190_a("Custom items", x, y);
			if(isClicked) {
			field_74241_p = CreativeTabs.tabSpout.getTabIndex();
			ContainerCreative var3 = (ContainerCreative)this.inventorySlots;
			var3.itemList.clear();
			CreativeTabs.tabSpout.displayAllReleventItems(var3.itemList);
			this.currentScroll = 0.0F;
			var3.scrollTo(0.0F);
			}
		}
	}
	
	public void renderInInventory(boolean beforeBackground) {
		if(!beforeBackground && field_74241_p != 12) {
			return;
		}
		this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/allitems.png"));
		boolean var3 = true;
		int var4 = 7;
		int var7 = this.guiLeft + 28 * (var4 - 2);
		int var8 = this.guiTop;
		byte var9 = 32;

		if (var4 == 5) {
			var7 = this.guiLeft + this.xSize - 28;
		} else if (var4 > 0) {
			var7 += var4;
		}

		if (var3) {
			var8 -= 28;
		} else {
			var8 += this.ySize - 4;
		}
		
		var8 = this.guiTop;
		var7 = this.guiLeft-26;

		GL11.glDisable(GL11.GL_LIGHTING);
		this.drawTexturedModalRect(var7, var8, 56, 0, 28, var9);
		this.zLevel = 100.0F;
		itemRenderer.zLevel = 100.0F;
		var7 += 6;
		var8 += 8 + (var3 ? 1 : -1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		//Spout start - bypass stupidity
		ItemStack var10;
		if(MaterialData.getCustomItems().length == 0) { 
			var10 = new ItemStack(Item.flint);		
		} else {
			CustomItem cItem = MaterialData.getCustomItems()[0];
			var10 = new ItemStack(cItem.getRawId(), 1, cItem.getRawData());
		}
		//Spout end 
		itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, var10, var7, var8);
		itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, var10, var7, var8);
		GL11.glDisable(GL11.GL_LIGHTING);
		itemRenderer.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}
	//Spout end
}
