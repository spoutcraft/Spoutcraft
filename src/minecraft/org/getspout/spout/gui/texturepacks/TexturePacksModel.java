package org.getspout.spout.gui.texturepacks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.TexturePackBase;
import net.minecraft.src.TexturePackList;

import org.getspout.spout.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.gui.AbstractListModel;

import com.pclewis.mcpatcher.mod.TextureUtils;

public class TexturePacksModel extends AbstractListModel {

	TexturePackList textures = SpoutClient.getHandle().texturePackList;
	List<TexturePackItem> items = new ArrayList<TexturePackItem>();
	
	public TexturePacksModel() {
	}
	
	public int getSize() {
		return items.size();
	}

	@Override
	public TexturePackItem getItem(int row) {
		return items.get(row);
	}

	@Override
	public void onSelected(int item, boolean doubleClick) {
		// TODO Auto-generated method stub
		
	}
	
	public void update() {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if(wasSandboxed) SpoutClient.disableSandbox();
		textures = SpoutClient.getHandle().texturePackList;
		try{
			textures.updateAvaliableTexturePacks();
			items.clear();
			for(TexturePackBase pack:getTextures()) {
				items.add(new TexturePackItem(pack));
			}
		} finally {
			if(wasSandboxed) SpoutClient.enableSandbox();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<TexturePackBase> getTextures() {
		return (List<TexturePackBase>)textures.availableTexturePacks();
	}
	
	public void changeTexturePack(TexturePackBase pack) {
		//TODO
	}
}
