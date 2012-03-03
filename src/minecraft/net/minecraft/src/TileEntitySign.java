package net.minecraft.src;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public class TileEntitySign extends TileEntity {

	public String[] signText = new String[]{"", "", "", ""};
	public int lineBeingEdited = -1;
	public int columnBeingEdited; //Spout
	private boolean isEditable = true;
	//Spout start
	private byte text = -1; //-1 means invalid cache, 0 means false, 1 means true
	
	public boolean hasText() {
		if (text != -1) {
			return text != 0;
		}
		text = 0;
		for (int i = 0; i < signText.length; i++) {
			if (signText[i] != null && !signText[i].isEmpty()) {
				text = 1;
				break;
			}
		}
		return text != 0;
	}
	
	public void recalculateText() {
		text = -1;
	}
	//Spout end

	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setString("Text1", this.signText[0]);
		par1NBTTagCompound.setString("Text2", this.signText[1]);
		par1NBTTagCompound.setString("Text3", this.signText[2]);
		par1NBTTagCompound.setString("Text4", this.signText[3]);
	}

	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		this.isEditable = false;
		super.readFromNBT(par1NBTTagCompound);

		for (int var2 = 0; var2 < 4; ++var2) {
			this.signText[var2] = par1NBTTagCompound.getString("Text" + (var2 + 1));
			if (this.signText[var2].length() > 15) {
				this.signText[var2] = this.signText[var2].substring(0, 15);
			}
		}
		//Spout start
		recalculateText();
		//Spout end
	}
}
