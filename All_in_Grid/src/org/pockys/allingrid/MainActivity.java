package org.pockys.allingrid;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.QuickContactBadge;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements OnItemClickListener {

	static final String TAG = "MainActivity";
	private ViewPager gridField;
	private ViewPager menuField;
	private int gridFieldCurrentItem = 0;
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
				.getGridFieldViews(4, 4, this)));
		gridField.setCurrentItem(gridFieldCurrentItem);

		menuField = (ViewPager) findViewById(R.id.menu_field);
		menuField.setAdapter(new CellPagerAdapter(menuController
				.getMenuFieldViews(4)));
		menuField.setCurrentItem(menuFieldCurrentItem);

	}

	public void onPause() {
		super.onPause();
		gridFieldCurrentItem = gridField.getCurrentItem();
		menuFieldCurrentItem = menuField.getCurrentItem();

		Log.d(TAG, "gridField currentItem : " + gridFieldCurrentItem
				+ " menuField currentItem: " + menuFieldCurrentItem);
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

	public static void makeToast(Context context, String message) {
		// with jam obviously
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, final View v, int position,
			long id) {
		// get contact uri from contact id
		ContactCellInfo contactCellInfo = (ContactCellInfo) v.getTag();
		String contactIdString = String.valueOf(contactCellInfo.getContactId());
		final Uri contactUri = Uri.withAppendedPath(
				ContactsContract.Data.CONTENT_URI, Uri.encode(contactIdString));
		makeToast(this, "contact id: " + contactIdString);

		final QuickContactBadge badge = new QuickContactBadge(this);
		badge.assignContactUri(contactUri);
		badge.setMode(ContactsContract.QuickContact.MODE_LARGE);
		((ViewGroup) v).addView(badge);
		badge.performClick();
		((ViewGroup) v).removeView(badge);

	}

}
