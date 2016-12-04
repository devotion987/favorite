package com.devotion.blue.web.menu;

import com.devotion.blue.message.MessageKit;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.addon.HookInvoker;

import java.util.LinkedList;

public class MenuManager {

	public static final String ACTION_INIT_MENU = "_INIT_MENU";

	static MenuManager manager = new MenuManager();
	static final LinkedList<MenuGroup> menuGroups = new LinkedList<>();

	public static MenuManager me() {
		return manager;
	}

	public String generateHtml() {
		if (menuGroups.isEmpty()) {
			MessageKit.sendMessage(ACTION_INIT_MENU, this);
		}

		HookInvoker.menuInitBefore(this);

		StringBuilder htmlBuilder = new StringBuilder();
		for (MenuGroup group : menuGroups) {
			htmlBuilder.append(group.generateHtml());
		}

		HookInvoker.menuInitAfter(this);

		return htmlBuilder.toString();
	}

	public void refresh() {
		menuGroups.clear();
	}

	public void addMenuGroup(MenuGroup gourp) {
		menuGroups.add(gourp);
	}

	public void addMenuGroup(int index, MenuGroup gourp) {
		menuGroups.add(index, gourp);
	}

	public void removeMenuGroupById(String id) {
		if (StringUtils.isBlank(id)) {
			return;
		}

		MenuGroup deleteGroup = null;
		for (MenuGroup menuGroup : menuGroups) {
			if (id.equals(menuGroup.getId())) {
				deleteGroup = menuGroup;
				break;
			}
		}
		if (deleteGroup != null) {
			menuGroups.remove(deleteGroup);
		}
	}

	public MenuGroup getMenuGroupById(String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		for (MenuGroup menuGroup : menuGroups) {
			if (id.equals(menuGroup.getId())) {
				return menuGroup;
			}
		}
		return null;
	}

	public LinkedList<MenuGroup> getMenuGroups() {
		return menuGroups;
	}

}
