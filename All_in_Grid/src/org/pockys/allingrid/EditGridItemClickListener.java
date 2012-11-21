package org.pockys.allingrid;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class EditGridItemClickListener implements OnItemClickListener {

	public static final String TAG = "EditClickListener";
	public static final int BACKGROUND_COLOR = Color.rgb(132, 189, 255);

	private Context mContext;

	public EditGridItemClickListener(Context context) {
		mContext = context;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		assert (view.getTag() instanceof ContactCellInfo) : "cellInfo is not instance of contact cellInfo!!";

		ContactCellInfo cellInfo = (ContactCellInfo) view.getTag();

		String contactId = cellInfo.getContactId();
		if (SelectedItemList.INSTANCE.contain(Integer.valueOf(contactId))) {
			view.setBackgroundColor(Color.TRANSPARENT);

			SelectedItemList.INSTANCE.remove(Integer.valueOf(contactId));
		} else {
			view.setBackgroundColor(BACKGROUND_COLOR);

			SelectedItemList.INSTANCE.add(Integer.valueOf(contactId));
		}
		// EditActivity.reDrawGridField();
		GroupCellInfo currentGroupInfo;
		currentGroupInfo = EditActivity.getCurrentGroupInfo();
		ContactControllerEdit contactController = new ContactControllerEdit(mContext,
				EditActivity.getSelection(currentGroupInfo));
		ArrayList<GridView> gridViewList = contactController
				.getGridFieldViews(4, 4);
		int GridCount = contactController.getcount();
		if (SelectedItemList.INSTANCE.getSize() > 0) {
			
			
			EditActivity.setAllClearVisibility(true);
			EditActivity.setAllselectVisibility(true);
			EditActivity.setDisconnectMenuVisibility(true);
			if((GridCount + 1)==SelectedItemList.INSTANCE.getSize() ){
				EditActivity.setAllselectVisibility(false);
					
			}
	
	} 
	else {
		EditActivity.setAllselectVisibility(true);
		EditActivity.setAllClearVisibility(false);
		EditActivity.setDisconnectMenuVisibility(false);
	}
		SelectedItemList.INSTANCE.logContactIdList(TAG);
	}
		
	public static void makeToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
}
