package org.pockys.allingrid;

import java.util.ArrayList;
import java.util.Iterator;

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

		int selectedPeopleCount = EditGridItemClickListener
				.getSelectedContactIdListSize();

		if (selectedPeopleCount == 0)
			return;

		assert (view.getTag() instanceof GroupCellInfo) : " is not instance of GroupCellInfo!!";
		GroupCellInfo groupCellInfo = (GroupCellInfo) view.getTag();

		MainActivity.makeToast(mContext, "Insert " + selectedPeopleCount
				+ " people to " + groupCellInfo.getDisplayName());

		String groupTitle = groupCellInfo.getDisplayName();

		// insert
		int groupId = groupCellInfo.getGroupId();

		Log.d(TAG, "Group ID: " + groupId);

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ContentProviderOperation.Builder op;

		Iterator<Integer> contactIdItr = EditGridItemClickListener
				.getSelectedContactIdListIterator();
		for (; contactIdItr.hasNext();) {
			int contactId = contactIdItr.next();

			if (groupTitle == "All") {

			} else if (groupTitle == "Favorite") {
				Log.d(TAG, "Favorite. contact Id: " + contactId);
				op = ContentProviderOperation
						.newUpdate(ContactsContract.Contacts.CONTENT_URI)
						.withSelection(
								ContactsContract.Contacts._ID + " = '"
										+ getRawContactId(contactId) + "'",
								null)
						.withValue(ContactsContract.Contacts.STARRED, 1);
				ops.add(op.build());

			} else {
				op = ContentProviderOperation
						.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID,
								getRawContactId(contactId))
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
		}

		catch (OperationApplicationException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		// ################################################################
		EditGridItemClickListener.clear();
		EditActivity.saveGridFieldCurrentItem();

		int gridFieldCurrentItem = EditActivity.getGridFieldCurrentItem();

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
		// ################################################################

	}

	private int getRawContactId(int contactId) {
		String[] selection = new String[] { ContactsContract.Data.RAW_CONTACT_ID, };
		String where = ContactsContract.Data.CONTACT_ID + " = '" + contactId
				+ "'";
		Cursor cursor = mContext.getContentResolver()
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
