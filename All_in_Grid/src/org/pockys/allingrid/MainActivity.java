package org.pockys.allingrid;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;
import android.widget.HorizontalScrollView;

public class MainActivity extends Activity {

	static final String TAG = "MainActivity";
	private GridView gridField;
	private HorizontalScrollView mainField;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		gridField = (GridView) findViewById(R.id.grid_field);
		gridField.setAdapter(new ContactAdapter(this));
		
		mainField = (HorizontalScrollView)findViewById(R.id.menu_field);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
