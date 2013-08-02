package com.uipeople.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FriendSearchSubjectActivity extends Activity
{
	TimeTableControl	mTimeTable;
	Context		mContext;
	Button		mBack;
	TextView	mTitle;
	
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		setContentView(R.layout.friend_search_subject_activity); 
		
		mTimeTable = (TimeTableControl)findViewById(R.id.table);
		mBack = (Button)findViewById(R.id.back);
		mBack.setOnClickListener(BackClickListener);
		mTitle = (TextView)findViewById(R.id.title);
		mContext = this;
		
		String subject = "";
		Bundle extras = getIntent().getExtras();
		{
			if(extras != null)
			{
				subject = extras.getString("subject");
			}
		}
		
		mTitle.setText(subject);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add( new BasicNameValuePair( "subject", subject));
		
		new WebServiceTask.HttpPostTask(mContext, MainActivity.url + "/friends/subject", params, TaskListener).execute("");
	}
	
	private View.OnClickListener BackClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		finish();
  	}
    };
    
    private View.OnClickListener CellClickListener = new View.OnClickListener() 
    {
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			ArrayList<Subject> list = (ArrayList<Subject>)v.getTag();

			if(list.size() == 0)
				return;
			
			String subject = "";
			Bundle extras = getIntent().getExtras();
			{
				if(extras != null)
				{
					Intent intent = new Intent(mContext, FriendSearchDetailActivity.class);
			   		intent.putParcelableArrayListExtra("data", list);
			   		intent.putExtra("subject", list.get(0).subject);
					mContext.startActivity(intent);
				}
			}
		}
	};
    
	private WebServiceTask.OnTaskListener TaskListener = new WebServiceTask.OnTaskListener()
    {
		@Override
		public void onPostExecute(WebServiceTask.Result result) 
		{
			FriendSubject table = FriendSubject.fromJson(result.data);
			Settings settings = new Settings(mContext);
			mTimeTable.renderFriendSubject(CellClickListener, table, settings.weekday);
		}
    };
}

class FriendSubject
{
	public HashMap<Subject, Subject> subjects = new HashMap<Subject, Subject>();
	
	static public FriendSubject fromJson(String data)
	{
		FriendSubject table = new FriendSubject();
		
		try
		{
			JSONArray arr = new JSONArray(data);
			for(int j = 0; j < arr.length(); j++)
			{
				JSONObject obj = (JSONObject)arr.get(j);
				String name = obj.getJSONObject("_id").getString("name");
				String email = obj.getJSONObject("_id").getString("email");
			
				if(!obj.isNull("subjects"))
				{
					JSONArray friends = obj.getJSONArray("subjects");
					
					for(int i =0; i < friends.length(); i++)
					{
						JSONObject o = (JSONObject)friends.get(i);
						Subject subject = new Subject();
						subject.subject = o.getString("subject");
						subject.weekday = o.getInt("weekday");
						subject.time = o.getInt("time");
						subject.color = o.getInt("color");
						subject.teacher = name;
						subject.email = email;
						table.subjects.put(subject, subject);
					}
				}
			}
		} catch (Exception e)
		{
			
		}
		
		return table;
	}
}

