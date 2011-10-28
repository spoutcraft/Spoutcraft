package org.spoutcraft.spoutcraftapi.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.UnsafeClass;

@UnsafeClass
public abstract class GenericScrollable extends GenericControl implements Scrollable {
	private ScrollBarPolicy sbpVert;
	protected ScrollBarPolicy sbpHoriz = sbpVert = ScrollBarPolicy.SHOW_IF_NEEDED;
	protected int innerSizeHoriz = 0, innerSizeVert = 0;
	protected int scrollX = 0, scrollY = 0;
	
	public GenericScrollable() {
	}
	
	public void render() {
		Spoutcraft.getRenderDelegate().render(this);
	}

	public int getInnerSize(Orientation axis) {
		switch(axis) {
		case HORIZONTAL:
			return innerSizeHoriz;
		case VERTICAL:
			return innerSizeVert;
		}
		return 0;
	}

	public int getScrollPosition(Orientation axis) {
		switch(axis) {
		case HORIZONTAL:
			return scrollX;
		case VERTICAL:
			return scrollY;
		}
		return 0;
	}

	public void setScrollPosition(Orientation axis, int position) {
		if(position < 0) {
			position = 0;
		}
		if(position > getMaximumScrollPosition(axis)) {
			position = getMaximumScrollPosition(axis);
		}
		switch(axis) {
		case HORIZONTAL:
			scrollX = position;
			break;
		case VERTICAL:
			scrollY = position;
			break;
		}
	}

	public void scroll(int x, int y) {
		int currentX, currentY;
		currentX = getScrollPosition(Orientation.HORIZONTAL);
		currentX += x;
		currentY = getScrollPosition(Orientation.VERTICAL);
		currentY += y;
		setScrollPosition(Orientation.HORIZONTAL, currentX);
		setScrollPosition(Orientation.VERTICAL, currentY);
	}

	public void ensureVisible(Rectangle rect) {
		int scrollTop = getScrollPosition(Orientation.VERTICAL);
		int scrollLeft = getScrollPosition(Orientation.HORIZONTAL);
		int scrollBottom = scrollTop + getViewportSize(Orientation.VERTICAL) - 10;
		int scrollRight = scrollLeft + getViewportSize(Orientation.HORIZONTAL) - 10;
		boolean correctHorizontal = false;
		boolean correctVertical = false;
		if(scrollTop <= rect.getTop() && rect.getBottom() <=scrollBottom) {
			//Fits vertical
			if(scrollLeft <= rect.getLeft() && rect.getRight() <= scrollRight) {
				//Fits completely
				//Nothing to do.
				return;
			} else {
				//Doesn't fit horizontal
				correctHorizontal = true;
			}
		} else {
			//Doesn't fit vertical
			correctVertical = true;
			if(scrollLeft <= rect.getLeft() && rect.getRight() <= scrollRight) {
				//But does fit horizontal
			} else {
				//Doesn't fit horizontal too
				correctHorizontal = true;
			}
		}
		if(correctHorizontal) {
			if(scrollRight < rect.getRight()) {
				setScrollPosition(Orientation.HORIZONTAL, rect.getRight() - getViewportSize(Orientation.HORIZONTAL) + 10);
			}
			if(scrollLeft > rect.getLeft()) {
				setScrollPosition(Orientation.HORIZONTAL, rect.getLeft());
			}
		}
		if(correctVertical) {
			if(scrollBottom < rect.getBottom()) {
				setScrollPosition(Orientation.VERTICAL, rect.getBottom() - getViewportSize(Orientation.VERTICAL) + 10);
			}
			if(scrollTop > rect.getTop()) {
				setScrollPosition(Orientation.VERTICAL, rect.getTop());
			}
		}
	}

	public int getMaximumScrollPosition(Orientation axis) {
		return (int) Math.max(0, getInnerSize(axis) - (axis==Orientation.HORIZONTAL?getWidth():getHeight()));
	}

	public boolean needsScrollBar(Orientation axis) {
		ScrollBarPolicy policy = getScrollBarPolicy(axis);
		switch(policy) {
		case SHOW_IF_NEEDED:
			return getMaximumScrollPosition(axis) > 0;
		case SHOW_ALWAYS:
			return true;
		case SHOW_NEVER:
			return false;
		default:
			return true;
		}
	}

	public void setScrollBarPolicy(Orientation axis, ScrollBarPolicy policy) {
		switch(axis) {
		case HORIZONTAL:
			sbpHoriz = policy;
			break;
		case VERTICAL:
			sbpVert = policy;
			break;
		}
	}
	
	public ScrollBarPolicy getScrollBarPolicy(Orientation axis) {
		switch(axis) {
		case HORIZONTAL:
			return sbpHoriz;
		case VERTICAL:
			return sbpVert;
		}
		return null;
	}
	
	protected void setInnerSize(Orientation axis, int size) {
		switch(axis) {
		case HORIZONTAL:
			innerSizeHoriz = size;
			break;
		case VERTICAL:
			innerSizeVert = size;
			break;
		}
	}

	public int getViewportSize(Orientation axis) {
		int size = 0;
		size = (int) (axis == Orientation.HORIZONTAL?getWidth():getHeight());
		if(needsScrollBar(axis.getOther())) {
			return size - 16;
		}
		return size;
	}

	public int getMargin(PositionOrientation pos) {
		switch(pos) {
		case TOP:
			return 5;
		default: 
			return 0;
		}
	}
	
	@Override
	public int getNumBytes() {
		return super.getNumBytes() + 6*4;
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		sbpHoriz = ScrollBarPolicy.getById(input.readInt());
		sbpVert = ScrollBarPolicy.getById(input.readInt());
		scrollX = input.readInt();
		scrollY = input.readInt();
		innerSizeHoriz = input.readInt();
		innerSizeVert = input.readInt();
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		output.writeInt(sbpHoriz.getId());
		output.writeInt(sbpVert.getId());
		output.writeInt(scrollX);
		output.writeInt(scrollY);
		output.writeInt(innerSizeHoriz);
		output.writeInt(innerSizeVert);
	}
}
