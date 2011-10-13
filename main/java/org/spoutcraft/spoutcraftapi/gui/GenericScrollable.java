package org.spoutcraft.spoutcraftapi.gui;

import java.util.HashMap;

import org.spoutcraft.spoutcraftapi.Spoutcraft;

public abstract class GenericScrollable extends GenericControl implements Scrollable {
	protected HashMap<Orientation, ScrollBarPolicy> scrollBarPolicy = new HashMap<Orientation, ScrollBarPolicy>();
	protected HashMap<Orientation, Integer> innerSize = new HashMap<Orientation, Integer>();
	protected HashMap<Orientation, Integer> scrollPosition = new HashMap<Orientation, Integer>();
	
	public GenericScrollable() {
		for(Orientation axis:Orientation.values()) {
			scrollBarPolicy.put(axis, ScrollBarPolicy.SHOW_IF_NEEDED);
			innerSize.put(axis, 0);
			scrollPosition.put(axis, 0);
		}
	}
	
	public void render() {
		Spoutcraft.getRenderDelegate().render(this);
	}

	public int getInnerSize(Orientation axis) {
		return innerSize.get(axis);
	}

	public int getScrollPosition(Orientation axis) {
		return scrollPosition.get(axis);
	}

	public void setScrollPosition(Orientation axis, int position) {
		if(position < 0) {
			position = 0;
		}
		if(position > getMaximumScrollPosition(axis)) {
			position = getMaximumScrollPosition(axis);
		}
		scrollPosition.put(axis, position);
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
		scrollBarPolicy.put(axis, policy);
	}
	
	public ScrollBarPolicy getScrollBarPolicy(Orientation axis) {
		return scrollBarPolicy.get(axis);
	}
	
	protected void setInnerSize(Orientation axis, int size) {
		innerSize.put(axis, size);
	}

	public int getViewportSize(Orientation axis) {
		int size = 0;
		size = (int) (axis == Orientation.HORIZONTAL?getWidth():getHeight());
		if(needsScrollBar(axis.getOther())) {
			return size - 16;
		}
		return size;
	}
}
