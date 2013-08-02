package com.uipeople.schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

//로긴 시도후 계정정보가 없거나 로길실패면 로긴 액티비티 띄움
public class FriendSearchActivity extends Activity
{
	Settings 	mSettings;
	Button 		mSearch;
	Context		mContext;
	ListView	mList;
	EditText	mName;
	EditText	mEmail;
	EditText	mSchool;
	EditText	mGrade;
	EditText	mGroup;
	FriendListAdapter	mAdapter;
	private ArrayList<FriendData> mArray;
	ProgressDialog	mProgress;
	
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		mSettings = new Settings(this);
		super.onCreate(saveInstanceState);
		setContentView(R.layout.friend_search_activity); 
		
		LinearLayout r = (LinearLayout)findViewById(R.id.layout);
		mList = (ListView)r.findViewById(R.id.list);
		mSearch = (Button)r.findViewById(R.id.do_search);
		mEmail = (EditText)r.findViewById(R.id.email);
		mName = (EditText)r.findViewById(R.id.name);
		mSchool = (EditText)r.findViewById(R.id.school);
		mGrade = (EditText)r.findViewById(R.id.grade);
		mGroup = (EditText)r.findViewById(R.id.group);
		
		mSearch.setOnClickListener(SearchClickListener);
		mContext = this;
	}
	
    private View.OnClickListener SearchClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		mProgress = ProgressDialog.show(mContext, null, null);
  		mProgress.setContentView(R.layout.progress);
  		new Task().execute("");
  	}
    };
    

    private static String convertStreamToString( InputStream ins )
    {
    	BufferedReader reader = new BufferedReader( new InputStreamReader(ins) );
    	StringBuilder sb = new StringBuilder();
    	
    	String line = null;
    	try
    	{
    		while((line = reader.readLine()) != null)
    		{
    			sb.append(line + "\n");
    		}
    	} catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    	
    	return sb.toString();
    }
    
    private class Result
    {
    	public Integer statusCode;
    	public String	data;
    }
    
    private class Task extends AsyncTask<String,Void,Result>
    {
    	
    	
    	@Override
    	protected Result doInBackground(String... p)
    	{
    		
      		
    		DefaultHttpClient httpClient = new DefaultHttpClient();
      		HttpPost httpPost = new HttpPost(MainActivity.url + "/friends/search");

      		List<NameValuePair> params = new ArrayList<NameValuePair>();
      		params.add( new BasicNameValuePair( "email", mEmail.getText().toString()));
      		params.add( new BasicNameValuePair( "name", mName.getText().toString()));
      		params.add( new BasicNameValuePair( "school", mSchool.getText().toString()));
      		Integer statusCode = 0;
      		Result result = new Result();
      		
      		try
      		{
      			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
      			HttpResponse respose = httpClient.execute( httpPost, TLoginActivity.mHttpContext );
      			statusCode = respose.getStatusLine().getStatusCode();
      			HttpEntity entity = respose.getEntity();

      			
      			if(entity != null)
      			{
      				InputStream ins = entity.getContent();
      				result.data = convertStreamToString( ins );
      				
      				try 
      				{
      					ins.close();
      				} catch (IOException e)
      				{
      					e.printStackTrace();
      				}
      			}
      		} catch ( Exception e)
      		{
      			e.printStackTrace();
      			statusCode = 201;
      		}
      		
  			result.statusCode = statusCode;
      		return result;
    	}
    	
    	protected void onPostExecute(Result result)
    	{
    		if(result.statusCode == 200)
    		{
    			Intent intent = new Intent(mContext, FriendResultActivity.class);
    			intent.putExtra("data", result.data);
      			startActivity(intent);
      			
    		}
    		else
    		{
    			Toast.makeText(mContext, getResources().getString(R.string.confirm_email), 10).show();
    		}
    		
    		if(mProgress != null)
    		{
    			mProgress.dismiss();
    		}
    	}
    }
}

