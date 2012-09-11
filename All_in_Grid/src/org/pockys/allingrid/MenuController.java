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

		String selection = ContactsContract.Groups.SUMMARY_COUNT + " > 0 AND "
				+ ContactsContract.Groups.TITLE
				+ " NOT LIKE 'Starred in Android'";

		Cursor cursor = mContext.getContentResolver().query(
				ContactsContract.Groups.CONTENT_SUMMARY_URI, GROUP_PROJECTION,
				selection, null, ContactsContract.Groups.TITLE + " ASC");

		return cursor;

	}

	private int menuListCount = 0;

	private ArrayList<CellInfo> getMenuList(int maxSize) {

		ArrayList<CellInfo> menuList = new ArrayList<CellInfo>();

		if (menuListCount++ == 0) {
			GroupCellInfo group = new GroupCellInfo();
			group.setDisplayName("All");
			group.setThumbnail(R.drawable.ic_all);
			menuList.add(group);

			group = new GroupCellInfo();
			group.setDisplayName("Favorite");
			group.setThumbnail(R.drawable.ic_favorite);
			menuList.add(group);

			maxSize -= 2;
		}

		for (int i = 0; mGroupTitleCursor.moveToNext() && i < maxSize; i++) {

			String displayName = mGroupTitleCursor.getString(mGroupTitleCursor
					.getColumnIndex(ContactsContract.Groups.TITLE));
			String groupIdString = mGroupTitleCursor
					.getString(mGroupTitleCursor
							.getColumnIndex(ContactsContract.Groups._ID));
			// String summaryCountString = mGroupTitleCursor
			// .getString(mGroupTitleCursor
			// .getColumnIndex(ContactsContract.Groups.SUMMARY_COUNT));

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
		ViewPager gridField = (ViewPager) ((Activity) mContext)
				.findViewById(R.id.grid_field);
		ContactController contactController = null;

		String groupTitle = groupCellInfo.getDisplayName();
		if (groupTitle.equals("All")) {
			contactController = new ContactController(mContext, null);
		} else if (groupTitle.equals("Favorite")) {
			contactController = new ContactController(mContext,
					ContactsContract.Contacts.STARRED + " = 1");
		} else {
			int groupId = groupCellInfo.getGroupId();

			contactController = new ContactController(
					mContext,
					ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID
							+ " = " + groupId);

		}

		Log.d(TAG, "current group: " + MainActivity.getCurrentGroupTitle()
				+ " current item : " + MainActivity.getGridFieldCurrentItem());

		int currentItem = 0;
		if (!groupTitle.equals(MainActivity.getCurrentGroupTitle())) {
			MainActivity.saveGridFieldCurrentItem();
			MainActivity.setCurrentGroupTitle(groupTitle);
			currentItem = MainActivity.getGridFieldCurrentItem(groupTitle);

			Log.d(TAG, "clicked group: " + groupTitle + " current item: "
					+ currentItem);
		}

		gridField.setAdapter(new CellPagerAdapter(contactController
				.getGridFieldViews(4, 4)));
		gridField.setCurrentItem(currentItem);

	}

}
