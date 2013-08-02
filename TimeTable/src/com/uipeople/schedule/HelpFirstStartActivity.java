package com.uipeople.schedule;

import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;

public class HelpFirstStartActivity extends Activity {
	
	Context mContext;
	private CustomViewPager mPager;
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		setContentView(R.layout.help_first_start_activity);
		mPager = (CustomViewPager)findViewById(R.id.pager);
        mPager.setAdapter(new PagerAdapterClass(getApplicationContext()));
        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        mContext = this;
	}
	
    private class PagerAdapterClass extends PagerAdapter {
    	private LayoutInflater mInflater;
    	private ImageButton mPlus;
    	private Boolean isMenuOpened;
    	
    	public PagerAdapterClass(Context c) {
    		super();
    		mInflater = LayoutInflater.from(c);
    	}
    	
    	@Override
    	public int getCount() {
    		return 6;
    	}
    	
    	@Override
    	public Object instantiateItem(View pager, int position) {
    		View v = null;
    		if(position == 0) {
    			ImageView img = new ImageView(mContext);
    			img.setImageResource(R.drawable.help_first_1);
    			v = img;
    		} else if(position == 1) {
    			ImageView img = new ImageView(mContext);
    			img.setImageResource(R.drawable.help_first_2);
    			v = img;
    		} else if(position == 2) {
    			ImageView img = new ImageView(mContext);
    			img.setImageResource(R.drawable.help_first_3);
    			v = img;
    		} else if(position == 3) {
    			ImageView img = new ImageView(mContext);
    			img.setImageResource(R.drawable.help_first_4);
    			v = img;
    		} else if(position == 4) {
    			ImageView img = new ImageView(mContext);
    			img.setImageResource(R.drawable.help_first_5);
    			v = img;
    		} else if(position == 5) {
    			v = mInflater.inflate(R.layout.help_first_start_lastpage, null);
    			View button = v.findViewById(R.id.start);
    			button.setOnClickListener(StartListener);
    		}
    		
    		((CustomViewPager)pager).addView(v, 0);
    		return v;
    	}
    	
    	private View.OnClickListener StartListener = new View.OnClickListener()
    	{
			@Override
			public void onClick(View arg0) {
				finish();
			}
    		
    	};
    	
    	@Override
    	public void destroyItem(View pager, int position, Object view) {
    		((CustomViewPager)pager).removeView((View)view);
    	}
    	
    	@Override
    	public boolean isViewFromObject(View pager, Object obj) {
    		return pager == obj;
    	}
    	
    	@Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
    	@Override public Parcelable saveState() { return null; }
    	@Override public void startUpdate(View arg0) {}
    	@Override public void finishUpdate(View arg0) {}
    
    }
}
