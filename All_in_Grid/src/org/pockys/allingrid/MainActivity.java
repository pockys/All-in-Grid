package org.pockys.allingrid;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemClickListener {

	static final String TAG = "MainActivity";
	private ViewPager gridField;
	private ViewPager mainField;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	}

	public void onResume() {
		super.onResume();

		gridField = (ViewPager) findViewById(R.id.grid_field);
		gridField.setAdapter(new CellPagerAdapter(getGridFieldViews(3, 4)));

		mainField = (ViewPager) findViewById(R.id.menu_field);
		mainField.setAdapter(new CellPagerAdapter(getMenuFieldViews(4)));

	}

	private ArrayList<GridView> getGridFieldViews(final int numColumns,
			final int numRows) {
		ArrayList<GridView> gridViews = new ArrayList<GridView>();

		Contact contact = new Contact(this);
		final int numCells = numColumns * numRows;
		for (int i = 0; i < contact.getSize() / numCells; i++) {
			GridView gridView = (GridView) LayoutInflater.from(this).inflate(
					R.layout.grid_view, null);
			gridView.setNumColumns(numColumns);
			gridView.setAdapter(new CellAdapter(this, contact
					.getContactsList(numCells)));
			gridView.setOnItemClickListener(this);

			gridViews.add(gridView);
		}
		if (contact.getSize() % numCells != 0) {
			GridView gridView = (GridView) LayoutInflater.from(this).inflate(
					R.layout.grid_view, null);
			gridView.setNumColumns(numColumns);
			gridView.setAdapter(new CellAdapter(this, contact
					.getContactsList(numCells)));
			gridView.setOnItemClickListener(this);

			gridViews.add(gridView);
		}

		return gridViews;
	}

	private ArrayList<GridView> getMenuFieldViews(final int numColumns) {
		ArrayList<GridView> menuViews = new ArrayList<GridView>();
		final int numCells = numColumns;

		MainMenu mainMenu = new MainMenu();

		for (int i = 0; i < mainMenu.getSize() / numCells; i++) {
			GridView menuView = (GridView) LayoutInflater.from(this).inflate(
					R.layout.grid_view, null);
			menuView.setNumColumns(numColumns);
			menuView.setAdapter(new CellAdapter(this, mainMenu
					.getMenuList(numCells)));
			menuViews.add(menuView);
		}
		if (mainMenu.getSize() % numCells != 0) {
			GridView menuView = (GridView) LayoutInflater.from(this).inflate(
					R.layout.grid_view, null);
			menuView.setNumColumns(numColumns);
			menuView.setAdapter(new CellAdapter(this, mainMenu
					.getMenuList(numCells)));
			menuViews.add(menuView);
		}

		return menuViews;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT)
		// .show();

		TextView textView = (TextView) v.findViewById(R.id.cell_label);
		String displayName = textView.getText().toString();
		textView.setVisibility(View.INVISIBLE);

		// Intent intent = new Intent(Intent.ACTION_VIEW);
		// Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
		// String.valueOf(contactID));
		// intent.setData(uri);
		// this.startActivity(intent);

	}
}
