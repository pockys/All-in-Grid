package org.pockys.allingrid;

import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CellAdapter extends BaseAdapter {

	static final String TAG = "ContactAdapter";

	private Context context;
	private LayoutInflater layoutInflater;

	private ArrayList<CellInfo> mCellInfoList;

	static class Contact {
		String displayName;
		Uri thumbnailUri;
	}

	public CellAdapter(Context _context, ArrayList<CellInfo> cellInfoList) {
		super();
		context = _context;
		layoutInflater = LayoutInflater.from(context);
		mCellInfoList = cellInfoList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCellInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View cell = convertView;
		if (convertView == null)
			cell = layoutInflater.inflate(R.layout.cell, null);

		CellInfo cellInfo = mCellInfoList.get(position);

		// set image
		ImageView imageView = (ImageView) cell.findViewById(R.id.cell_image);
		if (!cellInfo.isThereNoThumbnail())
			imageView.setImageURI(cellInfo.getThumbnailUri());

		if (cellInfo instanceof GroupCellInfo)
			imageView.setImageResource(cellInfo.getThumbnailResId());

		// set text
		TextView textView = (TextView) cell.findViewById(R.id.cell_label);
		textView.setText(cellInfo.getDisplayName());

		// if (cellInfo instanceof ContactCellInfo)
		// cell.setTag(((ContactCellInfo) cellInfo).getContactId());

		cell.setTag(cellInfo);

		return cell;

	}

}
