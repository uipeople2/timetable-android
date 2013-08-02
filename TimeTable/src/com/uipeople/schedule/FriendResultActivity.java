package com.uipeople.schedule;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class FriendResultActivity extends Activity
{
	Context		mContext;
	ListView	mList;
	Button		mBack;
	FriendListAdapter	mAdapter;
	private ArrayList<FriendData> mArray;
	
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		setContentView(R.layout.friend_result_activity); 
		
		LinearLayout r = (LinearLayout)findViewById(R.id.layout);
		mList = (ListView)r.findViewById(R.id.list);
		mBack = (Button)r.findViewById(R.id.back);
		mBack.setOnClickListener(BackClickListener);
		
		mContext = this;
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) 
		{
		    String value = extras.getString("data");
		    mArray = UserData.ConvertJsonToList(value);
			mAdapter = new FriendListAdapter(mContext, R.layout.friend_search_result_row, mArray, RowClickListener);
			mList.setAdapter(mAdapter);
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
    
    private View.OnClickListener RowClickListener = new View.OnClickListener() 
    {
	  	@Override
	  	public void onClick(View v) 
	  	{
	  		Integer position = (Integer) v.getTag();
	   		
			// 리스트에서 position에 맞는 데이터를 받아온다.
			FriendData data = mAdapter.getItem(position);
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
      		params.add( new BasicNameValuePair( "targetid", data.email));
      		new WebServiceTask.HttpPostTask(mContext, MainActivity.url + "/friends", params, ClickTaskListener).execute("");
			
      		mAdapter.remove(data);
	   		mAdapter.notifyDataSetChanged();
	  	}
    };
    
    private WebServiceTask.OnTaskListener ClickTaskListener = new WebServiceTask.OnTaskListener()
    {
		@Override
		public void onPostExecute(WebServiceTask.Result result) {
			if(result.statusCode == 200)
    		{
    			Toast.makeText(mContext, "add", 10).show();
    		}
    		else
    		{
    			Toast.makeText(mContext, "error", 10).show();
    		}
		}
    };
}
