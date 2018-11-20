package it.lsoft.rest.menu;

import java.util.ArrayList;

public class ApplicationMenu {
	String caption;
	String restCall;
	ArrayList<ApplicationMenu> subMenu;
	
	public ApplicationMenu(String caption, String restCall) {
		this.caption = caption;
	}
}
