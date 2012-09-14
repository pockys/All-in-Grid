package org.pockys.allingrid;

import java.util.ArrayList;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
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

	// private ArrayList<GridView> currentGridViewList = new
	// ArrayList<GridView>();

	private static CirclePageIndicator mCirclePageIndicator;
	private LinePageIndicator mLinePageIndicator;

	private static Context mContext;

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

		mContext = this;

	}

	public void onStart() {
		super.onStart();

		Log.d(TAG, "onStart");

		mMenuController = new MenuController(this);
		mContactController = new ContactController(this,
				getSelection(getCurrentGroupInfo()));
		// currentGridViewList = mContactController.getGridFieldViews(4, 4);

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

		mActionBar.setTitle(currentGroupInfo.getDisplayName());

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
			menuFieldCurrentItem = 0;
			mMenuField.setCurrentItem(0);
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
		// ContactController contactController;

		switch (item.getItemId()) {
		case android.R.id.home:

			ActionBar actionBar = this.getActionBar();
			actionBar.setTitle("All");
			actionBar.setDisplayHomeAsUpEnabled(false);

			SelectedItemList.INSTANCE
					.setSelectedGroupInfo(MenuController.AllGroupCellInfo);
			reDrawMenuField();

			resetGridField(MenuController.AllGroupCellInfo);

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

			final SharedPreferences sharedPreferences = getSharedPreferences(
					"sharePreferences", Context.MODE_PRIVATE);

			AlertDialog.Builder shuffleDialogBuilder = new AlertDialog.Builder(
					this);
			shuffleDialogBuilder.setTitle("Icon Shuffle");

			ListView listView = new ListView(mContext);
			listView.setAdapter(new BaseAdapter() {

				private final String[] items = { "All", "Category" };

				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					TextView textView = null;
					if (convertView == null) {
						textView = (TextView) LayoutInflater.from(mContext)
								.inflate(R.layout.list_view_item, null);

					} else
						textView = (TextView) convertView;

					textView.setText(items[position]);

					// Log.d(TAG, "textView text:  " + items[position]);

					return textView;
				}

				@Override
				public long getItemId(int position) {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public Object getItem(int position) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public int getCount() {
					// TODO Auto-generated method stub
					return 2;
				}
			});
			shuffleDialogBuilder.setView(listView);
			final AlertDialog shuffleDialog = shuffleDialogBuilder.create();
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> container, View view,
						int position, long id) {
					shuffleDialog.dismiss();

					if (position == 0) {
						IconListLib.INSTANCE.setCurrentCategoy(position);

						SharedPreferences.Editor editor = sharedPreferences
								.edit();
						editor.clear();
						editor.commit();

						resetGridField();

						makeToast(mContext, "All icons are shuffled!!");

					} else if (position == 1) {

						LayoutInflater inflater = LayoutInflater.from(mContext);
						GridView gridView = (GridView) inflater.inflate(
								R.layout.grid_view, null);
						gridView.setAdapter(new IconListAdapter(mContext, 1));
						AlertDialog.Builder categoryDialogBuilder = new AlertDialog.Builder(
								mContext);

						categoryDialogBuilder.setTitle("Change Icon");
						categoryDialogBuilder.setView(gridView);

						final AlertDialog categoryDialog = categoryDialogBuilder
								.create();

						gridView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> container,
									View view, int position, long id) {

								categoryDialog.dismiss();
								IconListLib.INSTANCE
										.setCurrentCategoy(position + 1);

								SharedPreferences.Editor editor = sharedPreferences
										.edit();

								ArrayList<GridView> currentGridViews = new ContactController(
										mContext,
										getSelection(currentGroupInfo))
										.getGridFieldViews(4, 4);

								Log.d(TAG, "currentGridViews size: "
										+ currentGridViews);
								for (int i = 0; i < currentGridViews.size(); i++) {

									GridView gridView = currentGridViews.get(i);
									Log.d(TAG, "[" + i + "]"
											+ " gridView Child Count: "
											+ gridView.getAdapter().getCount());

									for (int j = 0; j < gridView.getAdapter()
											.getCount(); j++) {

										View cell = gridView.getAdapter()
												.getView(j, null, gridView);
										ContactCellInfo contactInfo = (ContactCellInfo) cell
												.getTag();
										int contactId = contactInfo
												.getContactId();

										int imageNum = sharedPreferences
												.getInt(Integer
														.toString(contactId),
														-1);

										if (imageNum != -1) {
											Log.d(TAG,
													"preference to -1. contactId: "
															+ contactId
															+ " c: "
															+ (position + 1));

											editor.putInt(
													Integer.toString(contactId),
													-1);
										}
									}
								}
								// }

								// editor.clear();
								editor.commit();

								resetGridField();
							}

						});

						categoryDialog.show();

					}

				}

			});
			shuffleDialog.show();
		}
		return true;
	}

	public static void resetGridField() {
		resetGridField(currentGroupInfo);
	}

	public static void resetGridField(GroupCellInfo groupInfo) {
		saveCurrentItem();
		setCurrentGroupInfo(groupInfo);
		int currentItem = getCurrentItem();

		ContactController contactController = new ContactController(mContext,
				getSelection(groupInfo));

		mGridField.setAdapter(new CellPagerAdapter(contactController
				.getGridFieldViews(4, 4)));
		mGridField.setCurrentItem(currentItem);

		mCirclePageIndicator.setViewPager(mGridField);
		mCirclePageIndicator.setCurrentItem(currentItem);

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
