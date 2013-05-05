package net.minecraft.src;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
// Spout Start
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.ScreenType;
import org.spoutcraft.api.gui.Widget;
import org.spoutcraft.client.MCItemStackComparator;
import org.spoutcraft.client.gui.ScreenUtil;
import org.spoutcraft.client.inventory.InventoryUtil;
import org.spoutcraft.client.inventory.CraftItemStack;
// Spout End

public abstract class GuiContainer extends GuiScreen {

	/** Stacks renderer. Icons, stack size, health, etc... */
	// Spout Start
	protected static RenderItem itemRenderer;
	// Spout End

	/** The X size of the inventory window in pixels. */
	protected int xSize = 176;

	/** The Y size of the inventory window in pixels. */
	protected int ySize = 166;

	/** A list of the players inventory slots. */
	public Container inventorySlots;

	/**
	 * Starting X position for the Gui. Inconsistent use for Gui backgrounds.
	 */
	protected int guiLeft;

	/**
	 * Starting Y position for the Gui. Inconsistent use for Gui backgrounds.
	 */
	protected int guiTop;
	private Slot theSlot;

	/** Used when touchscreen is enabled */
	private Slot clickedSlot = null;

	/** Used when touchscreen is enabled */
	private boolean isRightMouseClick = false;

	/** Used when touchscreen is enabled */
	private ItemStack draggedStack = null;
	private int field_85049_r = 0;
	private int field_85048_s = 0;
	private Slot returningStackDestSlot = null;
	private long returningStackTime = 0L;

	/** Used when touchscreen is enabled */
	private ItemStack returningStack = null;
	private Slot field_92033_y = null;
	private long field_92032_z = 0L;
	protected final Set field_94077_p = new HashSet();
	protected boolean field_94076_q;
	private int field_94071_C = 0;
	private int field_94067_D = 0;
	private boolean field_94068_E = false;
	private int field_94069_F;
	private long field_94070_G = 0L;
	private Slot field_94072_H = null;
	private int field_94073_I = 0;
	private boolean field_94074_J;
	private ItemStack field_94075_K = null;

	// Spout Start
	private Button orderByAlphabet, orderById;
	static {
		itemRenderer = GuiScreen.ourItemRenderer;
	}
	// Spout Start

	public GuiContainer(Container par1Container) {
		this.inventorySlots = par1Container;
		this.field_94068_E = true;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		super.initGui();
		this.mc.thePlayer.openContainer = this.inventorySlots;
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;

		// Spout Start
		if (Spoutcraft.hasPermission("spout.plugin.sortinventory")) {
			orderByAlphabet = new GenericButton("A-Z");
			orderById = new GenericButton("Id");
			orderByAlphabet.setTooltip("Will sort the inventory contents by their name");
			orderById.setTooltip("Will sort the inventory contents by their id");
			ScreenType type = ScreenUtil.getType(this);

			if (type == ScreenType.PLAYER_INVENTORY) {
				if (!this.mc.thePlayer.getActivePotionEffects().isEmpty()) {
					orderByAlphabet.setGeometry((guiLeft+146), (guiTop+65), 27, 13);
					orderById.setGeometry((guiLeft+176), (guiTop+65), 22, 13);
				} else {
					orderByAlphabet.setGeometry((guiLeft+86), (guiTop+65), 27, 13);
					orderById.setGeometry((guiLeft+116), (guiTop+65), 22, 13);
				}
			} else if (type == ScreenType.CHEST_INVENTORY) {
				orderByAlphabet.setGeometry((guiLeft+115), (guiTop+3), 27, 12);
				orderById.setGeometry((guiLeft+145), (guiTop+3), 22, 12);
			}

			IInventory inv = inventorySlots.getIInventory();
			if (inv != null && inventorySlots.isSortableInventory()) {
				getScreen().attachWidgets("Spoutcraft", orderByAlphabet, orderById);
			}
		}
		// Spout End
	}

