package org.pockys.allingrid;

import java.util.Hashtable;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.LinePageIndicator;

public class MainActivity extends Activity {

	static final String TAG = "MainActivity";
	private static ViewPager mGridField;
	private ViewPager mMenuField;

	private static Hashtable<String, Integer> gridFieldcurrentItemByGroup = new Hashtable<String, Integer>();
	private static String currentGroupSelection = null;
	private static int gridFieldCurrentItem = 0;
	private int menuFieldCurrentItem = 0;

	private MenuController mMenuController;
	private ContactController mContactController;

	private CirclePageIndicator mCirclePageIndicator;
	private LinePageIndicator mLinePageIndicator;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.show();

	}

	public void onResume() {
		super.onResume();

		mMenuController = new MenuController(this);
		mContactController = new ContactController(this, currentGroupSelection);

		mGridField = (ViewPager) findViewById(R.id.grid_field);
		mGridField.setAdapter(new CellPagerAdapter(mContactController
				.getGridFieldViews(4, 4)));
		mGridField.setCurrentItem(getGridFieldCurrentItem());

		mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator_grid);
		mCirclePageIndicator.setViewPager(mGridField);
		mCirclePageIndicator.setCurrentItem(getGridFieldCurrentItem());

		mMenuField = (ViewPager) findViewById(R.id.menu_field);
		mMenuField.setAdapter(new CellPagerAdapter(mMenuController
				.getMenuFieldViews(4)));
		mMenuField.setCurrentItem(menuFieldCurrentItem);

		mLinePageIndicator = (LinePageIndicator) findViewById(R.id.line_page_indicator_menu);
		mLinePageIndicator.setViewPager(mMenuField);
		mLinePageIndicator.setCurrentItem(menuFieldCurrentItem);

		Log.d(TAG, "onResume: gridField currentGroup: "
				+ getCurrentGroupSelection() + " currentItem: "
				+ getGridFieldCurrentItem());

	}

	public void onPause() {
		super.onPause();
		// gridFieldCurrentItem = gridField.getCurrentItem();
		saveGridFieldCurrentItem();
		menuFieldCurrentItem = mMenuField.getCurrentItem();

		Log.d(TAG, "onPause: gridField currentGroup: "
				+ getCurrentGroupSelection() + " gridField currentItem: "
				+ mGridField.getCurrentItem());

	}

	@Override
	public void onBackPressed() {
		Log.d(TAG,
				"onBackPressed Called. gridField currentItem: "
						+ mGridField.getCurrentItem());

		final String groupSelection = getCurrentGroupSelection();

		if (groupSelection == null && mGridField.getCurrentItem() == 0) {
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

		} else if (groupSelection != null && mGridField.getCurrentItem() == 0) {
			setCurrentGroupSelection(null);

			getActionBar().setDisplayHomeAsUpEnabled(false);

			mContactController = new ContactController(this, null);
			mGridField = (ViewPager) findViewById(R.id.grid_field);
			mGridField.setAdapter(new CellPagerAdapter(mContactController
					.getGridFieldViews(4, 4)));
			mGridField.setCurrentItem(getGridFieldCurrentItem());

			mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator_grid);
			mCirclePageIndicator.setViewPager(mGridField);
			mCirclePageIndicator.setCurrentItem(getGridFieldCurrentItem());
			getActionBar().setTitle("All");
		} else {

			setGridFieldCurrentItem(0);
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

			MainActivity.saveGridFieldCurrentItem();
			MainActivity.setCurrentGroupSelection(null);
			int currentItem = MainActivity.getGridFieldCurrentItem(null);

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
		// with jam obviously
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void saveGridFieldCurrentItem() {
		setGridFieldCurrentItem(mGridField.getCurrentItem());
	}

	public static void setGridFieldCurrentItem(String groupSelection,
			int currentItem) {
		if (groupSelection == null)
			gridFieldCurrentItem = currentItem;
		else
			gridFieldcurrentItemByGroup.put(groupSelection, currentItem);
	}

	public static void setGridFieldCurrentItem(int currentItem) {
		setGridFieldCurrentItem(getCurrentGroupSelection(), currentItem);
	}

	public static int getGridFieldCurrentItem(String groupSelection) {
		if (groupSelection == null)
			return gridFieldCurrentItem;
		else if (gridFieldcurrentItemByGroup.get(groupSelection) == null)
			return 0;
		else
			return gridFieldcurrentItemByGroup.get(groupSelection);
	}

	public static int getGridFieldCurrentItem() {
		return getGridFieldCurrentItem(getCurrentGroupSelection());
	}

	public static String getCurrentGroupSelection() {
		return currentGroupSelection;
	}

	public static void setCurrentGroupSelection(String currentGroupSelection) {
		MainActivity.currentGroupSelection = currentGroupSelection;
	}

}
