package org.pockys.allingrid;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import android.app.ActionBar;
import android.app.ActionBar.OnMenuVisibilityListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
<<<<<<< HEAD
import android.database.Cursor;
import android.net.Uri;
=======
>>>>>>> branch 'master' of https://github.com/pockys/All-in-Grid.git
import android.os.Bundle;
import android.provider.ContactsContract;
<<<<<<< HEAD
import android.provider.ContactsContract.CommonDataKinds;
=======
>>>>>>> branch 'master' of https://github.com/pockys/All-in-Grid.git
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

public class MainActivity extends Activity{

	static final String TAG = "MainActivity";
<<<<<<< HEAD
	
	static final String TAG2 = "Check";
	
	private static final int MENU_SAMP = 0;
=======
	private static ViewPager mGridField;
	private static ViewPager mMenuField;
>>>>>>> branch 'master' of https://github.com/pockys/All-in-Grid.git

<<<<<<< HEAD
	private static ViewPager mGridField;
	private static ViewPager mMenuField;

=======
>>>>>>> branch 'master' of https://github.com/pockys/All-in-Grid.git
	private ActionBar mActionBar;

	private int menuFieldCurrentItem = 0;

	private MenuController mMenuController;
	private ContactControllerMain mContactController;

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
<<<<<<< HEAD
		mContactController = new ContactControllerMain(this,
=======
		mContactController = new ContactController(this,
>>>>>>> branch 'master' of https://github.com/pockys/All-in-Grid.git
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
<<<<<<< HEAD
		mActionBar.setTitle(currentGroupInfo.getDisplayName());
=======

		mActionBar.setTitle(currentGroupInfo.getDisplayName());

>>>>>>> branch 'master' of https://github.com/pockys/All-in-Grid.git
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

<<<<<<< HEAD
			mContactController = new ContactControllerMain(this, null);
=======
			mContactController = new ContactController(this, null);
>>>>>>> branch 'master' of https://github.com/pockys/All-in-Grid.git
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
		String currentGroupTitle = currentGroupInfo.getDisplayName();
if(currentGroupTitle != "All"){
		MenuItem Samp = menu.add(0, MENU_SAMP,0,"サンプル");
		Samp.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// makeToast(this, item.getTitle().toString());


		
		
		Intent intent;
		// ContactController contactController;

		switch (item.getItemId()) {
		case MENU_SAMP:
			

			makeToast(mContext, "Yes");
			break;
			
			
		
		case android.R.id.home:

			ActionBar actionBar = this.getActionBar();
			actionBar.setTitle("All");
			actionBar.setDisplayHomeAsUpEnabled(false);

			SelectedItemList.INSTANCE
					.setSelectedGroupInfo(MenuController.AllGroupCellInfo);
			reDrawMenuField();

			resetGridField(MenuController.AllGroupCellInfo);

			break;
<<<<<<< HEAD

=======
		// case R.id.menu_phone:
		// intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
		// startActivity(intent);
		// break;
>>>>>>> branch 'master' of https://github.com/pockys/All-in-Grid.git
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
			
		case R.id.menu_phone:
			
			ArrayList<Integer> callList = new ArrayList<Integer>();
			
			callList = SelectedItemList.INSTANCE.getSelectedContactIdList();
			
			StringBuilder s2 = new StringBuilder();
			int call_contactId;
			if(callList.size()==1){
				call_contactId = callList.get(0);
				
				
				
				Cursor phone_number = getContentResolver().query(CommonDataKinds.Phone.CONTENT_URI,
						new String[]{CommonDataKinds.Phone.DATA}, 
						ContactsContract.Data.CONTACT_ID + "=" + call_contactId,
						null, null);
				
				if(phone_number.getCount()==0){
					makeToast(mContext, "YESYES");
					break;
				}
				
				phone_number.moveToFirst();
				s2 = s2.append(phone_number.getString(0));
			}

			Intent phone_intent = new Intent();
			phone_intent.setAction(Intent.ACTION_DIAL);
			phone_intent.setData(Uri.parse("tel:"+s2));
			
			
			break;
			
		case R.id.send_mail:
			
			ArrayList<Integer> ops = new ArrayList<Integer>();
			
			ops = SelectedItemList.INSTANCE.getSelectedContactIdList();
			
			StringBuilder s1 = new StringBuilder();
			
			int contactId;
			for(int i = 0; i < SelectedItemList.INSTANCE.getSize(); i++){
				contactId = ops.get(i);
				
				
				
				Cursor email = getContentResolver().query(CommonDataKinds.Email.CONTENT_URI,
						new String[]{CommonDataKinds.Email.DATA}, 
						ContactsContract.Data.CONTACT_ID + "=" + contactId,
						null, null);
				
			
				if (email.moveToFirst()){
					do{
						s1 = s1.append(email.getString(0));
					}while(email.moveToNext());
				}
				s1 = s1.append(",");
				Log.d(TAG2,"s1::"+SelectedItemList.INSTANCE.getSize());

			
			}

			
			
			String sText="これを送ります！";
			Uri uri = Uri.parse("mailto:" + s1); //宛先を生成
			Intent intent2 = new Intent(Intent.ACTION_SENDTO,uri);   //宛先情報
			intent2.putExtra(Intent.EXTRA_TEXT, sText);          //本文を指定
			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent2);      //Activity 起動
			
			break;
			
		case R.id.sort:
						
			sortlib.INSTANCE.setCurrentSort(1);

			resetGridField();			
//			mMenuController = new MenuController(this);
//			mContactController = new ContactControllerMain(this,
//					getSelection(getCurrentGroupInfo()));

//			mGridField.setAdapter(new CellPagerAdapter(mContactController
//					.getGridFieldViews(4, 4)));
//
//			mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator_grid);
//			mCirclePageIndicator.setViewPager(mGridField);
//
//			mMenuField.setAdapter(new CellPagerAdapter(mMenuController
//					.getMenuFieldViews(4)));
//
//			mLinePageIndicator = (LinePageIndicator) findViewById(R.id.line_page_indicator_menu);
//			mLinePageIndicator.setViewPager(mMenuField);
//
//			mActionBar.setTitle(currentGroupInfo.getDisplayName());
//			mGridField.setCurrentItem(getCurrentItem());
//			mCirclePageIndicator.setCurrentItem(getCurrentItem());
//			mMenuField.setCurrentItem(menuFieldCurrentItem);
//			mLinePageIndicator.setCurrentItem(menuFieldCurrentItem);
//
//			Log.d(TAG, "onResume: gridField currentGroup: "
//					+ getCurrentGroupInfo().getDisplayName() + " currentItem: "
//					+ getCurrentItem());
//
//			SelectedItemList.INSTANCE.setSelectedGroupInfo(currentGroupInfo);
			
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
<<<<<<< HEAD
								
=======
								// }

								// editor.clear();
>>>>>>> branch 'master' of https://github.com/pockys/All-in-Grid.git
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
<<<<<<< HEAD
=======

		}
	}
>>>>>>> branch 'master' of https://github.com/pockys/All-in-Grid.git

<<<<<<< HEAD
		}
	}
 static void reDrawMenuField() {
=======
	public static void reDrawMenuField() {
>>>>>>> branch 'master' of https://github.com/pockys/All-in-Grid.git
		for (int i = 0; i < mMenuField.getChildCount(); i++) {
			GridView currentGridView = (GridView) mMenuField.getChildAt(i);
			BaseAdapter adapter = ((BaseAdapter) currentGridView.getAdapter());
			adapter.notifyDataSetChanged();
		}
	}

}
