package org.pockys.allingrid;

import java.util.ArrayList;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.Property;
import android.view.GestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import java.lang.Math;

public class GestureView extends SurfaceView implements 
GestureDetector.OnGestureListener,
SurfaceHolder.Callback,
Runnable {

	private static final String TAG ="Check";
	private static final String TAG2 ="Filing";
	private static final String TAG3 ="Direction";
	private static final String TAG4 ="Distance";
	private static final String TAG5 ="Action";
	
	private ArrayList<String> info;
	private GestureDetector gestureDetector;
	public Bitmap image;
	public Bitmap Sizeimage;
	
	
	int flag = 0;
	
	int ImageWidth;
	int ImageHeight;
	
	int ImageWidthCenter;
	int ImageHeightCenter;
	
	int DisplayWidth;
	int DisplayHeight;
	
	int DisplayWidthCenter;
	int DisplayHeightCenter;
	
	private int PosX;
    private int PosY;
	
	int circleVx = 5;
	int circleVy = 5;

	Thread thread = null;
	boolean isAttached;
	Resources r;
	
	final static int RIGHT      = 100;
	final static int LEFT       = 200;
	final static int UP         = 300;
	final static int DOWN       = 400;
	final static int RIGHT_UP   = 500;
	final static int RIGHT_DOWN = 600;
	final static int LEFT_UP    = 700;
	final static int LEFT_DOWN  = 800;
	int status;
	
	
	public GestureView(Context context){
		super(context);
		getHolder().addCallback(this);
		
		info = new ArrayList<String>();
		info.add("GestureEx");
		
		BitmapFactory.Options bmfOptions = new BitmapFactory.Options();
		bmfOptions.inSampleSize = 4;

		//setBackgroundColor(Color.WHITE);
		r=context.getResources();
		image = BitmapFactory.decodeResource(r, R.drawable.ic_launcher);
		Sizeimage = Bitmap.createScaledBitmap(image, 300, 300, false);
				
		Display disp =
				((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).
				getDefaultDisplay();
		
		DisplayWidth  = disp.getWidth();
		DisplayHeight = disp.getHeight();
		
		DisplayWidthCenter  = DisplayWidth/2;
		DisplayHeightCenter = DisplayHeight/2;
		
		ImageWidth  = Sizeimage.getWidth();
		ImageHeight = Sizeimage.getHeight();
		
		ImageWidthCenter  = ImageWidth/2;
		ImageHeightCenter = ImageHeight/2;
		
		PosX = DisplayWidthCenter  - ImageWidthCenter;
		PosY = DisplayHeightCenter - ImageHeightCenter;
		
		status = 0;
		
		
		thread = new Thread(this);
		

		gestureDetector = new GestureDetector(context,this);
		setFocusable(true);
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		
		
		
		
		
		canvas.drawBitmap(image, PosX, PosY,null);
	}
	private void addInfo(String str){
		info.add(0,str);
		while(info.size()>30)info.remove(info.size()-1);
		//invalidate();
	}
/*	
	private void doDraw(SurfaceHolder holder) {
		        Canvas canvas = holder.lockCanvas();
		        onDraw(canvas);
		        holder.unlockCanvasAndPost(canvas);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		//canvas.drawColor(Color.BLACK);			
		canvas.drawBitmap(image,PosX, PosY,null);
	}
*/
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(Sizeimage,PosX, PosY,null);
        holder.unlockCanvasAndPost(canvas);
//		// TODO Auto-generated method stub
		//doDraw(holder);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
            Canvas canvas = getHolder().lockCanvas();
            if (canvas != null)
            {
                canvas.drawColor(Color.WHITE);
                          
                canvas.drawBitmap(Sizeimage,PosX, PosY,null);
                getHolder().unlockCanvasAndPost(canvas);

                if(status == RIGHT){
               
                	PosX += circleVx;
                	Log.d(TAG3,"MOVE_RIGHT");
                }
                	
                else if(status == LEFT){
                	
                	PosX -= circleVx;
                	Log.d(TAG3,"MOVE_LEFT");
                }
                else if(status == UP){
                	
                	PosY -= circleVy;
                	Log.d(TAG3,"MOVE_UP");
                }
                else if(status == DOWN){
                	
                	PosY += circleVy;
                	Log.d(TAG3,"MOVE_DOWN");
                	
                }
                
                else if(status == RIGHT_UP){
                    
                	PosX += circleVx;
                	PosY -= circleVy;
                	Log.d(TAG3,"MOVE_RIGHT_UP");
                }
                	
                else if(status == RIGHT_DOWN){
                	
                	PosX += circleVx;
                	PosY += circleVy;
                	Log.d(TAG3,"MOVE_RIGHT_DOWN");
                }
                else if(status == LEFT_UP){
                	
                	PosX -= circleVx;
                	PosY -= circleVy;
                	Log.d(TAG3,"MOVE_LEFT_UP");
                }
                else if(status == LEFT_DOWN){
                	
                	PosX -= circleVx;
                	PosY += circleVy;
                	Log.d(TAG3,"MOVE_LEFT_DOWN");
                	
                }


            }
        }	
	}

	

	/*@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		isAttached=true;
		thread = new Thread(this);
		thread.start();
		
		Canvas canvas = holder.lockCanvas();
		canvas.drawBitmap(image, PosX, PosY,null);
		holder.unlockCanvasAndPost(canvas);
		
	}*/
	
