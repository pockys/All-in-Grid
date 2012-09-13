package org.pockys.allingrid;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.LinePageIndicator;

public class EditActivity extends Activity {

	public static final String TAG = "EditActivity";

	private static ViewPager gridField;
	private ViewPager menuField;

	private MenuController menuController;
	private ContactController contactController;

	private static int gridFieldCurrentItem = 0;
	private int menuFieldCurrentItem = 0;

	private EditGridItemClickListener mEditClickListener;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(LayoutInflater.from(this).inflate(R.layout.main, null));

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		mEditClickListener = new EditGridItemClickListener(this);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// app icon in action bar clicked; go home
		case android.R.id.home:

			EditGridItemClickListener.clear();

			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.menu_disconnect:

			int selectedPeopleCount = EditGridItemClickListener
					.getSelectedContactIdListSize();

			if (selectedPeopleCount == 0)
				return true;

			MainActivity.makeToast(this, "Disconnect " + selectedPeopleCount
					+ " people from ");

			// disconnect

			// ################################################################
			EditGridItemClickListener.clear();
			saveGridFieldCurrentItem();

			contactController = new ContactController(this, null);
			contactController.setOnItemClickListener(mEditClickListener);

			gridField = (ViewPager) findViewById(R.id.grid_field);
			gridField.setAdapter(new CellPagerAdapter(contactController
					.getGridFieldViews(4, 4)));
			gridField.setCurrentItem(getGridFieldCurrentItem());

			CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator_grid);
			circlePageIndicator.setViewPager(gridField);
			circlePageIndicator.setCurrentItem(getGridFieldCurrentItem());
			// ################################################################

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onResume() {
		super.onResume();

		menuController = new MenuController(this,
				new EditMenuItemClickListener(this));
		contactController = new ContactController(this, null);
		contactController.setOnItemClickListener(mEditClickListener);

		gridField = (ViewPager) findViewById(R.id.grid_field);
		gridField.setAdapter(new CellPagerAdapter(contactController
				.getGridFieldViews(4, 4)));
		gridField.setCurrentItem(getGridFieldCurrentItem());

		CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator_grid);
		circlePageIndicator.setViewPager(gridField);
		circlePageIndicator.setCurrentItem(getGridFieldCurrentItem());

		menuField = (ViewPager) findViewById(R.id.menu_field);
		menuField.setAdapter(new CellPagerAdapter(menuController
				.getMenuFieldViews(4)));
		menuField.setCurrentItem(menuFieldCurrentItem);

		LinePageIndicator linePageIndicator = (LinePageIndicator) findViewById(R.id.line_page_indicator_menu);
		linePageIndicator.setViewPager(menuField);
		linePageIndicator.setCurrentItem(menuFieldCurrentItem);

		Log.d(TAG, "onResume: gridField ");
	}

	@Override
	public void onPause() {
		super.onPause();

		saveGridFieldCurrentItem();
		menuFieldCurrentItem = menuField.getCurrentItem();

	}

	@Override
	public void onBackPressed() {
		Log.d(TAG,
				"onBackPressed Called. gridField currentItem: "
						+ gridField.getCurrentItem());

		if (gridField.getCurrentItem() == 0) {
			super.onBackPressed();
			EditGridItemClickListener.clear();
		} else {
			gridField.setCurrentItem(0);
		}

	}

	public static void saveGridFieldCurrentItem() {
		setGridFieldCurrentItem(gridField.getCurrentItem());
	}

	public static int getGridFieldCurrentItem() {
		return gridFieldCurrentItem;
	}

	public static void setGridFieldCurrentItem(int gridFieldCurrentItem) {
		EditActivity.gridFieldCurrentItem = gridFieldCurrentItem;
	}

}
