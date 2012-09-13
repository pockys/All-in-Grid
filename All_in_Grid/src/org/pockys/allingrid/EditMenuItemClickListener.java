package org.pockys.allingrid;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.viewpagerindicator.CirclePageIndicator;

public class EditMenuItemClickListener implements OnItemClickListener {

	public static final String TAG = "EditMenuItemClickListener";

	private Context mContext;

	public EditMenuItemClickListener(Context context) {
		mContext = context;

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		int selectedPeopleCount = SelectedItemList.INSTANCE.getSize();

		assert (view.getTag() instanceof GroupCellInfo) : " is not instance of GroupCellInfo!!";
		GroupCellInfo groupCellInfo = (GroupCellInfo) view.getTag();
		String groupTitle = groupCellInfo.getDisplayName();
		int groupId = groupCellInfo.getGroupId();

		ActionBar actionBar = ((Activity) mContext).getActionBar();

		Log.d(TAG, "Group ID: " + groupId);
		if (selectedPeopleCount == 0) {

			actionBar.setTitle("Edit - " + groupTitle);
			EditActivity.setCurrentGroupInfo(groupCellInfo);

			int gridFieldCurrentItem = EditActivity.getCurrentItem();
			String selection = null;
			if (groupTitle == "All") {

			} else if (groupTitle == "Favorite") {
				selection = ContactsContract.Contacts.STARRED + " = 1 ";

			} else {
				selection = ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID
						+ " = '" + groupId + "'";
			}

			ContactController contactController = new ContactController(
					mContext, selection);
			contactController
					.setOnItemClickListener(new EditGridItemClickListener(
							mContext));

			ViewPager gridField = (ViewPager) ((Activity) mContext)
					.findViewById(R.id.grid_field);
			gridField.setAdapter(new CellPagerAdapter(contactController
					.getGridFieldViews(4, 4)));
			gridField.setCurrentItem(gridFieldCurrentItem);

			CirclePageIndicator circlePageIndicator = (CirclePageIndicator) ((Activity) mContext)
					.findViewById(R.id.circle_page_indicator_grid);
			circlePageIndicator.setViewPager(gridField);
			circlePageIndicator.setCurrentItem(gridFieldCurrentItem);
			return;
		} else {

			// insert
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			ContentProviderOperation.Builder op;

			Iterator<Integer> contactIdItr = SelectedItemList.INSTANCE
					.getIterator();
			for (; contactIdItr.hasNext();) {
				int contactId = contactIdItr.next();

				if (groupTitle == "All") {
					MainActivity.makeToast(mContext, "Clear all selection");
					SelectedItemList.INSTANCE.clear();
					reDraw();
					return;

				} else if (groupTitle == "Favorite") {
					Log.d(TAG, "Favorite. contact Id: " + contactId);
					op = ContentProviderOperation
							.newUpdate(ContactsContract.Contacts.CONTENT_URI)
							.withSelection(
									ContactsContract.Contacts._ID + " = '"
											+ contactId + "'", null)
							.withValue(ContactsContract.Contacts.STARRED, 1);
					ops.add(op.build());

				} else {
					op = ContentProviderOperation
							.newInsert(ContactsContract.Data.CONTENT_URI)
							.withValue(ContactsContract.Data.RAW_CONTACT_ID,
									getRawContactId(mContext, contactId))
							.withValue(ContactsContract.Data.MIMETYPE,
									ContactsContract.Data.CONTENT_TYPE)
							.withValue(
									ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,
									groupId);
					ops.add(op.build());
				}

			}

			try {
				ContentProviderResult[] results = mContext.getContentResolver()
						.applyBatch(ContactsContract.AUTHORITY, ops);

				MainActivity.makeToast(mContext, "Insert " + results.length
						+ " people to " + groupCellInfo.getDisplayName());

			}

			catch (OperationApplicationException e) {
				MainActivity.makeToast(mContext, "Something was wrong!!");
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} finally {
				resetField();

			}

		}
	}

	private void reDraw() {
		ViewPager gridField = (ViewPager) ((Activity) mContext)
				.findViewById(R.id.grid_field);
		for (int i = 0; i < gridField.getChildCount(); i++) {
			GridView currentGridView = (GridView) gridField.getChildAt(i);
			BaseAdapter adapter = ((BaseAdapter) currentGridView.getAdapter());
			adapter.notifyDataSetChanged();

		}
	}

	private void resetField() {

		SelectedItemList.INSTANCE.clear();
		EditActivity.saveCurrentItem();

		int gridFieldCurrentItem = EditActivity.getCurrentItem();

		ContactController contactController = new ContactController(mContext,
				null);
		contactController.setOnItemClickListener(new EditGridItemClickListener(
				mContext));

		ViewPager gridField = (ViewPager) ((Activity) mContext)
				.findViewById(R.id.grid_field);
		gridField.setAdapter(new CellPagerAdapter(contactController
				.getGridFieldViews(4, 4)));
		gridField.setCurrentItem(gridFieldCurrentItem);

		CirclePageIndicator circlePageIndicator = (CirclePageIndicator) ((Activity) mContext)
				.findViewById(R.id.circle_page_indicator_grid);
		circlePageIndicator.setViewPager(gridField);
		circlePageIndicator.setCurrentItem(gridFieldCurrentItem);

	}

	public static int getRawContactId(Context context, int contactId) {
		String[] selection = new String[] { ContactsContract.Data.RAW_CONTACT_ID, };
		String where = ContactsContract.Data.CONTACT_ID + " = '" + contactId
				+ "'";
		Cursor cursor = context.getContentResolver()
				.query(ContactsContract.Data.CONTENT_URI, selection, where,
						null, null);
		for (; cursor.moveToNext();) {
			String rawContactIdString = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
			if (rawContactIdString != null) {
				cursor.close();
				return Integer.parseInt(rawContactIdString);
			}
		}

		cursor.close();
		return -1;
	}

}
