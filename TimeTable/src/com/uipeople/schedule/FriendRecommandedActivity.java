package com.uipeople.schedule;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FriendRecommandedActivity extends Activity
{
	Context		mContext;
	ListView	mList;
	FriendListAdapter	mAdapter;
	ProgressDialog	mProgress;
	
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		setContentView(R.layout.friend_recommanded_activity); 
		
		LinearLayout r = (LinearLayout)findViewById(R.id.layout);
		mList = (ListView)r.findViewById(R.id.list);
		
		mContext = this;
	}
	
    class ViewHolder {
        ImageView profile_pic;
        TextView name;
        TextView info;
    }
}