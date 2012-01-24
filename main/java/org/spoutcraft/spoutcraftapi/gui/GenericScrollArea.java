package org.spoutcraft.spoutcraftapi.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.UnsafeClass;
import org.spoutcraft.spoutcraftapi.addon.Addon;

@UnsafeClass
public class GenericScrollArea extends GenericScrollable implements ScrollArea {
	protected HashMap<Widget, Addon> widgets = new HashMap<Widget, Addon>();
	protected int playerId;
	protected boolean bgvis;
	protected int mouseX = -1, mouseY = -1;
	@SuppressWarnings("unused")
	private int screenHeight, screenWidth;
	
	public GenericScrollArea(int playerId) {
		this.playerId = playerId;
	}

	public GenericScrollArea() {
		super();
		screenWidth = Spoutcraft.getClient().getRenderDelegate().getScreenWidth();
		screenHeight = Spoutcraft.getClient().getRenderDelegate().getScreenHeight();
	}
	
	public void renderContents() {
		Spoutcraft.getRenderDelegate().renderContents(this);
	}

	public WidgetType getType() {
		return WidgetType.ScrollArea;
	}

	@Override
	public int getVersion() {
		return super.getVersion() + 0;
	}

	public Widget[] getAttachedWidgets() {
		Widget[] list = new Widget[widgets.size()];
		list = widgets.keySet().toArray(list);
		return list;
	}

	@Deprecated
	public Screen attachWidget(Widget widget) {
		return attachWidget(null, widget);
	}
	
	public Screen attachWidget(Addon addon, Widget widget) {
		widgets.put(widget, addon);
		widget.setAddon(addon);
		widget.setAnchor(WidgetAnchor.TOP_LEFT);
		widget.setScreen(this);
		updateInnerSize();
		return this;
	}
	
	public Screen attachWidgets(Addon addon, Widget ...widgets) {
		for(Widget widget:widgets) {
			this.widgets.put(widget, addon);
			widget.setAddon(addon);
			widget.setAnchor(WidgetAnchor.TOP_LEFT);
			widget.setScreen(this);
		}
		updateInnerSize();
		return this;
	}

	public void updateInnerSize() {
		int height = 0;
		int width = 0;
		for(Widget w:widgets.keySet()) {
			height = (int) Math.max(height, w.getY() + w.getHeight());
			width = (int) Math.max(width, w.getX() + w.getWidth());
		}
		setInnerSize(Orientation.HORIZONTAL, width);
		setInnerSize(Orientation.VERTICAL, height + 5);
	}

	public Screen removeWidget(Widget widget) {
		widgets.remove(widget);
		widget.setScreen(null);
		updateInnerSize();
		return this;
	}
	
	public Screen removeWidgets(Addon addon) {
		for (Widget i : getAttachedWidgets()) {
			if (widgets.get(i) != null && widgets.get(i).equals(addon)) {
				removeWidget(i);
			}
		}
		updateInnerSize();
		return this;
	}
	
	public boolean containsWidget(Widget widget) {
		return containsWidget(widget.getId());
	}
	
	public boolean containsWidget(UUID id) {
		return getWidget(id) != null;
	}
	
	public Widget getWidget(UUID id) {
		for (Widget w : widgets.keySet()) {
			if (w.getId().equals(id)) {
				return w;
			}
		}
		return null;
	}
	
	public boolean updateWidget(Widget widget) {
		if (widgets.containsKey(widget)) {
			Addon addon = widgets.get(widget);
			widgets.remove(widget);
			widgets.put(widget, addon);
			widget.setScreen(this);
			return true;
		}
		return false;
	}

	@Override
	public void onTick() {
		for (Widget widget : widgets.keySet()) {
			widget.onTick();
		}
		screenWidth = Spoutcraft.getClient().getRenderDelegate().getScreenWidth();
		screenHeight = Spoutcraft.getClient().getRenderDelegate().getScreenHeight();
	}

	public Screen setBgVisible(boolean enable) {
		bgvis = enable;
		return this;
	}

	public boolean isBgVisible() {
		return bgvis;
	}

	@Override
	public int getNumBytes() {
		return super.getNumBytes() + 1;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		setBgVisible(input.readBoolean());
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		output.writeBoolean(isBgVisible());
	}

	protected boolean canRender(Widget widget) {
		return widget.isVisible();
	}

	RenderPriority[] rvalues = RenderPriority.values();

	public Screen setMouseX(int mouseX) {
		this.mouseX = mouseX;
		return this;
	}

	public Screen setMouseY(int mouseY) {
		this.mouseY = mouseY;
		return this;
	}

	public int getMouseX() {
		return getScreen().getMouseX();
	}

	public int getMouseY() {
		return getScreen().getMouseY();
	}

	@Override
	public Control copy() {
		throw new UnsupportedOperationException("You can not create a copy of a screen");
	}

	public ScreenType getScreenType() {
		return getScreen().getScreenType();
	}

	public Set<Widget> getAttachedWidgetsAsSet() {
		return Collections.unmodifiableSet(widgets.keySet());
	}

	public Widget[] getAttachedWidgets(boolean recursive) {
		Widget[] list = new Widget[widgets.size()];
		Set<Widget> allwidgets = new HashSet<Widget>();
		allwidgets.addAll(widgets.keySet());
		if(recursive) {
			for(Widget w:widgets.keySet()) {
				if(w instanceof Screen) {
					allwidgets.addAll(((Screen)w).getAttachedWidgetsAsSet(true));
				}
			}
		}
		allwidgets.toArray(list);
		return list;
	}

	public Set<Widget> getAttachedWidgetsAsSet(boolean recursive) {
		Set<Widget> allwidgets = new HashSet<Widget>();
		allwidgets.addAll(widgets.keySet());
		if(recursive) {
			for(Widget w:widgets.keySet()) {
				if(w instanceof Screen) {
					allwidgets.addAll(((Screen)w).getAttachedWidgetsAsSet(true));
				}
			}
		}
		return allwidgets;
	}
}
