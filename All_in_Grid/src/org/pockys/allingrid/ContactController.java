package org.pockys.allingrid;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
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

public class ContactController implements OnItemClickListener,
		OnItemLongClickListener {

	private static final String TAG = "ContactController";

	private Context mContext;
	private Cursor mContactsCursor;
	private OnItemClickListener mOnItemClickListener = this;
	// private String tab = "Fight";

	private SharedPreferences sharedPreferences;

	public ContactController(Context context) {
		this(context, null);
	}

	public ContactController(Context context, String selection) {
		mContext = context;
		mContactsCursor = getContacts(selection);
		sharedPreferences = context.getSharedPreferences("sharePreferences",
				Context.MODE_PRIVATE);
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
				ContactsContract.Data.DISPLAY_NAME, };
		String CONTACTS_SORT_ORDER = ContactsContract.Data.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";

		if (selection != null || selection == "")
			selection += " AND ";
		else
			selection = "";

		selection += ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'";
		return mContext.getContentResolver().query(
				ContactsContract.Data.CONTENT_URI, PROJECTION, selection, null,
				CONTACTS_SORT_ORDER);
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
		// get contact uri from contact id
		ContactCellInfo contactCellInfo = (ContactCellInfo) v.getTag();
		String contactIdString = String.valueOf(contactCellInfo.getContactId());
		final Uri contactUri = Uri.withAppendedPath(
				ContactsContract.Contacts.CONTENT_URI,
				Uri.encode(contactIdString));

		final QuickContactBadge badge = new QuickContactBadge(mContext);
		badge.assignContactUri(contactUri);
		badge.setMode(ContactsContract.QuickContact.MODE_LARGE);
		((ViewGroup) v).addView(badge);
		badge.performClick();
		((ViewGroup) v).removeView(badge);

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int arg2,
			long arg3) {

		Log.d(TAG, "onItemLongClick");

		final View cell = view;

		assert (view.getTag() instanceof ContactCellInfo);
		ContactCellInfo contactCellInfo = (ContactCellInfo) view.getTag();

		final int contactId = contactCellInfo.getContactId();

		final CharSequence[] items = { "Change Icon", "Edit Profile" };

		AlertDialog.Builder FirstBuilder = new AlertDialog.Builder(mContext);
		FirstBuilder.setTitle("Pick a color");
		OnClickListener listner = new OnClickListener() {

			@Override
			public void onClick(final DialogInterface dia, int arg1) {
				if (arg1 == 0) {
					CustomAdapter adp = new CustomAdapter(mContext,
							R.layout.row);

					LayoutInflater inflater = LayoutInflater.from(mContext);
					ListView lv = (ListView) inflater.inflate(
							R.layout.listview, null);

					lv.setAdapter(adp);
					lv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> v, View arg1,
								int arg2, long arg3) {

							IconInfo Samp = IconListLib.INSTANCE
									.getIconInfo(arg2);

							Log.d(TAG, "Check! ");

							ImageView imageView = (ImageView) cell
									.findViewById(R.id.cell_image);
							imageView.setImageResource(Samp.getImage());

							SharedPreferences.Editor editor = sharedPreferences
									.edit();
							editor.putInt(Integer.toString(contactId), arg2);
							editor.commit();

							// test
							Intent intent = new Intent(mContext,
									MainActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							mContext.startActivity(intent);

						}

					});

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							mContext);

					alertDialogBuilder.setTitle("Change Icon");
					alertDialogBuilder.setView(lv);

					alertDialogBuilder.create().show();

				}

				else if (arg1 == 1) {

					Intent intent = new Intent(Intent.ACTION_EDIT);
					Uri contactUri = Uri.withAppendedPath(
							ContactsContract.Contacts.CONTENT_URI, ""
									+ contactId);
					intent.setData(contactUri);

					mContext.startActivity(intent);

				}

			}

		};
		FirstBuilder.setItems(items, listner);
		FirstBuilder.create().show();

		return false;

	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		mOnItemClickListener = onItemClickListener;
	}

}

class CustomAdapter extends BaseAdapter {

	LayoutInflater mLayoutInflater;
	Context context;
	String tag = "test";

	public CustomAdapter(Context _context, int resource) {
		super();
		context = _context;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {

		mLayoutInflater = LayoutInflater.from(context);
		if (v == null)
			v = mLayoutInflater.inflate(R.layout.row, parent, false);

		IconInfo icon = (IconInfo) getItem(position);

		TextView countrycode = (TextView) v.findViewById(R.id.countrycode);
		countrycode.setText(icon.getName());

		TextView countryname = (TextView) v.findViewById(R.id.countryname);
		countryname.setText(icon.getInfo());

		ImageView app_icon = (ImageView) v.findViewById(R.id.app_icon);
		app_icon.setImageResource(icon.getImage());

		return v;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return IconListLib.INSTANCE.getIconInfoSize();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return IconListLib.INSTANCE.getIconInfo(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
