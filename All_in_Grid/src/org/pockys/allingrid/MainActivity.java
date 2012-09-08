package org.pockys.allingrid;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.GridView;

public class MainActivity extends Activity {

	static final String TAG = "MainActivity";
	private GridView gridField;
	private ViewPager mainField;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		ViewPager viewPager = (ViewPager)findViewById(R.id.grid_field);
		viewPager.setAdapter(new ContactPagerAdapter(this));

//		gridField = (GridView) findViewById(R.id.grid_field);
//		gridField.setAdapter(new ContactAdapter(this));
		
//		mainField = (ViewPager) findViewById(R.id.menu_field);
		
		

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
