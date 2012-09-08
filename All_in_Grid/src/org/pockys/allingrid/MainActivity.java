package org.pockys.allingrid;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class MainActivity extends Activity {

	static final String TAG = "MainActivity";
	private ViewPager gridField;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		gridField = (ViewPager) findViewById(R.id.grid_field);
		gridField.setAdapter(new ContactPagerAdapter(this));


	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
