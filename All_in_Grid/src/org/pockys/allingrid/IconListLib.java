package org.pockys.allingrid;

import java.util.ArrayList;

public enum IconListLib {
	INSTANCE;

	private ArrayList<IconInfo> data = new ArrayList<IconInfo>();
	private ArrayList<IconInfo> kinokodata = new ArrayList<IconInfo>();
	
	private ArrayList<IconInfo> strange = new ArrayList<IconInfo>();
	
	private ArrayList<IconInfo> mono = new ArrayList<IconInfo>();

	private ArrayList<IconInfo> tizukigou = new ArrayList<IconInfo>();

	
	private ArrayList<IconInfo> ListView = new ArrayList<IconInfo>();

	
	int CurrentCategoy = 0;

	private IconListLib() {
//		data.add(new IconInfo(R.drawable.ic_gotouti_001, "gotouti_001",
//				"gotouti_001", 1));
//		data.add(new IconInfo(R.drawable.ic_gotouti_002, "gotouti_002",
//				"gotouti_002", 2));
//		data.add(new IconInfo(R.drawable.ic_gotouti_005, "gotouti_005",
//				"gotouti_005", 5));
//		data.add(new IconInfo(R.drawable.ic_gotouti_006, "gotouti_006",
//				"gotouti_006", 6));
//		data.add(new IconInfo(R.drawable.ic_gotouti_007, "gotouti_007",
//				"gotouti_006", 7));
//		data.add(new IconInfo(R.drawable.ic_gotouti_008, "gotouti_008",
//				"gotouti_008", 8));
		data.add(new IconInfo(R.drawable.ic_char_face001, "char_face001",
				"char_face001", 9));
		data.add(new IconInfo(R.drawable.ic_char_face002, "char_face002",
				"char_face002", 10));
		data.add(new IconInfo(R.drawable.ic_char_face003, "char_face003",
				"char_face003", 11));
		data.add(new IconInfo(R.drawable.ic_char_face004, "char_face004",
				"char_face004", 12));
		data.add(new IconInfo(R.drawable.ic_char_face005, "char_face005",
				"char_face005", 13));
		data.add(new IconInfo(R.drawable.ic_char_face006, "char_face006",
				"char_face006", 14));
		data.add(new IconInfo(R.drawable.ic_char_kinoko001, "char_kinoko001",
				"char_kinoko001", 15));
		data.add(new IconInfo(R.drawable.ic_char_kinoko002, "char_kinoko002",
				"char_kinoko002", 16));
		data.add(new IconInfo(R.drawable.ic_char_kinoko003, "char_kinoko003",
				"char_kinoko003", 17));
		data.add(new IconInfo(R.drawable.ic_char_kinoko004, "char_kinoko004",
				"char_kinoko004", 18));
		data.add(new IconInfo(R.drawable.ic_char_kinoko005, "char_kinoko005",
				"char_kinoko005", 19));
		data.add(new IconInfo(R.drawable.ic_char_kinoko006, "char_kinoko006",
				"char_kinoko006", 20));
		data.add(new IconInfo(R.drawable.ic_char_kinoko007, "char_kinoko007",
				"char_kinoko007", 21));
		data.add(new IconInfo(R.drawable.ic_char_kinoko008, "char_kinoko008",
				"char_kinoko008", 22));
		data.add(new IconInfo(R.drawable.ic_char_kinoko009, "char_kinoko009",
				"char_kinoko009", 23));
		data.add(new IconInfo(R.drawable.ic_char_kinoko010, "char_kinoko010",
				"char_kinoko001", 24));
		data.add(new IconInfo(R.drawable.ic_char_kinoko011, "char_kinoko011",
				"char_kinoko011", 25));
		data.add(new IconInfo(R.drawable.ic_char_mono001, "char_mono001",
				"char_mono001", 26));
		data.add(new IconInfo(R.drawable.ic_char_mono002, "char_mono002",
				"char_mono002", 27));
		data.add(new IconInfo(R.drawable.ic_char_mono003, "char_mono003",
				"char_mono003", 28));
		data.add(new IconInfo(R.drawable.ic_char_mono004, "char_mono004",
				"char_mono004", 29));
		data.add(new IconInfo(R.drawable.ic_char_mono005, "char_mono004",
				"char_mono004", 30));
		data.add(new IconInfo(R.drawable.ic_char_strange001, "char_strange001",
				"char_strange001", 31));
		data.add(new IconInfo(R.drawable.ic_char_strange002, "char_strange002",
				"char_strange002", 32));
		data.add(new IconInfo(R.drawable.ic_char_strange003, "char_strange003",
				"char_strange001", 33));
		data.add(new IconInfo(R.drawable.ic_char_strange004, "char_strange004",
				"char_strange004", 34));
		data.add(new IconInfo(R.drawable.ic_char_strange005, "char_strange005",
				"char_strange005", 35));
		data.add(new IconInfo(R.drawable.ic_char_strange006, "char_strange006",
				"char_strange006", 36));
		data.add(new IconInfo(R.drawable.ic_char_strange007, "char_strange007",
				"char_strange007", 37));
		data.add(new IconInfo(R.drawable.ic_char_strange008, "char_strange008",
				"char_strange008", 38));


		
		//////kinoko
		kinokodata.add(new IconInfo(R.drawable.ic_char_kinoko001, "char_kinoko001",
				"char_kinoko001", 15));
		kinokodata.add(new IconInfo(R.drawable.ic_char_kinoko002, "char_kinoko002",
				"char_kinoko002", 16));
		kinokodata.add(new IconInfo(R.drawable.ic_char_kinoko003, "char_kinoko003",
				"char_kinoko003", 17));
		kinokodata.add(new IconInfo(R.drawable.ic_char_kinoko004, "char_kinoko004",
				"char_kinoko004", 18));
		kinokodata.add(new IconInfo(R.drawable.ic_char_kinoko005, "char_kinoko005",
				"char_kinoko005", 19));
		kinokodata.add(new IconInfo(R.drawable.ic_char_kinoko006, "char_kinoko006",
				"char_kinoko006", 20));
		kinokodata.add(new IconInfo(R.drawable.ic_char_kinoko007, "char_kinoko007",
				"char_kinoko007", 21));
		kinokodata.add(new IconInfo(R.drawable.ic_char_kinoko008, "char_kinoko008",
				"char_kinoko008", 22));
		kinokodata.add(new IconInfo(R.drawable.ic_char_kinoko009, "char_kinoko009",
				"char_kinoko009", 23));
		kinokodata.add(new IconInfo(R.drawable.ic_char_kinoko010, "char_kinoko010",
				"char_kinoko001", 24));
		kinokodata.add(new IconInfo(R.drawable.ic_char_kinoko011, "char_kinoko011",
				"char_kinoko011", 25));

		
		///////strange
		
		strange.add(new IconInfo(R.drawable.ic_char_strange001, "char_strange001",
				"char_strange001", 31));
		strange.add(new IconInfo(R.drawable.ic_char_strange002, "char_strange002",
				"char_strange002", 32));
		strange.add(new IconInfo(R.drawable.ic_char_strange003, "char_strange003",
				"char_strange001", 33));
		strange.add(new IconInfo(R.drawable.ic_char_strange004, "char_strange004",
				"char_strange004", 34));
		strange.add(new IconInfo(R.drawable.ic_char_strange005, "char_strange005",
				"char_strange005", 35));
		strange.add(new IconInfo(R.drawable.ic_char_strange006, "char_strange006",
				"char_strange006", 36));
		strange.add(new IconInfo(R.drawable.ic_char_strange007, "char_strange007",
				"char_strange007", 37));
		strange.add(new IconInfo(R.drawable.ic_char_strange008, "char_strange008",
				"char_strange008", 38));
		
		
		//////mono
		
		mono.add(new IconInfo(R.drawable.ic_char_mono001, "char_mono001",
				"char_mono001", 26));
		mono.add(new IconInfo(R.drawable.ic_char_mono002, "char_mono002",
				"char_mono002", 27));
		mono.add(new IconInfo(R.drawable.ic_char_mono003, "char_mono003",
				"char_mono003", 28));
		mono.add(new IconInfo(R.drawable.ic_char_mono004, "char_mono004",
				"char_mono004", 29));
		mono.add(new IconInfo(R.drawable.ic_char_mono005, "char_mono004",
				"char_mono004", 30));
		

		/////////////////////////////tizukigou
		
		
		tizukigou.add(new IconInfo(R.drawable.ic_char_tizukigou001, "char_tizukigou001",
				"char_tizukigou001", 1));

		tizukigou.add(new IconInfo(R.drawable.ic_char_tizukigou002, "char_tizukigou002",
				"char_tizukigou002", 2));
		
		tizukigou.add(new IconInfo(R.drawable.ic_char_tizukigou003, "char_tizukigou003",
				"char_tizukigou003", 3));


		
		///////////////List
		ListView.add(new IconInfo(R.drawable.ic_char_kinoko001, "char_kinoko001",
				"char_kinoko001", 15));

		
		ListView.add(new IconInfo(R.drawable.ic_char_strange001, "char_strange001",
				"char_strange001", 31));

		ListView.add(new IconInfo(R.drawable.ic_char_mono001, "char_mono001",
				"char_mono001", 26));

		
		ListView.add(new IconInfo(R.drawable.ic_char_tizukigou001, "char_tizukigou001",
				"char_tizukigou001", 1));

	}
	
