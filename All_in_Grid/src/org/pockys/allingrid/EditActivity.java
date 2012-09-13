package org.pockys.allingrid;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.LinePageIndicator;

public class EditActivity extends Activity {

	public static final String TAG = "EditActivity";

	private static ViewPager mGridField;
	private static ViewPager mMenuField;

	private ActionBar mActionBar;

	private MenuController menuController;
	private ContactController mContactController;

	private int menuFieldCurrentItem = -1;

	private EditGridItemClickListener mEditClickListener;
	private CirclePageIndicator mCirclePageIndicator;
	private LinePageIndicator mLinePageIndicator;

	private static Hashtable<GroupCellInfo, Integer> currentItemTable;
	private static GroupCellInfo currentGroupInfo;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(LayoutInflater.from(this).inflate(R.layout.main, null));

		mActionBar = getActionBar();
		// mActionBar.setTitle("Edit - All");
		mActionBar.setDisplayHomeAsUpEnabled(true);

		currentItemTable = MainActivity.cloneCurrentItemTable();
		currentGroupInfo = MainActivity.getCurrentGroupInfo();

		mGridField = (ViewPager) findViewById(R.id.grid_field);
		mMenuField = (ViewPager) findViewById(R.id.menu_field);

		mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator_grid);
		mLinePageIndicator = (LinePageIndicator) findViewById(R.id.line_page_indicator_menu);

		mEditClickListener = new EditGridItemClickListener(this);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		final Context context = this;
		AlertDialog.Builder builder;

		switch (item.getItemId()) {
		case android.R.id.home:
			SelectedItemList.INSTANCE.clear();

			// if (currentGroupInfo.getDisplayName() == "All") {
			builder = new AlertDialog.Builder(this);
			builder.setTitle("Quit Editing");
			builder.setPositiveButton("Yes", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					Intent intent = new Intent(context, MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
			});
			builder.setNegativeButton("No", null);
			builder.create().show();
			// }
			/*
			 * else { saveCurrentItem();
			 * setCurrentGroupInfo(MenuController.AllGroupCellInfo);
			 * SelectedItemList.INSTANCE
			 * .setSelectedGroupInfo(MenuController.AllGroupCellInfo);
			 * EditActivity.reDrawMenuField();
			 * 
			 * mActionBar.setTitle("Edit - All");
			 * 
			 * mContactController = new ContactController(this, null);
			 * mContactController.setOnItemClickListener(mEditClickListener);
			 * 
			 * mGridField.setAdapter(new CellPagerAdapter(mContactController
			 * .getGridFieldViews(4, 4)));
			 * mGridField.setCurrentItem(getCurrentItem());
			 * 
			 * mCirclePageIndicator.setViewPager(mGridField);
			 * mCirclePageIndicator.setCurrentItem(getCurrentItem());
			 * 
			 * }
			 */
			return true;
		case R.id.menu_disconnect:

			int selectedPeopleCount = SelectedItemList.INSTANCE.getSize();

			if (selectedPeopleCount == 0)
				return true;

			// disconnect

			final int groupId = currentGroupInfo.getGroupId();
			final String groupTitle = currentGroupInfo.getDisplayName();

			Log.d(TAG, "Group ID: " + groupId);

			final ContentResolver contentResolver = this.getContentResolver();

			String dialogTitle;
			String dialogMessage;
			String positiveButtonMessage;
			String negativeButtonMessage;

			if (groupTitle == "All") {
				dialogTitle = "Disconnect from all groups";
				dialogMessage = "ARE YOU SERIOUS??";
				positiveButtonMessage = "Uh-huh";
				negativeButtonMessage = "Oops, I missed!!";
			} else {
				dialogTitle = "Disconnect - " + groupTitle;
				dialogMessage = selectedPeopleCount
						+ " people will be disconnected";
				positiveButtonMessage = "Yes";
				negativeButtonMessage = "No";
			}
			builder = new AlertDialog.Builder(this);
			builder.setTitle(dialogTitle);
			builder.setMessage(dialogMessage);
			builder.setNegativeButton(negativeButtonMessage,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							SelectedItemList.INSTANCE.clear();
							reDrawGridField();

						}
					});
			builder.setPositiveButton(positiveButtonMessage,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.d(TAG, "Yes");

							ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
							ContentProviderOperation.Builder op;

							Iterator<Integer> contactIdItr = SelectedItemList.INSTANCE
									.getIterator();
							for (; contactIdItr.hasNext();) {
								int contactId = contactIdItr.next();

								if (groupTitle == "All") {

								} else if (groupTitle == "Favorite") {
									Log.d(TAG, "Favorite. contact Id: "
											+ contactId);
									op = ContentProviderOperation
											.newUpdate(
													ContactsContract.Contacts.CONTENT_URI)
											.withSelection(
													ContactsContract.Contacts._ID
															+ " = '"
															+ contactId + "'",
													null)
											.withValue(
													ContactsContract.Contacts.STARRED,
													0);
									ops.add(op.build());

								} else {
									op = ContentProviderOperation
											.newDelete(
													ContactsContract.Data.CONTENT_URI)
											.withSelection(
													ContactsContract.Data.RAW_CONTACT_ID
															+ " = '"
															+ EditMenuItemClickListener
																	.getRawContactId(
																			context,
																			contactId)
															+ "' AND "
															+ ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID
															+ " = '"
															+ groupId
															+ "'", null);

									ops.add(op.build());
								}

							}

							try {
								ContentProviderResult[] results = contentResolver
										.applyBatch(ContactsContract.AUTHORITY,
												ops);

								if (results == null) {
									MainActivity.makeToast(context,
											"Something was wrong!!");
								} else if (results.length > 0) {

									MainActivity.makeToast(context,
											"Disconnect " + results.length
													+ " people from "
													+ groupTitle);
								}

							}

							catch (OperationApplicationException e) {
								e.printStackTrace();
							} catch (RemoteException e) {
								e.printStackTrace();
							} finally {
								SelectedItemList.INSTANCE.clear();
								saveCurrentItem();

								if (currentGroupInfo.getDisplayName() == "All") {
									reDrawGridField();
								} else {
									mContactController = new ContactController(
											context,
											getSelection(currentGroupInfo));
									if (mContactController.getSize() == 0) {
										setCurrentGroupInfo(MenuController.AllGroupCellInfo);
										setCurrentItem(0);
										SelectedItemList.INSTANCE
												.setSelectedGroupInfo(MenuController.AllGroupCellInfo);
										reDrawMenuField();

										mContactController = new ContactController(
												context, null);

									}

									mContactController
											.setOnItemClickListener(mEditClickListener);

									mGridField.setAdapter(new CellPagerAdapter(
											mContactController
													.getGridFieldViews(4, 4)));
									mGridField.setCurrentItem(getCurrentItem());

									CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator_grid);
									circlePageIndicator
											.setViewPager(mGridField);
									circlePageIndicator
											.setCurrentItem(getCurrentItem());
								}

							}

						}
					});

			builder.create().show();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void resetField() {
		SelectedItemList.INSTANCE.clear();
		saveCurrentItem();

		mContactController = new ContactController(this,
				MainActivity.getSelection(currentGroupInfo));
		if (currentGroupInfo.getDisplayName() != "All"
				&& mContactController.getSize() == 0) {
			setCurrentGroupInfo(MenuController.AllGroupCellInfo);
			SelectedItemList.INSTANCE
					.setSelectedGroupInfo(MenuController.AllGroupCellInfo);
			reDrawMenuField();
			mContactController = new ContactController(this, null);
		}
		mContactController.setOnItemClickListener(mEditClickListener);

		mGridField.setAdapter(new CellPagerAdapter(mContactController
				.getGridFieldViews(4, 4)));
		mGridField.setCurrentItem(getCurrentItem());

		CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator_grid);
		circlePageIndicator.setViewPager(mGridField);
		circlePageIndicator.setCurrentItem(getCurrentItem());
	}

	public void onStart() {
		super.onStart();

		mActionBar.setTitle("Edit - " + currentGroupInfo.getDisplayName());

		menuController = new MenuController(this);
		menuController.setOnItemClickListener(new EditMenuItemClickListener(
				this));
		mContactController = new ContactController(this,
				MainActivity.getSelection(currentGroupInfo));
		mContactController.setOnItemClickListener(mEditClickListener);

		mGridField = (ViewPager) findViewById(R.id.grid_field);
		mGridField.setAdapter(new CellPagerAdapter(mContactController
				.getGridFieldViews(4, 4)));

		mCirclePageIndicator.setViewPager(mGridField);

		mMenuField.setAdapter(new CellPagerAdapter(menuController
				.getMenuFieldViews(4)));

		mLinePageIndicator.setViewPager(mMenuField);

		if (menuFieldCurrentItem == -1) {
			menuFieldCurrentItem = menuController.getPagedCount(4,
					currentGroupInfo, false);
		}

	}

	public void onResume() {
		super.onResume();

		Log.d(TAG,
				"onResume: gridField currentGroup: "
						+ currentGroupInfo.getDisplayName() + " current Item: "
						+ getCurrentItem());

		mGridField.setCurrentItem(getCurrentItem());
		mCirclePageIndicator.setCurrentItem(getCurrentItem());

		mMenuField.setCurrentItem(menuFieldCurrentItem);
		mLinePageIndicator.setCurrentItem(menuFieldCurrentItem);

	}

	@Override
	public void onPause() {
		super.onPause();

		saveCurrentItem();
		menuFieldCurrentItem = mMenuField.getCurrentItem();

	}

	@Override
	public void onBackPressed() {
		Log.d(TAG,
				"onBackPressed Called. gridField currentItem: "
						+ mGridField.getCurrentItem());

		String currentGroupTitle = currentGroupInfo.getDisplayName();

		if (SelectedItemList.INSTANCE.getSize() > 0) {
			SelectedItemList.INSTANCE.clear();
			reDrawGridField();
		}

		else if (currentGroupTitle == "All" && mGridField.getCurrentItem() == 0) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Quit Editing");
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

			mActionBar.setTitle("Edit - All");

			mContactController = new ContactController(this, null);
			mGridField.setAdapter(new CellPagerAdapter(mContactController
					.getGridFieldViews(4, 4)));
			mGridField.setCurrentItem(getCurrentItem());

			mCirclePageIndicator.setViewPager(mGridField);
			mCirclePageIndicator.setCurrentItem(getCurrentItem());

			menuFieldCurrentItem = 0;
			mMenuField.setCurrentItem(0);

			mLinePageIndicator.setCurrentItem(0);

		} else {

			setCurrentItem(0);
			mGridField.setCurrentItem(0);
		}

	}

	public static GroupCellInfo getCurrentGroupInfo() {
		return currentGroupInfo;
	}

	public static void setCurrentGroupInfo(GroupCellInfo currentGroupInfo) {
		EditActivity.currentGroupInfo = currentGroupInfo;
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
