package com.devotion.blue.web.menu;

import com.devotion.blue.utils.StringUtils;

import java.util.LinkedList;
import java.util.List;


public class MenuGroup {

	public static final String TYPE_NORMAL = "_normal";
	public static final String TYPE_BLOCK = "_block";

	private String id;
	private String iconClass;
	private String text;
	private String type = TYPE_NORMAL;
	private List<MenuItem> menuItems;

	public MenuGroup() {
	}

	public MenuGroup(String id, String iconClass, String text) {
		this.id = id;
		this.iconClass = iconClass;
		this.text = text;
	}

	public MenuGroup(String id, String iconClass, String text, String type) {
		this.id = id;
		this.iconClass = iconClass;
		this.text = text;
		this.type = type;
	}

	public static MenuGroup createBlockGroup() {
		MenuGroup group = new MenuGroup();
		group.setType(TYPE_BLOCK);
		return group;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIconClass() {
		return iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}

	public void addMenuItem(MenuItem item) {
		if (this.menuItems == null) {
			menuItems = new LinkedList<MenuItem>();
		}
		menuItems.add(item);
	}

	public void addMenuItem(int index, MenuItem item) {
		if (this.menuItems == null) {
			menuItems = new LinkedList<MenuItem>();
		}
		menuItems.add(index, item);
	}

	public void removeMenuItemById(String id) {
		if (StringUtils.isBlank(id)) {
			return;
		}

		if (menuItems == null || menuItems.isEmpty()) {
			return;
		}

		MenuItem deleteItem = null;
		for (MenuItem item : menuItems) {
			if (id.equals(item.getId())) {
				deleteItem = item;
				break;
			}
		}
		if (deleteItem != null) {
			menuItems.remove(deleteItem);
		}
	}

	public String generateHtml() {
		if (TYPE_BLOCK.equals(type)) {
			return "<li class=\"jpress_block\"></li>";
		}

		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<li class=\"treeview\" id=\"" + id + "\">");
		{
			htmlBuilder.append("<a href=\"#\">");
			htmlBuilder.append("<i class=\"" + iconClass + "\" ></i>");
			htmlBuilder.append("<span>" + text + "</span>");
			htmlBuilder.append("<i class=\"fa fa-angle-left pull-right\" ></i>");
			htmlBuilder.append("</a>");
			htmlBuilder.append("<ul class=\"treeview-menu\">");

			if (menuItems != null && menuItems.size() > 0) {
				for (MenuItem item : menuItems) {
					htmlBuilder.append(item.generateHtml());
				}
			}

			htmlBuilder.append("</ul>");
		}
		htmlBuilder.append("</li>");
		return htmlBuilder.toString();
	}

}
