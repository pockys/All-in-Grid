package org.pockys.allingrid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
	private LayoutInflater layoutInflater;

	private Contact[] mContactsList;

	static class Contact {
		String displayName;
		Uri thumbnailUri;
	}

	public ContactAdapter(Context _context, Contact[] contactList) {
		super();
		context = _context;

		layoutInflater = LayoutInflater.from(context);
		defaultBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_launcher);

		mContactsList = contactList;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mContactsList.length;
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
		if (mContactsList[position].thumbnailUri != null)
			imageView.setImageURI(mContactsList[position].thumbnailUri);
		else
			imageView.setImageBitmap(defaultBitmap);

		// set text
		TextView textView = (TextView) cell.findViewById(R.id.cell_label);
		textView.setText(mContactsList[position].displayName);
		Log.d(TAG, "generated: " + position);

		return cell;

	}

}
