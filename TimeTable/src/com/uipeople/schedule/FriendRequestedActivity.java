package com.uipeople.schedule;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;

import com.handmark.pulltorefresh.extras.listfragment.PullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;

public final class FriendRequestedActivity extends FragmentActivity
{
	Context		mContext;
	ListView	mList;
	FriendListAdapter	mAdapter;
	private ArrayList<FriendData> mArray;
	ProgressDialog	mProgress;
	private	PullToRefreshListFragment	mPullRefreshListFragment;
	private PullToRefreshListView 	mPullRefreshListView;
	private static int count = 1;
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		setContentView(R.layout.friend_requested_activity); 
		
		mPullRefreshListFragment = (PullToRefreshListFragment)getSupportFragmentManager().findFragmentById(R.id.frag_ptr_list);
		mPullRefreshListView = mPullRefreshListFragment.getPullToRefreshListView();
		mPullRefreshListView.setOnRefreshListener(RefreshListener);
		mList = mPullRefreshListView.getRefreshableView();
		mPullRefreshListFragment.setListShown(true);
		mContext = this;

		new WebServiceTask.HttpGetTask(mContext, MainActivity.url + "/users", TaskListener).execute("");
	}
	
	protected void onResume() {
        super.onResume();

        IntentFilter iff = new IntentFilter("notify-request");
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, iff);
    }
	
	protected void onPause() {
		  super.onPause();
		  LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
		}

	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			// Get extra data included in the Intent
			String message = intent.getStringExtra("message");
			new WebServiceTask.HttpGetTask(mContext, MainActivity.url + "/users", TaskListener).execute("");
		}
	};
	
	@Override
	protected void onDestroy() {
	  // Unregister since the activity is about to be closed.
	  LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	  super.onDestroy();
	}
		
	private OnRefreshListener<ListView> RefreshListener = new OnRefreshListener<ListView>() 
	{
		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) 
		{
			// TODO Auto-generated method stub
			
	    	new WebServiceTask.HttpGetTask(mContext, MainActivity.url + "/users", TaskListener).execute("");
	    };
    };
    
    public WebServiceTask.OnTaskListener TaskListener = new WebServiceTask.OnTaskListener()
    {
		@Override
		public void onPostExecute(WebServiceTask.Result result) 
		{
			if(result.statusCode == 200)
			{
				mArray = UserData.ConvertJsonToList(result.data);
				
				mAdapter = new FriendListAdapter(mContext, R.layout.friend_search_result_row, mArray);
				mList.setAdapter(mAdapter);
			} else
			{
				Intent intent = new Intent(mContext, TLoginActivity.class);
	  			startActivity(intent);
			}

			mPullRefreshListView.onRefreshComplete();
			if(mProgress != null)
			{
				mProgress.dismiss();
				mProgress = null;
			}

		}
    };
}

