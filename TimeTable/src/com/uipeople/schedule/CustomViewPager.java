package com.uipeople.schedule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

	private boolean enabled;
	Paint paint = new Paint();

	public CustomViewPager(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    this.enabled = true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (this.enabled) {
	        return super.onTouchEvent(event);
	    }

	    return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
	    if (this.enabled) {
	        return super.onInterceptTouchEvent(event);
	    }

	    return false;
	}

	public void setPagingEnabled(boolean enabled) {
	    this.enabled = enabled;
	} }