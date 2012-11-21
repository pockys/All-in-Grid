package org.pockys.allingrid;

import java.util.ArrayList;
import java.util.Hashtable;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.provider.ContactsContract.CommonDataKinds;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.LinePageIndicator;

public class MainActivity extends Activity{

	static final String TAG = "MainActivity";

	static final String TAG2 = "Check";
	

	private static ViewPager mGridField;
	private static ViewPager mMenuField;

	private ActionBar mActionBar;

	private int menuFieldCurrentItem = 0;

	private MenuController mMenuController;
	private ContactControllerMain mContactController;
	private static CirclePageIndicator mCirclePageIndicator;
	private LinePageIndicator mLinePageIndicator;
	private static Context mContext;

	private static Menu mMenu;
	
	private static GroupCellInfo currentGroupInfo = MenuController.AllGroupCellInfo;
	private static Hashtable<GroupCellInfo, Integer> currentItemTable = new Hashtable<GroupCellInfo, Integer>();
	
	SharedPreferences FirstSharedPreferences;
	String first = "first";
	
	
	public static Hashtable<GroupCellInfo, Integer> cloneCurrentItemTable() {
		return (Hashtable<GroupCellInfo, Integer>) currentItemTable.clone();
	}

	public static int getCurrentItem() {
		return getCurrentItem(currentGroupInfo);
	}

	public static int getCurrentItem(GroupCellInfo groupInfo) {
		Integer value = currentItemTable.get(groupInfo);
		return (value == null) ? 0 : value;
	}

	public static void saveCurrentItem() {
		setCurrentItem(mGridField.getCurrentItem());
	}

	public static void setCurrentItem(int currentItem) {
		setCurrentItem(getCurrentGroupInfo(), currentItem);
	}

	public static void setCurrentItem(GroupCellInfo groupInfo, int currentItem) {
		currentItemTable.put(groupInfo, currentItem);
	}

	public static String getSelection(GroupCellInfo groupInfo) {
		String groupTitle = groupInfo.getDisplayName();

		if (groupTitle.equals("All")) {
			return null;
		} else if (groupTitle.equals("Favorite")) {
			return ContactsContract.Contacts.STARRED + " = 1";
		} else {
			return ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID
					+ " = " + groupInfo.getGroupId();
		}
	}

	public static GroupCellInfo getCurrentGroupInfo() {
		return currentGroupInfo;
	}

	public static void setCurrentGroupInfo(GroupCellInfo currentGroupInfo) {
		MainActivity.currentGroupInfo = currentGroupInfo;
	}
	
	public static void setAllselectVisibility(boolean visible) {
		mMenu.findItem(R.id.menu_all_select).setVisible(visible);

	}

	public static void setAllClearVisibility(boolean visible) {
		mMenu.findItem(R.id.menu_all_clear).setVisible(visible);

	}
	
