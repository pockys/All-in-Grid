package org.pockys.allingrid;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.viewpagerindicator.CirclePageIndicator;

public class MenuController implements OnItemClickListener {

	private static final String TAG = "MenuController";
	public static final int BACKGROUND_COLOR = Color.rgb(255, 200, 102);

	public static final GroupCellInfo AllGroupCellInfo;
	public static final GroupCellInfo FavoriteGroupCellInfo;

	static {
		AllGroupCellInfo = new GroupCellInfo();
		AllGroupCellInfo.setDisplayName("All");
		AllGroupCellInfo.setThumbnail(R.drawable.ic_all);

		FavoriteGroupCellInfo = new GroupCellInfo();
		FavoriteGroupCellInfo.setDisplayName("Favorite");
		FavoriteGroupCellInfo.setThumbnail(R.drawable.ic_favorite);
	}

	private Context mContext;
	private Cursor mGroupTitleCursor;
	private OnItemClickListener mOnItemClickListener = this;

	public int getSize() {
		return mGroupTitleCursor.getCount();
	}

	public MenuController(Context context) {
		this(context, true);
	}

	public MenuController(Context context, boolean onlyNotEmpty) {
		mContext = context;
		mGroupTitleCursor = getGroupTitles(onlyNotEmpty);
	}

	private Cursor getGroupTitles(boolean onlyNotEmpty) {
		final String[] GROUP_PROJECTION = new String[] {
				ContactsContract.Groups._ID, ContactsContract.Groups.TITLE,

				ContactsContract.Groups.SUMMARY_COUNT };

		String selection = ContactsContract.Groups.TITLE
				+ " NOT LIKE 'Starred in Android'" + " AND "
				+ ContactsContract.Groups.ACCOUNT_TYPE + " LIKE 'com.google'";
		if (onlyNotEmpty) {
			selection += " AND " + ContactsContract.Groups.SUMMARY_COUNT
					+ " > 0";
		}

		Cursor cursor = mContext.getContentResolver().query(
				ContactsContract.Groups.CONTENT_SUMMARY_URI, GROUP_PROJECTION,
				selection, null, ContactsContract.Groups.TITLE + " ASC");

		return cursor;
	}

	public int getPagedCount(final int maxSize, GroupCellInfo groupInfo,
			boolean onlyNotEmpty) {

		String groupTitle = groupInfo.getDisplayName();
		int groupId = groupInfo.getGroupId();

		if (groupTitle.equals("All") || groupTitle.equals("Favorite")) {
			return 0;
		}

		Cursor cursor = getGroupTitles(onlyNotEmpty);
		for (int i = 0; cursor.moveToNext(); i++) {
			if (cursor.getString(
					cursor.getColumnIndex(ContactsContract.Groups._ID)).equals(
					String.valueOf(groupId)))
				return (i + 2) / maxSize;
		}

		return 0;
	}

	private int menuListCount = 0;

	private ArrayList<CellInfo> getMenuList(int maxSize) {

		ArrayList<CellInfo> menuList = new ArrayList<CellInfo>();

		if (menuListCount++ == 0) {
			menuList.add(AllGroupCellInfo);
			menuList.add(FavoriteGroupCellInfo);

			maxSize -= 2;
		}

		for (int i = 0; mGroupTitleCursor.moveToNext() && i < maxSize; i++) {

			String displayName = mGroupTitleCursor.getString(mGroupTitleCursor
					.getColumnIndex(ContactsContract.Groups.TITLE));
			String groupIdString = mGroupTitleCursor
					.getString(mGroupTitleCursor
							.getColumnIndex(ContactsContract.Groups._ID));

			GroupCellInfo group = new GroupCellInfo();
			group.setDisplayName(displayName);
			group.setThumbnail(R.drawable.ic_user_group);
			group.setGroupId(Integer.valueOf(groupIdString));
			menuList.add(group);
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
			menuView.setAdapter(new CellAdapter(mContext, getMenuList(numCells)));
			menuView.setOnItemClickListener(mOnItemClickListener);
			menuViewList.add(menuView);
		}
		if (this.getSize() % numCells != 0) {
			GridView menuView = (GridView) LayoutInflater.from(mContext)
					.inflate(R.layout.grid_view, null);
			menuView.setNumColumns(numColumns);
			menuView.setAdapter(new CellAdapter(mContext, getMenuList(numCells)));
			menuView.setOnItemClickListener(mOnItemClickListener);
			menuViewList.add(menuView);
		}

		mGroupTitleCursor.close();

		return menuViewList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

		ViewPager gridField = (ViewPager) ((Activity) mContext)
				.findViewById(R.id.grid_field);

		GroupCellInfo selectedGroupInfo = (GroupCellInfo) v.getTag();
		String selectedGroupTitle = selectedGroupInfo.getDisplayName();

		v.setBackgroundColor(BACKGROUND_COLOR);
		SelectedItemList.INSTANCE.setSelectedGroupInfo(selectedGroupInfo);
		MainActivity.reDrawMenuField();

		GroupCellInfo currentGroupInfo = MainActivity.getCurrentGroupInfo();
		Log.d(TAG, "current group: "
				+ MainActivity.getCurrentGroupInfo().getDisplayName()
				+ " current item : " + MainActivity.getCurrentItem());

		ActionBar actionBar = ((Activity) mContext).getActionBar();
		actionBar.setTitle(selectedGroupTitle);

		if (selectedGroupInfo.equals(MenuController.AllGroupCellInfo)) {
			actionBar.setDisplayHomeAsUpEnabled(false);
		} else {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		if (selectedGroupInfo.equals(currentGroupInfo)) {
			MainActivity.setCurrentItem(0);
			gridField.setCurrentItem(0);
		} else {

			MainActivity.saveCurrentItem();
			MainActivity.setCurrentGroupInfo(selectedGroupInfo);
			int currentItem = MainActivity.getCurrentItem();

			ContactController contactController = new ContactController(
					mContext, MainActivity.getSelection(selectedGroupInfo));

			gridField.setAdapter(new CellPagerAdapter(contactController
					.getGridFieldViews(4, 4)));
			gridField.setCurrentItem(currentItem);
			CirclePageIndicator circlePageIndicator = (CirclePageIndicator) ((Activity) mContext)
					.findViewById(R.id.circle_page_indicator_grid);
			circlePageIndicator.setViewPager(gridField);
			circlePageIndicator.setCurrentItem(currentItem);
		}

		Log.d(TAG, "clicked group: " + selectedGroupInfo.getDisplayName()
				+ " current item: " + MainActivity.getCurrentItem());
	}

	public OnItemClickListener getOnItemClickListener() {
		return mOnItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
		this.mOnItemClickListener = mOnItemClickListener;
	}

}
