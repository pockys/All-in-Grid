package org.pockys.allingrid;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MainMenu implements OnItemClickListener {

	private static final String TAG = "MainMenu";

	// private ArrayList<CellInfo> mMenuList = new ArrayList<CellInfo>();
	private Context mContext;
	private Cursor mGroupTitleCursor;

	public int getSize() {
		return mGroupTitleCursor.getCount();
	}

	public MainMenu(Context context) {
		mContext = context;

		mGroupTitleCursor = getGroupTitles();
	}

	private Cursor getGroupTitles() {
		final String[] GROUP_PROJECTION = new String[] {
				ContactsContract.Groups._ID, ContactsContract.Groups.TITLE };

		Cursor cursor = mContext.getContentResolver().query(
				ContactsContract.Groups.CONTENT_URI, GROUP_PROJECTION, null,
				null, ContactsContract.Groups.TITLE + " ASC");

		return cursor;

	}

	public ArrayList<CellInfo> getMenuList(int maxSize) {

		ArrayList<CellInfo> menuList = new ArrayList<CellInfo>();

		for (int i = 0; mGroupTitleCursor.moveToNext() && i < maxSize; i++) {

			String displayName = mGroupTitleCursor.getString(mGroupTitleCursor
					.getColumnIndex(ContactsContract.Groups.TITLE));
			String groupIdString = mGroupTitleCursor
					.getString(mGroupTitleCursor
							.getColumnIndex(ContactsContract.Groups._ID));

			Log.d(TAG, "getMenuList. displayName: " + displayName);

			GroupCellInfo group = new GroupCellInfo();
			group.setDisplayName(displayName);
			group.setThumbnail(R.drawable.ic_user_group);
			group.setGroupId(Integer.valueOf(groupIdString));
			menuList.add(group);
		}

		for (Iterator<CellInfo> it = menuList.iterator(); it.hasNext();) {
			Log.d(TAG, "menuList: " + it.next().getDisplayName());
		}
		return menuList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

	}

}
