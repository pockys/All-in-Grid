package org.pockys.allingrid;

import java.util.ArrayList;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.QuickContactBadge;

public class MainActivity extends Activity implements OnItemClickListener {

	static final String TAG = "MainActivity";
	private ViewPager gridField;
	private ViewPager mainField;
	private int gridFieldCurrentItem = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	}

	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume: gridFieldCurrentItem: " + gridFieldCurrentItem);

		gridField = (ViewPager) findViewById(R.id.grid_field);
		gridField.setAdapter(new CellPagerAdapter(getGridFieldViews(4, 4)));
		gridField.setCurrentItem(gridFieldCurrentItem);

		mainField = (ViewPager) findViewById(R.id.menu_field);
		mainField.setAdapter(new CellPagerAdapter(getMenuFieldViews(4)));

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
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

		String contactIdString = v.getTag().toString();

		// get contact uri from contact id
		Uri contactUri = Uri.withAppendedPath(
				ContactsContract.Contacts.CONTENT_URI,
				Uri.encode(contactIdString));
		// Toast.makeText(MainActivity.this,
		// "contactUri: " + contactUri + " contactId: " + contactIdString,
		// Toast.LENGTH_SHORT).show();

		QuickContactBadge badge = new QuickContactBadge(this);
		badge.assignContactUri(contactUri);
		badge.setMode(ContactsContract.QuickContact.MODE_LARGE);
		badge.setImageResource(R.drawable.ic_launcher);
		badge.performClick();

	}

	public void onPause() {
		super.onPause();
		gridFieldCurrentItem = gridField.getCurrentItem();
		Log.d(TAG, "onPause: gridFieldCurrentItem: " + gridFieldCurrentItem);
	}
}
