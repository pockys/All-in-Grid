package org.pockys.allingrid;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ContactController {

	private Context mContext;
	private Cursor mContactsCursor;

	public ContactController(Context context) {
		this(context, null);
	}

	public ContactController(Context context, String selection) {
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

		Uri contactUri = ContactsContract.Data.CONTENT_URI;

		String[] PROJECTION = new String[] { ContactsContract.Data.CONTACT_ID,
				ContactsContract.Data.PHOTO_URI,
				ContactsContract.Data.DISPLAY_NAME, };
		String CONTACTS_SORT_ORDER = ContactsContract.Data.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";

		return mContext.getContentResolver().query(contactUri, PROJECTION,
				selection, null, CONTACTS_SORT_ORDER);
	}

	public ArrayList<GridView> getGridFieldViews(final int numColumns,
			final int numRows, OnItemClickListener itemClickListener) {
		ArrayList<GridView> gridViewList = new ArrayList<GridView>();

		// Contact contact = new Contact(this);
		final int numCells = numColumns * numRows;
		for (int i = 0; i < this.getSize() / numCells; i++) {
			GridView gridView = (GridView) LayoutInflater.from(mContext)
					.inflate(R.layout.grid_view, null);
			gridView.setNumColumns(numColumns);
			gridView.setAdapter(new CellAdapter(mContext, this
					.getContactsList(numCells)));
			gridView.setOnItemClickListener(itemClickListener);

			gridViewList.add(gridView);
		}
		if (this.getSize() % numCells != 0) {
			GridView gridView = (GridView) LayoutInflater.from(mContext)
					.inflate(R.layout.grid_view, null);
			gridView.setNumColumns(numColumns);
			gridView.setAdapter(new CellAdapter(mContext, this
					.getContactsList(numCells)));
			gridView.setOnItemClickListener(itemClickListener);

			gridViewList.add(gridView);
		}

		return gridViewList;
	}

}