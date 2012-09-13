package org.pockys.allingrid;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
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

		// insert
		int groupId = groupCellInfo.getGroupId();
		Uri contactUri = ContactsContract.Data.CONTENT_URI;

		Log.d(TAG, "Group ID: " + groupId);

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ContentProviderOperation.Builder op;

		Iterator<Integer> contactIdItr = EditGridItemClickListener
				.getSelectedContactIdListIterator();
		for (; contactIdItr.hasNext();) {
			int contactId = contactIdItr.next();

			// Get account data from system
			// AuthenticatorDescription[] accountTypes = AccountManager.get(
			// mContext).getAuthenticatorTypes();

			String accountType = "com.google";
			String accountName = "likepeppermint@gmail.com";

			op = ContentProviderOperation
					.newInsert(ContactsContract.RawContacts.CONTENT_URI)
					.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE,
							accountType)
					.withValue(ContactsContract.RawContacts.ACCOUNT_NAME,
							accountName);
			ops.add(op.build());

			op = ContentProviderOperation
					.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(
							ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
					.withValue(
							ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
							"GOLLLLLLLLLD");
			ops.add(op.build());

			op = ContentProviderOperation
					.newInsert(contactUri)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(
							// ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE,
							// ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)
							ContactsContract.Data.MIMETYPE,
							ContactsContract.Data.CONTENT_TYPE)
					.withValue(
							ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,
							groupId);
			ops.add(op.build());

		}

		try {
			ContentProviderResult[] results = mContext.getContentResolver()
					.applyBatch(ContactsContract.AUTHORITY, ops);

			// for debug ******************
			Iterator<Integer> it = EditGridItemClickListener
					.getSelectedContactIdListIterator();
			for (; it.hasNext();) {

				int contactId = it.next();
				String[] selection = new String[] {
						ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,
						ContactsContract.Data.RAW_CONTACT_ID };
				String where = ContactsContract.Data.CONTACT_ID + " = "
						+ contactId;
				Cursor cursor = mContext.getContentResolver().query(contactUri,
						selection, where, null, null);

				ArrayList<Integer> rawContactIdList = new ArrayList<Integer>();

				for (; cursor.moveToNext();) {
					String groupIdString = cursor
							.getString(cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID));
					String rawContactIdString = cursor
							.getString(cursor
									.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
					rawContactIdList.add(Integer.parseInt(rawContactIdString));
					Log.d(TAG, "Contact Id: " + contactId + " Group Id: "
							+ groupIdString + " raw Contact Id: "
							+ rawContactIdString);
				}
			}
			// for debug ******************

		} catch (OperationApplicationException e) {
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
}
