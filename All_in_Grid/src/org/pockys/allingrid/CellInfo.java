package org.pockys.allingrid;

import android.graphics.Bitmap;
import android.net.Uri;

public class CellInfo {
	protected String mDisplayName;
	protected Uri mThumbnailUri;
	protected Bitmap mThumbnailBitmap;
	protected int mResId = -1;
	
	/*shohei write*/
	protected long mId;

	public long getmId() {
		return mId;
	}
	
	public void setId(long id) {
		mId = id;
	}
	
	/*shohei write*/
	
	public String getDisplayName() {
		return mDisplayName;
	}

	public void setDisplayName(String displayName) {
		this.mDisplayName = displayName;
	}

	public Uri getThumbnailUri() {
		return mThumbnailUri;
	}

	public void setThumbnail(Uri thumbnailUri) {
		this.mThumbnailUri = thumbnailUri;
	}

	public Bitmap getThumbnailBitmap() {
		return mThumbnailBitmap;
	}

	public void setThumbnail(Bitmap thumbnailBitmap) {
		this.mThumbnailBitmap = thumbnailBitmap;
	}

	public void setThumbnail(int resId) {
		mResId = resId;
	}

	public boolean isThereNoThumbnail() {
		return mThumbnailUri == null && mThumbnailBitmap == null
				&& mResId == -1;
	}

}
