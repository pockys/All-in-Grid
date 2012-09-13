package org.pockys.allingrid;

import java.util.ArrayList;

public enum IconListLib {
	INSTANCE;

	private ArrayList<IconInfo> data = new ArrayList<IconInfo>();

	private IconListLib() {
		data.add(new IconInfo(R.drawable.ic_gotouti_001, "gotouti_001",
				"gotouti_001", 1));
		data.add(new IconInfo(R.drawable.ic_gotouti_002, "gotouti_002",
				"gotouti_002", 2));
		data.add(new IconInfo(R.drawable.ic_gotouti_003, "gotouti_003",
				"gotouti_003", 3));
		data.add(new IconInfo(R.drawable.ic_gotouti_004, "gotouti_004",
				"gotouti_004", 4));
		data.add(new IconInfo(R.drawable.ic_gotouti_005, "gotouti_005",
				"gotouti_005", 5));
		data.add(new IconInfo(R.drawable.ic_gotouti_006, "gotouti_006",
				"gotouti_006", 6));
		data.add(new IconInfo(R.drawable.ic_gotouti_007, "gotouti_007",
				"gotouti_006", 7));
		data.add(new IconInfo(R.drawable.ic_gotouti_008, "gotouti_008",
				"gotouti_008", 8));
		data.add(new IconInfo(R.drawable.ic_tizukigou_001, "tizukigou_001",
				"tizukigou_001", 9));
		data.add(new IconInfo(R.drawable.ic_tizukigou_002, "tizukigou_002",
				"tizukigou_002", 10));
		data.add(new IconInfo(R.drawable.ic_tizukigou_003, "tizukigou_003",
				"tizukigou_003", 11));
		data.add(new IconInfo(R.drawable.ic_tizukigou_004, "tizukigou_004",
				"tizukigou_004", 12));
	}
	
	public IconInfo getIconInfo(int position) {
		return data.get(position);
	}
	
	public int getIconInfoSize(){
		return data.size();
	}

	ArrayList<IconInfo> back_lib() {
		return data;
	}

	void dataclear() {
		data.clear();
	}

}
