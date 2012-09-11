package org.pockys.allingrid;

import java.util.Hashtable;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager; 
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	static final String TAG = "MainActivity";
	private static ViewPager gridField;
	private ViewPager menuField;

	private static Hashtable<String, Integer> gridFieldcurrentItemByGroup = new Hashtable<String, Integer>();
	private static String currentGroupTitle = "All";
	private int menuFieldCurrentItem = 0;

	private MenuController menuController;
	private ContactController contactController;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ActionBar actionBar = getActionBar();
		actionBar.show();

	}

	public void onResume() {
		super.onResume();

		menuController = new MenuController(this);
		contactController = new ContactController(this);

		gridField = (ViewPager) findViewById(R.id.grid_field);
		gridField.setAdapter(new CellPagerAdapter(contactController
				.getGridFieldViews(4, 4)));
		gridField.setCurrentItem(getGridFieldCurrentItem());

		menuField = (ViewPager) findViewById(R.id.menu_field);
		menuField.setAdapter(new CellPagerAdapter(menuController
				.getMenuFieldViews(4)));
		menuField.setCurrentItem(menuFieldCurrentItem);

		Log.d(TAG, "onResume: gridField currentItem: "
				+ getGridFieldCurrentItem());

	}

	public void onPause() {
		super.onPause();
		// gridFieldCurrentItem = gridField.getCurrentItem();
		saveGridFieldCurrentItem();
		menuFieldCurrentItem = menuField.getCurrentItem();

		Log.d(TAG,
				"onPause: gridField currentItem: " + gridField.getCurrentItem());

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		makeToast(this, item.getTitle().toString());

		Intent intent;
		switch (item.getItemId()) {
		case R.id.menu_phone:
			intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
			startActivity(intent);
			break;
		case R.id.menu_icon:

			break;
		case R.id.menu_search:

			break;
		case R.id.menu_edit:

			break;
		case R.id.menu_add_user:
			// makeToast("Add");
			intent = new Intent(Intent.ACTION_INSERT);
			intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
			this.startActivity(intent);
			break;
		case R.id.menu_add_group:
			intent = new Intent(Intent.ACTION_INSERT);
			intent.setType(ContactsContract.Groups.CONTENT_ITEM_TYPE);
			this.startActivity(intent);
			break;
		}

		return true;
	}

	public static void makeToast(Context context, String message) {
		// with jam obviously
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
	public static void saveGridFieldCurrentItem() {
		setGridFieldCurrentItem(gridField.getCurrentItem());
	}

	public static void setGridFieldCurrentItem(String groupTitle,
			int currentItem) {
		gridFieldcurrentItemByGroup.put(groupTitle, currentItem);
	}

	public static void setGridFieldCurrentItem(int currentItem) {
		setGridFieldCurrentItem(getCurrentGroupTitle(), currentItem);
	}

	public static int getGridFieldCurrentItem(String groupTitle) {
		if (gridFieldcurrentItemByGroup.get(groupTitle) == null)
			return 0;
		else
			return gridFieldcurrentItemByGroup.get(groupTitle);
	}

	public static int getGridFieldCurrentItem() {
		return getGridFieldCurrentItem(getCurrentGroupTitle());
	}

	public static String getCurrentGroupTitle() {
		return currentGroupTitle;
	}

	public static void setCurrentGroupTitle(String _currentGroupTitle) {
		currentGroupTitle = _currentGroupTitle;
	}
}
