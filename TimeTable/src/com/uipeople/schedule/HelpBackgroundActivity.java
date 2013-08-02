package com.uipeople.schedule;

import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class HelpBackgroundActivity extends Activity {
	
	private Button	mBackButton;
	private Context mContext;
	private CustomViewPager mPager;
	
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		setContentView(R.layout.help_background_activity);
		mBackButton = (Button)findViewById(R.id.back);
		mBackButton.setOnClickListener(BackClickListener);
		mPager = (CustomViewPager)findViewById(R.id.pager);
        mPager.setAdapter(new PagerAdapterClass(getApplicationContext()));
        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
		mContext = this;
	}
	
	private View.OnClickListener BackClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		finish();
  	}
    };
    
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
    		return 2;
    	}
    	
    	@Override
    	public Object instantiateItem(View pager, int position) {
    		View v = null;
    		ImageView img;
    		switch(position) 
    		{
    		case 0:
    			img = new ImageView(mContext);
    			img.setImageResource(R.drawable.help_background_1);
    			v = img;
    			break;
    		case 1:
    			img = new ImageView(mContext);
    			img.setImageResource(R.drawable.help_background_2);
    			v = img;
    			break;
    		}
    		
    		((CustomViewPager)pager).addView(v, 0);
    		return v;
    	}
    	
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
