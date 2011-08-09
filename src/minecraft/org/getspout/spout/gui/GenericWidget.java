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
	
	public GenericWidget() {

	}
	
	public int getNumBytes() {
		return 37 + PacketUtil.getNumBytes(tooltip);
	}

	public int getVersion() {
		return 0;
	}
	
	public GenericWidget(int upperLeftX, int upperLeftY, int width, int height) {
		this.upperLeftX = upperLeftX;
		this.upperLeftY = upperLeftY;
		this.width = width;
		this.height = height;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		setX(input.readInt());
		setY(input.readInt());
		setWidth(input.readInt());
		setHeight(input.readInt());
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
	public double getWidth() {
		return (width * (getScreen() != null ? (getScreen().getWidth() / 240f) : 1) );
	}
	
	@Override
	public Widget setWidth(int width) {
		this.width = width;
		return this;
	}

	@Override
	public double getHeight() {
		return (height * (getScreen() != null ? (getScreen().getHeight() / 427f) : 1) );
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
		return (upperLeftX * (getScreen() != null ? (getScreen().getWidth() / 240f) : 1) );
	}

	@Override
	public double getScreenY() {
		return (upperLeftY * (getScreen() != null ? (getScreen().getHeight() / 427f) : 1 ));
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