	public IconInfo getAllIconInfo(int position) {
		return data.get(position);
	}
	
	public int getAllIconInfoSize(){
		return data.size();
	}
	
	public IconInfo getstrangeIconInfo(int position) {
		return strange.get(position);
	}
	
	public int getstrangeIconInfoSize(){
		return strange.size();
	}

	public IconInfo getkinokoIconInfo(int position) {
		return kinokodata.get(position);
	}
	
	public int getkinokoIconInfoSize(){
		return kinokodata.size();
	}

	public IconInfo getmonoIconInfo(int position) {
		return mono.get(position);
	}
	
	public int getmonoIconInfoSize(){
		return mono.size();
	}


	public IconInfo getListViewIconInfo(int position) {
		return ListView.get(position);
	}
	
	public int getListViewIconInfoSize(){
		return ListView.size();
	}

	public IconInfo gettizukigouViewIconInfo(int position) {
		return tizukigou.get(position);
	}
	
	public int gettizukigouViewIconInfoSize(){
		return tizukigou.size();
	}
	
	
	ArrayList<IconInfo> back_lib() {
		return data;
	}

	public int getCurrentCategoy(){
		return CurrentCategoy;
	}
	public void setCurrentCategoy(int cu ){
		CurrentCategoy = cu;
	}
	void dataclear() {
		data.clear();
	}

}
