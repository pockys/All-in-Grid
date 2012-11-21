package org.pockys.allingrid;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.ActionBar;
//import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.viewpagerindicator.CirclePageIndicator;

public class EditMenuItemClickListener implements OnItemClickListener{

	public static final String TAG = "EditMenuItemClickListener";

	private Context mContext;
	

	public EditMenuItemClickListener(Context context) {
		mContext = context;
		
	}

	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Log.d(TAG, "Test listner");
		int selectedPeopleCount = SelectedItemList.INSTANCE.getSize();

		
		assert (view.getTag() instanceof GroupCellInfo) : " is not instance of GroupCellInfo!!";
		final GroupCellInfo selectedGroupInfo = (GroupCellInfo) view.getTag();
		final String selectedGroupTitle = selectedGroupInfo.getDisplayName();
		final String groupId = selectedGroupInfo.getGroupId();
		
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

				ContactControllerEdit contactController = new ContactControllerEdit(
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
			AlertDialog.Builder Insert = new AlertDialog.Builder(mContext);
			Insert.setTitle(selectedGroupTitle + "�O���[�v�� "+ SelectedItemList.INSTANCE.getSize()+
					" �l�ǉ����܂��� ??");
			Insert.setPositiveButton("������Ƃ܂����I", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				dialog.cancel();	
				}
			});
			Insert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// insert
					ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
					ContentProviderOperation.Builder op;

					Iterator<Integer> contactIdItr = SelectedItemList.INSTANCE
							.getIterator();
					for (; contactIdItr.hasNext();) {
						int contactId = contactIdItr.next();

						if (selectedGroupTitle == "All") {
							MainActivity.makeToast(mContext, "�S���񂽂���������");
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

						MainActivity.makeToast(mContext, selectedGroupInfo.getDisplayName()+
								"�O���[�v�� " + results.length + " �l�ǉ����܂��� ");

					}

					catch (OperationApplicationException e) {
						MainActivity.makeToast(mContext, "Something was wrong!!");
						e.printStackTrace();
					} catch (RemoteException e) {
						e.printStackTrace();
					} finally {
						
						SelectedItemList.INSTANCE.clear();
						EditActivity.setAllselectVisibility(true);
						EditActivity.setAllClearVisibility(false);
						EditActivity.setDisconnectMenuVisibility(false);

						EditActivity.reDrawGridField();
						
						

					}
				}	
								
			});
			Insert.show();
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
