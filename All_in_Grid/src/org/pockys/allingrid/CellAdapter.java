package org.pockys.allingrid;

import java.util.ArrayList;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CellAdapter extends BaseAdapter {

	private static final String TAG = "CellAdapter";

	private Context mContext;
	private LayoutInflater layoutInflater;

	private SharedPreferences sharedPreferences;
	private SharedPreferences sharedPreferencesGroup;
	public SharedPreferences AllIcon;
	public SharedPreferences AllGroupIcon;
	ArrayList<Integer> iconList = new ArrayList<Integer>();

	private ArrayList<CellInfo> mCellInfoList;


	public CellAdapter(Context _context, ArrayList<CellInfo> cellInfoList) {
		super();

		sharedPreferences = _context.getSharedPreferences("sharePreferences",
				Context.MODE_PRIVATE);
		sharedPreferencesGroup = _context.getSharedPreferences("sharePreferencesGroup",
				Context.MODE_PRIVATE);
		AllIcon = _context.getSharedPreferences("AllIcon",
				Context.MODE_PRIVATE);
		AllGroupIcon = _context.getSharedPreferences("AllGroupIcon",
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
		
		int imageResId = 0;
		
		if (convertView == null)
			cell = layoutInflater.inflate(R.layout.cell, null);

			CellInfo cellInfo = mCellInfoList.get(position);

			
			if (cellInfo instanceof ContactCellInfo) {
				
				SharedPreferences.Editor editor = sharedPreferences.edit();

				String contactId = ((ContactCellInfo) cellInfo).getContactId();
	
				
				imageResId = sharedPreferences.getInt(
						contactId, -1);
					if (imageResId == -1) {
		
						int radomnametemp = 0;
						
						int Icontemp = AllIcon.getInt(
								Integer.toString(1), -1);
						if(Icontemp == -1)putIconData();
						
						
						IconCategoryInfo info = IconListLib.INSTANCE.getIconCategoryInfo(
								IconListLib.INSTANCE.getCurrentCategoy());
								
						int rand = (int) (Math.random() * info.getCategoryAmount() + 1);
						radomnametemp = AllIcon.getInt(
								Integer.toString(rand + info.getCategoryIndex()), -1);
						imageResId = radomnametemp;
						Log.d(TAG, "Cell preference generated. contactId: " + contactId
								+ " imageResId: " + imageResId);
		
						editor.putInt(contactId,
								 radomnametemp);
						editor.commit();
					}
				
				else if(imageResId == -2){
					
					imageResId = AllIcon.getInt(Integer.toString(IconListLib.INSTANCE.getCurrentType()), -1);
					editor.putInt(contactId,imageResId);
					editor.commit();
				}	
				
				
			ImageView imageView = (ImageView) cell
					.findViewById(R.id.cell_image);
			imageView.setImageResource(imageResId);


				if (SelectedItemList.INSTANCE.contain(Integer.valueOf(contactId))) {
					cell.setBackgroundColor(EditGridItemClickListener.BACKGROUND_COLOR);

				} else {
					cell.setBackgroundColor(Color.TRANSPARENT);

				}
				
		}

			if (cellInfo instanceof GroupCellInfo) {
				SharedPreferences.Editor editor = sharedPreferencesGroup.edit();
								
				String GroupId = ((GroupCellInfo) cellInfo).getGroupId();

			int GroupIcontemp = AllGroupIcon.getInt(
					Integer.toString(1), -1);
			if(GroupIcontemp == -1)putGroupIconData();
			
			imageResId = sharedPreferencesGroup.getInt(
					GroupId, -3);
			
			ImageView imageView = (ImageView) cell
					.findViewById(R.id.cell_image);
			
			if(imageResId == -3){
				
				imageView.setImageResource(((GroupCellInfo)cellInfo).getThumbnailResId());
						
			}
				
			
			else{
				
				imageView.setImageResource(imageResId);

				
			}
			GroupCellInfo selectedGroupInfo = SelectedItemList.INSTANCE
					.getSelectedGroupInfo();
			if (selectedGroupInfo.getDisplayName().equals(
					cellInfo.getDisplayName())) {
				cell.setBackgroundColor(MenuController.BACKGROUND_COLOR);
			} else {
				cell.setBackgroundColor(Color.TRANSPARENT);
			}
		}

		// set text
		TextView textView = (TextView) cell.findViewById(R.id.cell_label);
		textView.setText(cellInfo.getDisplayName());

		cell.setTag(cellInfo);

		return cell;

	}
	
	private void putIconData(){
		
		SharedPreferences.Editor editor = AllIcon.edit();
		editor.putInt(Integer.toString(1),R.drawable.ic_char_face001);
		editor.putInt(Integer.toString(2),R.drawable.ic_char_face002);
		editor.putInt(Integer.toString(3),R.drawable.ic_char_face003);
		editor.putInt(Integer.toString(4),R.drawable.ic_char_face004);
		editor.putInt(Integer.toString(5),R.drawable.ic_char_face005);
		editor.putInt(Integer.toString(6),R.drawable.ic_char_face006);
		editor.putInt(Integer.toString(7),R.drawable.ic_char_kinoko001);
		editor.putInt(Integer.toString(8),R.drawable.ic_char_kinoko002);
		editor.putInt(Integer.toString(9),R.drawable.ic_char_kinoko003);
		editor.putInt(Integer.toString(10),R.drawable.ic_char_kinoko004);
		editor.putInt(Integer.toString(11),R.drawable.ic_char_kinoko005);
		editor.putInt(Integer.toString(12),R.drawable.ic_char_kinoko006);
		editor.putInt(Integer.toString(13),R.drawable.ic_char_kinoko007);
		editor.putInt(Integer.toString(14),R.drawable.ic_char_kinoko008);
		editor.putInt(Integer.toString(15),R.drawable.ic_char_kinoko009);
		editor.putInt(Integer.toString(16),R.drawable.ic_char_kinoko010);
		editor.putInt(Integer.toString(17),R.drawable.ic_char_kinoko011);
		editor.putInt(Integer.toString(18),R.drawable.ic_char_mono001);
		editor.putInt(Integer.toString(19),R.drawable.ic_char_mono002);
		editor.putInt(Integer.toString(20),R.drawable.ic_char_mono003);
		editor.putInt(Integer.toString(21),R.drawable.ic_char_mono004);
		editor.putInt(Integer.toString(22),R.drawable.ic_char_mono005);
		editor.putInt(Integer.toString(23),R.drawable.ic_char_strange001);
		editor.putInt(Integer.toString(24),R.drawable.ic_char_strange002);
		editor.putInt(Integer.toString(25),R.drawable.ic_char_strange003);
		editor.putInt(Integer.toString(26),R.drawable.ic_char_strange004);
		editor.putInt(Integer.toString(27),R.drawable.ic_char_strange005);
		editor.putInt(Integer.toString(28),R.drawable.ic_char_strange006);
		editor.putInt(Integer.toString(29),R.drawable.ic_char_strange007);
		editor.putInt(Integer.toString(30),R.drawable.ic_char_strange008);

		editor.commit();
		
	}
	
	private void putGroupIconData(){
		
		SharedPreferences.Editor editor = AllGroupIcon.edit();
		editor.putInt(Integer.toString(1),R.drawable.ic_group1_001);
		editor.putInt(Integer.toString(2),R.drawable.ic_group1_002);
		editor.putInt(Integer.toString(3),R.drawable.ic_group1_003);
		editor.putInt(Integer.toString(4),R.drawable.ic_group1_004);
		editor.putInt(Integer.toString(5),R.drawable.ic_group2_001);
		editor.putInt(Integer.toString(6),R.drawable.ic_group2_002);
		editor.putInt(Integer.toString(7),R.drawable.ic_group2_003);
		editor.putInt(Integer.toString(8),R.drawable.ic_group2_004);		
		editor.putInt(Integer.toString(9),R.drawable.ic_group3_001);
		editor.putInt(Integer.toString(10),R.drawable.ic_group3_002);
		editor.putInt(Integer.toString(11),R.drawable.ic_group3_003);
		editor.putInt(Integer.toString(12),R.drawable.ic_group3_004);
		editor.putInt(Integer.toString(13),R.drawable.ic_group_all001);
		editor.putInt(Integer.toString(14),R.drawable.ic_group_all002);
		editor.putInt(Integer.toString(15),R.drawable.ic_group_all003);
		editor.putInt(Integer.toString(16),R.drawable.ic_group_all004);
		editor.putInt(Integer.toString(17),R.drawable.ic_group_favorite001);
		editor.putInt(Integer.toString(18),R.drawable.ic_group_favorite002);
		editor.putInt(Integer.toString(19),R.drawable.ic_group_favorite003);
		editor.putInt(Integer.toString(20),R.drawable.ic_group_favorite004);
		
		editor.commit();
	}
		
				
	
}
