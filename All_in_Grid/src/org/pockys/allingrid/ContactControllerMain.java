package org.pockys.allingrid;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

public class ContactControllerMain implements OnItemClickListener,
		OnItemLongClickListener {

	private static final String TAG = "ContactController";

	private Context mContext;
	private Cursor mContactsCursor;
	private OnItemClickListener mOnItemClickListener = this;

	private SharedPreferences sharedPreferences;

	public ContactControllerMain(Context context) {
		this(context, null);		
	}

	public ContactControllerMain(Context context, String selection) {
		mContext = context;
		mContactsCursor = getContacts(selection);
		
	}

	public int getSize() {
		return mContactsCursor.getCount();
	}

	private ArrayList<CellInfo> getContactsList(int maxSize) {

		ArrayList<CellInfo> contactsArrayList = new ArrayList<CellInfo>();

		for (int i = 0; mContactsCursor.moveToNext() && i < maxSize; i++) {
			String displayName = mContactsCursor.getString(mContactsCursor
					.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));

			String contactIdString = mContactsCursor.getString(mContactsCursor
					.getColumnIndex(ContactsContract.Data.CONTACT_ID));
			
			Uri thumbnailUri = null;

			ContactCellInfo contact = new ContactCellInfo();
			contact.setDisplayName(displayName);
			contact.setThumbnail(thumbnailUri);
			contact.setContactId(contactIdString);
			

			contactsArrayList.add(contact);
		}

		return contactsArrayList;

	}

	private Cursor getContacts(String selection) {

		
		
		String temp;
		if(IconListLib.INSTANCE.getCurrentSort() == 0){
			temp =	ContactsContract.Contacts.DISPLAY_NAME+ " COLLATE LOCALIZED ASC";
		}
		else{
			temp =	ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED DESC";
		}

		if (selection != null || selection == "")
			selection += " AND ";
		else 
		selection = "";

			String[] PROJECTION = new String[] { ContactsContract.Data.DISPLAY_NAME,

					ContactsContract.Data.CONTACT_ID
					};
			
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

		String contactId = cellInfo.getContactId();
		String MailAdress = cellInfo.getMailAdress();
		if (SelectedItemList.INSTANCE.contain(Integer.valueOf(contactId))) {
			v.setBackgroundColor(Color.TRANSPARENT);

			SelectedItemList.INSTANCE.remove(Integer.valueOf(contactId));
		} else {
			v.setBackgroundColor(EditGridItemClickListener.BACKGROUND_COLOR);

			SelectedItemList.INSTANCE.add(Integer.valueOf(contactId));
			SelectedItemList.INSTANCE.addMail(MailAdress);
		}
		
		if (SelectedItemList.INSTANCE.getSize() > 0) {
			
			if(SelectedItemList.INSTANCE.getSize() == 1){
				MainActivity.setPhoneMenuVisibility(true);
				MainActivity.setSendMailtMenuVisibility(true);
				MainActivity.setAllClearVisibility(true);
				MainActivity.setAllselectVisibility(true);
			}
			else{
				MainActivity.setPhoneMenuVisibility(false);
				MainActivity.setSendMailtMenuVisibility(true);
				MainActivity.setAllClearVisibility(true);
				MainActivity.setAllselectVisibility(true);
			}
			
		} 
		else {
			MainActivity.setSendMailtMenuVisibility(false);
			MainActivity.setPhoneMenuVisibility(false);
			MainActivity.setAllClearVisibility(false);
		}

		SelectedItemList.INSTANCE.logContactIdList(TAG);
	}


	@Override
	public boolean onItemLongClick(AdapterView<?> parent, final View cell,
			int position, long id) {
		
		return false;
	}

}

