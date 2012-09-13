package org.pockys.allingrid;

import java.util.ArrayList;

import android.app.ActionBar;
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

import com.viewpagerindicator.CirclePageIndicator;

public class MenuController implements OnItemClickListener {

	private static final String TAG = "MainMenu";

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
				+ " NOT LIKE 'Starred in Android'";
		if (onlyNotEmpty) {
			selection += " AND " + ContactsContract.Groups.SUMMARY_COUNT
					+ " > 0";

		}

		Cursor cursor = mContext.getContentResolver().query(
				ContactsContract.Groups.CONTENT_SUMMARY_URI, GROUP_PROJECTION,
				selection, null, ContactsContract.Groups.TITLE + " ASC");

		return cursor;

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
			// String summaryCountString = mGroupTitleCursor
			// .getString(mGroupTitleCursor
			// .getColumnIndex(ContactsContract.Groups.SUMMARY_COUNT));

			GroupCellInfo group = new GroupCellInfo();
			group.setDisplayName(displayName);
			group.setThumbnail(R.drawable.ic_user_group);
			group.setGroupId(Integer.valueOf(groupIdString));
			menuList.add(group);
		}
		//
		// for (Iterator<CellInfo> it = menuList.iterator(); it.hasNext();) {
		// Log.d(TAG, "menuList: " + it.next().getDisplayName());
		// }
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
			menuView.setOnItemClickListener(getOnItemClickListener());
			menuViewList.add(menuView);
		}
		if (this.getSize() % numCells != 0) {
			GridView menuView = (GridView) LayoutInflater.from(mContext)
					.inflate(R.layout.grid_view, null);
			menuView.setNumColumns(numColumns);
			menuView.setAdapter(new CellAdapter(mContext, this
					.getMenuList(numCells)));
			menuView.setOnItemClickListener(getOnItemClickListener());
			menuViewList.add(menuView);
		}

		mGroupTitleCursor.close();

		return menuViewList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

		GroupCellInfo groupCellInfo = (GroupCellInfo) v.getTag();
		ViewPager gridField = (ViewPager) ((Activity) mContext)
				.findViewById(R.id.grid_field);

		String groupTitle = groupCellInfo.getDisplayName();
		((Activity) mContext).getActionBar().setTitle(groupTitle);

		ContactController contactController = null;
		String selection = null;
		if (groupTitle.equals("All")) {

		} else if (groupTitle.equals("Favorite")) {
			selection = ContactsContract.Contacts.STARRED + " = 1";
		} else {
			int groupId = groupCellInfo.getGroupId();
			selection = ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID
					+ " = " + groupId;

		}
		contactController = new ContactController(mContext, selection);

		Log.d(TAG, "current group: " + MainActivity.getCurrentGroupSelection()
				+ " current item : " + MainActivity.getGridFieldCurrentItem());

		ActionBar actionBar = ((Activity) mContext).getActionBar();

		int currentItem = 0;
		if (selection == null) {
			actionBar.setDisplayHomeAsUpEnabled(false);
			if (MainActivity.getCurrentGroupSelection() == null) {
				MainActivity.setGridFieldCurrentItem(0);
				gridField.setCurrentItem(0);
			} else {
				MainActivity.saveGridFieldCurrentItem();
				MainActivity.setCurrentGroupSelection(null);
				currentItem = MainActivity.getGridFieldCurrentItem(selection);

				gridField.setAdapter(new CellPagerAdapter(contactController
						.getGridFieldViews(4, 4)));
				gridField.setCurrentItem(currentItem);
				CirclePageIndicator circlePageIndicator = (CirclePageIndicator) ((Activity) mContext)
						.findViewById(R.id.circle_page_indicator_grid);
				circlePageIndicator.setViewPager(gridField);
				circlePageIndicator.setCurrentItem(currentItem);
			}
		} else {
			actionBar.setDisplayHomeAsUpEnabled(true);
			if (selection.equals(MainActivity.getCurrentGroupSelection())) {
				MainActivity.setGridFieldCurrentItem(0);
				gridField.setCurrentItem(0);
			} else {

				MainActivity.saveGridFieldCurrentItem();
				MainActivity.setCurrentGroupSelection(selection);
				currentItem = MainActivity.getGridFieldCurrentItem(selection);

				gridField.setAdapter(new CellPagerAdapter(contactController
						.getGridFieldViews(4, 4)));
				gridField.setCurrentItem(currentItem);
				CirclePageIndicator circlePageIndicator = (CirclePageIndicator) ((Activity) mContext)
						.findViewById(R.id.circle_page_indicator_grid);
				circlePageIndicator.setViewPager(gridField);
				circlePageIndicator.setCurrentItem(currentItem);
			}
		}
		Log.d(TAG, "clicked group: " + selection + " current item: "
				+ currentItem);

	}

	public OnItemClickListener getOnItemClickListener() {
		return mOnItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
		this.mOnItemClickListener = mOnItemClickListener;
	}

}
