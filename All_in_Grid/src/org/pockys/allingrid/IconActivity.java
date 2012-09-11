package org.pockys.allingrid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class IconActivity extends Activity implements OnItemClickListener{
	String[] words = {
			"All Shuffle",
			"GOTOUTI YURUKYARA",
			"TIZUKIGOU"
			
	};

	public void OnCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.iconActivity);
		ListAdapter la = (ListAdapter)new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,words);
		
		ListView lv = (ListView)findViewBy(R.id.listview);
		lv.setAdapter(la);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	

}
