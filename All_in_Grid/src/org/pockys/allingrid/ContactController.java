package org.pockys.allingrid;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
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

public class ContactController implements OnItemClickListener,
		OnItemLongClickListener {

	private static final String TAG = "ContactController";

	private Context mContext;
	private Cursor mContactsCursor;
	private OnItemClickListener mOnItemClickListener = this;

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
	public boolean onItemLongClick(AdapterView<?> parent, final View cell,
			int position, long id) {
		Log.d(TAG, "onItemLongClick");

		assert (cell.getTag() instanceof ContactCellInfo);
		final ContactCellInfo contactCellInfo = (ContactCellInfo) cell.getTag();

		final int contactId = contactCellInfo.getContactId();
		final boolean added = containFavorites(contactCellInfo);

		AlertDialog.Builder FirstBuilder = new AlertDialog.Builder(mContext);
		FirstBuilder.setTitle("Edit - " + contactCellInfo.getDisplayName());
		ListView listView = new ListView(mContext);

		listView.setAdapter(new BaseAdapter() {
			final String[] items = { "Change Icon", "Edit Profile",
					(added) ? "Remove from Favorites" : "Add to Favorites", };

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView textView = null;
				if (convertView == null) {
					textView = (TextView) LayoutInflater.from(mContext)
							.inflate(R.layout.list_view_item, null);

				} else
					textView = (TextView) convertView;

				textView.setText(items[position]);

				// Log.d(TAG, "textView text:  " + items[position]);

				return textView;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return items.length;
			}
		});
		FirstBuilder.setView(listView);
		final AlertDialog firstDialog = FirstBuilder.create();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> container, View view,
					int position, long id) {
				firstDialog.dismiss();

				if (position == 0) {
					LayoutInflater inflater = LayoutInflater.from(mContext);

					GridView gridView = (GridView) inflater.inflate(
							R.layout.grid_view, null);
					gridView.setNumColumns(4);
					gridView.setAdapter(new IconListAdapter(mContext, 0));

					AlertDialog.Builder iconChangeDialog = new AlertDialog.Builder(
							mContext);
					iconChangeDialog.setTitle("Change Icon");
					iconChangeDialog.setView(gridView);
					final AlertDialog iconDialog = iconChangeDialog.create();

					gridView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							iconDialog.dismiss();

							IconInfo Samp = IconListLib.INSTANCE
									.getAllIconInfo(position);

							Log.d(TAG, "Check! ");

							ImageView imageView = (ImageView) cell
									.findViewById(R.id.cell_image);
							imageView.setImageResource(Samp.getImage());

							SharedPreferences.Editor editor = sharedPreferences
									.edit();
							editor.putInt(Integer.toString(contactId),
									Samp.getImage());
							editor.commit();

						}

					});

					iconDialog.show();

				}

				else if (position == 1) {

					Intent intent = new Intent(Intent.ACTION_EDIT);
					Uri contactUri = Uri.withAppendedPath(
							ContactsContract.Contacts.CONTENT_URI, ""
									+ contactId);
					intent.setData(contactUri);
					mContext.startActivity(intent);

				} else if (position == 2) {
					setFavorites(contactCellInfo, !added);

					if (MainActivity.getCurrentGroupInfo().getDisplayName() == "Favorite") {
						MainActivity
								.resetGridField(MenuController.FavoriteGroupCellInfo);
					}
				}

			}

		});

		firstDialog.show();

		return false;

	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		mOnItemClickListener = onItemClickListener;
	}

	public boolean containFavorites(ContactCellInfo contactInfo) {
		String[] PROJECTION = new String[] { ContactsContract.Data.CONTACT_ID,
				ContactsContract.Data.DISPLAY_NAME, };

		String selection = ContactsContract.Contacts.STARRED + " = '1'";
		selection += " AND " + ContactsContract.Contacts.IN_VISIBLE_GROUP
				+ " = '1'";
		selection += " AND " + ContactsContract.Data.CONTACT_ID + " = '"
				+ contactInfo.getContactId() + "'";
		Cursor cursor = mContext.getContentResolver().query(
				ContactsContract.Data.CONTENT_URI, PROJECTION, selection, null,
				null);

		int count = cursor.getCount();
		cursor.close();

		return (count > 0);
	}

	public void setFavorites(ContactCellInfo contactInfo, boolean add) {

		int contactId = contactInfo.getContactId();

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ops.add(ContentProviderOperation
				.newUpdate(ContactsContract.Contacts.CONTENT_URI)
				.withSelection(
						ContactsContract.Contacts._ID + " = '" + contactId
								+ "'", null)
				.withValue(ContactsContract.Contacts.STARRED, (add) ? 1 : 0)
				.build());

		try {
			ContentProviderResult[] results = mContext.getContentResolver()
					.applyBatch(ContactsContract.AUTHORITY, ops);

			if (results == null) {
				MainActivity.makeToast(mContext, "Something was wrong!!");
			} else if (results.length > 0) {

				String message = contactInfo.getDisplayName() + " is ";
				message += (add) ? "added to Favorites"
						: "removed from Favorites";

				MainActivity.makeToast(mContext, message);
			}
		}

		catch (OperationApplicationException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

}
