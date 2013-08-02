package com.uipeople.schedule;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

public class FriendAddActivity extends TabActivity
{
	Settings 	mSettings;
	Button		mBack;
	Button 		mSearch;
	Context		mContext;
	Boolean		mEditMode;
	ListView	mList;
	FriendListAdapter	mAdapter;
	TabHost		mTabHost;
	Drawable	mOriginal;
	private ArrayList<FriendData> mArray;
	private static int count = 1;
	
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		mSettings = new Settings(this);
		super.onCreate(saveInstanceState);
		setContentView(R.layout.friend_add_activity); 
		
		LinearLayout r = (LinearLayout)findViewById(R.id.layout);
		mBack = (Button)r.findViewById(R.id.back);
		mList = (ListView)r.findViewById(R.id.list);
		
		mBack.setOnClickListener(BackClickListener);
		mContext = this;
		
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		Resources	res = getResources();
		
		intent = new Intent(this, FriendSearchActivity.class);
		spec = tabHost.newTabSpec("Search")
				.setIndicator(res.getString(R.string.friend_search), res.getDrawable(R.drawable.friend_add_activity_search))
				.setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent(this, FriendRequestedActivity.class);
		spec = tabHost.newTabSpec("Requested")
				.setIndicator(res.getString(R.string.friend_requested), res.getDrawable(R.drawable.request))
				.setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent(this, FriendRecommandedNateOnActivity.class);
		spec = tabHost.newTabSpec("Recommanded")
				.setIndicator(res.getString(R.string.friend_recommanded), res.getDrawable(R.drawable.recommand))
				.setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent(this, FriendNoticeActivity.class);
		spec = tabHost.newTabSpec("Notice")
				.setIndicator(res.getString(R.string.friend_notice), res.getDrawable(R.drawable.notice))
				.setContent(intent);
		tabHost.addTab(spec);
		
		mTabHost = tabHost; 
		
		mOriginal = mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).getBackground();
		
		mOriginal.setColorFilter(0xeeeeee, Mode.SRC);
		setSelectedTabColor();
        tabHost.setOnTabChangedListener(TabChangeListener);
        
        IntentFilter iff = new IntentFilter("notify-request");
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, iff);
        
     
	}
	
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			if(!mTabHost.getCurrentTabTag().equals("Requested"))
			{
				// Get extra data included in the Intent
				NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		        Notification note = new Notification(R.drawable.ic_launcher, "Today Class", System.currentTimeMillis());
		        Intent notificationIntent = new Intent(mContext, FriendAddActivity.class);
		        notificationIntent.putExtra("tab", "Requested");
		        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
		               Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
		        String message = intent.getStringExtra("message");
		        note.setLatestEventInfo(mContext, "Today Class", message, pendingIntent);
		        note.number = count++;
		        note.defaults |= Notification.DEFAULT_SOUND;
				note.defaults |= Notification.DEFAULT_VIBRATE;
				note.defaults |= Notification.DEFAULT_LIGHTS;
				note.flags |= Notification.FLAG_AUTO_CANCEL;
		        notificationManager.notify(0, note);
			}
		}
	};
	
	private void setSelectedTabColor()
	{
		
		for(int i=0; i < mTabHost.getTabWidget().getChildCount(); i++)
		{
			if(mTabHost.getCurrentTab() == i)
				continue;
			
			mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.WHITE);
			TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
	        tv.setTextColor(Color.BLACK);
		}
		
		mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab())
        .setBackground(mOriginal);
		
		TextView tv = (TextView) mTabHost.getCurrentTabView().findViewById(android.R.id.title); //Unselected Tabs
         tv.setTextColor(Color.BLACK);	
	}
	
	private TabHost.OnTabChangeListener TabChangeListener = new TabHost.OnTabChangeListener() {
		
		@Override
		public void onTabChanged(String tabId) {
			// TODO Auto-generated method stub

			setSelectedTabColor();
		}
	};
	
	private View.OnClickListener BackClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		finish();
  	}
    };
    
    private View.OnClickListener SearchClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		finish();
  	}
    };
}

