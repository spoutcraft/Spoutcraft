package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public abstract class GuiSlot
{
	private final Minecraft mc;

	/**
	 * The width of the GuiScreen. Affects the container rendering, but not the overlays.
	 */
	private final int width;

	/**
	 * The height of the GuiScreen. Affects the container rendering, but not the overlays or the scrolling.
	 */
	private final int height;

	/** The top of the slot container. Affects the overlays and scrolling. */
	protected final int top;

	/** The bottom of the slot container. Affects the overlays and scrolling. */
	protected final int bottom;
	private final int right;
	private final int left = 0;

	/** The height of a slot. */
	protected final int slotHeight;

	/** button id of the button used to scroll up */
	private int scrollUpButtonID;

	/** the buttonID of the button used to scroll down */
	private int scrollDownButtonID;
	protected int field_35409_k;
	protected int field_35408_l;

	/** where the mouse was in the window when you first clicked to scroll */
	private float initialClickY;

	/**
	 * what to multiply the amount you moved your mouse by(used for slowing down scrolling when over the items and no on
	 * scroll bar)
	 */
	private float scrollMultiplier;

	/** how far down this slot has been scrolled */
	public float amountScrolled; //Spout private -> public

	/** the element in the list that was selected */
	private int selectedElement;

	/** the time when this button was last clicked. */
	private long lastClicked;
	private boolean field_25123_p;
	private boolean field_27262_q;
	private int field_27261_r;

	public GuiSlot(Minecraft par1Minecraft, int par2, int par3, int par4, int par5, int par6)
	{
		initialClickY = -2F;
		selectedElement = -1;
		lastClicked = 0L;
		field_25123_p = true;
		mc = par1Minecraft;
		width = par2;
		height = par3;
		top = par4;
		bottom = par5;
		slotHeight = par6;
		right = par2;
	}

	public void func_27258_a(boolean par1)
	{
		field_25123_p = par1;
	}

	protected void func_27259_a(boolean par1, int par2)
	{
		field_27262_q = par1;
		field_27261_r = par2;

		if (!par1)
		{
			field_27261_r = 0;
		}
	}

	/**
	 * Gets the size of the current slot list.
	 */
	 protected abstract int getSize();

	/**
	 * the element in the slot that was clicked, boolean for wether it was double clicked or not
	 */
	 protected abstract void elementClicked(int i, boolean flag);

	 /**
	  * returns true if the element passed in is currently selected
	  */
	 protected abstract boolean isSelected(int i);

	 /**
	  * return the height of the content being scrolled
	  */
	 protected int getContentHeight()
	 {
		 return getSize() * slotHeight + field_27261_r;
	 }

	 protected abstract void drawBackground();

	 protected abstract void drawSlot(int i, int j, int k, int l, Tessellator tessellator);

	 protected void func_27260_a(int i, int j, Tessellator tessellator)
	 {
	 }

	 protected void func_27255_a(int i, int j)
	 {
	 }

	 protected void func_27257_b(int i, int j)
	 {
	 }

	 public int func_27256_c(int par1, int par2)
	 {
		 int i = width / 2 - 110;
		 int j = width / 2 + 110;
		 int k = ((par2 - top - field_27261_r) + (int)amountScrolled) - 4;
		 int l = k / slotHeight;

		 if (par1 >= i && par1 <= j && l >= 0 && k >= 0 && l < getSize())
		 {
			 return l;
		 }
		 else
		 {
			 return -1;
		 }
	 }

	 /**
	  * Registers the IDs that can be used for the scrollbar's buttons.
	  */
	  public void registerScrollButtons(List par1List, int par2, int par3)
	  {
		  scrollUpButtonID = par2;
		  scrollDownButtonID = par3;
	  }

	  /**
	   * stop the thing from scrolling out of bounds
	   */
	  private void bindAmountScrolled()
	  {
		  int i = getContentHeight() - (bottom - top - 4);

		  if (i < 0)
		  {
			  i /= 2;
		  }

		  if (amountScrolled < 0.0F)
		  {
			  amountScrolled = 0.0F;
		  }

		  if (amountScrolled > (float)i)
		  {
			  amountScrolled = i;
		  }
	  }

	  public void actionPerformed(GuiButton par1GuiButton)
	  {
		  if (!par1GuiButton.enabled)
		  {
			  return;
		  }

		  if (par1GuiButton.id == scrollUpButtonID)
		  {
			  amountScrolled -= (slotHeight * 2) / 3;
			  initialClickY = -2F;
			  bindAmountScrolled();
		  }
		  else if (par1GuiButton.id == scrollDownButtonID)
		  {
			  amountScrolled += (slotHeight * 2) / 3;
			  initialClickY = -2F;
			  bindAmountScrolled();
		  }
	  }

	  /**
	   * draws the slot to the screen, pass in mouse's current x and y and partial ticks
	   */
	  public void drawScreen(int par1, int par2, float par3)
	  {
		  field_35409_k = par1;
		  field_35408_l = par2;
		  drawBackground();
		  int i = getSize();
		  int j = width / 2 + 124;
		  int k = j + 6;

		  if (Mouse.isButtonDown(0))
		  {
			  if (initialClickY == -1F)
			  {
				  boolean flag = true;

				  if (par2 >= top && par2 <= bottom)
				  {
					  int i1 = width / 2 - 110;
					  int j1 = width / 2 + 110;
					  int l1 = ((par2 - top - field_27261_r) + (int)amountScrolled) - 4;
					  int j2 = l1 / slotHeight;

					  if (par1 >= i1 && par1 <= j1 && j2 >= 0 && l1 >= 0 && j2 < i)
					  {
						  boolean flag1 = j2 == selectedElement && System.currentTimeMillis() - lastClicked < 250L;
						  elementClicked(j2, flag1);
						  selectedElement = j2;
						  lastClicked = System.currentTimeMillis();
					  }
					  else if (par1 >= i1 && par1 <= j1 && l1 < 0)
					  {
						  func_27255_a(par1 - i1, ((par2 - top) + (int)amountScrolled) - 4);
						  flag = false;
					  }

					  if (par1 >= j && par1 <= k)
					  {
						  scrollMultiplier = -1F;
						  int l2 = getContentHeight() - (bottom - top - 4);

						  if (l2 < 1)
						  {
							  l2 = 1;
						  }

						  int k3 = (int)((float)((bottom - top) * (bottom - top)) / (float)getContentHeight());

						  if (k3 < 32)
						  {
							  k3 = 32;
						  }

						  if (k3 > bottom - top - 8)
						  {
							  k3 = bottom - top - 8;
						  }

						  scrollMultiplier /= (float)(bottom - top - k3) / (float)l2;
					  }
					  else
					  {
						  scrollMultiplier = 1.0F;
					  }

					  if (flag)
					  {
						  initialClickY = par2;
					  }
					  else
					  {
						  initialClickY = -2F;
					  }
				  }
				  else
				  {
					  initialClickY = -2F;
				  }
			  }
			  else if (initialClickY >= 0.0F)
			  {
				  amountScrolled -= ((float)par2 - initialClickY) * scrollMultiplier;
				  initialClickY = par2;
			  }
		  }
		  else
		  {
			  do
			  {
				  if (!Mouse.next())
				  {
					  break;
				  }

				  int l = Mouse.getEventDWheel();

				  if (l != 0)
				  {
					  if (l > 0)
					  {
						  l = -1;
					  }
					  else if (l < 0)
					  {
						  l = 1;
					  }

					  amountScrolled += (l * slotHeight) / 2;
				  }
			  }
			  while (true);

			  initialClickY = -1F;
		  }

		  bindAmountScrolled();
		  GL11.glDisable(GL11.GL_LIGHTING);
		  GL11.glDisable(GL11.GL_FOG);
		  Tessellator tessellator = Tessellator.instance;
		  GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/background.png"));
		  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		  float f = 32F;
		  tessellator.startDrawingQuads();
		  tessellator.setColorOpaque_I(0x202020);
		  tessellator.addVertexWithUV(left, bottom, 0.0D, (float)left / f, (float)(bottom + (int)amountScrolled) / f);
		  tessellator.addVertexWithUV(right, bottom, 0.0D, (float)right / f, (float)(bottom + (int)amountScrolled) / f);
		  tessellator.addVertexWithUV(right, top, 0.0D, (float)right / f, (float)(top + (int)amountScrolled) / f);
		  tessellator.addVertexWithUV(left, top, 0.0D, (float)left / f, (float)(top + (int)amountScrolled) / f);
		  tessellator.draw();
		  int k1 = width / 2 - 92 - 16;
		  int i2 = (top + 4) - (int)amountScrolled;

		  if (field_27262_q)
		  {
			  func_27260_a(k1, i2, tessellator);
		  }

		  for (int k2 = 0; k2 < i; k2++)
		  {
			  int i3 = i2 + k2 * slotHeight + field_27261_r;
			  int l3 = slotHeight - 4;

			  if (i3 > bottom || i3 + l3 < top)
			  {
				  continue;
			  }

			  if (field_25123_p && isSelected(k2))
			  {
				  int j4 = width / 2 - 110;
				  int l4 = width / 2 + 110;
				  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				  GL11.glDisable(GL11.GL_TEXTURE_2D);
				  tessellator.startDrawingQuads();
				  tessellator.setColorOpaque_I(0x808080);
				  tessellator.addVertexWithUV(j4, i3 + l3 + 2, 0.0D, 0.0D, 1.0D);
				  tessellator.addVertexWithUV(l4, i3 + l3 + 2, 0.0D, 1.0D, 1.0D);
				  tessellator.addVertexWithUV(l4, i3 - 2, 0.0D, 1.0D, 0.0D);
				  tessellator.addVertexWithUV(j4, i3 - 2, 0.0D, 0.0D, 0.0D);
				  tessellator.setColorOpaque_I(0);
				  tessellator.addVertexWithUV(j4 + 1, i3 + l3 + 1, 0.0D, 0.0D, 1.0D);
				  tessellator.addVertexWithUV(l4 - 1, i3 + l3 + 1, 0.0D, 1.0D, 1.0D);
				  tessellator.addVertexWithUV(l4 - 1, i3 - 1, 0.0D, 1.0D, 0.0D);
				  tessellator.addVertexWithUV(j4 + 1, i3 - 1, 0.0D, 0.0D, 0.0D);
				  tessellator.draw();
				  GL11.glEnable(GL11.GL_TEXTURE_2D);
			  }

			  drawSlot(k2, k1, i3, l3, tessellator);
		  }

		  GL11.glDisable(GL11.GL_DEPTH_TEST);
		  byte byte0 = 4;
		  overlayBackground(0, top, 255, 255);
		  overlayBackground(bottom, height, 255, 255);
		  GL11.glEnable(GL11.GL_BLEND);
		  GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		  GL11.glDisable(GL11.GL_ALPHA_TEST);
		  GL11.glShadeModel(GL11.GL_SMOOTH);
		  GL11.glDisable(GL11.GL_TEXTURE_2D);
		  tessellator.startDrawingQuads();
		  tessellator.setColorRGBA_I(0, 0);
		  tessellator.addVertexWithUV(left, top + byte0, 0.0D, 0.0D, 1.0D);
		  tessellator.addVertexWithUV(right, top + byte0, 0.0D, 1.0D, 1.0D);
		  tessellator.setColorRGBA_I(0, 255);
		  tessellator.addVertexWithUV(right, top, 0.0D, 1.0D, 0.0D);
		  tessellator.addVertexWithUV(left, top, 0.0D, 0.0D, 0.0D);
		  tessellator.draw();
		  tessellator.startDrawingQuads();
		  tessellator.setColorRGBA_I(0, 255);
		  tessellator.addVertexWithUV(left, bottom, 0.0D, 0.0D, 1.0D);
		  tessellator.addVertexWithUV(right, bottom, 0.0D, 1.0D, 1.0D);
		  tessellator.setColorRGBA_I(0, 0);
		  tessellator.addVertexWithUV(right, bottom - byte0, 0.0D, 1.0D, 0.0D);
		  tessellator.addVertexWithUV(left, bottom - byte0, 0.0D, 0.0D, 0.0D);
		  tessellator.draw();
		  int j3 = getContentHeight() - (bottom - top - 4);

		  if (j3 > 0)
		  {
			  int i4 = ((bottom - top) * (bottom - top)) / getContentHeight();

			  if (i4 < 32)
			  {
				  i4 = 32;
			  }

			  if (i4 > bottom - top - 8)
			  {
				  i4 = bottom - top - 8;
			  }

			  int k4 = ((int)amountScrolled * (bottom - top - i4)) / j3 + top;

			  if (k4 < top)
			  {
				  k4 = top;
			  }

			  tessellator.startDrawingQuads();
			  tessellator.setColorRGBA_I(0, 255);
			  tessellator.addVertexWithUV(j, bottom, 0.0D, 0.0D, 1.0D);
			  tessellator.addVertexWithUV(k, bottom, 0.0D, 1.0D, 1.0D);
			  tessellator.addVertexWithUV(k, top, 0.0D, 1.0D, 0.0D);
			  tessellator.addVertexWithUV(j, top, 0.0D, 0.0D, 0.0D);
			  tessellator.draw();
			  tessellator.startDrawingQuads();
			  tessellator.setColorRGBA_I(0x808080, 255);
			  tessellator.addVertexWithUV(j, k4 + i4, 0.0D, 0.0D, 1.0D);
			  tessellator.addVertexWithUV(k, k4 + i4, 0.0D, 1.0D, 1.0D);
			  tessellator.addVertexWithUV(k, k4, 0.0D, 1.0D, 0.0D);
			  tessellator.addVertexWithUV(j, k4, 0.0D, 0.0D, 0.0D);
			  tessellator.draw();
			  tessellator.startDrawingQuads();
			  tessellator.setColorRGBA_I(0xc0c0c0, 255);
			  tessellator.addVertexWithUV(j, (k4 + i4) - 1, 0.0D, 0.0D, 1.0D);
			  tessellator.addVertexWithUV(k - 1, (k4 + i4) - 1, 0.0D, 1.0D, 1.0D);
			  tessellator.addVertexWithUV(k - 1, k4, 0.0D, 1.0D, 0.0D);
			  tessellator.addVertexWithUV(j, k4, 0.0D, 0.0D, 0.0D);
			  tessellator.draw();
		  }

		  func_27257_b(par1, par2);
		  GL11.glEnable(GL11.GL_TEXTURE_2D);
		  GL11.glShadeModel(GL11.GL_FLAT);
		  GL11.glEnable(GL11.GL_ALPHA_TEST);
		  GL11.glDisable(GL11.GL_BLEND);
	  }

	  /**
	   * Overlays the background to hide scrolled items
	   */
	  private void overlayBackground(int par1, int par2, int par3, int par4)
	  {
		  GL11.glPushMatrix(); //Spout
		  Tessellator tessellator = Tessellator.instance;
		  GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/background.png"));
		  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		  GL11.glTranslatef(0, 0, -0.1f); // Spout
		  float f = 32F;
		  tessellator.startDrawingQuads();
		  tessellator.setColorRGBA_I(0x404040, par4);
		  tessellator.addVertexWithUV(0.0D, par2, 0.0D, 0.0D, (float)par2 / f);
		  tessellator.addVertexWithUV(width, par2, 0.0D, (float)width / f, (float)par2 / f);
		  tessellator.setColorRGBA_I(0x404040, par3);
		  tessellator.addVertexWithUV(width, par1, 0.0D, (float)width / f, (float)par1 / f);
		  tessellator.addVertexWithUV(0.0D, par1, 0.0D, 0.0D, (float)par1 / f);
		  tessellator.draw();
		  GL11.glPopMatrix(); //Spout
	  }
}
