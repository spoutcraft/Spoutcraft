package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiContainerCreative extends GuiContainer {
	private static InventoryBasic inventory = new InventoryBasic("tmp", 72);
	private float currentScroll;
	private boolean isScrolling;
	private boolean wasClicking;

	public GuiContainerCreative(EntityPlayer entityplayer) {
		super(new ContainerCreative(entityplayer));
		currentScroll = 0.0F;
		isScrolling = false;
		entityplayer.craftingInventory = inventorySlots;
		allowUserInput = true;
		entityplayer.addStat(AchievementList.openInventory, 1);
		ySize = 208;
	}

	public void updateScreen() {
		if (!mc.playerController.isInCreativeMode()) {
			mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
		}
	}

	protected void handleMouseClick(Slot slot, int i, int j, boolean flag) {
		if (slot != null) {
			if (slot.inventory == inventory) {
				InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
				ItemStack itemstack1 = inventoryplayer.getItemStack();
				ItemStack itemstack4 = slot.getStack();
				if (itemstack1 != null && itemstack4 != null && itemstack1.itemID == itemstack4.itemID) {
					if (j == 0) {
						if (flag) {
							itemstack1.stackSize = itemstack1.getMaxStackSize();
						}
						else if (itemstack1.stackSize < itemstack1.getMaxStackSize()) {
							itemstack1.stackSize++;
						}
					}
					else if (itemstack1.stackSize <= 1) {
						inventoryplayer.setItemStack(null);
					}
					else {
						itemstack1.stackSize--;
					}
				}
				else if (itemstack1 != null) {
					inventoryplayer.setItemStack(null);
				}
				else if (itemstack4 == null) {
					inventoryplayer.setItemStack(null);
				}
				else if (itemstack1 == null || itemstack1.itemID != itemstack4.itemID) {
					inventoryplayer.setItemStack(ItemStack.copyItemStack(itemstack4));
					ItemStack itemstack2 = inventoryplayer.getItemStack();
					if (flag) {
						itemstack2.stackSize = itemstack2.getMaxStackSize();
					}
				}
			}
			else {
				inventorySlots.slotClick(slot.slotNumber, j, flag, mc.thePlayer);
				ItemStack itemstack = inventorySlots.getSlot(slot.slotNumber).getStack();
				mc.playerController.func_35637_a(itemstack, (slot.slotNumber - inventorySlots.inventorySlots.size()) + 9 + 36);
			}
		}
		else {
			InventoryPlayer inventoryplayer1 = mc.thePlayer.inventory;
			if (inventoryplayer1.getItemStack() != null) {
				if (j == 0) {
					mc.thePlayer.dropPlayerItem(inventoryplayer1.getItemStack());
					mc.playerController.func_35639_a(inventoryplayer1.getItemStack());
					inventoryplayer1.setItemStack(null);
				}
				if (j == 1) {
					ItemStack itemstack3 = inventoryplayer1.getItemStack().splitStack(1);
					mc.thePlayer.dropPlayerItem(itemstack3);
					mc.playerController.func_35639_a(itemstack3);
					if (inventoryplayer1.getItemStack().stackSize == 0) {
						inventoryplayer1.setItemStack(null);
					}
				}
			}
		}
	}

	public void initGui() {
		if (!mc.playerController.isInCreativeMode()) {
			mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
		}
		else {
			super.initGui();
			controlList.clear();
		}
	}

	protected void drawGuiContainerForegroundLayer() {
		fontRenderer.drawString("Item selection", 8, 6, 0x404040);
	}

	public void handleMouseInput() {
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();
		if (i != 0) {
			int j = (((ContainerCreative)inventorySlots).itemList.size() / 8 - 8) + 1;
			if (i > 0) {
				i = 1;
			}
			if (i < 0) {
				i = -1;
			}
			currentScroll -= (double)i / (double)j;
			if (currentScroll < 0.0F) {
				currentScroll = 0.0F;
			}
			if (currentScroll > 1.0F) {
				currentScroll = 1.0F;
			}
			((ContainerCreative)inventorySlots).scrollTo(currentScroll);
		}
	}

	public void drawScreen(int i, int j, float f) {
		boolean flag = Mouse.isButtonDown(0);
		int k = guiLeft;
		int l = guiTop;
		int i1 = k + 155;
		int j1 = l + 17;
		int k1 = i1 + 14;
		int l1 = j1 + 160 + 2;
		if (!wasClicking && flag && i >= i1 && j >= j1 && i < k1 && j < l1) {
			isScrolling = true;
		}
		if (!flag) {
			isScrolling = false;
		}
		wasClicking = flag;
		if (isScrolling) {
			currentScroll = (float)(j - (j1 + 8)) / ((float)(l1 - j1) - 16F);
			if (currentScroll < 0.0F) {
				currentScroll = 0.0F;
			}
			if (currentScroll > 1.0F) {
				currentScroll = 1.0F;
			}
			((ContainerCreative)inventorySlots).scrollTo(currentScroll);
		}
		super.drawScreen(i, j, f);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(2896 /*GL_LIGHTING*/);
	}

	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int k = mc.renderEngine.getTexture("/gui/allitems.png");
		mc.renderEngine.bindTexture(k);
		int l = guiLeft;
		int i1 = guiTop;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
		int j1 = l + 155;
		int k1 = i1 + 17;
		int l1 = k1 + 160 + 2;
		drawTexturedModalRect(l + 154, i1 + 17 + (int)((float)(l1 - k1 - 17) * currentScroll), 0, 208, 16, 16);
	}

	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == 0) {
			mc.displayGuiScreen(new GuiAchievements(mc.statFileWriter));
		}
		if (guibutton.id == 1) {
			mc.displayGuiScreen(new GuiStats(this, mc.statFileWriter));
		}
	}

	static InventoryBasic getInventory() {
		return inventory;
	}
}
