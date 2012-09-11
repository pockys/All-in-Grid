package org.pockys.allingrid;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MenuController implements OnItemClickListener {

	private static final String TAG = "MainMenu";

	// private ArrayList<CellInfo> mMenuList = new ArrayList<CellInfo>();
	private Context mContext;
	private Cursor mGroupTitleCursor;

	public int getSize() {
		return mGroupTitleCursor.getCount();
	}

	public MenuController(Context context) {
		mContext = context;
		mGroupTitleCursor = getGroupTitles();
	}

	private Cursor getGroupTitles() {
		final String[] GROUP_PROJECTION = new String[] {
				ContactsContract.Groups._ID, ContactsContract.Groups.TITLE,
				ContactsContract.Groups.SUMMARY_COUNT };

		Cursor cursor = mContext.getContentResolver().query(
				ContactsContract.Groups.CONTENT_SUMMARY_URI, GROUP_PROJECTION,
				null, null, ContactsContract.Groups.TITLE + " ASC");

		return cursor;

	}

	private ArrayList<CellInfo> getMenuList(int maxSize) {

		ArrayList<CellInfo> menuList = new ArrayList<CellInfo>();

		for (int i = 0; mGroupTitleCursor.moveToNext() && i < maxSize; i++) {

			String displayName = mGroupTitleCursor.getString(mGroupTitleCursor
					.getColumnIndex(ContactsContract.Groups.TITLE));
			String groupIdString = mGroupTitleCursor
					.getString(mGroupTitleCursor
							.getColumnIndex(ContactsContract.Groups._ID));
			String summaryCountString = mGroupTitleCursor
					.getString(mGroupTitleCursor
							.getColumnIndex(ContactsContract.Groups.SUMMARY_COUNT));

			Log.d(TAG, "getMenuList. displayName: " + displayName
					+ " group id: " + groupIdString + " summary count: "
					+ summaryCountString);

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

	public ArrayList<GridView> getMenuFieldViews(final int numColumns) {
		ArrayList<GridView> menuViewList = new ArrayList<GridView>();
		final int numCells = numColumns;

		for (int i = 0; i < this.getSize() / numCells; i++) {
			GridView menuView = (GridView) LayoutInflater.from(mContext)
					.inflate(R.layout.grid_view, null);
			menuView.setNumColumns(numColumns);
			menuView.setAdapter(new CellAdapter(mContext, this
					.getMenuList(numCells)));
			menuView.setOnItemClickListener(this);
			menuViewList.add(menuView);
		}
		if (this.getSize() % numCells != 0) {
			GridView menuView = (GridView) LayoutInflater.from(mContext)
					.inflate(R.layout.grid_view, null);
			menuView.setNumColumns(numColumns);
			menuView.setAdapter(new CellAdapter(mContext, this
					.getMenuList(numCells)));
			menuView.setOnItemClickListener(this);
			menuViewList.add(menuView);
		}

		return menuViewList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		GroupCellInfo groupCellInfo = (GroupCellInfo) v.getTag();
		MainActivity.makeToast(mContext, groupCellInfo.getDisplayName());

//		String groupTitle = groupCellInfo.getDisplayName();
		int groupId = groupCellInfo.getGroupId();

//		ViewPager gridField = (ViewPager) ((Activity) mContext)
//				.findViewById(R.id.grid_field);
//		ContactController contactController = new ContactController(mContext,
//				ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID
//						+ " = " + groupId);
//		gridField.setAdapter(new CellPagerAdapter(contactController
//				.getGridFieldViews(4, 4)));

	}

}
