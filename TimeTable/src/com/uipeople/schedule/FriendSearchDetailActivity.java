package com.uipeople.schedule;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

//로긴 시도후 계정정보가 없거나 로길실패면 로긴 액티비티 띄움
public class FriendSearchDetailActivity extends Activity
{
	Button		mBack;
	Context		mContext;
	ListView	mList;
	FriendListAdapter	mAdapter;
	TextView	mTitle;
	
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		setContentView(R.layout.friend_search_detail_activity); 
		
		mBack = (Button)findViewById(R.id.back);
		mList = (ListView)findViewById(R.id.list);
		mTitle = (TextView)findViewById(R.id.title);
		
		mBack.setOnClickListener(BackClickListener);
		mContext = this;
		
		Bundle extras = getIntent().getExtras();
		{
			if(extras != null)
			{
				ArrayList<Subject> list = extras.getParcelableArrayList("data");
				ArrayList<FriendData> friends = new ArrayList<FriendData>();
				for(Subject s : list)
				{
					FriendData friend = FriendActivity.getFriend(s.email);
					if(friend == null)
						continue;
					
					friends.add(friend);
				}
				
				mTitle.setText(extras.getString("subject"));
				mAdapter = new FriendListAdapter(mContext, R.layout.friendlist_row, friends);
				mList.setAdapter(mAdapter);
			}
		}
	}
	
	private View.OnClickListener BackClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		finish();
  	}
    };
    
	private View.OnClickListener EditClickListener = new View.OnClickListener() 
    {
		@Override
	  	public void onClick(View v) 
	  	{
			Intent intent = new Intent(mContext, FriendAddActivity.class);
			startActivity(intent);
	  	}
    };
    
    private View.OnClickListener Borrow1Listener = new View.OnClickListener() 
    {
		@Override
		public void onClick(View v) 
		{
	   		Intent intent = new Intent(mContext, FriendSearchSubjectActivity.class);
	   		intent.putExtra("subject", "science");
			mContext.startActivity(intent);
		}
	};
	
	private View.OnClickListener Borrow2Listener = new View.OnClickListener() 
    {
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			
		}
	};
	
	private View.OnClickListener Borrow3Listener = new View.OnClickListener() 
    {
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			
		}
	};
}

