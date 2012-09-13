package org.pockys.allingrid;

import java.util.Hashtable;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.LinePageIndicator;

public class MainActivity extends Activity {

	static final String TAG = "MainActivity";
	private static ViewPager mGridField;
	private static ViewPager mMenuField;

	private ActionBar mActionBar;

	private int menuFieldCurrentItem = 0;

	private MenuController mMenuController;
	private ContactController mContactController;

	private CirclePageIndicator mCirclePageIndicator;
	private LinePageIndicator mLinePageIndicator;

	private static GroupCellInfo currentGroupInfo = MenuController.AllGroupCellInfo;
	private static Hashtable<GroupCellInfo, Integer> currentItemTable = new Hashtable<GroupCellInfo, Integer>();

	public static Hashtable<GroupCellInfo, Integer> cloneCurrentItemTable() {
		return (Hashtable<GroupCellInfo, Integer>) currentItemTable.clone();
	}

	public static int getCurrentItem() {
		return getCurrentItem(currentGroupInfo);
	}

	public static int getCurrentItem(GroupCellInfo groupInfo) {
		Integer value = currentItemTable.get(groupInfo);
		return (value == null) ? 0 : value;
	}

	public static void saveCurrentItem() {
		setCurrentItem(mGridField.getCurrentItem());
	}

	public static void setCurrentItem(int currentItem) {
		setCurrentItem(getCurrentGroupInfo(), currentItem);
	}

	public static void setCurrentItem(GroupCellInfo groupInfo, int currentItem) {
		currentItemTable.put(groupInfo, currentItem);
	}

	public static String getSelection(GroupCellInfo groupInfo) {
		String groupTitle = groupInfo.getDisplayName();

		if (groupTitle.equals("All")) {
			return null;
		} else if (groupTitle.equals("Favorite")) {
			return ContactsContract.Contacts.STARRED + " = 1";
		} else {
			return ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID
					+ " = " + groupInfo.getGroupId();
		}
	}

	public static GroupCellInfo getCurrentGroupInfo() {
		return currentGroupInfo;
	}

	public static void setCurrentGroupInfo(GroupCellInfo currentGroupInfo) {
		MainActivity.currentGroupInfo = currentGroupInfo;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(false);
		mActionBar.show();

		mGridField = (ViewPager) findViewById(R.id.grid_field);
		mMenuField = (ViewPager) findViewById(R.id.menu_field);

	}

	public void onStart() {
		super.onStart();

		mMenuController = new MenuController(this);
		mContactController = new ContactController(this,
				getSelection(getCurrentGroupInfo()));

		mGridField.setAdapter(new CellPagerAdapter(mContactController
				.getGridFieldViews(4, 4)));

		mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator_grid);
		mCirclePageIndicator.setViewPager(mGridField);

		mMenuField.setAdapter(new CellPagerAdapter(mMenuController
				.getMenuFieldViews(4)));

