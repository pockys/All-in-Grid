package org.pockys.allingrid;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;

public class MainActivity extends Activity {

	static final String TAG = "MainActivity";
	private GridView gridView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		gridView = (GridView) findViewById(R.id.grid);
		// ContactAdapter contactAdapter = new ContactAdapter(this);
		// Log.d(TAG, "contactAdapter generated");

		gridView.setAdapter(new ContactAdapter(this));

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
