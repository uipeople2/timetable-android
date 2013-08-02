package com.uipeople.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FriendTimetableActivity extends Activity
{
	TimeTableControl	mTimeTable;
	Context		mContext;
	Button		mBack;
	Button		mCopy;
	TextView	mTitle;
	FriendTimeTable	mFriendTable;
	
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		setContentView(R.layout.friend_timetable_activity); 
		
		mTimeTable = (TimeTableControl)findViewById(R.id.table);
		mBack = (Button)findViewById(R.id.back);
		mCopy = (Button)findViewById(R.id.copy);
		mBack.setOnClickListener(BackClickListener);
		mCopy.setOnClickListener(CopyClickListener);
		mTitle = (TextView)findViewById(R.id.title);
		mContext = this;
		
		String targetid = "";
		String name = "";
		Bundle extras = getIntent().getExtras();
		{
			if(extras != null)
			{
				targetid = extras.getString("targetid");
				name = extras.getString("name");
			}
		}
		
		mTitle.setText(String.format(getResources().getString(R.string.friends_timetable), name));
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add( new BasicNameValuePair( "targetid", targetid));
		
		new WebServiceTask.HttpPostTask(mContext, MainActivity.url + "/friends/timetable", params, TaskListener).execute("");
	}
	
	private View.OnClickListener BackClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		finish();
  	}
    };
    
    private View.OnClickListener CopyClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		showDialog();
  	}
  	
  	private void showDialog()
	{
		DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
		  		dialog.dismiss();
		  		Settings settings = new Settings(mContext);
		  		settings.table = mFriendTable.subjects;
		  		settings.saveSettings();

		  		Toast.makeText(mContext, R.string.completed_copy, Toast.LENGTH_SHORT).show();
			}
		};
		
		DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		};
		
		new AlertDialog.Builder(mContext)
			.setTitle(R.string.check_copy)
			.setPositiveButton(R.string.ok, ok)
			.setNegativeButton(R.string.cancel, cancel)
			.show();
	}
    };
    
	private WebServiceTask.OnTaskListener TaskListener = new WebServiceTask.OnTaskListener()
    {
		@Override
		public void onPostExecute(WebServiceTask.Result result) 
		{
			FriendTimeTable table = FriendTimeTable.fromJson(result.data);
			mFriendTable = table;
			Settings settings = new Settings(mContext);
			mTimeTable.renderFriend(table, settings.weekday);
		}
    };
}

class FriendTimeTable
{
	public String name = "";
	public HashMap<Subject, Subject> subjects = new HashMap<Subject, Subject>();
	
	static public FriendTimeTable fromJson(String data)
	{
		FriendTimeTable table = new FriendTimeTable();
		
		try
		{
			JSONObject obj = new JSONObject(data);
			table.name = obj.getString("name");
			
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
					table.subjects.put(subject, subject);
				}
			}
		} catch (Exception e)
		{
			
		}
		
		return table;
	}
}

class MyProfile
{
	public String name = "";
	public Integer revision = 0;
	public HashMap<Subject, Subject> subjects = new HashMap<Subject, Subject>();
	
	static public MyProfile fromJson(String data)
	{
		MyProfile table = new MyProfile();
		
		try
		{
			JSONObject obj = new JSONObject(data);
			table.name = obj.getString("name");
			table.revision = obj.getInt("revision");
			
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
					subject.teacher = o.getString("teacher");
					subject.email = o.getString("email");
					subject.memo = o.getString("memo");
					subject.phone = o.getString("phone");
					subject.classRoom = o.getString("classroom");
					subject.period = o.getInt("period");
					table.subjects.put(subject, subject);
				}
			}
		} catch (Exception e)
		{
			
		}
		
		return table;
	}
}

