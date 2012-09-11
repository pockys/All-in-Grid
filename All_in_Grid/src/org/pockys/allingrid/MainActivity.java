package org.pockys.allingrid;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.QuickContactBadge;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {

	static final String TAG = "MainActivity";
	private ViewPager gridField;
	private ViewPager menuField;
	private int gridFieldCurrentItem = 0;

	private int menuFieldCurrentItem = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ActionBar actionBar = getActionBar();
		// actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.show();
	}

	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume: gridFieldCurrentItem: " + gridFieldCurrentItem);

		gridField = (ViewPager) findViewById(R.id.grid_field);
		gridField.setAdapter(new CellPagerAdapter(getGridFieldViews(4, 4)));
		gridField.setCurrentItem(gridFieldCurrentItem);

		menuField = (ViewPager) findViewById(R.id.menu_field);
		menuField.setAdapter(new CellPagerAdapter(getMenuFieldViews(4)));
		menuField.setCurrentItem(menuFieldCurrentItem);

	}

	private ArrayList<GridView> getGridFieldViews(final int numColumns,
			final int numRows) {
		ArrayList<GridView> gridViewList = new ArrayList<GridView>();

		Contact contact = new Contact(this);
		final int numCells = numColumns * numRows;
		for (int i = 0; i < contact.getSize() / numCells; i++) {
			GridView gridView = (GridView) LayoutInflater.from(this).inflate(
					R.layout.grid_view, null);
			gridView.setNumColumns(numColumns);
			gridView.setAdapter(new CellAdapter(this, contact
					.getContactsList(numCells)));
			gridView.setOnItemClickListener(this);

			gridViewList.add(gridView);
		}
		if (contact.getSize() % numCells != 0) {
			GridView gridView = (GridView) LayoutInflater.from(this).inflate(
					R.layout.grid_view, null);
			gridView.setNumColumns(numColumns);
			gridView.setAdapter(new CellAdapter(this, contact
					.getContactsList(numCells)));
			gridView.setOnItemClickListener(this);

			gridViewList.add(gridView);
		}

		return gridViewList;
	}

	private ArrayList<GridView> getMenuFieldViews(final int numColumns) {
		ArrayList<GridView> menuViewList = new ArrayList<GridView>();
		final int numCells = numColumns;

		MainMenu mainMenu = new MainMenu(this);
		Log.d(TAG, "getMenuFieldVIews. numCells: " + numCells
				+ "mainMenu size: " + mainMenu.getSize());

		for (int i = 0; i < mainMenu.getSize() / numCells; i++) {
			GridView menuView = (GridView) LayoutInflater.from(this).inflate(
					R.layout.grid_view, null);
			menuView.setNumColumns(numColumns);
			menuView.setAdapter(new CellAdapter(this, mainMenu
					.getMenuList(numCells)));
			menuView.setOnItemClickListener(mainMenu);
			menuViewList.add(menuView);
		}
		if (mainMenu.getSize() % numCells != 0) {
			GridView menuView = (GridView) LayoutInflater.from(this).inflate(
					R.layout.grid_view, null);
			menuView.setNumColumns(numColumns);
			menuView.setAdapter(new CellAdapter(this, mainMenu
					.getMenuList(numCells)));
			menuView.setOnItemClickListener(mainMenu);
			menuViewList.add(menuView);
		}

		return menuViewList;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		makeToast(item.getTitle().toString());

		Intent intent;
		switch (item.getItemId()) {
		case R.id.menu_phone:
			intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
			startActivity(intent);
			break;
		case R.id.menu_sort:

			break;
		case R.id.menu_search:

			break;
		case R.id.menu_edit:

			break;
		case R.id.menu_add:
			// makeToast("Add");
			intent = new Intent(Intent.ACTION_INSERT);
			intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
			this.startActivity(intent);
			break;
		}

		return true;
	}

	public void makeToast(String message) {
		// with jam obviously
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

		// get contact uri from contact id

		ContactCellInfo contactCellInfo = (ContactCellInfo) v.getTag();
		String contactIdString = String.valueOf(contactCellInfo.getContactId());
		Uri contactUri = Uri.withAppendedPath(
				ContactsContract.Contacts.CONTENT_URI,
				Uri.encode(contactIdString));

		QuickContactBadge badge = new QuickContactBadge(this);
		badge.assignContactUri(contactUri);
		badge.setMode(ContactsContract.QuickContact.MODE_LARGE);
		((ViewGroup) v).addView(badge);
		badge.performClick();
		((ViewGroup) v).removeView(badge);

	}

	public void onPause() {
		super.onPause();
		gridFieldCurrentItem = gridField.getCurrentItem();
		Log.d(TAG, "onPause: gridFieldCurrentItem: " + gridFieldCurrentItem);

		menuFieldCurrentItem = menuField.getCurrentItem();

	}
}
