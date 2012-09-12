package org.pockys.allingrid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CellAdapter extends BaseAdapter {

	static final String TAG = "ContactAdapter";

	private Context mContext;
	private LayoutInflater layoutInflater;

	SharedPreferences sharedPreferences;

	ArrayList<Integer> iconList = new ArrayList<Integer>();

	private ArrayList<CellInfo> mCellInfoList;

	public CellAdapter(Context _context, ArrayList<CellInfo> cellInfoList) {
		super();

		sharedPreferences = _context.getSharedPreferences("sharePreferences",
				Context.MODE_PRIVATE);

		iconList.add(R.drawable.ic_action_search);
		iconList.add(R.drawable.ic_launcher);
		iconList.add(R.drawable.ic_menu_edit);
		iconList.add(R.drawable.ic_menu_phone);
		iconList.add(R.drawable.ic_menu_phone_ring);
		iconList.add(R.drawable.ic_menu_search);
		iconList.add(R.drawable.ic_menu_sort);
		iconList.add(R.drawable.ic_gotouti_001);
		iconList.add(R.drawable.ic_gotouti_002);
		iconList.add(R.drawable.ic_gotouti_003);
		iconList.add(R.drawable.ic_gotouti_004);
		iconList.add(R.drawable.ic_gotouti_005);
		iconList.add(R.drawable.ic_gotouti_006);
		iconList.add(R.drawable.ic_gotouti_007);
		iconList.add(R.drawable.ic_gotouti_008);
		iconList.add(R.drawable.ic_tizukigou_001);
		iconList.add(R.drawable.ic_tizukigou_002);
		iconList.add(R.drawable.ic_tizukigou_003);
		iconList.add(R.drawable.ic_tizukigou_004);
		iconList.add(R.drawable.ic_tizukigou_005);
		iconList.add(R.drawable.ic_tizukigou_006);
		iconList.add(R.drawable.ic_tizukigou_007);
		iconList.add(R.drawable.ic_tizukigou_008);
		int iconListSize = iconList.size();

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

			int rand = sharedPreferences.getInt(Integer.toString(contactId), 0);
			if (rand == 0) {

				SharedPreferences.Editor editor = sharedPreferences.edit();
				rand = (int) (Math.random() * iconList.size());
				editor.putInt(Integer.toString(contactId), rand);
				editor.commit();
			}

			int randomname = iconList.get(rand);
			ImageView imageView = (ImageView) cell
					.findViewById(R.id.cell_image);
			cellInfo.setThumbnail(randomname);
			imageView.setImageResource(randomname);

			if (((Activity) mContext).getClass().equals(EditActivity.class)
					&& EditGridItemClickListener.containContactId(contactId)) {
				// imageView.setAlpha(EditClickListener.ALPHA_VALUE);
				// imageView
				// .setBackgroundColor(EditClickListener.BACKGROUND_COLOR);

				// cell.setAlpha(EditClickListener.ALPHA_VALUE);
				cell.setBackgroundColor(EditGridItemClickListener.BACKGROUND_COLOR);

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
