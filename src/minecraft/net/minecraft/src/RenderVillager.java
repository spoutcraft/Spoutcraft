package net.minecraft.src;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityVillager;
import net.minecraft.src.ModelVillager;
import net.minecraft.src.RenderLiving;
import org.lwjgl.opengl.GL11;

public class RenderVillager extends RenderLiving {

	protected ModelVillager field_40295_c;
    

	public RenderVillager() {
		super(new ModelVillager(0.0F), 0.5F);
		this.field_40295_c = (ModelVillager)this.mainModel;
	}

	protected int func_40293_a(EntityVillager var1, int var2, float var3) {
		return -1;
	}

	public void renderVillager(EntityVillager var1, double var2, double var4, double var6, float var8, float var9) {
		super.doRenderLiving(var1, var2, var4, var6, var8, var9);
	}

	protected void func_40290_a(EntityVillager var1, double var2, double var4, double var6) {}

	protected void func_40291_a(EntityVillager var1, float var2) {
		super.renderEquippedItems(var1, var2);
	}

	protected void func_40292_b(EntityVillager var1, float var2) {
		float var3 = 0.9375F;
		GL11.glScalef(var3, var3, var3);
	}

	protected void passSpecialRender(EntityLiving var1, double var2, double var4, double var6) {
		//Spout start
        //this.func_40290_a((EntityVillager)var1, var2, var4, var6);
        super.passSpecialRender(var1, var2, var4, var6);
        //Spout end
	}

	protected void preRenderCallback(EntityLiving var1, float var2) {
		this.func_40292_b((EntityVillager)var1, var2);
	}

	protected int shouldRenderPass(EntityLiving var1, int var2, float var3) {
		return this.func_40293_a((EntityVillager)var1, var2, var3);
	}

	protected void renderEquippedItems(EntityLiving var1, float var2) {
		this.func_40291_a((EntityVillager)var1, var2);
	}

	public void doRenderLiving(EntityLiving var1, double var2, double var4, double var6, float var8, float var9) {
		this.renderVillager((EntityVillager)var1, var2, var4, var6, var8, var9);
	}

	public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
		this.renderVillager((EntityVillager)var1, var2, var4, var6, var8, var9);
	}
}
