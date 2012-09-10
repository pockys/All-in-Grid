package org.pockys.allingrid;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class Contact {

	private Context mContext;
	private Cursor mContactsCursor;

	public Contact(Context context) {
		mContext = context;
		mContactsCursor = getContacts();
	}

	public int getSize() {
		return mContactsCursor.getCount();
	}

	public ArrayList<CellInfo> getContactsList(int maxSize) {

		ArrayList<CellInfo> contactsArrayList = new ArrayList<CellInfo>();

		for (int i = 0; mContactsCursor.moveToNext() && i < maxSize; i++) {

			String displayName = mContactsCursor.getString(mContactsCursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			String uriString = mContactsCursor.getString(mContactsCursor
					.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
			String contactIdString = mContactsCursor.getString(mContactsCursor
					.getColumnIndex(ContactsContract.Contacts._ID));

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

	public Cursor getContacts() {

		Uri contactUri = ContactsContract.Contacts.CONTENT_URI;
		String[] PROJECTION = new String[] { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.PHOTO_URI,
				ContactsContract.Contacts.DISPLAY_NAME };
		String CONTACTS_SORT_ORDER = ContactsContract.Contacts.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";

		return mContext.getContentResolver().query(contactUri, PROJECTION,
				null, null, CONTACTS_SORT_ORDER);
	}

}
