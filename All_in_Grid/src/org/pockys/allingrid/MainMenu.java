package org.pockys.allingrid;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class MainMenu implements OnItemClickListener {

	private static final String TAG = "MainMenu";

	private ArrayList<CellInfo> mMenuList = new ArrayList<CellInfo>();

	private Iterator<CellInfo> it;

	private Context mContext;

	public int getSize() {
		return mMenuList.size();
	}

	public MainMenu(Context context) {
		mContext = context;

		CellInfo sortCell = new CellInfo();
		sortCell.setDisplayName("Sort");
		mMenuList.add(sortCell);

		CellInfo editCell = new CellInfo();
		editCell.setDisplayName("Edit");
		mMenuList.add(editCell);

		CellInfo addCell = new CellInfo();
		addCell.setDisplayName("Add");
		mMenuList.add(addCell);

		CellInfo sizeCell = new CellInfo();
		sizeCell.setDisplayName("Size");
		mMenuList.add(sizeCell);

		CellInfo searchCell = new CellInfo();
		searchCell.setDisplayName("Search");
		mMenuList.add(searchCell);

		CellInfo testCell = new CellInfo();
		testCell.setDisplayName("Test");
		mMenuList.add(testCell);

		it = mMenuList.iterator();

	}

	public ArrayList<CellInfo> getMenuList(int maxSize) {

		ArrayList<CellInfo> menuList = new ArrayList<CellInfo>();

		maxSize = Math.min(maxSize, mMenuList.size());
		for (int i = 0; i < maxSize && it.hasNext(); i++)
			menuList.add(it.next());

		return menuList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		TextView textView = (TextView) v.findViewById(R.id.cell_label);
		String displayName = textView.getText().toString();

		if (displayName == "Add") {
			Intent intent = new Intent(Intent.ACTION_INSERT);
			intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
			mContext.startActivity(intent);
		}

	}

}
