package com.uipeople.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.RequestBundle;

public class FriendRecommandedNateOnActivity extends Activity
{
	Context		mContext;
	ListView	mList;
	FriendListAdapter	mAdapter;
	private ArrayList<FriendData> mArray;
	ProgressDialog	mProgress;
	
	APIRequest api;
	RequestBundle requestBundle;
	
	String URL = Const.SERVER + "/nateon/buddies";
	Map<String, Object> param;
	NateOnAPI	mNateOn;
	
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		setContentView(R.layout.friend_recommanded_activity); 
		
		LinearLayout r = (LinearLayout)findViewById(R.id.layout);
		mList = (ListView)r.findViewById(R.id.list);
		mContext = this;

		mArray = new ArrayList<FriendData>();
		mNateOn = new NateOnAPI();
		mNateOn.loadFriendList(OnLoadFriendListListener);
	}
	
	NateOnAPI.OnLoadFriendListListener OnLoadFriendListListener = new NateOnAPI.OnLoadFriendListListener() {
		
		@Override
		public void onPostExecute(String statusCode, ArrayList<FriendData> friends) {
			// TODO Auto-generated method stub
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			for(int i = 0; i < friends.size(); i++)
			{
				String nateId = friends.get(i).email;
				param.add( new BasicNameValuePair( String.format("emails[%d]", i), nateId));
			}
				
			new WebServiceTask.HttpPostTask(mContext, MainActivity.url + "/friends/recommand", param, TaskListener).execute("");
		}
	};
	
	private WebServiceTask.OnTaskListener TaskListener = new WebServiceTask.OnTaskListener()
    {
		@Override
		public void onPostExecute(WebServiceTask.Result result) {
			// TODO Auto-generated method stub
		    mArray = UserData.ConvertJsonToList(result.data);
			mList.setAdapter(new FriendListAdapter(mContext, R.layout.friend_search_result_row, mArray));
		}
    };
}