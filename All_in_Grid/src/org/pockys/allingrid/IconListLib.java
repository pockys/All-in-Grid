package org.pockys.allingrid;

import java.util.ArrayList;

public enum IconListLib {
	INSTANCE;

	private ArrayList<IconInfo> ListView = new ArrayList<IconInfo>();
	
	private ArrayList<IconCategoryInfo> IconCategoryInfoList = new ArrayList<IconCategoryInfo>();


	private int CurrentCategoy = 0;
	
	private int currentsort = 0;
	
	private int CurrentType = 0;
	
	private IconListLib() {
		
		//////////////CategoryInfo
		
		IconCategoryInfoList.add(new IconCategoryInfo(29, 0));
		
		IconCategoryInfoList.add(new IconCategoryInfo(10, 6));
		
		IconCategoryInfoList.add(new IconCategoryInfo(7, 22));
		
		IconCategoryInfoList.add(new IconCategoryInfo(4, 17));
		
		IconCategoryInfoList.add(new IconCategoryInfo(0, 7));
		
		
		
		
		 /////////////List
		ListView.add(new IconInfo(R.drawable.ic_char_kinoko001,
				"char_kinoko001"));

		ListView.add(new IconInfo(R.drawable.ic_char_strange001,
				"char_strange001"));

		ListView.add(new IconInfo(R.drawable.ic_char_mono001, "char_mono001"));

		//ListView.add(new IconInfo(R.drawable.ic_char_face001,
			//	"char_tizukigou001"));


	}
	public IconCategoryInfo getIconCategoryInfo(int position) {
		return IconCategoryInfoList.get(position);
	}

	public IconInfo getListViewIconInfo(int position) {
		return ListView.get(position);
	}

	public int getListViewIconInfoSize() {
		return ListView.size();
	}
	

	public int getCurrentCategoy() {
		return CurrentCategoy;
	}

	public void setCurrentCategoy(int cu) {
		CurrentCategoy = cu;
	}
	
	public int getCurrentSort() {
		return currentsort;
	}

	public void setCurrentSort(int cu) {
		currentsort = cu;
	}
	
	public int getCurrentType() {
		return CurrentType;
	}

	public void setCurrenType(int cu) {
		CurrentType = cu;
	}

}
