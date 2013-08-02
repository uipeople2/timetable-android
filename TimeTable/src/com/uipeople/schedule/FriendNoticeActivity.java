package com.uipeople.schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendNoticeActivity extends Activity
{
	Context		mContext;
	ListView	mList;
	FriendListAdapter	mAdapter;
	private ArrayList<FriendData> mArray;
	HashMap<String, FriendData> mMap;
	ProgressDialog	mProgress;
	NateOnAPI	mNateOn;
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		setContentView(R.layout.friend_recommanded_activity); 
		
		LinearLayout r = (LinearLayout)findViewById(R.id.layout);
		mList = (ListView)r.findViewById(R.id.list);
		
		mContext = this;
		mNateOn = new NateOnAPI();
		mNateOn.loadFriendList(OnLoadFriendListListener);
		mMap = new HashMap<String, FriendData>();
		mArray = new ArrayList<FriendData>();
	}
	
	NateOnAPI.OnLoadFriendListListener OnLoadFriendListListener = new NateOnAPI.OnLoadFriendListListener() 
	{
		@Override
		public void onPostExecute(String statusCode, ArrayList<FriendData> friends) 
		{
			// TODO Auto-generated method stub
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			for(int i = 0; i < friends.size(); i++)
			{
				String nateId = friends.get(i).email;
				param.add( new BasicNameValuePair( String.format("emails[%d]", i), nateId));
				mMap.put(nateId, friends.get(i));
			}
				
			new WebServiceTask.HttpPostTask(mContext, MainActivity.url + "/friends/notice", param, TaskListener).execute("");
		}
	};
	
	private WebServiceTask.OnTaskListener TaskListener = new WebServiceTask.OnTaskListener()
    {
		@Override
		public void onPostExecute(WebServiceTask.Result result) 
		{
			// TODO Auto-generated method stub
		    ArrayList<FriendData> r = UserData.ConvertJsonToList(result.data);
		    
		    for(FriendData f : r)
		    {
		    	mMap.remove(f.email);
		    }
		    
		    for(FriendData f : mMap.values())
		    {
		    	f.status = 3;
		    	mArray.add(f);
		    }
		    
			mList.setAdapter(new FriendListAdapter(mContext, R.layout.friend_search_result_row, mArray));
		}
    };
}