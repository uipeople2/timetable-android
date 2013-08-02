
package com.uipeople.schedule;

import java.util.ArrayList;

import com.uipeople.schedule.TimeTableControl.TypeThema;

import android.R.anim;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class Path implements View.OnClickListener {
	private Context context;

	private static final int length = 220;
	private static final int duration = 100;
	private static final int sub_duration = 200;
	private static final int sub_select_duration = 200;
	private static final int sub_offset = 30;
	
	private ImageButton plus_button;
	
	private ArrayList<PathButton> buttons;
	private int buttonCount;
	private boolean isMenuOpened = false;
	private View.OnClickListener	mMenuListener;
	
	
	public Path(Context c, Activity view, OnClickListener menuListener)
	{
		mMenuListener = menuListener;
		context = c;
        plus_button = (ImageButton)view.findViewById(R.id.plus_button);
       
        plus_button.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isMenuOpened) {
					isMenuOpened = true;
				} else {
					isMenuOpened = false;
				}
				startMenuAnimation(isMenuOpened);
			}
		});
        
        buttons = new ArrayList<PathButton>();
        
        PathButton setting_button = (PathButton)view.findViewById(R.id.setting);
        setting_button.setOnClickListener(ClickListener );
        buttons.add(setting_button);
        
        PathButton share_button = (PathButton)view.findViewById(R.id.share);
        share_button.setOnClickListener(ClickListener );
        buttons.add(share_button);
        
        PathButton photo_button = (PathButton)view.findViewById(R.id.photo);
        photo_button.setOnClickListener(ClickListener );
        buttons.add(photo_button);
        
        PathButton list_button = (PathButton)view.findViewById(R.id.list);
        list_button.setOnClickListener(ClickListener );
        buttons.add(list_button);
	}
	
	private void startSubButtonSelectedAnimation(int index) {
		for(int i = 0 ; i < buttons.size() ; i++) {
			if(index == i) {
				PathButton view = buttons.get(i);
				
				AnimationSet animation = new AnimationSet(false);
				
				Animation translate = new TranslateAnimation(
		        		0.0f, view.getXOffset()
		        		, 0.0f, view.getYOffset());
				translate.setDuration(0);
				
				Animation scale = new ScaleAnimation(
						1.0f, 2.5f
						, 1.0f, 2.5f
						, Animation.RELATIVE_TO_SELF, 0.5f
						, Animation.RELATIVE_TO_SELF, 0.5f);
				scale.setDuration(sub_select_duration);
				
				Animation alpha = new AlphaAnimation(1.0f, 0.0f);
				alpha.setDuration(sub_select_duration);
				
				animation.addAnimation(scale);
				animation.addAnimation(translate);
				animation.addAnimation(alpha);
				
				view.startAnimation(animation);
			} else {
				PathButton view = buttons.get(i);
				
				AnimationSet animation = new AnimationSet(false);
				
				Animation translate = new TranslateAnimation(
		        		0.0f, view.getXOffset()
		        		, 0.0f, view.getYOffset());
				translate.setDuration(0);
				
				Animation scale = new ScaleAnimation(
						1.0f, 0.0f
						, 1.0f, 0.0f
						, Animation.RELATIVE_TO_SELF, 0.5f
						, Animation.RELATIVE_TO_SELF, 0.5f);
				scale.setDuration(sub_select_duration);
				
				Animation alpha = new AlphaAnimation(1.0f, 0.0f);
				alpha.setDuration(sub_select_duration);
				
				animation.addAnimation(scale);
				animation.addAnimation(translate);
				animation.addAnimation(alpha);
				
				view.startAnimation(animation);
			}
		}
		
		if(isMenuOpened) {
			isMenuOpened = false;
			
			Animation rotate = new RotateAnimation(
					-45, 0
					, Animation.RELATIVE_TO_SELF, 0.5f
					, Animation.RELATIVE_TO_SELF, 0.5f);
			
			rotate.setInterpolator(AnimationUtils.loadInterpolator(context,
	                        android.R.anim.anticipate_overshoot_interpolator));
			rotate.setFillAfter(true);
			rotate.setDuration(sub_select_duration);
			plus_button.startAnimation(rotate);
		}
	}
	
	private void startSubButtonAnimation(int index, boolean open) 
	{
		PathButton view = buttons.get(index);
		
		float endX = length * FloatMath.cos(
				(float) (Math.PI * 1/2 - (Math.PI * 1/2 * (index)/(buttons.size()-1))) + (float)(Math.PI * 0.5f)
				);
		float endY = length * FloatMath.sin(
				(float) (Math.PI * 1/2 - (Math.PI * 1/2 * (index)/(buttons.size()-1))) + (float)(Math.PI * 0.5f)
				);
		
		AnimationSet animation = new AnimationSet(false);
		Animation translate;
		Animation rotate = new RotateAnimation(
				0, 360
				, Animation.RELATIVE_TO_SELF, 0.5f
				, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(sub_duration);
		rotate.setRepeatCount(1);
		rotate.setInterpolator(AnimationUtils.loadInterpolator(context,
	            android.R.anim.accelerate_interpolator));
		
        RelativeLayout.LayoutParams params = (LayoutParams)view.getLayoutParams();

		if(open) {
			translate = new TranslateAnimation(
	        		-endX, 0
	        		, endY, 0);
	        translate.setDuration(sub_duration);
	        translate.setInterpolator(AnimationUtils.loadInterpolator(context,
	                android.R.anim.overshoot_interpolator));
	        translate.setStartOffset(sub_offset*index);
			
			params.rightMargin = (int) -endX + params.rightMargin;
			params.bottomMargin = (int) endY + params.bottomMargin;
			view.setLayoutParams(params);
			
			Animation alpha = new AlphaAnimation(0.0f, 1.0f);
			alpha.setDuration(300);
			animation.addAnimation(alpha);
			if(view.getBackground() != null)
				view.getBackground().setAlpha(255);
			
			view.setAlpha(1.0f);
			
		} else {
			translate = new TranslateAnimation(
					endX, 0
	        		, -endY, 0);
			translate.setDuration(sub_duration);
			translate.setStartOffset(sub_offset*(buttons.size()-(index+1)));
			translate.setInterpolator(AnimationUtils.loadInterpolator(context,
	                android.R.anim.anticipate_interpolator));
			
			params.rightMargin = (int) endX + params.rightMargin;
			params.bottomMargin = (int) -endY + params.bottomMargin;
	        view.setLayoutParams(params);
	        
	        Animation alpha = new AlphaAnimation(1.0f, 0.0f);
			alpha.setDuration(300);
			animation.addAnimation(alpha);
		}
	    
		animation.setFillEnabled(false);
		animation.setFillAfter(true);
		animation.addAnimation(rotate);
		animation.addAnimation(translate);
		
		view.startAnimation(animation);
	}
	
	
	private void startMenuAnimation(boolean open) {
		Animation rotate;
		
		if(open)
			rotate = new RotateAnimation(
					0, 45
					, Animation.RELATIVE_TO_SELF, 0.5f
					, Animation.RELATIVE_TO_SELF, 0.5f);
		else
			rotate = new RotateAnimation(
					-45, 0
					, Animation.RELATIVE_TO_SELF, 0.5f
					, Animation.RELATIVE_TO_SELF, 0.5f);
		
		rotate.setInterpolator(AnimationUtils.loadInterpolator(context,
	                    android.R.anim.anticipate_overshoot_interpolator));
		rotate.setFillAfter(true);
		rotate.setDuration(duration);
		plus_button.startAnimation(rotate);
		
		for(int i = 0 ; i < buttons.size() ; i++) {
			startSubButtonAnimation(i, open);
		}
	}
	
	private Animation.AnimationListener animListener = new AnimationListener() 
	{  
		public void onAnimationEnd(Animation anim)  
		{    
			buttonCount++;
			
			if(buttonCount != buttons.size())
			{
				return;
			}
			
			for(int i = 0 ; i < buttons.size() ; i++) 
			{
				PathButton view = buttons.get(i);
				RelativeLayout.LayoutParams params = (LayoutParams)view.getLayoutParams();
				
			
			//	view.setVisibility(View.INVISIBLE);
		     //   view.setLayoutParams(params);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
	};
	
	 private View.OnClickListener ClickListener = new View.OnClickListener() 
     {
	   	@Override
	   	public void onClick(View v) 
	   	{
	   		isMenuOpened = false;
	   		startMenuAnimation(isMenuOpened);
	   		mMenuListener.onClick(v);
	   	}
     };
     
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case  R.id.setting:
		{
			Toast.makeText(context, "Camera Clicked", Toast.LENGTH_SHORT).show();
			startSubButtonSelectedAnimation(0);
		}
		break;
		case R.id.list:
		{
			Toast.makeText(context, "With Clicked", Toast.LENGTH_SHORT).show();
			startSubButtonSelectedAnimation(1);
		}
		break;
	
		}
		
	}
}
