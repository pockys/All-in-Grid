package org.pockys.allingrid;

import java.security.AllPermission;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IconListAdapter extends BaseAdapter {

	private static final String TAG = "test";
	
	public SharedPreferences AllIcon;
	
	private LayoutInflater mLayoutInflater;
	private Context mContext;

	private int mType;

	public IconListAdapter(Context context, int type) {
		super();
		mContext = context;
		mType = type;

		mLayoutInflater = LayoutInflater.from(mContext);
		AllIcon = context.getSharedPreferences("AllIcon",
				Context.MODE_PRIVATE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		LinearLayout cell = (LinearLayout) view;
		if (view == null)
			cell = (LinearLayout) mLayoutInflater.inflate(R.layout.cell, null);
		// cell.setOrientation(LinearLayout.HORIZONTAL);

		IconInfo iconInfo = (IconInfo) getItem(position);

		ImageView icon = (ImageView) cell.findViewById(R.id.cell_image);
		icon.setImageResource(iconInfo.getImage());

		TextView iconTitle = (TextView) cell.findViewById(R.id.cell_label);
		// iconTitle.setText(iconInfo.getName());

		// mLayoutInflater = LayoutInflater.from(mContext);
		// if (view == null)
		// view = mLayoutInflater.inflate(R.layout.subrow, parent, false);
		//
		// IconInfo icon = (IconInfo) getItem(position);
		//
		// TextView countryname = (TextView) view
		// .findViewById(R.id.countryname);
		// countryname.setText(icon.getInfo());
		//
		// ImageView app_icon = (ImageView) view.findViewById(R.id.app_icon);
		// app_icon.setImageResource(icon.getImage());

		// }

		return cell;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		if (mType == 0) {
			return AllIcon.getAll().size();//IconListLib.INSTANCE.getAllIconInfoSize();
		} else {

			return IconListLib.INSTANCE.getListViewIconInfoSize();

		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		int temp = AllIcon.getInt(Integer.toString(position + 1), -1);
		IconInfo tempture = new IconInfo(temp, null);

		if (mType == 0) {
			return tempture;
		} else {
			return IconListLib.INSTANCE.getListViewIconInfo(position);

		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
