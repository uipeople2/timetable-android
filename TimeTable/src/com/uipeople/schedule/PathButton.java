package com.uipeople.schedule;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageButton;

public class PathButton extends ImageButton {
	private float x_offset = 0;
	private float y_offset = 0;
	
	public PathButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public PathButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PathButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void getHitRect(Rect outRect) {
		Rect curr = new Rect();
	    super.getHitRect(curr);
	    
	    outRect.bottom = (int) (curr.bottom + y_offset);
	    outRect.top = (int) (curr.top + y_offset);
	    outRect.left = (int) (curr.left + x_offset);
	    outRect.right = (int) (curr.right + x_offset);
	}
	
	public void setOffset(float endX, float endY) {
		x_offset = endX;
		y_offset = endY;
	}
	
	public float getXOffset() {
		return x_offset;
	}
	
	public float getYOffset() {
		return y_offset;
	}
}
