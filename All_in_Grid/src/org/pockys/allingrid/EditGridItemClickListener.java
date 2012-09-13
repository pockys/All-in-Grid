package org.pockys.allingrid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class EditGridItemClickListener implements OnItemClickListener {

	public static final String TAG = "EditClickListener";
	public static final int BACKGROUND_COLOR = Color.rgb(132, 189, 255);
	// public static final float ALPHA_VALUE = 0.7f;

	private Context mContext;

	public EditGridItemClickListener(Context context) {
		mContext = context;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		assert (view.getTag() instanceof ContactCellInfo) : "cellInfo is not instance of contact cellInfo!!";

		ContactCellInfo cellInfo = (ContactCellInfo) view.getTag();

		int contactId = cellInfo.getContactId();
		if (SelectedItemList.INSTANCE.contain(contactId)) {
			view.setBackgroundColor(Color.TRANSPARENT);

			SelectedItemList.INSTANCE.remove(contactId);
		} else {
			view.setBackgroundColor(BACKGROUND_COLOR);

			SelectedItemList.INSTANCE.add(contactId);
		}
		// EditActivity.reDrawGridField();

		SelectedItemList.INSTANCE.logContactIdList(TAG);
	}
}
