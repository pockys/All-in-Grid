package org.pockys.allingrid;

import java.util.ArrayList;
import java.util.Iterator;

public class MainMenu {

	private static final String TAG = "MainMenu";

	private ArrayList<CellInfo> mMenuList = new ArrayList<CellInfo>();

	private Iterator<CellInfo> it;

	public int getSize() {
		return mMenuList.size();
	}

	public MainMenu() {

		CellInfo sortCell = new CellInfo();
		sortCell.setDisplayName("Sort");
		mMenuList.add(sortCell);

		CellInfo editCell = new CellInfo();
		editCell.setDisplayName("Edit");
		mMenuList.add(editCell);

		CellInfo addCell = new CellInfo();
		addCell.setDisplayName("Add");
		mMenuList.add(addCell);

		CellInfo sizeCell = new CellInfo();
		sizeCell.setDisplayName("Size");
		mMenuList.add(sizeCell);

		CellInfo testCell = new CellInfo();
		testCell.setDisplayName("Test");
		mMenuList.add(testCell);

		it = mMenuList.iterator();

	}

	public ArrayList<CellInfo> getMenuList(int maxSize) {

		ArrayList<CellInfo> menuList = new ArrayList<CellInfo>();

		maxSize = Math.min(maxSize, mMenuList.size());
		for (int i = 0; i < maxSize && it.hasNext(); i++)
			menuList.add(it.next());

		return menuList;
	}

}
