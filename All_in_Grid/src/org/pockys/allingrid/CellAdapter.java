package org.pockys.allingrid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CellAdapter extends BaseAdapter {

	private static final String TAG = "ContactAdapter";

	private Context mContext;
	private LayoutInflater layoutInflater;

	private SharedPreferences sharedPreferences;

	ArrayList<Integer> iconList = new ArrayList<Integer>();

	private ArrayList<CellInfo> mCellInfoList;

	public CellAdapter(Context _context, ArrayList<CellInfo> cellInfoList) {
		super();

		sharedPreferences = _context.getSharedPreferences("sharePreferences",
				Context.MODE_PRIVATE);

		mContext = _context;
		layoutInflater = LayoutInflater.from(mContext);
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

		if (cellInfo instanceof ContactCellInfo) {

			int contactId = ((ContactCellInfo) cellInfo).getContactId();

			int rand = sharedPreferences
					.getInt(Integer.toString(contactId), -1);
			if (rand == -1) {

				SharedPreferences.Editor editor = sharedPreferences.edit();
				rand = (int) (Math.random() * IconListLib.INSTANCE
						.getIconInfoSize());
				editor.putInt(Integer.toString(contactId), rand);
				editor.commit();
			}
			IconInfo randomname = IconListLib.INSTANCE.getIconInfo(rand);
			int Randname = randomname.getImage();
			ImageView imageView = (ImageView) cell
					.findViewById(R.id.cell_image);
			imageView.setImageResource(Randname);

			if (((Activity) mContext).getClass().equals(EditActivity.class)) {
				if (SelectedItemList.INSTANCE.contain(contactId)) {
					cell.setBackgroundColor(EditGridItemClickListener.BACKGROUND_COLOR);
				} else {
					cell.setBackgroundColor(Color.TRANSPARENT);
				}
			}
		}

		if (cellInfo instanceof GroupCellInfo) {
			ImageView imageView = (ImageView) cell
					.findViewById(R.id.cell_image);

			imageView.setImageResource(cellInfo.getThumbnailResId());
		}

		// set text
		TextView textView = (TextView) cell.findViewById(R.id.cell_label);
		textView.setText(cellInfo.getDisplayName());

		// if (cellInfo instanceof ContactCellInfo)
		// cell.setTag(((ContactCellInfo) cellInfo).getContactId());

		cell.setTag(cellInfo);

		return cell;

	}

}
