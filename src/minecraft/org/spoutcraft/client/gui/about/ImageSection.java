package org.spoutcraft.client.gui.about;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.Sys;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.Color;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericTexture;
import org.spoutcraft.spoutcraftapi.gui.RenderPriority;
import org.spoutcraft.spoutcraftapi.gui.Widget;

public class ImageSection extends Section {
	private GuiNewAbout screen;
	private List<Image> images = new LinkedList<ImageSection.Image>();
	private int height = 0;
	
	private class Image {
		public GenericTexture texture;
		public String url = "";
		public GenericLabel description = null;
		public GenericButton button = new GenericButton() {
			{
				setPriority(RenderPriority.Highest);
			}
			
			@Override
			public void render() {
				//Don't render ;)
			}
			
			@Override
			public void onButtonClick(ButtonClickEvent event) {
				Sys.openURL(url);
				super.onButtonClick(event);
			}
		};
	}
	
	@Override
	public List<Widget> getWidgets() {
		List<Widget> ret = super.getWidgets();
		for(Image img:images) {
			ret.add(img.texture);
			ret.add(img.button);
			if(img.description != null) {
				ret.add(img.description);
			}
		}
		return ret;
	}

	@Override
	public int getHeight() {
		return super.getHeight() + 5 + height;
	}

	@Override
	public void update() {
		height = 0;
		int x = getX();
		int y = getY() + super.getHeight() + 5;
		for(Image img:images) {
			int th = img.texture.getOriginalHeight();
			int tw = img.texture.getOriginalWidth();
			int theight = (int) (getWidth() * ((float) th / (float) tw));
			img.texture.setGeometry(x, y, getWidth(), theight);
			img.button.setGeometry(x, y, getWidth(), theight);
			int h = theight + 5;
			
			if(img.description != null) {
				img.description.setX(x);
				img.description.setY(y + theight + 5);
				img.description.setWidth(getWidth());
				img.description.setWrapLines(true);
				img.description.recalculateLines();
				h += 5 + img.description.getHeight();
			}
			
			y += h;
			height += h;
		}
		super.update();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init(final GuiNewAbout screen, String title, Object yaml) {
		setTitle(title);
		this.screen = screen;
		
		HashMap<String, Object> images = (HashMap<String, Object>) yaml;
		for(Object img:images.values()) {
			HashMap<String, String> attr = (HashMap<String, String>) img;
			Image image = new Image();
			image.texture = new GenericTexture(attr.get("image"));
			image.texture.setFinishDelegate(new Runnable() {
				@Override
				public void run() {
					screen.update();
				}
			});
			image.url = attr.get("website");
			image.texture.setTooltip(attr.get("tooltip"));
			if(attr.containsKey("description") && !attr.get("description").isEmpty()) {
				image.description = new GenericLabel(attr.get("description"));
				image.description.setTextColor(new Color(0xaaaaaa));
			}
			this.images.add(image);
			
			//TODO description
		}
	}

}
