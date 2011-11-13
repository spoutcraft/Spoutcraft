package org.spoutcraft.spoutcraftapi.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;

public class GenericComboBox extends GenericButton implements ComboBox {
	private List<String> items = new ArrayList<String>();
	private ComboBoxModel model;
	private GenericListView view;
	private Gradient background;
	private Screen screen;
	
	public GenericComboBox() {
		model = new ComboBoxModel();
		view = new ComboBoxView(model, this);
		view.setSelection(0);
		view.setVisible(false);
		background = new GenericGradient();
		background.setTopColor(new Color(1f,1f,1f));
		background.setBottomColor(new Color(1f,1f,1f));
	}
	
	public GenericComboBox(ComboBoxModel model) {
		this.model = model;
		view = new ComboBoxView(model, this);
		view.setSelection(0);
		view.setVisible(false);
		background = new GenericGradient();
		background.setTopColor(new Color(1f,1f,1f));
		background.setBottomColor(new Color(1f,1f,1f));
	}
	
	public WidgetType getType() {
		return WidgetType.ComboBox;
	}

	public void render() {
		Spoutcraft.getRenderDelegate().render(this);
	}

	public ComboBox setItems(List<String> items) {
		this.items = items;
		model.setList(items);
		setSelection(0);
		return this;
	}

	public List<String> getItems() {
		return Collections.unmodifiableList(items);
	}

	public ComboBox openList() {

		screen = getScreen();
		while (!(screen instanceof GenericScreen)) {
			if(screen.getScreen() != null)
				screen = screen.getScreen();
			else 
				break;
		}
		
		screen.attachWidget(getAddon(), view);
		view.setVisible(true);
//		view.setX((int) getActualX());
//		view.setY((int) (getActualY() + getHeight()));
//		view.setWidth((int) getWidth());
//		int needed = view.getInnerSize(Orientation.VERTICAL) + 10;
//		int available = (int) (screen.getHeight() - view.getActualY() - 5);
//		
//		System.out.println("Screen height: "+screen.getHeight()+" y: "+view.getActualY());
//		
//		System.out.println("available: "+available);
//		
//		if(available < 20) {
//			available = getY() - 5;
//			view.setY(getY() - Math.min(available, needed));
//		}
//		
//		view.setHeight(Math.min(needed, available));
		view.setPriority(RenderPriority.Lowest); //Makes it the top-most widget
		view.setFocus(true);
//		background.setX(view.getX()).setY(view.getY()).setWidth((int) view.getWidth()).setHeight((int) view.getHeight());
//		background.setPriority(RenderPriority.Low);
//		screen.attachWidget(getAddon(), background);
		view.setSelection(view.getSelectedRow());
		return this;
	}

	public ComboBox closeList() {
		view.setVisible(false);
		screen.removeWidget(view);
//		screen.removeWidget(background);
		return this;
	}
	
	public boolean isOpen() {
		return view.isVisible();
	}
	
	@Override
	public Button setText(String text) {return this;}

	public String getSelectedItem() {
		ComboBoxItem item = model.getItem(view.getSelectedRow());
		return item == null ? "" : item.getText();
	}

	public int getSelectedRow() {
		return view.getSelectedRow();
	}
	
	public void onSelectionChanged(int i, String text) {}

	@Override
	public String getText() {
		return getSelectedItem();
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		super.onButtonClick(event);
		if(!isOpen()) {
			openList();
		} else {
			closeList();
		}
	}


	public ComboBox setSelection(int row) {
		view.setSelection(row);
		return this;
	}

	protected class ComboBoxModel extends AbstractListModel {
		List<String> list = new ArrayList<String>();
		List<ComboBoxItem> items = new ArrayList<GenericComboBox.ComboBoxItem>();
		
		public void setList(List<String> list) {
			this.list = list;
			updateItems();
		}
		
		private void updateItems() {
			items.clear();
			for(String l:list) {
				ComboBoxItem item = new ComboBoxItem(this);
				item.setTitle(l);
				items.add(item);
			}
		}

		@Override
		public ComboBoxItem getItem(int row) {
			if(row < 0 || row >= getSize()) return null;
			return items.get(row);
		}

		@Override
		public int getSize() {
			return items.size();
		}

		@Override
		public void onSelected(int item, boolean doubleClick) {
		}

		public void handleClick(ComboBoxItem comboBoxItem) {
			int i = items.indexOf(comboBoxItem);
			onSelectionChanged(i, getItem(i).getText());
			closeList();
		}

		public boolean isLast(ComboBoxItem comboBoxItem) {
			return items.indexOf(comboBoxItem) == items.size() - 1;
		}
	}
	
	public class ComboBoxItem implements ListWidgetItem {
		private String text;
		private ListWidget widget;
		private ComboBoxModel model;
		
		public ComboBoxItem(ComboBoxModel model) {
			this.model = model;
		}
		
		public void setListWidget(ListWidget widget) {
			this.widget = widget;
		}

		public ListWidget getListWidget() {
			return widget;
		}

		public int getHeight() {
			return 11;
		}

		public void render(int x, int y, int width, int height) {
			Spoutcraft.getRenderDelegate().getMinecraftFont().drawString(text, x+2, y+2, 0xffffffff);
			if(!model.isLast(this)) {
				RenderUtil.drawRectangle(x, y+11, x+width, y+11+1, 0xffaaaaaa);
			}
		}

		public void onClick(int x, int y, boolean doubleClick) {
			if(x != -1) {
				model.handleClick(this);
			} else if(doubleClick) {
				model.handleClick(this);
			}
		}

		public String getText() {
			return text;
		}

		public void setTitle(String title) {
			this.text = title;
		}
	}
	
	protected class ComboBoxView extends GenericListView {

		@Override
		public double getWidth() {
			return combo.getWidth();
		}
		@Override
		public double getHeight() {
			int a = getAvailableHeight(false) - 5;
			if(a < 30) {
				a = getAvailableHeight(true) - 5;
			}
			return Math.min(a, getInnerSize(Orientation.VERTICAL));
		}
		@Override
		public int getX() {
			return (int) combo.getActualX();
		}
		@Override
		public int getY() {
			int h = (int) getHeight();
			int a = getAvailableHeight(false);
			if(a < 30) {
				return (int) (combo.getActualY()-h);
			} else {
				return (int) (combo.getActualY() + combo.getHeight());
			}
		}
		
		protected int getAvailableHeight(boolean top) {
			if(!top) {
				return (int) (getScreen().getActualHeight() - combo.getActualY() - 5);
			} else {
				return (int) (combo.getActualY() - 5);
			}
		}
		
		ComboBox combo;
		public ComboBoxView(AbstractListModel model, ComboBox box) {
			super(model);
			combo = box;
		}
		
	}
}
