package org.pockys.allingrid;

import java.util.ArrayList;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class CellPagerAdapter extends PagerAdapter {

	private static final String TAG = "ContactPagerAdapter";

	// private final int mNumColumns;
	// private final int mNumRows;
	// private final int mNumCells;
	// private Context mContext;
	// private Cursor mContactsCursor;
 
	private ArrayList<GridView> mPagedViews = new ArrayList<GridView>();

	public CellPagerAdapter(ArrayList<GridView> pagedViews) {
		super();
		mPagedViews = pagedViews;
	}

	@Override
	public int getCount() {
		return mPagedViews.size();
	}                                                                                                              

	@Override
	public Object instantiateItem(View container, int position) {

		((ViewGroup) container).addView(mPagedViews.get(position));

		return mPagedViews.get(position);
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		ViewPager viewPager = ((ViewPager) arg0);
		viewPager.removeView((View) arg2);

	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);

	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

}
