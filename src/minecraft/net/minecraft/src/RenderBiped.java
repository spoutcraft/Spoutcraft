package net.minecraft.src;

import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderLiving;
import org.lwjgl.opengl.GL11;
//spout start
import org.spoutcraft.spoutcraftapi.material.MaterialData;
//spout end

public class RenderBiped extends RenderLiving {

	protected ModelBiped modelBipedMain;
	protected float field_40296_d;

	public RenderBiped(ModelBiped par1ModelBiped, float par2) {
		this(par1ModelBiped, par2, 1.0F);
		this.modelBipedMain = par1ModelBiped;
	}

	public RenderBiped(ModelBiped par1ModelBiped, float par2, float par3) {
		super(par1ModelBiped, par2);
		this.modelBipedMain = par1ModelBiped;
		this.field_40296_d = par3;
	}

	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
		super.renderEquippedItems(par1EntityLiving, par2);
		ItemStack var3 = par1EntityLiving.getHeldItem();
		if (var3 != null) {
			GL11.glPushMatrix();
			this.modelBipedMain.bipedRightArm.postRender(0.0625F);
			GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
			float var4;
			if (var3.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[var3.itemID].getRenderType())) {
				var4 = 0.5F;
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				var4 *= 0.75F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(var4, -var4, var4);
			} else if (var3.itemID == Item.bow.shiftedIndex) {
				var4 = 0.625F;
				GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
				GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(var4, -var4, var4);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			//spout start
			} else if (Item.itemsList[var3.itemID].isFull3D() || var3.itemID == Item.flint.shiftedIndex && MaterialData.getCustomItem(var3.getItemDamage()) instanceof org.spoutcraft.spoutcraftapi.material.Tool) {
			//spout end
				var4 = 0.625F;
				GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
				GL11.glScalef(var4, -var4, var4);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				var4 = 0.375F;
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(var4, var4, var4);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			this.renderManager.itemRenderer.renderItem(par1EntityLiving, var3, 0);
			if (var3.getItem().func_46058_c()) {
				this.renderManager.itemRenderer.renderItem(par1EntityLiving, var3, 1);
			}

			GL11.glPopMatrix();
		}

	}
}
