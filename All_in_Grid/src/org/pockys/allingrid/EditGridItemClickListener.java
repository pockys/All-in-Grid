package org.pockys.allingrid;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class EditGridItemClickListener implements OnItemClickListener {

	public static final String TAG = "EditClickListener";
	public static final int BACKGROUND_COLOR = Color.rgb(255, 160, 10);
	public static final float ALPHA_VALUE = 0.7f;

	private Context mContext;

	public EditGridItemClickListener(Context context) {
		mContext = context;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		// ImageView imageView = (ImageView) view.findViewById(R.id.cell_image);

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

		SelectedItemList.INSTANCE.logContactIdList(TAG);
	}
}