		mLinePageIndicator = (LinePageIndicator) findViewById(R.id.line_page_indicator_menu);
		mLinePageIndicator.setViewPager(mMenuField);
	}

	public void onResume() {
		super.onResume();

		mGridField.setCurrentItem(getCurrentItem());
		mCirclePageIndicator.setCurrentItem(getCurrentItem());
		mMenuField.setCurrentItem(menuFieldCurrentItem);
		mLinePageIndicator.setCurrentItem(menuFieldCurrentItem);

		Log.d(TAG, "onResume: gridField currentGroup: "
				+ getCurrentGroupInfo().getDisplayName() + " currentItem: "
				+ getCurrentItem());

		SelectedItemList.INSTANCE.setSelectedGroupInfo(currentGroupInfo);

	}

	public void onPause() {
		super.onPause();
		saveCurrentItem();
		menuFieldCurrentItem = mMenuField.getCurrentItem();

		Log.d(TAG,
				"onPause: gridField currentGroup: "
						+ currentGroupInfo.getDisplayName()
						+ " gridField currentItem: "
						+ mGridField.getCurrentItem());

	}

	@Override
	public void onBackPressed() {
		Log.d(TAG,
				"onBackPressed Called. gridField currentItem: "
						+ mGridField.getCurrentItem());

		String currentGroupTitle = currentGroupInfo.getDisplayName();

		if (currentGroupTitle == "All" && mGridField.getCurrentItem() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Do you want to quit?");
			builder.setPositiveButton("Yes", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			builder.setNegativeButton("No", null);
			builder.create().show();

		} else if (currentGroupTitle != "All"
				&& mGridField.getCurrentItem() == 0) {
			setCurrentGroupInfo(MenuController.AllGroupCellInfo);
			SelectedItemList.INSTANCE
					.setSelectedGroupInfo(MenuController.AllGroupCellInfo);
			reDrawMenuField();

			mActionBar.setDisplayHomeAsUpEnabled(false);
			mActionBar.setTitle("All");

			mContactController = new ContactController(this, null);
			mGridField.setAdapter(new CellPagerAdapter(mContactController
					.getGridFieldViews(4, 4)));
			mGridField.setCurrentItem(getCurrentItem());

			mCirclePageIndicator.setViewPager(mGridField);
			mCirclePageIndicator.setCurrentItem(getCurrentItem());

		} else {

			setCurrentItem(0);
			mGridField.setCurrentItem(0);
		}

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// makeToast(this, item.getTitle().toString());

		Intent intent;
		ContactController contactController;
		switch (item.getItemId()) {
		case android.R.id.home:

			ActionBar actionBar = this.getActionBar();
			actionBar.setTitle("All");
			actionBar.setDisplayHomeAsUpEnabled(false);

			saveCurrentItem();
			setCurrentGroupInfo(MenuController.AllGroupCellInfo);
			int currentItem = getCurrentItem();

			contactController = new ContactController(this, null);

			mGridField.setAdapter(new CellPagerAdapter(contactController
					.getGridFieldViews(4, 4)));
			mGridField.setCurrentItem(currentItem);
			CirclePageIndicator circlePageIndicator = (CirclePageIndicator) this
					.findViewById(R.id.circle_page_indicator_grid);
			circlePageIndicator.setViewPager(mGridField);
			circlePageIndicator.setCurrentItem(currentItem);

			break;
		case R.id.menu_phone:
			intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
			startActivity(intent);
			break;
		case R.id.menu_edit:
			intent = new Intent(this, EditActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_add:
			intent = new Intent(Intent.ACTION_INSERT);

			String packageName = "com.android.contacts";
			String className = ".activities.PeopleActivity";
			intent.setComponent(new ComponentName(packageName, packageName
					+ className));
			startActivity(intent);
			break;
		case R.id.menu_shuffle_icons:

			SharedPreferences.Editor editor = getSharedPreferences(
					"sharePreferences", Context.MODE_PRIVATE).edit();
			editor.clear();
			editor.commit();

			MenuController menuController = new MenuController(this);
			contactController = new ContactController(this);

			ViewPager gridField = (ViewPager) this
					.findViewById(R.id.grid_field);
			gridField.setAdapter(new CellPagerAdapter(contactController
					.getGridFieldViews(4, 4)));

			ViewPager menuField = (ViewPager) this
					.findViewById(R.id.menu_field);
			menuField.setAdapter(new CellPagerAdapter(menuController
					.getMenuFieldViews(4)));

			MainActivity.makeToast(this, "All icons are shuffled!!");

			break;
		}

		return true;
	}

	public static void makeToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void reDrawGridField() {
		for (int i = 0; i < mGridField.getChildCount(); i++) {
			GridView currentGridView = (GridView) mGridField.getChildAt(i);
			BaseAdapter adapter = ((BaseAdapter) currentGridView.getAdapter());
			adapter.notifyDataSetChanged();

		}
	}

	public static void reDrawMenuField() {
		for (int i = 0; i < mMenuField.getChildCount(); i++) {
			GridView currentGridView = (GridView) mMenuField.getChildAt(i);
			BaseAdapter adapter = ((BaseAdapter) currentGridView.getAdapter());
			adapter.notifyDataSetChanged();
		}
	}

}