	public void FirstTutorial(){
		
		AlertDialog.Builder TutorialDialogBuilder = new AlertDialog.Builder(this);
		TutorialDialogBuilder.setTitle("使用する前に");
		TutorialDialogBuilder.setIcon(R.drawable.ic_char_face001);
		TutorialDialogBuilder.setMessage("キャラグルを使用するにあたって、「設定」＞「アカウントと同期」でグーグルアカウントと連絡先の同期にチェックを入れてください。" +
				"また、Gmailのグループを作成してこのアプリを利用してください");
		TutorialDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				AlertDialog.Builder TutorialDialogBuilderFirst = new AlertDialog.Builder(mContext);		
				TutorialDialogBuilderFirst.setTitle("メイン画面");
				LayoutInflater inflater = LayoutInflater.from(mContext);  
		        final View image1 = inflater.inflate(R.layout.tutorial_image,null);  
		        ImageView tutorialImage = (ImageView)image1.findViewById(R.id.tutorialImage); 
		        tutorialImage.setImageResource(R.drawable.ic_tutorial_001);
		        
		        TutorialDialogBuilderFirst.setView(image1);
		        TutorialDialogBuilderFirst.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
				
						AlertDialog.Builder TutorialDialogBuilderSecond = new AlertDialog.Builder(mContext);		
						TutorialDialogBuilderSecond.setTitle("エディット画面");
						LayoutInflater inflater = LayoutInflater.from(mContext);  
				        final View image2 = inflater.inflate(R.layout.tutorial_image,null);  
				        ImageView tutorialImage = (ImageView)image2.findViewById(R.id.tutorialImage); 
				        tutorialImage.setImageResource(R.drawable.ic_tutorial_002);
				        
				        TutorialDialogBuilderSecond.setView(image2);
				        TutorialDialogBuilderSecond.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
						
								AlertDialog.Builder TutorialDialogBuilderThird = new AlertDialog.Builder(mContext);		
								TutorialDialogBuilderThird.setTitle("エディット画面");
								LayoutInflater inflater = LayoutInflater.from(mContext);  
						        final View image3 = inflater.inflate(R.layout.tutorial_image,null);  
						        ImageView tutorialImage = (ImageView)image3.findViewById(R.id.tutorialImage); 
						        tutorialImage.setImageResource(R.drawable.ic_tutorial_003);
						        
						        TutorialDialogBuilderThird.setView(image3);
						        TutorialDialogBuilderThird.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										// TODO Auto-generated method stub
										
									}
						        });
								// TODO Auto-generated method stub
						        TutorialDialogBuilderThird.create().show();
								
							}
						});
					
				        TutorialDialogBuilderSecond.create().show();
					}}); 
		        
				TutorialDialogBuilderFirst.create().show();
				// TODO Auto-generated method stub
				
			}
		});
		TutorialDialogBuilder.create().show();
		
		SharedPreferences.Editor editor = FirstSharedPreferences.edit();
		editor.putInt(first, 1).commit();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		FirstSharedPreferences = this.getSharedPreferences("FirstSharePreferences",
				Context.MODE_PRIVATE);
		
		int flag = FirstSharedPreferences.getInt(first, -1);
		if(flag ==- 1)FirstTutorial();

		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(false);
		mActionBar.show();

		mGridField = (ViewPager) findViewById(R.id.grid_field);
		mMenuField = (ViewPager) findViewById(R.id.menu_field);

		mContext = this;

	}
	

	public void onStart() {
		super.onStart();

		Log.d(TAG, "onStart");

		mMenuController = new MenuController(this);

		mContactController = new ContactControllerMain(this,
				getSelection(getCurrentGroupInfo()));

		mGridField.setAdapter(new CellPagerAdapter(mContactController
				.getGridFieldViews(4, 4)));

		mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator_grid);
		mCirclePageIndicator.setViewPager(mGridField);

		mMenuField.setAdapter(new CellPagerAdapter(mMenuController
				.getMenuFieldViews(4)));

		mLinePageIndicator = (LinePageIndicator) findViewById(R.id.line_page_indicator_menu);
		mLinePageIndicator.setViewPager(mMenuField);
	}

	public void onResume() {
		super.onResume();

		
		mActionBar.setTitle(currentGroupInfo.getDisplayName());


		
		mGridField.setCurrentItem(getCurrentItem());
		mCirclePageIndicator.setCurrentItem(getCurrentItem());
		mMenuField.setCurrentItem(menuFieldCurrentItem);
		mLinePageIndicator.setCurrentItem(menuFieldCurrentItem);

		Log.d(TAG, "onResume: gridField currentGroup: "
				+ getCurrentGroupInfo().getDisplayName() + " currentItem: "
				+ getCurrentItem());

		SelectedItemList.INSTANCE.setSelectedGroupInfo(currentGroupInfo);

	}

	public void onPause() {
		super.onPause();
		saveCurrentItem();
		menuFieldCurrentItem = mMenuField.getCurrentItem();

		Log.d(TAG,
				"onPause: gridField currentGroup: "
						+ currentGroupInfo.getDisplayName()
						+ " gridField currentItem: "
						+ mGridField.getCurrentItem());

	}

	@Override
	public void onBackPressed() {
		Log.d(TAG,
				"onBackPressed Called. gridField currentItem: "
						+ mGridField.getCurrentItem());

		String currentGroupTitle = currentGroupInfo.getDisplayName();

		if (currentGroupTitle == "All" && mGridField.getCurrentItem() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("終了しますか?");
			builder.setPositiveButton("OK", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			builder.setNegativeButton("ちょっとまった！", null);
			builder.create().show();

		} else if (currentGroupTitle != "All"
				&& mGridField.getCurrentItem() == 0) {
			setCurrentGroupInfo(MenuController.AllGroupCellInfo);
			SelectedItemList.INSTANCE
					.setSelectedGroupInfo(MenuController.AllGroupCellInfo);
			menuFieldCurrentItem = 0;
			mMenuField.setCurrentItem(0);
			reDrawMenuField();

			mActionBar.setDisplayHomeAsUpEnabled(false);
			mActionBar.setTitle("All");


			mContactController = new ContactControllerMain(this, null);
			mGridField.setAdapter(new CellPagerAdapter(mContactController
					.getGridFieldViews(4, 4)));
			mGridField.setCurrentItem(getCurrentItem());

			mCirclePageIndicator.setViewPager(mGridField);
			mCirclePageIndicator.setCurrentItem(getCurrentItem());

		} else {

			setCurrentItem(0);
			mGridField.setCurrentItem(0);
		}

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		mMenu = menu;
		getMenuInflater().inflate(R.menu.activity_main, menu);
		String currentGroupTitle = currentGroupInfo.getDisplayName();

		return true;
	}
	
	
	public static void setSendMailtMenuVisibility(boolean visible) {
		mMenu.findItem(R.id.send_mail).setVisible(visible);

	}
	
	public static void setPhoneMenuVisibility(boolean visible) {
		mMenu.findItem(R.id.menu_phone).setVisible(visible);

	}
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem SendMailMenu = menu.findItem(R.id.send_mail);
		MenuItem PhoneMenu = menu.findItem(R.id.menu_phone);
		if (SelectedItemList.INSTANCE.getSize() > 0) {
			if(SelectedItemList.INSTANCE.getSize() == 1){
				PhoneMenu.setVisible(true);
				SendMailMenu.setVisible(true);
			}
			else{
			SendMailMenu.setVisible(true);
			PhoneMenu.setVisible(false);
			}

		} else {
			SendMailMenu.setVisible(false);
			PhoneMenu.setVisible(false);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
				
		Intent intent;
		
		switch (item.getItemId()) {
				
				
		case android.R.id.home:

			ActionBar actionBar = this.getActionBar();
			actionBar.setTitle("All");
			actionBar.setDisplayHomeAsUpEnabled(false);

			SelectedItemList.INSTANCE
					.setSelectedGroupInfo(MenuController.AllGroupCellInfo);
			reDrawMenuField();

			resetGridField(MenuController.AllGroupCellInfo);

			break;

		case R.id.menu_edit:
			intent = new Intent(this, EditActivity.class);
			startActivity(intent);
			break;
			
		case R.id.menu_phone:
			
			ArrayList<Integer> callList = new ArrayList<Integer>();
			
			callList = SelectedItemList.INSTANCE.getSelectedContactIdList();
			
			StringBuilder s2 = new StringBuilder();
			int call_contactId;
			if(callList.size()==1){
				call_contactId = callList.get(0);
				
				
				
				Cursor phone_number = getContentResolver().query(CommonDataKinds.Phone.CONTENT_URI,
						new String[]{CommonDataKinds.Phone.DATA}, 
						ContactsContract.Data.CONTACT_ID + "=" + call_contactId,
						null, null);
				
				if(phone_number.getCount()==0){
					makeToast(mContext, "YESYES");
					break;
				}
				
				phone_number.moveToFirst();
				s2 = s2.append(phone_number.getString(0));
				makeToast(mContext, s2.toString());
			}

			Intent phone_intent = new Intent();
			phone_intent.setAction(Intent.ACTION_DIAL);
			phone_intent.setData(Uri.parse("tel:"+s2));
			startActivity(phone_intent);
			
			break;
			
		case R.id.send_mail:
			
			ArrayList<Integer> ops = new ArrayList<Integer>();
			
			ops = SelectedItemList.INSTANCE.getSelectedContactIdList();
			
			StringBuilder s1 = new StringBuilder();
			
			int contactId;
			for(int i = 0; i < SelectedItemList.INSTANCE.getSize(); i++){
				contactId = ops.get(i);
				
				
				
				Cursor email = getContentResolver().query(CommonDataKinds.Email.CONTENT_URI,
						new String[]{CommonDataKinds.Email.DATA}, 
						ContactsContract.Data.CONTACT_ID + "=" + contactId,
						null, null);
				
			
				if (email.moveToFirst()){
					do{
						s1 = s1.append(email.getString(0));
					}while(email.moveToNext());
				}
				s1 = s1.append(",");
				Log.d(TAG2,"s1::"+SelectedItemList.INSTANCE.getSize());

			
			}

			
			
			String sText="これを送ります！";
			Uri uri = Uri.parse("mailto:" + s1); //宛先を生成
			Intent intent2 = new Intent(Intent.ACTION_SENDTO,uri);   //宛先情報
			intent2.putExtra(Intent.EXTRA_TEXT, sText);          //本文を指定
			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent2);      //Activity 起動
			
			break;
			
		case R.id.sort:
						
			if(IconListLib.INSTANCE.getCurrentSort() == 0){
			IconListLib.INSTANCE.setCurrentSort(1);
			}
			else{
				IconListLib.INSTANCE.setCurrentSort(0);
				
			}
			resetGridField();			

			
			break;
			
		

		case R.id.menu_all_select:

			ContactControllerEdit contactControllerAllSelect = new ContactControllerEdit(this,
					getSelection(currentGroupInfo));
			ArrayList<GridView> gridViewListAllSelect = contactControllerAllSelect
					.getGridFieldViews(4, 4);
			Log.d(TAG, "gridViewList size: " + gridViewListAllSelect.size());
			for (int i = 0; i < gridViewListAllSelect.size(); i++) {
				GridView gridView = gridViewListAllSelect.get(i);
				for (int j = 0; j < gridView.getAdapter().getCount(); j++) {
					View cell = gridView.getAdapter()
							.getView(j, null, gridView);
					ContactCellInfo contactInfo = (ContactCellInfo) cell
							.getTag();
					String contactIdAll = contactInfo.getContactId();

					Log.d(TAG, "[" + i + ", " + j + "]" + "contactId: "
							+ contactIdAll);
					
					SelectedItemList.INSTANCE.add(Integer.valueOf(contactIdAll));
					setAllselectVisibility(false);
					setAllClearVisibility(true);
					
				}
			}

			reDrawGridField();

			return true;
			
	
		case R.id.menu_all_clear:

			ContactControllerEdit contactControllerAllClear = new ContactControllerEdit(this,
					getSelection(currentGroupInfo));
			ArrayList<GridView> gridViewListAllClear = contactControllerAllClear
					.getGridFieldViews(4, 4);
			Log.d(TAG, "gridViewList size: " + gridViewListAllClear.size());
			for (int i = 0; i < gridViewListAllClear.size(); i++) {
				GridView gridView = gridViewListAllClear.get(i);
				for (int j = 0; j < gridView.getAdapter().getCount(); j++) {
					View cell = gridView.getAdapter()
							.getView(j, null, gridView);
					ContactCellInfo contactInfo = (ContactCellInfo) cell
							.getTag();
					String contactIdAll = contactInfo.getContactId();

					Log.d(TAG, "[" + i + ", " + j + "]" + "contactId: "
							+ contactIdAll);
					
					SelectedItemList.INSTANCE.remove(Integer.valueOf(contactIdAll));
					setAllselectVisibility(true);
					setAllClearVisibility(false);
					
				}
			}

			reDrawGridField();

			return true;

		case R.id.manual:
			FirstTutorial();
			
			return false;
			
		case R.id.menu_shuffle_icons:

			final SharedPreferences sharedPreferences = getSharedPreferences(
					"sharePreferences", Context.MODE_PRIVATE);

			AlertDialog.Builder shuffleDialogBuilder = new AlertDialog.Builder(
					this);
			shuffleDialogBuilder.setTitle("Icon Shuffle");

			ListView listView = new ListView(mContext);
			listView.setAdapter(new BaseAdapter() {

				private final String[] items = { "All", "Category" };

				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					TextView textView = null;
					if (convertView == null) {
						textView = (TextView) LayoutInflater.from(mContext)
								.inflate(R.layout.list_view_item, null);

					} else
						textView = (TextView) convertView;

					textView.setText(items[position]);


					return textView;
				}

				@Override
				public long getItemId(int position) {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public Object getItem(int position) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public int getCount() {
					// TODO Auto-generated method stub
					return 2;
				}
			});
			shuffleDialogBuilder.setView(listView);
			final AlertDialog shuffleDialog = shuffleDialogBuilder.create();
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> container, View view,
						int position, long id) {
					shuffleDialog.dismiss();

					if (position == 0) {
						IconListLib.INSTANCE.setCurrentCategoy(position);

						SharedPreferences.Editor editor = sharedPreferences
								.edit();
						editor.clear();
						editor.commit();

						resetGridField();

						makeToast(mContext, "All icons are shuffled!!");

					} else if (position == 1) {

						LayoutInflater inflater = LayoutInflater.from(mContext);
						GridView gridView = (GridView) inflater.inflate(
								R.layout.grid_view, null);
						gridView.setAdapter(new IconListAdapter(mContext, 1));
						AlertDialog.Builder categoryDialogBuilder = new AlertDialog.Builder(
								mContext);

						categoryDialogBuilder.setTitle("アイコン チェンジ");
						categoryDialogBuilder.setView(gridView);

						final AlertDialog categoryDialog = categoryDialogBuilder
								.create();

						gridView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> container,
									View view, int position, long id) {
								
								categoryDialog.dismiss();

								IconListLib.INSTANCE
										.setCurrentCategoy(position + 1);

								SharedPreferences.Editor editor = sharedPreferences
										.edit();

								ArrayList<GridView> currentGridViews = new ContactControllerMain(
										mContext,
										getSelection(currentGroupInfo))
										.getGridFieldViews(4, 4);

								Log.d(TAG, "currentGridViews size: "
										+ currentGridViews);
								
								for (int i = 0; i < currentGridViews.size(); i++) {

									GridView gridView = currentGridViews.get(i);
									
									for (int j = 0; j < gridView.getAdapter()
											.getCount(); j++) {

										View cell = gridView.getAdapter()
												.getView(j, null, gridView);
										ContactCellInfo contactInfo = (ContactCellInfo) cell.getTag();
										String contactId = contactInfo.getContactId();
										int imageNum = sharedPreferences.getInt(contactId,-1);

										if (imageNum != -1) {


											editor.putInt(contactId,
													-1);
										}
									}
								}

								editor.commit();

								resetGridField();
							}

						});

						categoryDialog.show();

					}

				}

			});
			shuffleDialog.show();
		}
		return true;
	}

	public static void resetGridField() {
		resetGridField(currentGroupInfo);
	}

	public static void resetGridField(GroupCellInfo groupInfo) {
		saveCurrentItem();
		setCurrentGroupInfo(groupInfo);
		int currentItem = getCurrentItem();

		ContactControllerMain contactController = new ContactControllerMain(mContext,
				getSelection(groupInfo));

		mGridField.setAdapter(new CellPagerAdapter(contactController
				.getGridFieldViews(4, 4)));
		mGridField.setCurrentItem(currentItem);

		mCirclePageIndicator.setViewPager(mGridField);
		mCirclePageIndicator.setCurrentItem(currentItem);

	}

	public static void reDrawGridField() {
		for (int i = 0; i < mGridField.getChildCount(); i++) {
			GridView currentGridView = (GridView) mGridField.getChildAt(i);
			BaseAdapter adapter = ((BaseAdapter) currentGridView.getAdapter());
			adapter.notifyDataSetChanged();

		
		}
	}
	public static void reDrawMenuField() {
		for (int i = 0; i < mMenuField.getChildCount(); i++) {
			GridView currentGridView = (GridView) mMenuField.getChildAt(i);
			BaseAdapter adapter = ((BaseAdapter) currentGridView.getAdapter());
			adapter.notifyDataSetChanged();
		}
	}
	
	public static void makeToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

}
