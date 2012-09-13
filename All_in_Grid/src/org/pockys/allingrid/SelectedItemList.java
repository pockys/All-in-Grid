package org.pockys.allingrid;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;

public enum SelectedItemList {
	INSTANCE;

	private GroupCellInfo mSelectedGroupInfo = MenuController.AllGroupCellInfo;

	private ArrayList<Integer> mSelectedContactIdList = new ArrayList<Integer>();

	public boolean contain(int contactId) {
		return mSelectedContactIdList.contains(contactId);
	}

	public int getSize() {
		return mSelectedContactIdList.size();
	}

	public Iterator<Integer> getIterator() {
		return mSelectedContactIdList.iterator();
	}

	public boolean remove(int contactId) {
		return mSelectedContactIdList.remove(Integer.valueOf(contactId));
	}

	public void add(int contactId) {
		mSelectedContactIdList.add(contactId);
	}

	public void clear() {
		mSelectedContactIdList.clear();

		Log.d("SelectedItemList", "mSelected ContactId List clear");
	}

	public void logContactIdList(String TAG) {
		String list = "";
		for (int i = 0; i < mSelectedContactIdList.size(); i++) {
			list += " " + mSelectedContactIdList.get(i);
		}
		Log.d(TAG, "mSelected ContactId List\n" + list);

	}
 
	public GroupCellInfo getSelectedGroupInfo() {
		return mSelectedGroupInfo;
	}

	public void setSelectedGroupInfo(GroupCellInfo mSelectedGroupInfo) {
		this.mSelectedGroupInfo = mSelectedGroupInfo;
	}

}
