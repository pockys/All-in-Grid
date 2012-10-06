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
		GroupCellInfo selectedGroupInfo = (GroupCellInfo) view.getTag();
		String selectedGroupTitle = selectedGroupInfo.getDisplayName();
		int groupId = selectedGroupInfo.getGroupId();
 
		ActionBar actionBar = ((Activity) mContext).getActionBar();

		if (selectedPeopleCount == 0) {

			// set background highlighted
			SelectedItemList.INSTANCE.setSelectedGroupInfo(selectedGroupInfo);
			EditActivity.reDrawMenuField();

			actionBar.setTitle("Edit - " + selectedGroupTitle);

			GroupCellInfo currentGroupInfo = EditActivity.getCurrentGroupInfo();
			Log.d(TAG, "current group: "
					+ EditActivity.getCurrentGroupInfo().getDisplayName()
					+ " current item : " + EditActivity.getCurrentItem());
			Log.d(TAG,
					"selected group: " + selectedGroupTitle + " current item: "
							+ EditActivity.getCurrentItem(selectedGroupInfo));

			ViewPager gridField = (ViewPager) ((Activity) mContext)
					.findViewById(R.id.grid_field);
			if (selectedGroupInfo.equals(currentGroupInfo)) {
				EditActivity.setCurrentItem(0);
				gridField.setCurrentItem(0);
			} else {

				EditActivity.saveCurrentItem();
				EditActivity.setCurrentGroupInfo(selectedGroupInfo);
				int currentItem = EditActivity.getCurrentItem();

				ContactController contactController = new ContactController(
						mContext, EditActivity.getSelection(selectedGroupInfo));
				contactController
						.setOnItemClickListener(new EditGridItemClickListener(
								mContext));

				gridField.setAdapter(new CellPagerAdapter(contactController
						.getGridFieldViews(4, 4)));
				gridField.setCurrentItem(currentItem);
				CirclePageIndicator circlePageIndicator = (CirclePageIndicator) ((Activity) mContext)
						.findViewById(R.id.circle_page_indicator_grid);
				circlePageIndicator.setViewPager(gridField);
				circlePageIndicator.setCurrentItem(currentItem);
			}

			return;
		} else {

			// insert
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			ContentProviderOperation.Builder op;

			Iterator<Integer> contactIdItr = SelectedItemList.INSTANCE
					.getIterator();
			for (; contactIdItr.hasNext();) {
				int contactId = contactIdItr.next();

				if (selectedGroupTitle == "All") {
					MainActivity.makeToast(mContext, "Clear all selection");
					SelectedItemList.INSTANCE.clear();
					EditActivity.reDrawGridField();
					return;

				} else if (selectedGroupTitle == "Favorite") {
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
						+ " people to " + selectedGroupInfo.getDisplayName());

			}

			catch (OperationApplicationException e) {
				MainActivity.makeToast(mContext, "Something was wrong!!");
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} finally {
				// resetField();
				SelectedItemList.INSTANCE.clear();
				EditActivity.reDrawGridField();

			}

		}
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
