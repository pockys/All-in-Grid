package org.pockys.allingrid;

import java.util.ArrayList;
import java.util.Set;

import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

public class ContactControllerMain implements OnItemClickListener,
		OnItemLongClickListener {

	private static final String TAG = "ContactController";

	private Context mContext;
	private Cursor mContactsCursor;
	private OnItemClickListener mOnItemClickListener = this;

	private SharedPreferences sharedPreferences;
	private SharedPreferences sortPreferences;

	public ContactControllerMain(Context context) {
		this(context, null);
		sortPreferences = context.getSharedPreferences("sortPreference",
				Context.MODE_PRIVATE);
//		sharedPreferences = context.getSharedPreferences("sharePreferences",
//				Context.MODE_PRIVATE);
		
	}

	public ContactControllerMain(Context context, String selection) {
		mContext = context;
		mContactsCursor = getContacts(selection);
//		sharedPreferences = context.getSharedPreferences("sharePreferences",
//				Context.MODE_PRIVATE);
//		sortPreferences = context.getSharedPreferences("sortPreference",
//				Context.MODE_PRIVATE);

	}

	public int getSize() {
		return mContactsCursor.getCount();
	}

	private ArrayList<CellInfo> getContactsList(int maxSize) {

		ArrayList<CellInfo> contactsArrayList = new ArrayList<CellInfo>();

		for (int i = 0; mContactsCursor.moveToNext() && i < maxSize; i++) {

			String displayName = mContactsCursor.getString(mContactsCursor
					.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
			String uriString = mContactsCursor.getString(mContactsCursor
					.getColumnIndex(ContactsContract.Data.PHOTO_URI));
			String contactIdString = mContactsCursor.getString(mContactsCursor
					.getColumnIndex(ContactsContract.Data.CONTACT_ID));
			
			Uri thumbnailUri = null;
			if (uriString != null)
				thumbnailUri = Uri.parse(uriString);

			ContactCellInfo contact = new ContactCellInfo();
			contact.setDisplayName(displayName);
			contact.setThumbnail(thumbnailUri);
			contact.setContactId(Integer.valueOf(contactIdString));
			

			contactsArrayList.add(contact);
		}

		return contactsArrayList;

	}

	private Cursor getContacts(String selection) {

		String[] PROJECTION = new String[] { ContactsContract.Data.CONTACT_ID,
				ContactsContract.Data.PHOTO_URI,
				ContactsContract.Data.DISPLAY_NAME,};
		
//		String CONTACTS_SORT_ORDER = sortPreferences.getString("SORT", "TEMP");
//		String temp;
//		SharedPreferences.Editor editor = sharedPreferences.edit();
//		editor.putString("sort", ContactsContract.Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
//		temp = sortPreferences.getString("sort", "");
//		
//		if(temp == "TEMP"){
			
//		String temp =	ContactsContract.Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
//		editor.putString("sort", temp);
//		editor.commit();
//
//		}
		String temp;
		if(sortlib.INSTANCE.getCurrentSort() == 0){
			temp =	ContactsContract.Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
		}
		else{
			temp =	ContactsContract.Data.DISPLAY_NAME + " COLLATE LOCALIZED DESC";
		}

		if (selection != null || selection == "")
			selection += " AND ";
		else
			selection = "";

		selection += ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'";
		return mContext.getContentResolver().query(
				ContactsContract.Data.CONTENT_URI, PROJECTION, selection, null,
				temp);
	}

	public ArrayList<GridView> getGridFieldViews(final int numColumns,
			final int numRows) {
		ArrayList<GridView> gridViewList = new ArrayList<GridView>();

		// Contact contact = new Contact(this);
		final int numCells = numColumns * numRows;
		for (int i = 0; i < this.getSize() / numCells; i++) {
			GridView gridView = (GridView) LayoutInflater.from(mContext)
					.inflate(R.layout.grid_view, null);
			gridView.setNumColumns(numColumns);
			gridView.setAdapter(new CellAdapter(mContext, this
					.getContactsList(numCells)));
			gridView.setOnItemClickListener(mOnItemClickListener);
			gridView.setOnItemLongClickListener(this);

			gridViewList.add(gridView);
		}
		if (this.getSize() % numCells != 0) {
			GridView gridView = (GridView) LayoutInflater.from(mContext)
					.inflate(R.layout.grid_view, null);
			gridView.setNumColumns(numColumns);
			gridView.setAdapter(new CellAdapter(mContext, this
					.getContactsList(numCells)));
			gridView.setOnItemClickListener(mOnItemClickListener);
			gridView.setOnItemLongClickListener(this);

			gridViewList.add(gridView);
		}
		mContactsCursor.close();

		return gridViewList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, final View v, int position,
			long id) {
		
		assert (v.getTag() instanceof ContactCellInfo) : "cellInfo is not instance of contact cellInfo!!";

		ContactCellInfo cellInfo = (ContactCellInfo) v.getTag();

		int contactId = cellInfo.getContactId();
		String MailAdress = cellInfo.getMailAdress();
		if (SelectedItemList.INSTANCE.contain(contactId)) {
			v.setBackgroundColor(Color.TRANSPARENT);

			SelectedItemList.INSTANCE.remove(contactId);
		} else {
			v.setBackgroundColor(Color.WHITE);

			SelectedItemList.INSTANCE.add(contactId);
			SelectedItemList.INSTANCE.addMail(MailAdress);
		}

		SelectedItemList.INSTANCE.logContactIdList(TAG);
	}


	@Override
	public boolean onItemLongClick(AdapterView<?> parent, final View cell,
			int position, long id) {
		
		return false;
	}

}

