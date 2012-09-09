package org.pockys.allingrid;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ShortAdapter extends BaseAdapter {

	
	

	public Context context;
	public Cursor contactsCursor;
	public Contact[] contactsList;
	public Uri contactUri;
	
	ContentResolver cr;

	public class Contact {
		String displayName;
		Uri thumbnailUri;
	}

	public ShortAdapter(Context _context) {
		super();
		context = _context;

		contactUri = ContactsContract.Contacts.CONTENT_URI;
		contactsCursor = getContacts();

		contactsList = new Contact[contactsCursor.getCount()];
		
		cr = context.getContentResolver();

		for (int i = 0; contactsCursor.moveToNext(); i++) {
			String displayName = contactsCursor
					.getString(contactsCursor
							.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
			String uriString = contactsCursor.getString(contactsCursor
					.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));

			Uri thumbnailUri = null;
			if (uriString != null)
				thumbnailUri = Uri.parse(uriString);

			contactsList[i] = new Contact();
			contactsList[i].displayName = displayName;
			contactsList[i].thumbnailUri = thumbnailUri;
		}

	}

	public Cursor getContacts() {

		contactUri = ContactsContract.Contacts.CONTENT_URI;
		String[] PROJECTION = new String[] { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.PHOTO_URI,
				ContactsContract.Contacts.DISPLAY_NAME };
		String CONTACTS_SORT_ORDER = ContactsContract.Contacts.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";

		return context.getContentResolver().query(contactUri, PROJECTION, null,
				null, CONTACTS_SORT_ORDER);
	}
	
	public Cursor ReturnCursor(){
		
		return contactsCursor;
	}
	
	public ContentResolver ReturnResolver(){
		
		return cr;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contactsList.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}