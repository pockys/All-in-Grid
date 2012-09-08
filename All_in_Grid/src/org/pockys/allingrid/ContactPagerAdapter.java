package org.pockys.allingrid;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class ContactPagerAdapter extends PagerAdapter {

	private static final String TAG = "ContactPagerAdapter";
	private static final int MAX_CELL_NUM = 16;

	private Context mContext;
	private Cursor mContactsCursor;

	private GridView[] mPagedGridViews;

	public ContactPagerAdapter(Context context) {
		super();
		mContext = context;
		mContactsCursor = getContacts();

		int viewNum = mContactsCursor.getCount() / MAX_CELL_NUM;
		mPagedGridViews = new GridView[viewNum];
		for (int i = 0; i < viewNum; i++) {
			mPagedGridViews[i] = (GridView) LayoutInflater.from(mContext).inflate(
					R.layout.grid_view, null);
			mPagedGridViews[i].setAdapter(new ContactAdapter(mContext,
					getContactsList()));
		}

	}

	@Override
	public int getCount() {
		return (int) (mContactsCursor.getCount() / MAX_CELL_NUM);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		container.addView(mPagedGridViews[position]);

		return mPagedGridViews[position];
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);

	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);

	}

	public ContactAdapter.Contact[] getContactsList() {

		ArrayList<ContactAdapter.Contact> contactsArrayList = new ArrayList<ContactAdapter.Contact>();

		for (int i = 0; mContactsCursor.moveToNext() && i < MAX_CELL_NUM; i++) {

			String displayName = mContactsCursor
					.getString(mContactsCursor
							.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
			String uriString = mContactsCursor.getString(mContactsCursor
					.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));

			Uri thumbnailUri = null;
			if (uriString != null)
				thumbnailUri = Uri.parse(uriString);

			ContactAdapter.Contact contact = new ContactAdapter.Contact();
			contact.displayName = displayName;
			contact.thumbnailUri = thumbnailUri;

			contactsArrayList.add(contact);
		}

		ContactAdapter.Contact[] contactsList = new ContactAdapter.Contact[contactsArrayList
				.size()];
		for (int i = 0; i < contactsList.length; i++)
			contactsList[i] = contactsArrayList.get(i);

		return contactsList;
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
