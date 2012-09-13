package org.pockys.allingrid;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
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

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.LinePageIndicator;

public class EditActivity extends Activity {

	public static final String TAG = "EditActivity";

	private static ViewPager gridField;
	private ViewPager menuField;

	private MenuController menuController;
	private ContactController contactController;

	private static int gridFieldCurrentItem = 0;
	private int menuFieldCurrentItem = 0;

	private EditGridItemClickListener mEditClickListener;

	private static GroupCellInfo currentGroupInfo = MenuController.AllGroupCellInfo;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(LayoutInflater.from(this).inflate(R.layout.main, null));

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Edit - All");
		actionBar.setDisplayHomeAsUpEnabled(true);

		mEditClickListener = new EditGridItemClickListener(this);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// app icon in action bar clicked; go home
		case android.R.id.home:

			SelectedItemList.INSTANCE.clear();

			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.menu_disconnect:

			int selectedPeopleCount = SelectedItemList.INSTANCE.getSize();

			if (selectedPeopleCount == 0)
				return true;

			// disconnect

			int groupId = currentGroupInfo.getGroupId();
			String groupTitle = currentGroupInfo.getDisplayName();

			Log.d(TAG, "Group ID: " + groupId);

			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			ContentProviderOperation.Builder op;

			Iterator<Integer> contactIdItr = SelectedItemList.INSTANCE
					.getIterator();
			for (; contactIdItr.hasNext();) {
				int contactId = contactIdItr.next();

				if (groupTitle == "All") {

				} else if (groupTitle == "Favorite") {
					Log.d(TAG, "Favorite. contact Id: " + contactId);
					op = ContentProviderOperation
							.newUpdate(ContactsContract.Contacts.CONTENT_URI)
							.withSelection(
									ContactsContract.Contacts._ID + " = '"
											+ contactId + "'", null)
							.withValue(ContactsContract.Contacts.STARRED, 0);
					ops.add(op.build());

				} else {
					op = ContentProviderOperation
							.newDelete(ContactsContract.Data.CONTENT_URI)
							.withSelection(
									ContactsContract.Data.RAW_CONTACT_ID
											+ " = '"
											+ EditMenuItemClickListener.getRawContactId(
													this, contactId)
											+ "' AND "
											+ ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID
											+ " = '" + groupId + "'", null);

					ops.add(op.build());
				}

			}

			try {
				ContentProviderResult[] results = this.getContentResolver()
						.applyBatch(ContactsContract.AUTHORITY, ops);

				if (results == null) {
					MainActivity.makeToast(this, "Something was wrong!!");
				} else {

					MainActivity.makeToast(this, "Disconnect " + results.length
							+ " people from " + groupTitle);
				}

			}

			catch (OperationApplicationException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} finally {
				resetField();
			}

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void resetField() {
		SelectedItemList.INSTANCE.clear();
		saveGridFieldCurrentItem();

		contactController = new ContactController(this, null);
		contactController.setOnItemClickListener(mEditClickListener);

		gridField = (ViewPager) findViewById(R.id.grid_field);
		gridField.setAdapter(new CellPagerAdapter(contactController
				.getGridFieldViews(4, 4)));
		gridField.setCurrentItem(getGridFieldCurrentItem());

		CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator_grid);
		circlePageIndicator.setViewPager(gridField);
		circlePageIndicator.setCurrentItem(getGridFieldCurrentItem());
	}

	public void onResume() {
		super.onResume();

		menuController = new MenuController(this, false);
		menuController.setOnItemClickListener(new EditMenuItemClickListener(
				this));
		contactController = new ContactController(this, null);
		contactController.setOnItemClickListener(mEditClickListener);

		gridField = (ViewPager) findViewById(R.id.grid_field);
		gridField.setAdapter(new CellPagerAdapter(contactController
				.getGridFieldViews(4, 4)));
		gridField.setCurrentItem(getGridFieldCurrentItem());

		CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator_grid);
		circlePageIndicator.setViewPager(gridField);
		circlePageIndicator.setCurrentItem(getGridFieldCurrentItem());

		menuField = (ViewPager) findViewById(R.id.menu_field);
		menuField.setAdapter(new CellPagerAdapter(menuController
				.getMenuFieldViews(4)));
		menuField.setCurrentItem(menuFieldCurrentItem);

		LinePageIndicator linePageIndicator = (LinePageIndicator) findViewById(R.id.line_page_indicator_menu);
		linePageIndicator.setViewPager(menuField);
		linePageIndicator.setCurrentItem(menuFieldCurrentItem);

		Log.d(TAG, "onResume: gridField ");
	}

	@Override
	public void onPause() {
		super.onPause();

		saveGridFieldCurrentItem();
		menuFieldCurrentItem = menuField.getCurrentItem();

	}

	@Override
	public void onBackPressed() {
		Log.d(TAG,
				"onBackPressed Called. gridField currentItem: "
						+ gridField.getCurrentItem());

		if (gridField.getCurrentItem() == 0) {
			super.onBackPressed();
			SelectedItemList.INSTANCE.clear();
		} else {
			gridField.setCurrentItem(0);
		}

	}

	public static void saveGridFieldCurrentItem() {
		setGridFieldCurrentItem(gridField.getCurrentItem());
	}

	public static int getGridFieldCurrentItem() {
		return gridFieldCurrentItem;
	}

	public static void setGridFieldCurrentItem(int gridFieldCurrentItem) {
		EditActivity.gridFieldCurrentItem = gridFieldCurrentItem;
	}

	public static GroupCellInfo getCurrentGroupInfo() {
		return currentGroupInfo;
	}

	public static void setCurrentGroupInfo(GroupCellInfo currentGroupInfo) {
		EditActivity.currentGroupInfo = currentGroupInfo;
	}

}