	@Override
	public void drawWidgets(int x, int y, float z) {
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		super.drawWidgets(x, y, z);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public void buttonClicked(Button btn) {
		if (btn == orderByAlphabet || btn == orderById) {
			try {
				IInventory inv = inventorySlots.getIInventory();
				if (inv != null) {
					if (inv instanceof InventoryPlayer) {
						compactInventory(inv, true);
						sortPlayerInventory(btn == orderByAlphabet);
					} else {
						compactInventory(inv, false);
						sortInventory(inv, (btn == orderByAlphabet));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public int getNumItems(IInventory inventory) {
		int used = 0;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			if (inventory.getStackInSlot(i) != null) {
				used++;
			}
		}
		return used;
	}

	public void compactInventory(IInventory inventory, boolean player) {
		// To keep mp compatibility, fake window clicks
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);
			if (item != null && item.stackSize < item.getMaxStackSize()) {

				// Find a place to put this
				int orig = i;
				// Avoid the hotbar
				if (orig < 9 && player) {
					continue;
				}
				handleMouseClick(null, orig, 0, 0); // Pick up the item

				for (int j = 0; j < inventory.getSizeInventory(); j++) {
					if (j != i) {
						ItemStack other = inventory.getStackInSlot(j);
						if (other != null && other.itemID == item.itemID && other.getItemDamage() == item.getItemDamage()) {
							int slot = j;
							// Avoid the hotbar
							if (slot < 9 && player) {
								continue;
							}
							handleMouseClick(null, slot, 0, 0); // Merge with the existing stack we found

							// Move onto the next item to merge if this one is completely used up
							ItemStack cursor = Minecraft.theMinecraft.thePlayer.inventory.getItemStack();
							if (cursor == null) {
								break;
							}
						}
					}
				}

				// If we didn't merge all of the item, put it back
				ItemStack cursor = Minecraft.theMinecraft.thePlayer.inventory.getItemStack();
				if (cursor != null) {
					handleMouseClick(null, orig, 0, 0);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void sortPlayerInventory(boolean byName) {
		// To keep mp compatibility, fake window clicks
		InventoryPlayer inventory = Minecraft.theMinecraft.thePlayer.inventory;
		for (int itemPass = 0; itemPass < getNumItems(inventory); itemPass++) {
			for (int pass = 0; pass < inventory.mainInventory.length; pass++) {
				ArrayList<ItemStack> items = new ArrayList<ItemStack>();
				for (int i = 0; i < inventory.mainInventory.length; i++) {
					ItemStack item = inventory.mainInventory[i];
					if (item == null || i < 9) {
						items.add(null);
					} else {
						items.add(new PositionedItemStack(item, i));
					}
				}
				Collections.sort(items, new MCItemStackComparator(byName));

				while (true) {
					if (items.get(pass) instanceof PositionedItemStack) {
						PositionedItemStack item = (PositionedItemStack) items.get(pass);
						// Left click pick up item
						int origSlot = item.position;
						if (origSlot < 9) {
							break;
						}
						int newSlot = pass;
						if (origSlot != newSlot) {
							// Left click pick up item
							handleMouseClick(null, origSlot, 0, 0);

							// Left click place item down
							handleMouseClick(null, newSlot, 0, 0);

							ItemStack cursor = Minecraft.theMinecraft.thePlayer.inventory.getItemStack();
							if (cursor != null) {
								handleMouseClick(null, origSlot, 0, 0);
							}
						}
						break;
					}
					pass++;
					if (pass >= inventory.mainInventory.length) {
						break;
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void sortInventory(IInventory inventory, boolean byName) {
		// To keep mp compatibility, fake window clicks
		for (int itemPass = 0; itemPass < getNumItems(inventory); itemPass++) {
			for (int pass = 0; pass < inventory.getSizeInventory(); pass++) {
				ArrayList<ItemStack> items = new ArrayList<ItemStack>();
				for (int i = 0; i < inventory.getSizeInventory(); i++) {
					ItemStack item = inventory.getStackInSlot(i);
					if (item == null) {
						items.add(null);
					} else {
						items.add(new PositionedItemStack(item, i));
					}
				}
				Collections.sort(items, new MCItemStackComparator(byName));

				while (true) {
					if (items.get(pass) instanceof PositionedItemStack) {
						PositionedItemStack item = (PositionedItemStack) items.get(pass);
						//Left click pick up item
						Slot origSlot = getSlotFromPosition(item.position);
						Slot newSlot = getSlotFromPosition(pass);
						if (newSlot != origSlot) {
							handleMouseClick(origSlot, 0, 0, 0);

							// Left click place item down
							handleMouseClick(newSlot, 0, 0, 0);

							ItemStack cursor = Minecraft.theMinecraft.thePlayer.inventory.getItemStack();
							if (cursor != null) {
								handleMouseClick(origSlot, 0, 0, 0);
							}
						}
						break;
					}
					pass++;
					if (pass >= inventory.getSizeInventory()) {
						break;
					}
				}
			}
		}
	}

	public Slot getSlotFromPosition(int pos) {
		return InventoryUtil.getSlotFromPosition(pos, inventorySlots);
	}
	// Spout End

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		int var4 = this.guiLeft;
		int var5 = this.guiTop;
		this.drawGuiContainerBackgroundLayer(par3, par1, par2);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		super.drawScreen(par1, par2, par3);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)var4, (float)var5, 0.0F);
		this.drawGuiContainerForegroundLayer(par1, par2);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		this.theSlot = null;
		short var6 = 240;
		short var7 = 240;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6 / 1.0F, (float)var7 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int var9;

		for (int var13 = 0; var13 < this.inventorySlots.inventorySlots.size(); ++var13) {
			Slot var14 = (Slot)this.inventorySlots.inventorySlots.get(var13);
			this.drawSlotInventory(var14);

			if (this.isMouseOverSlot(var14, par1, par2)) {
				this.theSlot = var14;
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				int var8 = var14.xDisplayPosition;
				var9 = var14.yDisplayPosition;
				this.drawGradientRect(var8, var9, var8 + 16, var9 + 16, -2130706433, -2130706433);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}

		InventoryPlayer var15 = this.mc.thePlayer.inventory;
		ItemStack var16 = this.draggedStack == null ? var15.getItemStack() : this.draggedStack;

		if (var16 != null) {
			byte var18 = 8;
			var9 = this.draggedStack == null ? 8 : 16;
			String var10 = null;

			if (this.draggedStack != null && this.isRightMouseClick) {
				var16 = var16.copy();
				var16.stackSize = MathHelper.ceiling_float_int((float)var16.stackSize / 2.0F);
			} else if (this.field_94076_q && this.field_94077_p.size() > 1) {
				var16 = var16.copy();
				var16.stackSize = this.field_94069_F;

				if (var16.stackSize == 0) {
					var10 = "" + EnumChatFormatting.YELLOW + "0";
				}
			}

			this.drawItemStack(var16, par1 - var4 - var18, par2 - var5 - var9, var10);
		}

		if (this.returningStack != null) {
			float var17 = (float)(Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;

			if (var17 >= 1.0F) {
				var17 = 1.0F;
				this.returningStack = null;
			}

			var9 = this.returningStackDestSlot.xDisplayPosition - this.field_85049_r;
			int var20 = this.returningStackDestSlot.yDisplayPosition - this.field_85048_s;
			int var11 = this.field_85049_r + (int)((float)var9 * var17);
			int var12 = this.field_85048_s + (int)((float)var20 * var17);
			this.drawItemStack(this.returningStack, var11, var12, (String)null);
		}

		if (var15.getItemStack() == null && this.theSlot != null && this.theSlot.getHasStack()) {
			ItemStack var19 = this.theSlot.getStack();
			this.drawItemStackTooltip(var19, par1 - var4 + 8, par2 - var5 + 8);
		}

		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}

	private void drawItemStack(ItemStack par1ItemStack, int par2, int par3, String par4Str) {
		GL11.glTranslatef(0.0F, 0.0F, 32.0F);
		this.zLevel = 200.0F;
		itemRenderer.zLevel = 200.0F;
		itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, par1ItemStack, par2, par3);
		itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, par1ItemStack, par2, par3 - (this.draggedStack == null ? 0 : 8), par4Str);
		this.zLevel = 0.0F;
		itemRenderer.zLevel = 0.0F;
	}

	protected void drawItemStackTooltip(ItemStack par1ItemStack, int par2, int par3) {
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		// Spout Start
		List var4 = Arrays.asList(Spoutcraft.getMaterialManager().getToolTip(new CraftItemStack(par1ItemStack)).split("\n"));
		// Spout End

		if (!var4.isEmpty()) {
			int var5 = 0;
			int var7;
			int var6;

			for (var6 = 0; var6 < var4.size(); ++var6) {
				var7 = this.fontRenderer.getStringWidth((String)var4.get(var6));

				if (var7 > var5) {
					var5 = var7;
				}
			}

			var6 = par2 + 12;
			var7 = par3 - 12;
			int var9 = 8;

			if (var4.size() > 1) {
				var9 += 2 + (var4.size() - 1) * 10;
			}

			if (this.guiTop + var7 + var9 + 6 > this.height) {
				var7 = this.height - var9 - this.guiTop - 6;
			}

			this.zLevel = 300.0F;
			itemRenderer.zLevel = 300.0F;
			int var10 = -267386864;
			this.drawGradientRect(var6 - 3, var7 - 4, var6 + var5 + 3, var7 - 3, var10, var10);
			this.drawGradientRect(var6 - 3, var7 + var9 + 3, var6 + var5 + 3, var7 + var9 + 4, var10, var10);
			this.drawGradientRect(var6 - 3, var7 - 3, var6 + var5 + 3, var7 + var9 + 3, var10, var10);
			this.drawGradientRect(var6 - 4, var7 - 3, var6 - 3, var7 + var9 + 3, var10, var10);
			this.drawGradientRect(var6 + var5 + 3, var7 - 3, var6 + var5 + 4, var7 + var9 + 3, var10, var10);
			int var11 = 1347420415;
			int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
				this.drawGradientRect(var6 - 3, var7 - 3 + 1, var6 - 3 + 1, var7 + var9 + 3 - 1, var11, var12);
				this.drawGradientRect(var6 + var5 + 2, var7 - 3 + 1, var6 + var5 + 3, var7 + var9 + 3 - 1, var11, var12);
				this.drawGradientRect(var6 - 3, var7 - 3, var6 + var5 + 3, var7 - 3 + 1, var11, var11);
				this.drawGradientRect(var6 - 3, var7 + var9 + 2, var6 + var5 + 3, var7 + var9 + 3, var12, var12);

				for (int var13 = 0; var13 < var4.size(); ++var13) {
					String var14 = (String)var4.get(var13);

					if (var13 == 0) {
						var14 = "\u00a7" + Integer.toHexString(par1ItemStack.getRarity().rarityColor) + var14;
					} else {
						var14 = EnumChatFormatting.GRAY + var14;
					}

					this.fontRenderer.drawStringWithShadow(var14, var6, var7, -1);

					if (var13 == 0) {
						var7 += 2;
					}

					var7 += 10;
				}

				this.zLevel = 0.0F;
				itemRenderer.zLevel = 0.0F;
		}
	}

	/**
	 * Draws the text when mouse is over creative inventory tab. Params: current creative tab to be checked, current mouse
	 * x position, current mouse y position.
	 */
	protected void drawCreativeTabHoveringText(String par1Str, int par2, int par3) {
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		int var4 = this.fontRenderer.getStringWidth(par1Str);
		int var5 = par2 + 12;
		int var6 = par3 - 12;
		byte var8 = 8;
		this.zLevel = 300.0F;
		itemRenderer.zLevel = 300.0F;
		int var9 = -267386864;
		this.drawGradientRect(var5 - 3, var6 - 4, var5 + var4 + 3, var6 - 3, var9, var9);
		this.drawGradientRect(var5 - 3, var6 + var8 + 3, var5 + var4 + 3, var6 + var8 + 4, var9, var9);
		this.drawGradientRect(var5 - 3, var6 - 3, var5 + var4 + 3, var6 + var8 + 3, var9, var9);
		this.drawGradientRect(var5 - 4, var6 - 3, var5 - 3, var6 + var8 + 3, var9, var9);
		this.drawGradientRect(var5 + var4 + 3, var6 - 3, var5 + var4 + 4, var6 + var8 + 3, var9, var9);
		int var10 = 1347420415;
		int var11 = (var10 & 16711422) >> 1 | var10 & -16777216;
				this.drawGradientRect(var5 - 3, var6 - 3 + 1, var5 - 3 + 1, var6 + var8 + 3 - 1, var10, var11);
				this.drawGradientRect(var5 + var4 + 2, var6 - 3 + 1, var5 + var4 + 3, var6 + var8 + 3 - 1, var10, var11);
				this.drawGradientRect(var5 - 3, var6 - 3, var5 + var4 + 3, var6 - 3 + 1, var10, var10);
				this.drawGradientRect(var5 - 3, var6 + var8 + 2, var5 + var4 + 3, var6 + var8 + 3, var11, var11);
				this.fontRenderer.drawStringWithShadow(par1Str, var5, var6, -1);
				this.zLevel = 0.0F;
				itemRenderer.zLevel = 0.0F;
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				RenderHelper.enableStandardItemLighting();
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	protected abstract void drawGuiContainerBackgroundLayer(float var1, int var2, int var3);

	/**
	 * Draws an inventory slot
	 */
	private void drawSlotInventory(Slot par1Slot) {
		int var2 = par1Slot.xDisplayPosition;
		int var3 = par1Slot.yDisplayPosition;
		ItemStack var4 = par1Slot.getStack();
		boolean var5 = false;
		boolean var6 = par1Slot == this.clickedSlot && this.draggedStack != null && !this.isRightMouseClick;
		ItemStack var7 = this.mc.thePlayer.inventory.getItemStack();
		String var8 = null;

		if (par1Slot == this.clickedSlot && this.draggedStack != null && this.isRightMouseClick && var4 != null) {
			var4 = var4.copy();
			var4.stackSize /= 2;
		} else if (this.field_94076_q && this.field_94077_p.contains(par1Slot) && var7 != null) {
			if (this.field_94077_p.size() == 1) {
				return;
			}

			if (Container.func_94527_a(par1Slot, var7, true) && this.inventorySlots.func_94531_b(par1Slot)) {
				var4 = var7.copy();
				var5 = true;
				Container.func_94525_a(this.field_94077_p, this.field_94071_C, var4, par1Slot.getStack() == null ? 0 : par1Slot.getStack().stackSize);

				if (var4.stackSize > var4.getMaxStackSize()) {
					var8 = EnumChatFormatting.YELLOW + "" + var4.getMaxStackSize();
					var4.stackSize = var4.getMaxStackSize();
				}

				if (var4.stackSize > par1Slot.getSlotStackLimit()) {
					var8 = EnumChatFormatting.YELLOW + "" + par1Slot.getSlotStackLimit();
					var4.stackSize = par1Slot.getSlotStackLimit();
				}
			} else {
				this.field_94077_p.remove(par1Slot);
				this.func_94066_g();
			}
		}

		this.zLevel = 100.0F;
		itemRenderer.zLevel = 100.0F;

		if (var4 == null) {
			Icon var9 = par1Slot.getBackgroundIconIndex();

			if (var9 != null) {
				GL11.glDisable(GL11.GL_LIGHTING);
				this.mc.renderEngine.bindTexture("/gui/items.png");
				this.drawTexturedModelRectFromIcon(var2, var3, var9, 16, 16);
				GL11.glEnable(GL11.GL_LIGHTING);
				var6 = true;
			}
		}

		if (!var6) {
			if (var5) {
				drawRect(var2, var3, var2 + 16, var3 + 16, -2130706433);
			}

			GL11.glEnable(GL11.GL_DEPTH_TEST);
			itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, var4, var2, var3);
			itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, var4, var2, var3, var8);
		}

		itemRenderer.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}

	private void func_94066_g() {
		ItemStack var1 = this.mc.thePlayer.inventory.getItemStack();

		if (var1 != null && this.field_94076_q) {
			this.field_94069_F = var1.stackSize;
			ItemStack var4;
			int var5;

			for (Iterator var2 = this.field_94077_p.iterator(); var2.hasNext(); this.field_94069_F -= var4.stackSize - var5) {
				Slot var3 = (Slot)var2.next();
				var4 = var1.copy();
				var5 = var3.getStack() == null ? 0 : var3.getStack().stackSize;
				Container.func_94525_a(this.field_94077_p, this.field_94071_C, var4, var5);

				if (var4.stackSize > var4.getMaxStackSize()) {
					var4.stackSize = var4.getMaxStackSize();
				}

				if (var4.stackSize > var3.getSlotStackLimit()) {
					var4.stackSize = var3.getSlotStackLimit();
				}
			}
		}
	}

	/**
	 * Returns the slot at the given coordinates or null if there is none.
	 */
	private Slot getSlotAtPosition(int par1, int par2) {
		for (int var3 = 0; var3 < this.inventorySlots.inventorySlots.size(); ++var3) {
			Slot var4 = (Slot)this.inventorySlots.inventorySlots.get(var3);

			if (this.isMouseOverSlot(var4, par1, par2)) {
				return var4;
			}
		}

		return null;
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		boolean var4 = par3 == this.mc.gameSettings.keyBindPickBlock.keyCode + 100;

		if (par3 == 0 || par3 == 1 || var4) {
			Slot var5 = this.getSlotAtPosition(par1, par2);
			int var6 = this.guiLeft;
			int var7 = this.guiTop;
			boolean var8 = par1 < var6 || par2 < var7 || par1 >= var6 + this.xSize || par2 >= var7 + this.ySize;
			int var9 = -1;

			if (var5 != null) {
				var9 = var5.slotNumber;
			}

			if (var8) {
				// Spout Start
				boolean isSpoutSlot = false;
				for (Widget w : getScreen().getAttachedWidgets(true)) {
					if (isInBoundingRect(w, par1, par2)) {
						if (w instanceof org.spoutcraft.api.gui.Slot) {
							isSpoutSlot = true;
							break;
						}
					}
				}
				if (!isSpoutSlot) {
					var9 = -999;
				}
				// Spout End
			}

			if (this.mc.gameSettings.touchscreen && var8 && this.mc.thePlayer.inventory.getItemStack() == null) {
				this.mc.displayGuiScreen((GuiScreen)null);
				return;
			}

			if (var9 != -1) {
				if (this.mc.gameSettings.touchscreen) {
					if (var5 != null && var5.getHasStack()) {
						this.clickedSlot = var5;
						this.draggedStack = null;
						this.isRightMouseClick = par3 == 1;
					} else {
						this.clickedSlot = null;
					}
				} else if (var4) {
					this.handleMouseClick(var5, var9, par3, 3);
				} else {
					boolean var10 = var9 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
					this.handleMouseClick(var5, var9, par3, var10 ? 1 : 0);
				}
			}
		}
	}

	protected void func_85041_a(int par1, int par2, int par3, long par4) {
		if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
			if (par3 == 0 || par3 == 1) {
				Slot var6 = this.getSlotAtPosition(par1, par2);

				if (this.draggedStack == null) {
					if (var6 != this.clickedSlot) {
						this.draggedStack = this.clickedSlot.getStack().copy();
					}
				} else if (this.draggedStack.stackSize > 1 && var6 != null && this.func_92031_b(var6)) {
					long var7 = Minecraft.getSystemTime();

					if (this.field_92033_y == var6) {
						if (var7 - this.field_92032_z > 500L) {
							this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
							this.handleMouseClick(var6, var6.slotNumber, 1, 0);
							this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
							this.field_92032_z = var7 + 750L;
							--this.draggedStack.stackSize;
						}
					} else {
						this.field_92033_y = var6;
						this.field_92032_z = var7;
					}
				}
			}
		}
	}

	/**
	 * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
	 * mouseMove, which==0 or which==1 is mouseUp
	 */
	protected void mouseMovedOrUp(int par1, int par2, int par3) {
		if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
			if (par3 == 0 || par3 == 1) {
				Slot var4 = this.getSlotAtPosition(par1, par2);
				int var5 = this.guiLeft;
				int var6 = this.guiTop;
				boolean var7 = par1 < var5 || par2 < var6 || par1 >= var5 + this.xSize || par2 >= var6 + this.ySize;
				int var8 = -1;

				if (var4 != null) {
					var8 = var4.slotNumber;
				}

				if (var7) {
					var8 = -999;
				}

				if (this.draggedStack == null && var4 != this.clickedSlot) {
					this.draggedStack = this.clickedSlot.getStack();
				}

				boolean var9 = this.func_92031_b(var4);

				if (var8 != -1 && this.draggedStack != null && var9) {
					this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, par3, 0);
					this.handleMouseClick(var4, var8, 0, 0);

					if (this.mc.thePlayer.inventory.getItemStack() != null) {
						this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, par3, 0);
						this.field_85049_r = par1 - var5;
						this.field_85048_s = par2 - var6;
						this.returningStackDestSlot = this.clickedSlot;
						this.returningStack = this.draggedStack;
						this.returningStackTime = Minecraft.getSystemTime();
					} else {
						this.returningStack = null;
					}
				} else if (this.draggedStack != null) {
					this.field_85049_r = par1 - var5;
					this.field_85048_s = par2 - var6;
					this.returningStackDestSlot = this.clickedSlot;
					this.returningStack = this.draggedStack;
					this.returningStackTime = Minecraft.getSystemTime();
				}

				this.draggedStack = null;
				this.clickedSlot = null;
			}
		}
	}

	private boolean func_92031_b(Slot par1Slot) {
		boolean var2 = par1Slot == null || !par1Slot.getHasStack();

		if (par1Slot != null && par1Slot.getHasStack() && this.draggedStack != null && ItemStack.areItemStackTagsEqual(par1Slot.getStack(), this.draggedStack)) {
			var2 |= par1Slot.getStack().stackSize + this.draggedStack.stackSize <= this.draggedStack.getMaxStackSize();
		}

		return var2;
	}

	/**
	 * Returns if the passed mouse position is over the specified slot.
	 */
	private boolean isMouseOverSlot(Slot par1Slot, int par2, int par3) {
		return this.isPointInRegion(par1Slot.xDisplayPosition, par1Slot.yDisplayPosition, 16, 16, par2, par3);
	}

	/**
	 * Args: left, top, width, height, pointX, pointY. Note: left, top are local to Gui, pointX, pointY are local to screen
	 */
	protected boolean isPointInRegion(int par1, int par2, int par3, int par4, int par5, int par6) {
		int var7 = this.guiLeft;
		int var8 = this.guiTop;
		par5 -= var7;
		par6 -= var8;
		return par5 >= par1 - 1 && par5 < par1 + par3 + 1 && par6 >= par2 - 1 && par6 < par2 + par4 + 1;
	}

	protected void handleMouseClick(Slot par1Slot, int par2, int par3, int par4) {
		if (par1Slot != null) {
			par2 = par1Slot.slotNumber;
		}

		this.mc.playerController.windowClick(this.inventorySlots.windowId, par2, par3, par4, this.mc.thePlayer);
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		if (par2 == 1 || par2 == this.mc.gameSettings.keyBindInventory.keyCode) {
			this.mc.thePlayer.closeScreen();
		}

		this.checkHotbarKeys(par2);

		if (par2 == this.mc.gameSettings.keyBindPickBlock.keyCode && this.theSlot != null && this.theSlot.getHasStack()) {
			this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, this.ySize, 3);
		}
	}

	/**
	 * This function is what controls the hotbar shortcut check when you press a number key when hovering a stack.
	 */
	protected boolean checkHotbarKeys(int par1) {
		if (this.mc.thePlayer.inventory.getItemStack() == null && this.theSlot != null) {
			for (int var2 = 0; var2 < 9; ++var2) {
				if (par1 == 2 + var2) {
					this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, var2, 2);
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed() {
		if (this.mc.thePlayer != null) {
			this.inventorySlots.onCraftGuiClosed(this.mc.thePlayer);
		}
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		super.updateScreen();

		if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead) {
			this.mc.thePlayer.closeScreen();
		}
	}
}

// Spout Start
class PositionedItemStack extends ItemStack {
	final int position;
	public PositionedItemStack(ItemStack item, int position) {
		super(item.itemID, item.stackSize, item.getItemDamage());
		this.position = position;
	}

}
// Spout End
