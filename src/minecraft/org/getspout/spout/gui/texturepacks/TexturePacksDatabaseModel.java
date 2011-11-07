package org.getspout.spout.gui.texturepacks;

import java.util.HashMap;

import org.getspout.spout.gui.database.AbstractAPIModel;

public class TexturePacksDatabaseModel extends AbstractAPIModel {
	public TexturePacksDatabaseModel() {
		API = "http://textures.getspout.org/api2.php";
	}
	
	@Override
	protected void refreshList(boolean clear) {
		if(clear) {
			entries.clear();
		}
		for(Object obj:apiData) {
			HashMap<String, Object> item = (HashMap<String, Object>) obj;
			TextureItem t = new TextureItem();
			t.setName((String) item.get("name"));
			t.setAuthor((String) item.get("authors"));
			t.setResolution(Integer.valueOf((String) item.get("resolution")));
			t.setId(Integer.valueOf((String) item.get("Id")));
			t.setDescription((String) item.get("desc"));
			t.setSize((Integer) item.get("size"));
			t.setForumlink((String) item.get("forumlink"));
			t.updateInstalled();
			entries.add(t);
		}
		update();
	}
	
	public String getDefaultUrl() {
		return "http://textures.getspout.org/api2.php?featured";
	}

}
