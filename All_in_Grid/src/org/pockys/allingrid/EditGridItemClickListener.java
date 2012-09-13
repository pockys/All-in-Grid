package org.pockys.allingrid;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class EditGridItemClickListener implements OnItemClickListener {

	public static final String TAG = "EditClickListener";
	public static final int BACKGROUND_COLOR = Color.rgb(255, 160, 10);
	public static final float ALPHA_VALUE = 0.7f;

	private Context mContext;
	private static ArrayList<Integer> mSelectedContactIdList = new ArrayList<Integer>();

	public static boolean containContactId(int contactId) {
		return mSelectedContactIdList.contains(contactId);
	}

	public static int getSelectedContactIdListSize() {
		return mSelectedContactIdList.size();
	}

	public static Iterator<Integer> getSelectedContactIdListIterator() {
		return mSelectedContactIdList.iterator();
	}

	public EditGridItemClickListener(Context context) {
		mContext = context;
	}

	public static void clear() {
		mSelectedContactIdList.clear();

		Log.d(TAG, "mSelected ContactId List clear");
	}

	public void logContactIdList() {
		String list = "";
		for (int i = 0; i < mSelectedContactIdList.size(); i++) {
			list += " " + mSelectedContactIdList.get(i);
		}
		Log.d(TAG, "mSelected ContactId List\n" + list);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		// ImageView imageView = (ImageView) view.findViewById(R.id.cell_image);

		assert (view.getTag() instanceof ContactCellInfo) : "cellInfo is not instance of contact cellInfo!!";

		ContactCellInfo cellInfo = (ContactCellInfo) view.getTag();
		int contactId = cellInfo.getContactId();
		if (mSelectedContactIdList.contains(contactId)) {
			view.setBackgroundColor(Color.TRANSPARENT);
			mSelectedContactIdList.remove(Integer.valueOf(contactId));
		} else {
			view.setBackgroundColor(BACKGROUND_COLOR);
			mSelectedContactIdList.add(contactId);
		}

		logContactIdList();
	}
}