//	void doDraw(SurfaceHolder holder) {
//		Log.d(TAG,"doDraw");
//		Canvas canvas = holder.lockCanvas();
//		//canvas.drawBitmap(image,PosX,PosY, null);
//		onDraw(canvas);
//		holder.unlockCanvasAndPost(canvas);
//		//invalidate();
//	}
//	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		isAttached = false;
		while (thread.isAlive());
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
		gestureDetector.onTouchEvent(event);
		return true;
	}
	
	public boolean onDown(MotionEvent e){
		
		Log.d(TAG5,"onDown"); 
		
		addInfo("Down("+(int)e.getX()+","+(int)e.getY()+")");
		
		
		Log.d(TAG,"Touch_circleX_circleY,"+PosX+","+PosY);
		Log.d(TAG,"Touch_X_Y,"+e.getX()+","+e.getY());
		if(((PosX + ImageWidthCenter  > e.getX()/* - ImageWidth / 2*/)&&(PosX - ImageWidthCenter < e.getX() /*+ ImageWidth / 2*/)) 
    			&& ((PosY + ImageHeightCenter > e.getY()/* - ImageHeight / 2*/)&&(PosY - ImageHeightCenter < e.getY()/* + ImageHeight / 2*/))){
    		
    		Log.d(TAG,"PosX + ImageWidthCenter::"+(PosX + ImageWidthCenter));
    		Log.d(TAG,"PosX - ImageWidthCenter::"+(PosX - ImageWidthCenter));
    		Log.d(TAG,"e.getX()::"+e.getX());

    		Log.d(TAG,"PosY + ImageHeightCenter::"+(PosX + ImageHeightCenter));
    		Log.d(TAG,"PosY - ImageHeightCenter::"+(PosX - ImageHeightCenter));
    		Log.d(TAG,"e.getY()::"+e.getY());

			//thread.start();
        flag = 1;
    	}
		return false;
	}
	
	public void onShowPress(MotionEvent e){
		addInfo("ShowPress("+(int)e.getX()+","+(int)e.getY()+")");
	}
	
	public boolean onSingleTapUp(MotionEvent e){
		Log.d(TAG5,"onSingleTapUp");
		addInfo("Up("+(int)e.getX()+","+(int)e.getY()+")");
		flag = 0;
		return false;
	}
	
	public void onLongPress(MotionEvent e){
		addInfo("LongPress("+(int)e.getX()+","+(int)e.getY()+")");
	}
	
	public boolean onFling(MotionEvent e0,MotionEvent e1,
			float velocityX, float velocityY){
		Log.d(TAG5,"onFling");
		addInfo("Fling("+(int)velocityX+","+(int)velocityY+")");
		if(flag == 1){
//		Log.d(TAG2,"e0:"+e0);
//		Log.d(TAG2,"e1:"+e1);
		Log.d(TAG2,"velocityX:"+velocityX);
		Log.d(TAG2,"velocityY:"+velocityY);
		double DifferenceX = Math.sqrt((e0.getX()- e1.getX())*(e0.getX()- e1.getX()));
		double DifferenceY = Math.sqrt((e0.getY()- e1.getY())*(e0.getY()- e1.getY()));
		double DifferenceXY =Math.sqrt((DifferenceX - DifferenceY) * (DifferenceX - DifferenceY)); 
		double distance = Math.sqrt((double)((int)e0.getX()- (int)e1.getX())*((int)e0.getX()- (int)e1.getX()) + 
				((int)e0.getY()- (int)e1.getY())*((int)e0.getY()- (int)e1.getY()));
		if(DifferenceXY>200){
			if(DifferenceX>DifferenceY){
				Log.d(TAG4,"Distance"+DifferenceXY);
				if((int)e0.getX()>(int)e1.getX()){
					//LEFT
					if(velocityX < -1000){
					status = LEFT;
					Log.d(TAG3,"LEFT");
					//Log.d(TAG4,"Distance"+DifferenceXY);
					}
				}
				else{
					//RIGHT
					if(velocityX > 1000){
					status = RIGHT;
					Log.d(TAG3,"RIGHT");
					//Log.d(TAG4,"Distance"+DifferenceXY);
					}
				}
			}
			else{
				Log.d(TAG4,"Distance"+DifferenceXY);
				if((int)e0.getY()>(int)e1.getY()){
					//UP
					if(velocityY < -1000){
					status = UP;
					Log.d(TAG3,"UP");
					//Log.d(TAG4,"Distance"+DifferenceXY);
					}
				}
				else{
					//DOWN
					if(velocityY > 1000){
					status = DOWN;
					Log.d(TAG3,"DOWN");
					//Log.d(TAG4,"Distance"+DifferenceXY);
					}
				}
			}
		}
		else{
			if((int)e0.getX()>(int)e1.getX()){
				if((int)e0.getY()>(int)e1.getY()){
					//LEFT_UP
					if(velocityX < -1000 && velocityY < -1000){
					status = LEFT_UP;
					Log.d(TAG3,"LEFT_UP");
					}
					
				}
				else{
					//LEFT_DOWN
					if(velocityX < -1000 && velocityY > 1000){
					status = LEFT_DOWN;
					Log.d(TAG3,"LEFT_DOWN");
					}
					
				}
			}
			else{
				if((int)e0.getY()>(int)e1.getY()){
					//RIGHT_UP
					if(velocityX > 1000 && velocityY < -1000){
						status = RIGHT_UP;
						Log.d(TAG3,"LEFT_UP");
						}
				}
				else{
					//RIGHT_DOWN
					if(velocityX > 1000 && velocityY > 1000){
						status = RIGHT_DOWN;
						Log.d(TAG3,"LEFT_UP");
					}
					
				}
			}
		}

		thread.start();
		flag = 0;
		}
		return false;
	}
	
	
	
	

	public boolean onScroll(MotionEvent e0,MotionEvent e1,
			float distanceX, float distanceY){
		Log.d(TAG5,"onScroll");
		Log.d(TAG,"Scroll,"+(int)distanceX+","+(int)distanceY);
		Log.d(TAG,"Scroll,"+(int)distanceX+","+(int)distanceY);
		addInfo("Scroll("+(int)distanceX+","+(int)distanceY+")");
		if(flag == 1 ){
			
			Log.d(TAG,"Touch_Move");	
			PosX -= (int)distanceX;
			PosY -= (int)distanceY;
		}
		
		surfaceCreated(getHolder()); 
		return false;
	}
	
	public boolean onSingleTapConfirmed(MotionEvent e){
		addInfo("SingleTap("+(int)e.getX()+","+(int)e.getY()+")");
		return false;
	}
	
	public boolean onDoubleTap(MotionEvent e){
		addInfo("DoubleTap("+(int)e.getX()+","+(int)e.getY()+")");
		return false;
	}
	
	public boolean onDoubleTapEvent(MotionEvent e){
		addInfo("DoubleTapEvent("+(int)e.getX()+","+(int)e.getY()+")");
		return false;
	}
}

