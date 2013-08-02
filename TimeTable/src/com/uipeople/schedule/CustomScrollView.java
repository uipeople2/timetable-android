package com.uipeople.schedule;

import android.content.Context;
import android.hardware.Camera.PreviewCallback;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.ScrollView;
import android.widget.Toast;

public class CustomScrollView extends ScrollView
{
	private int mDeltaY;
	Context	mContext;
	
	public CustomScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		mDeltaY = 200;
	}
	
	public void lastScrollY(int y)
	{
		//mDeltaY = y;
	}
	
	@Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        {
        	super.onScrollChanged(x, y, oldx, oldy);
        }
    }
}
