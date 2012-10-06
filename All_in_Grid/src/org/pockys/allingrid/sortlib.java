package org.pockys.allingrid;

public enum sortlib {
	INSTANCE;
	
	private int currentsort = 0;
	
	public int getCurrentSort() {
		return currentsort;
	}

	public void setCurrentSort(int cu) {
		currentsort = cu;
	}

}
