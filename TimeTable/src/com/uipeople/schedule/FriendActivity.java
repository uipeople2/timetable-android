package com.uipeople.schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

//로긴 시도후 계정정보가 없거나 로길실패면 로긴 액티비티 띄움
public class FriendActivity extends Activity
{
	Settings 	mSettings;
	Button		mBack;
	Button 		mEdit;
	Context		mContext;
	Boolean		mEditMode;
	ListView	mList;
	FriendListAdapter	mAdapter;
	ProgressDialog	mProgress;
	Button		mBorrow1;
	Button		mBorrow2;
	Button		mBorrow3;
	ImageButton		mBorrowOthers;
	Button		mBorrowSearch;
	Button		mBorrowCancel;
	EditText	mHiddenInput;
	LinearLayout	mBorrowList;
	LinearLayout	mBorrowHiddenList;
	
	static ArrayList<FriendData>	mFriends;
	
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		mSettings = new Settings(this);
		super.onCreate(saveInstanceState);
		setContentView(R.layout.friend_activity); 
		
		LinearLayout r = (LinearLayout)findViewById(R.id.layout);
		mBack = (Button)r.findViewById(R.id.back);
		mEdit = (Button)r.findViewById(R.id.add);
		mList = (ListView)r.findViewById(R.id.list);
		mBorrow1 = (Button)findViewById(R.id.borrow_1);
		mBorrow2 = (Button)findViewById(R.id.borrow_2);
		mBorrow3 = (Button)findViewById(R.id.borrow_3);
		mBorrowOthers = (ImageButton)findViewById(R.id.borrow_others);
		mHiddenInput = (EditText)findViewById(R.id.borrow_input);
		mBorrowList = (LinearLayout)findViewById(R.id.borrow_list);
		mBorrowHiddenList = (LinearLayout)findViewById(R.id.borrow_hidden);
		mBorrowSearch = (Button)findViewById(R.id.borrow_search);
		mBorrowCancel = (Button)findViewById(R.id.borrow_cancel);
		
		mBack.setOnClickListener(BackClickListener);
		mEdit.setOnClickListener(EditClickListener);
		mBorrow1.setOnClickListener(Borrow1Listener);
		mBorrow2.setOnClickListener(Borrow2Listener);
		mBorrow3.setOnClickListener(Borrow3Listener);
		mBorrowOthers.setOnClickListener(BorrowOthersListener);
		mBorrowSearch.setOnClickListener(BorrowSearchListener);
		mBorrowCancel.setOnClickListener(BorrowCancelListener);
		
		mEditMode = false;
		mContext = this;
		
		new WebServiceTask.HttpGetTask(mContext, MainActivity.url + "/friends", TaskListener).execute("");
	}
	
	public static FriendData getFriend(String email)
	{
		for(int i = 0; i < mFriends.size(); i++)
		{
			if(mFriends.get(i).email.equals(email))
				return mFriends.get(i);
		}
		
		return null;
	}
	
	private WebServiceTask.OnTaskListener TaskListener = new WebServiceTask.OnTaskListener()
    {
		@Override
		public void onPostExecute(WebServiceTask.Result result) {
			
			if(result.statusCode == 200)
			{
				ArrayList<FriendData> user = UserData.ConvertJsonToList(result.data);
				mAdapter = new FriendListAdapter(mContext, R.layout.friendlist_row, user);
				mList.setAdapter(mAdapter);
				mFriends = user;
			} else
			{
				Toast.makeText(mContext, R.string.error, Toast.LENGTH_SHORT).show();
				finish();
			}
		}
    };
	
	protected void onRestart() {
		super.onRestart();
		new WebServiceTask.HttpGetTask(mContext, MainActivity.url + "/friends", TaskListener, false).execute("");
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
	   		intent.putExtra("subject", "체육");
			mContext.startActivity(intent);
		}
	};
	
	private View.OnClickListener Borrow2Listener = new View.OnClickListener() 
    {
		@Override
		public void onClick(View v) 
		{
			Intent intent = new Intent(mContext, FriendSearchSubjectActivity.class);
	   		intent.putExtra("subject", "미술");
			mContext.startActivity(intent);
		}
	};
	
	private View.OnClickListener Borrow3Listener = new View.OnClickListener() 
    {
		@Override
		public void onClick(View v) 
		{
			Intent intent = new Intent(mContext, FriendSearchSubjectActivity.class);
	   		intent.putExtra("subject", "음악");
			mContext.startActivity(intent);
		}
	};
	
	private View.OnClickListener BorrowOthersListener = new View.OnClickListener() 
    {
		@Override
		public void onClick(View v) 
		{
			mBorrowHiddenList.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
			mBorrowHiddenList.requestLayout();
		}
	};
	
	private View.OnClickListener BorrowCancelListener = new View.OnClickListener() 
    {
		@Override
		public void onClick(View v) 
		{
			mBorrowHiddenList.getLayoutParams().height = 0;
			mBorrowHiddenList.requestLayout();
		}
	};
	
	private View.OnClickListener BorrowSearchListener = new View.OnClickListener() 
    {
		@Override
		public void onClick(View v) 
		{
			if(mHiddenInput.getText().length() == 0)
				return;
			
			Intent intent = new Intent(mContext, FriendSearchSubjectActivity.class);
	   		intent.putExtra("subject", mHiddenInput.getText().toString());
			mContext.startActivity(intent);
		}
	};
}

