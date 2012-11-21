package org.pockys.allingrid;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.LinePageIndicator;

public class EditActivity extends Activity {

	public static final String TAG = "EditActivity";

	private static ViewPager mGridField;
	private static ViewPager mMenuField;

	private ActionBar mActionBar;

	private MenuController menuController;
	private ContactControllerEdit mContactController;

	private int menuFieldCurrentItem = -1;

	private EditGridItemClickListener mEditClickListener;
	private static CirclePageIndicator mCirclePageIndicator;
	private LinePageIndicator mLinePageIndicator;

	private static Menu mMenu;

	private static Hashtable<GroupCellInfo, Integer> currentItemTable;
	private static GroupCellInfo currentGroupInfo;

	public static void setDisconnectMenuVisibility(boolean visible) {
		mMenu.findItem(R.id.menu_disconnect).setVisible(visible);

	}
	
	public static void setAllselectVisibility(boolean visible) {
		mMenu.findItem(R.id.menu_all_select).setVisible(visible);

	}

	public static void setAllClearVisibility(boolean visible) {
		mMenu.findItem(R.id.menu_all_clear).setVisible(visible);

	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(LayoutInflater.from(this).inflate(R.layout.main, null));

		mActionBar = getActionBar();
		mActionBar.setTitle("Edit - All");
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 204, 204, 204)));

		currentItemTable = MainActivity.cloneCurrentItemTable();
		currentGroupInfo = MainActivity.getCurrentGroupInfo();

		mGridField = (ViewPager) findViewById(R.id.grid_field);
		mGridField.setBackgroundColor(Color.argb(255, 204, 204, 204));

		mMenuField = (ViewPager) findViewById(R.id.menu_field);
		mMenuField.setBackgroundColor(Color.argb(255, 224, 224, 224));
		
		mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator_grid);
		mCirclePageIndicator.setBackgroundColor(Color.argb(255, 204, 204, 204));
		mLinePageIndicator = (LinePageIndicator) findViewById(R.id.line_page_indicator_menu);
		mLinePageIndicator.setBackgroundColor(Color.argb(255, 224, 224, 224));

		mEditClickListener = new EditGridItemClickListener(this);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		mMenu = menu;
		getMenuInflater().inflate(R.menu.activity_edit, menu);
		return true;
	}
	
	public static Menu getmenu(){
		return mMenu;
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		
		ContactControllerEdit contactController = new ContactControllerEdit(this,
				getSelection(currentGroupInfo));
		ArrayList<GridView> gridViewList = contactController
				.getGridFieldViews(4, 4);
		
		if (SelectedItemList.INSTANCE.getSize() > 0) {
			setAllselectVisibility(true);
			setAllClearVisibility(true);
			setDisconnectMenuVisibility(true);
			if((contactController.getcount() + 1)== SelectedItemList.INSTANCE.getSize()){
				setAllselectVisibility(false);
				
			}


		} else {
			
			setAllselectVisibility(true);
			setAllClearVisibility(false);
			setDisconnectMenuVisibility(false);
			
			}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		final Context context = this;
		AlertDialog.Builder builder;
		Intent intent;

		switch (item.getItemId()) {

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
					String contactId = contactInfo.getContactId();
					
					SelectedItemList.INSTANCE.add(Integer.valueOf(contactId));
					
				}
			}
			setAllselectVisibility(false);
			setAllClearVisibility(true);
			setDisconnectMenuVisibility(true);
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
					String contactId = contactInfo.getContactId();

					Log.d(TAG, "[" + i + ", " + j + "]" + "contactId: "
							+ contactId);
					
					SelectedItemList.INSTANCE.remove(Integer.valueOf(contactId));
					setAllselectVisibility(true);
					setAllClearVisibility(false);
					setDisconnectMenuVisibility(true);
					
				}
			}

			reDrawGridField();

			return true;
		case R.id.menu_disconnect:

			int selectedPeopleCount = SelectedItemList.INSTANCE.getSize();
			if (selectedPeopleCount == 0)

				return true;

			// disconnect

			final String groupId = currentGroupInfo.getGroupId();
			final String groupTitle = currentGroupInfo.getDisplayName();

			Log.d(TAG, "Group ID: " + groupId);

			final ContentResolver contentResolver = this.getContentResolver();

			String dialogTitle;
			String dialogMessage;
			String positiveButtonMessage;
			String negativeButtonMessage;

			if (groupTitle == "All") {
				dialogTitle = " All グループからははずせないよ";
				positiveButtonMessage = "OK";
				negativeButtonMessage = "OK";
			} else {
				dialogTitle = " グループからはずす　";
				dialogMessage = groupTitle + " グループから　"+ selectedPeopleCount
						+ " 人はずす";
				positiveButtonMessage = "OK";
				negativeButtonMessage = "ちょっとまった！";
			}

			builder = new AlertDialog.Builder(this);
			// builder.setCancelable(false);
			builder.setTitle(dialogTitle);
			builder.setNegativeButton(negativeButtonMessage,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							SelectedItemList.INSTANCE.clear();
							reDrawGridField();

						}
					});
			if(groupTitle != "All"){
			builder.setPositiveButton(positiveButtonMessage,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
							ContentProviderOperation.Builder op;

							Iterator<Integer> contactIdItr = SelectedItemList.INSTANCE
									.getIterator();
							for (; contactIdItr.hasNext();) {
								int contactId = contactIdItr.next();

								if (groupTitle == "All") {

								} else if (groupTitle == "Favorite") {
									Log.d(TAG, "Favorite. contact Id: "
											+ contactId);
									op = ContentProviderOperation
											.newUpdate(
													ContactsContract.Contacts.CONTENT_URI)
											.withSelection(
													ContactsContract.Contacts._ID
															+ " = '"
															+ contactId + "'",
													null)
											.withValue(
													ContactsContract.Contacts.STARRED,
													0);
									ops.add(op.build());

								} else {
									op = ContentProviderOperation
											.newDelete(
													ContactsContract.Data.CONTENT_URI)
											.withSelection(
													ContactsContract.Data.RAW_CONTACT_ID
															+ " = '"
															+ EditMenuItemClickListener
																	.getRawContactId(
																			context,
																			contactId)
															+ "' AND "
															+ ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID
															+ " = '"
															+ groupId
															+ "'", null);

									ops.add(op.build());
								}

							}

							try {
								ContentProviderResult[] results = contentResolver
										.applyBatch(ContactsContract.AUTHORITY,
												ops);

								if (results == null) {
									MainActivity.makeToast(context,
											"Something was wrong!!");
								} else if (results.length > 0) {

									MainActivity.makeToast(context,
											  results.length
													+ "人 "
													+ groupTitle+"からはずしました ");
								}

							}

							catch (OperationApplicationException e) {
								e.printStackTrace();
							} catch (RemoteException e) {
								e.printStackTrace();
							} finally {
								SelectedItemList.INSTANCE.clear();
								saveCurrentItem();

								if (currentGroupInfo.getDisplayName() == "All") {
									reDrawGridField();
								} else {
									mContactController = new ContactControllerEdit(
											context,
											getSelection(currentGroupInfo));
									if (mContactController.getSize() == 0) {
										setCurrentGroupInfo(MenuController.AllGroupCellInfo);
										setCurrentItem(getCurrentItem());
										SelectedItemList.INSTANCE
												.setSelectedGroupInfo(MenuController.AllGroupCellInfo);
										reDrawMenuField();

										mContactController = new ContactControllerEdit(
												context, null);

									}

									mContactController
											.setOnItemClickListener(mEditClickListener);

									mGridField.setAdapter(new CellPagerAdapter(
											mContactController
													.getGridFieldViews(4, 4)));
									mGridField.setCurrentItem(getCurrentItem());

									CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator_grid);
									circlePageIndicator
											.setViewPager(mGridField);
									circlePageIndicator
											.setCurrentItem(getCurrentItem());
								}

							}

						}
					});
			}
			builder.create().show();
			
			return true;
			
		case R.id.menu_add:
			intent = new Intent(Intent.ACTION_INSERT);

			String packageName = "com.android.contacts";
			String className = ".activities.PeopleActivity";
			intent.setComponent(new ComponentName(packageName, packageName
					+ className));
			startActivity(intent);
			break;
			
		case R.id.menu_shuffle_icons:

			final SharedPreferences sharedPreferences = getSharedPreferences(
					"sharePreferences", Context.MODE_PRIVATE);

			AlertDialog.Builder IconShuffleDialogBuilder = new AlertDialog.Builder(
					this);
			IconShuffleDialogBuilder.setTitle("一気にアイコン チェンジ");

			ListView listView = new ListView(context);
			listView.setAdapter(new BaseAdapter() {

				private final String[] items = { "とーいつ", "シャッフル" };

				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					TextView textView = null;
					if (convertView == null) {
						textView = (TextView) LayoutInflater.from(context)
								.inflate(R.layout.list_view_item, null);

					} else
						textView = (TextView) convertView;

					textView.setText(items[position]);

					// Log.d(TAG, "textView text:  " + items[position]);

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
			IconShuffleDialogBuilder.setView(listView);
			final AlertDialog IconShuffleDialog = IconShuffleDialogBuilder.create();
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> container, View view,
						int position, long id) {
					
					if (position == 0) {
						LayoutInflater inflater = LayoutInflater.from(context);

						GridView gridView = (GridView) inflater.inflate(
						R.layout.grid_view, null);
						gridView.setNumColumns(4);
						gridView.setAdapter(new IconListAdapter(context, 0));

						AlertDialog.Builder iconChangeDialog = new AlertDialog.Builder(
								context);
						iconChangeDialog.setTitle("アイコン チェンジ");
						iconChangeDialog.setView(gridView);
						final AlertDialog iconDialog = iconChangeDialog.create();

						gridView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {

								iconDialog.dismiss();
								IconShuffleDialog.dismiss();
								
								IconListLib.INSTANCE.setCurrenType(position + 1);
								SharedPreferences.Editor editor = sharedPreferences
										.edit();

								ArrayList<GridView> currentGridViews = new ContactControllerMain(
										context,
										getSelection(currentGroupInfo))
										.getGridFieldViews(4, 4);

								Log.d(TAG, "currentGridViews size: "
										+ currentGridViews);
								for (int i = 0; i < currentGridViews.size(); i++) {

									GridView gridView = currentGridViews.get(i);
									Log.d(TAG, "[" + i + "]"
											+ " gridView Child Count: "
											+ gridView.getAdapter().getCount());

									for (int j = 0; j < gridView.getAdapter()
											.getCount(); j++) {

										View cell = gridView.getAdapter()
												.getView(j, null, gridView);
										ContactCellInfo contactInfo = (ContactCellInfo) cell
												.getTag();
										String contactId = contactInfo
												.getContactId();

										int imageNum = sharedPreferences
												.getInt(contactId,
														-2);

										if (imageNum != -2) {
											Log.d(TAG,
													"preference to -1. contactId: "
															+ contactId
															+ " c: "
															+ (position + 1));

											editor.putInt(contactId,
													-2);
											editor.commit();
											
										}
										
										reDrawGridField();
									}
									
								
								}

												
								
							}

						});

						iconDialog.show();
						


					} else if (position == 1) {
						IconListLib.INSTANCE.setCurrenType(0);
						AlertDialog.Builder shuffleDialogBuilder = new AlertDialog.Builder(context);
						shuffleDialogBuilder.setTitle("アイコン シャッフル");

						ListView listView = new ListView(context);
						listView.setAdapter(new BaseAdapter() {

							private final String[] items = { "オール", "カテゴリー" };

							@Override
							public View getView(int position, View convertView,
									ViewGroup parent) {
								TextView textView = null;
								if (convertView == null) {
									textView = (TextView) LayoutInflater.from(context)
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
								
								if (position == 0) {
									shuffleDialog.dismiss();
									
									IconListLib.INSTANCE.setCurrentCategoy(position);

									SharedPreferences.Editor editor = sharedPreferences
											.edit();
									editor.clear();
									editor.commit();

									resetGridField();


								} else if (position == 1) {

									shuffleDialog.dismiss();
									LayoutInflater inflater = LayoutInflater.from(context);
									GridView gridView = (GridView) inflater.inflate(
											R.layout.grid_view, null);
									gridView.setAdapter(new IconListAdapter(context, 1));
									AlertDialog.Builder categoryDialogBuilder = new AlertDialog.Builder(
											context);

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
													context,
													getSelection(currentGroupInfo))
													.getGridFieldViews(4, 4);

											Log.d(TAG, "currentGridViews size: "
													+ currentGridViews);
											for (int i = 0; i < currentGridViews.size(); i++) {

												GridView gridView = currentGridViews.get(i);
												Log.d(TAG, "[" + i + "]"
														+ " gridView Child Count: "
														+ gridView.getAdapter().getCount());

												for (int j = 0; j < gridView.getAdapter()
														.getCount(); j++) {

													View cell = gridView.getAdapter()
															.getView(j, null, gridView);
													ContactCellInfo contactInfo = (ContactCellInfo) cell
															.getTag();
													String contactId = contactInfo
															.getContactId();

													int imageNum = sharedPreferences
															.getInt(contactId,
																	-1);

													if (imageNum != -1) {
														Log.d(TAG,
																"preference to -1. contactId: "
																		+ contactId
																		+ " c: "
																		+ (position + 1));

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

				}

			});
			IconShuffleDialog.show();
		default:
			return super.onOptionsItemSelected(item);
		}
		return false;
	}

	public void onStart() {
		super.onStart();

		mActionBar.setTitle("Edit - " + currentGroupInfo.getDisplayName());
		
		menuController = new MenuControllerEdit(this);
		menuController.setOnItemClickListener(new EditMenuItemClickListener(
				this));
		
		mContactController = new ContactControllerEdit(this,
				MainActivity.getSelection(currentGroupInfo));
		mContactController.setOnItemClickListener(mEditClickListener);
		

		mGridField = (ViewPager) findViewById(R.id.grid_field);
		mGridField.setAdapter(new CellPagerAdapter(mContactController
				.getGridFieldViews(4, 4)));

		mCirclePageIndicator.setViewPager(mGridField);

		mMenuField.setAdapter(new CellPagerAdapter(menuController
				.getMenuFieldViews(4)));

		mLinePageIndicator.setViewPager(mMenuField);

		if (menuFieldCurrentItem == -1) {
			menuFieldCurrentItem = menuController.getPagedCount(4,
					currentGroupInfo, false);
		}

	}

	public void onResume() {
		super.onResume();		
		
		
		Log.d(TAG,
				"onResume: gridField currentGroup: "
						+ currentGroupInfo.getDisplayName() + " current Item: "
						+ getCurrentItem());
		mActionBar.setTitle("Edit - " + currentGroupInfo.getDisplayName());
		
		mGridField.setCurrentItem(getCurrentItem());
		mCirclePageIndicator.setCurrentItem(getCurrentItem());

		mMenuField.setCurrentItem(menuFieldCurrentItem);
		mLinePageIndicator.setCurrentItem(menuFieldCurrentItem);

	}

	@Override
	public void onPause() {
		super.onPause();

		saveCurrentItem();
		menuFieldCurrentItem = mMenuField.getCurrentItem();

	}

	@Override
	public void onBackPressed() {
		Log.d(TAG,
				"onBackPressed Called. gridField currentItem: "
						+ mGridField.getCurrentItem());

		String currentGroupTitle = currentGroupInfo.getDisplayName();

		if (SelectedItemList.INSTANCE.getSize() > 0) {
			SelectedItemList.INSTANCE.clear();
			reDrawGridField();
		}

		else if (currentGroupTitle == "All" && mGridField.getCurrentItem() == 0) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("エディット画面をおわりますか？");
			builder.setPositiveButton("はい", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			builder.setNegativeButton("いいえ", null);
			builder.create().show();

		} else if (currentGroupTitle != "All"
				&& mGridField.getCurrentItem() == 0) {
			setCurrentGroupInfo(MenuController.AllGroupCellInfo);
			SelectedItemList.INSTANCE
					.setSelectedGroupInfo(MenuController.AllGroupCellInfo);
			reDrawMenuField();

			mActionBar.setTitle("Edit - All");

			mContactController = new ContactControllerEdit(this, null);
			mContactController.setOnItemClickListener(mEditClickListener);
			mGridField.setAdapter(new CellPagerAdapter(mContactController
					.getGridFieldViews(4, 4)));
			mGridField.setCurrentItem(getCurrentItem());

			mCirclePageIndicator.setViewPager(mGridField);
			mCirclePageIndicator.setCurrentItem(getCurrentItem());

			menuFieldCurrentItem = 0;
			mMenuField.setCurrentItem(0);

			mLinePageIndicator.setCurrentItem(0);

		} else {

			setCurrentItem(0);
			mGridField.setCurrentItem(0);
		}

	}

	public static GroupCellInfo getCurrentGroupInfo() {
		return currentGroupInfo;
	}

	public static void setCurrentGroupInfo(GroupCellInfo currentGroupInfo) {
		EditActivity.currentGroupInfo = currentGroupInfo;
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
	
	public void resetGridField() {
		resetGridField(currentGroupInfo);
		mActionBar.setTitle("Edit - " + currentGroupInfo.getDisplayName());
		
	}

	public void resetGridField(GroupCellInfo groupInfo) {
		saveCurrentItem();
		setCurrentGroupInfo(groupInfo);
		int currentItem = getCurrentItem();

		
		mContactController = new ContactControllerEdit(this,
				MainActivity.getSelection(currentGroupInfo));
		mContactController.setOnItemClickListener(mEditClickListener);

		mGridField.setAdapter(new CellPagerAdapter(mContactController
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
}
