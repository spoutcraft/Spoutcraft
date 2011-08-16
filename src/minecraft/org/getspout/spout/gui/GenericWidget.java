package org.getspout.spout.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;
import org.getspout.spout.packet.PacketUtil;

public abstract class GenericWidget implements Widget{
	protected int upperLeftX = 0;
	protected int upperLeftY = 0;
	protected int width = 0;
	protected int height = 0;
	protected boolean visible = true;
	protected transient boolean dirty = true;
	protected transient Screen screen = null;
	protected RenderPriority priority = RenderPriority.Normal;
	protected UUID id = UUID.randomUUID();
	protected String tooltip = "";
	protected WidgetAnchor anchor = WidgetAnchor.TOP_LEFT;
	protected boolean scale = true;
	
	public GenericWidget() {

	}
	
	public int getNumBytes() {
		return 39 + PacketUtil.getNumBytes(tooltip);
	}

	public int getVersion() {
		return 1;
	}
	
	public GenericWidget(int upperLeftX, int upperLeftY, int width, int height) {
		this.upperLeftX = upperLeftX;
		this.upperLeftY = upperLeftY;
		this.width = width;
		this.height = height;
	}

	public Widget setAnchor(WidgetAnchor anchor) {
		this.anchor = anchor;
		return this;
	}
	
	public Widget setScale(boolean scale) {
		this.scale = scale;
		return this;
	}
	
	@Override
	public WidgetAnchor getAnchor() {
		return anchor;
	}
	
	@Override
	public boolean getScale() {
		return scale;
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		setX(input.readInt());
		setY(input.readInt());
		setWidth(input.readInt());
		setHeight(input.readInt());
		setScale(input.readBoolean());
		setAnchor(WidgetAnchor.getAnchor(input.readByte()));
		setVisible(input.readBoolean());
		setPriority(RenderPriority.getRenderPriorityFromId(input.readInt()));
		long msb = input.readLong();
		long lsb = input.readLong();
		this.id = new UUID(msb, lsb);
		setTooltip(PacketUtil.readString(input));
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(getX());
		output.writeInt(getY());
		output.writeInt(width);
		output.writeInt(height);
		output.writeBoolean(getScale());
		output.writeByte(anchor.getId());
		output.writeBoolean(isVisible());
		output.writeInt(priority.getId());
		output.writeLong(getId().getMostSignificantBits());
		output.writeLong(getId().getLeastSignificantBits());
		PacketUtil.writeString(output, getTooltip());
	}
	
	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	@Override
	public boolean isDirty() {
		return dirty;
	}
	
	@Override
	public UUID getId() {
		return id;
	}
	@Override
	public Screen getScreen() {
		return screen;
	}
	
	@Override
	public Widget setScreen(Screen screen) {
		this.screen = screen;
		return this;
	}
	
	@Override
	public RenderPriority getPriority() {
		return priority;
	}
	
	@Override
	public Widget setPriority(RenderPriority priority) {
		this.priority = priority;
		return this;
	}
	
	@Override
	public double getActualWidth() {
		return width;
	}
	
	@Override
	public Widget setWidth(int width) {
		this.width = width;
		return this;
	}

	@Override
	public double getActualHeight() {
		return height;
	}
	
	@Override
	public double getWidth() {
		return scale ? (getActualWidth() * (getScreen() != null ? (getScreen().getWidth() / 427f) : 1) ) : getActualWidth();
	}
	
	@Override
	public double getHeight() {
		return scale ? (getActualHeight() * (getScreen() != null ? (getScreen().getHeight() / 240f) : 1) ) : getActualHeight();
	}

	@Override
	public Widget setHeight(int height) {
		this.height = height;
		return this;
	}

	@Override
	public int getX() {
		return upperLeftX;
	}

	@Override
	public int getY() {
		return upperLeftY;
	}
	
	@Override
	public double getScreenX() {
		double left = (upperLeftX * (getScreen() != null ? (getScreen().getWidth() / 427f) : 1) );
		switch (anchor) {
			case TOP_CENTER:
			case CENTER_CENTER:
			case BOTTOM_CENTER:
				left -= getWidth() / 2;
				break;
			case TOP_RIGHT:
			case CENTER_RIGHT:
			case BOTTOM_RIGHT:
				left -= getWidth();
			break;
		}
		return left;
	}

	@Override
	public double getScreenY() {
		double top = (upperLeftY * (getScreen() != null ? (getScreen().getHeight() / 240f) : 1) );
		switch (anchor) {
			case CENTER_LEFT:
			case CENTER_CENTER:
			case CENTER_RIGHT:
				top -= getHeight() / 2;
				break;
			case BOTTOM_LEFT:
			case BOTTOM_CENTER:
			case BOTTOM_RIGHT:
				top -= getHeight();
			break;
		}
		return top;
	}

	@Override
	public Widget setX(int pos) {
		this.upperLeftX = pos;
		return this;
	}

	@Override
	public Widget setY(int pos) {
		this.upperLeftY = pos;
		return this;
	}

	@Override
	public Widget shiftXPos(int modX) {
		setX(getX() + modX);
		return this;
	}
	
	@Override
	public Widget shiftYPos(int modY) {
		setY(getY() + modY);
		return this;
	}
	
	@Override
	public boolean isVisible() {
		return visible;
	}
	
	@Override
	public Widget setVisible(boolean enable) {
		visible = enable;
		return this;
	}
	
	@Override
	public int hashCode() {
		return getId().hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof Widget && other.hashCode() == hashCode();
	}
	
	@Override
	public void onTick() {

	}
	
	@Override
	public void setTooltip(String tooltip){
		this.tooltip = tooltip;
	}
	
	@Override
	public String getTooltip(){
		return tooltip;
	}
}
