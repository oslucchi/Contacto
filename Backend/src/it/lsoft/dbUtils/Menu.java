package it.lsoft.dbUtils;

import java.util.ArrayList;

public class Menu extends DBInterface
{
	private static final long serialVersionUID = 3734196270916511332L;

	protected int idMenu;
	protected int idParent;
	protected int level;
	protected int roleConstraintView;
	protected int roleConstraintRead;
	protected int roleConstraintWrite;
	protected String caption;
	protected String restCall;
	protected ArrayList<Menu> subMenu;
		
	private void setNames()
	{
		tableName = "Menu";
		idColName = "idMenu";
	}

	public Menu()
	{
		setNames();
	}
	
	public Menu(String language, int idRole) throws Exception
	{
		setNames();
	}

	private void findSubMenu(ArrayList<Menu> menuOnDB, Menu root, int idParent, int level, int idRole)
	{
		int i = 0;
		Menu menuItem = null;
		while(menuOnDB.get(i).level < level)
		{
			i++;
			if (i == menuOnDB.size())
				return;
		}
		while(idParent != menuOnDB.get(i).idParent)
		{
			i++;
			if (i == menuOnDB.size())
				return;
		}
		while((i < menuOnDB.size()) && (level == menuOnDB.get(i).level))
		{			
			menuItem = menuOnDB.get(i);
			i++;
			if (root.subMenu == null)
			{
				root.subMenu = new ArrayList<>();
			}
			root.subMenu.add(menuItem);
			findSubMenu(menuOnDB, menuItem, menuItem.idMenu, menuItem.level + 1, idRole);
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public Menu getMenu(int idRole, String language) throws Exception
	{
		ArrayList<Menu> menuOnDB;
		Menu root = new Menu();
		String sql = 
				"SELECT a.idMenu, idParent, level, roleConstraintView, " + 
				"	   roleConstraintRead, roleConstraintWrite, caption, restCall " +
				"FROM Menu a INNER JOIN MenuCaption b ON ( " +
				"       a.idMenu = b.idMenu AND " +
				"	    b.idLanguage = '" + language + "' AND " +
				"      " + idRole + " >= roleConstraintView " +
				"     ) " +
				"WHERE roleConstraintView >= 0 " +
				"ORDER BY level, idParent";
		DBConnection conn = null;
		try
		{
			conn = DBInterface.connect();
			menuOnDB = (ArrayList<Menu>) DBInterface.populateCollection(conn, sql, Menu.class);
			root = new Menu();
			root.idMenu = 1;
			root.idParent = 1;
			root.level = 0;
			root.caption = "root";
		}
		catch(Exception e)
		{
			throw(e);
		}
		finally
		{
			DBInterface.disconnect(conn);
		}
		findSubMenu(menuOnDB, root, 1, 1, idRole);
		return root;
	}

	public int getIdMenu() {
		return idMenu;
	}

	public void setIdMenu(int idMenu) {
		this.idMenu = idMenu;
	}

	public int getIdParent() {
		return idParent;
	}

	public void setIdParent(int idParent) {
		this.idParent = idParent;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getRestCall() {
		return restCall;
	}

	public void setRestCall(String restCall) {
		this.restCall = restCall;
	}

	public ArrayList<Menu> getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(ArrayList<Menu> subMenu) {
		this.subMenu = subMenu;
	}
	
}