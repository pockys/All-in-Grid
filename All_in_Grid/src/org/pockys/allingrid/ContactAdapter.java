package org.pockys.allingrid;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter {

	// private static final int MAX_CELL_NUM = 16;
	// private static final int MAX_NAME_LEN = 8;
	static final String TAG = "ContactAdapter";

	private final Bitmap defaultBitmap;

	private Context context;
	private Cursor contactsCursor;
	private LayoutInflater layoutInflater;

	private Contact[] contactsList;

	private class Contact {
		String displayName;
		Uri thumbnailUri;
	}

	public ContactAdapter(Context _context) {
		super();
		context = _context;

		layoutInflater = LayoutInflater.from(context);
		defaultBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_launcher);

		contactsCursor = getContacts();

		contactsList = new Contact[contactsCursor.getCount()];

		for (int i = 0; contactsCursor.moveToNext(); i++) {
			String displayName = contactsCursor
					.getString(contactsCursor
							.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
			String uriString = contactsCursor.getString(contactsCursor
					.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
			// if (displayName.length() >= MAX_NAME_LEN)
			// displayName = displayName.substring(0, MAX_NAME_LEN) + "...";

			// displayName.

			Uri thumbnailUri = null;
			if (uriString != null)
				thumbnailUri = Uri.parse(uriString);

			contactsList[i] = new Contact();
			contactsList[i].displayName = displayName;
			contactsList[i].thumbnailUri = thumbnailUri;
		}

	}

	public Cursor getContacts() {

		Uri contactUri = ContactsContract.Contacts.CONTENT_URI;
		String[] PROJECTION = new String[] { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.PHOTO_URI,
				ContactsContract.Contacts.DISPLAY_NAME };
		String CONTACTS_SORT_ORDER = ContactsContract.Contacts.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";

		return context.getContentResolver().query(contactUri, PROJECTION, null,
				null, CONTACTS_SORT_ORDER);
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

		View cell = convertView;
		if (convertView == null)
			cell = layoutInflater.inflate(R.layout.cell, null);

		// set image
		ImageView imageView = (ImageView) cell.findViewById(R.id.cell_image);
		if (contactsList[position].thumbnailUri != null)
			imageView.setImageURI(contactsList[position].thumbnailUri);
		else
			imageView.setImageBitmap(defaultBitmap);

		// set text
		TextView textView = (TextView) cell.findViewById(R.id.cell_label);
		textView.setText(contactsList[position].displayName);
		Log.d(TAG, "generated: " + position);

		return cell;

	}

}
