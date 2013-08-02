package com.uipeople.schedule;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RegisterActivity extends Activity
{
	Settings 	mSettings;
	Button		mBack;
	Button		mRegister;
	Context		mContext;
	EditText	mEmail;
	EditText	mName;
	EditText	mPassword;
	EditText	mPasswordConfirm;
	ProgressDialog	mProgress = null;
	
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		mSettings = new Settings(this);
		super.onCreate(saveInstanceState);
		setContentView(R.layout.register_activity); 
		
		LinearLayout r = (LinearLayout)findViewById(R.id.layout);
		mBack = (Button)r.findViewById(R.id.back);
		mRegister = (Button)r.findViewById(R.id.register);
		mEmail = (EditText)r.findViewById(R.id.email);
		mName = (EditText)r.findViewById(R.id.name);
		mPassword = (EditText)r.findViewById(R.id.password);
		mPasswordConfirm = (EditText)r.findViewById(R.id.password_confirm);
		
		mBack.setOnClickListener(BackClickListener);
		mRegister.setOnClickListener(RegisterClickListener);
		mContext = this;
		
	}
	
	private View.OnClickListener BackClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		finish();
  	}
    };
    
    private View.OnClickListener RegisterClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		Task task = new Task();
  		
  		/*if(!isEmailValid(mEmail.getText()))
  		{
  			mEmail.setError(getResources().getString(R.string.invalid_email));
  			return;
  		}*/
  		
  		if(TextUtils.isEmpty(mPassword.getText()) || mPassword.getText().length()  < 4)
  		{
  			mPassword.setError(getResources().getString(R.string.too_short_password));
  			return;
  		}
  		
  		if(TextUtils.isEmpty(mName.getText()) || mName.getText().length()  < 1)
  		{
  			mName.setError(getResources().getString(R.string.too_short_name));
  			return;
  		}
  		
  		if(!mPassword.getText().toString().equals(mPasswordConfirm.getText().toString()))
  		{
  			Toast.makeText(mContext, getResources().getString(R.string.not_match_password), Toast.LENGTH_SHORT).show();
  			return;
  		}
  		
  		mProgress = ProgressDialog.show(mContext, null, null);
  		mProgress.setContentView(R.layout.progress);
  		task.execute("");
  	}
    };
    
    private boolean isEmailValid(CharSequence email)
    {
    	return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    private class Task extends AsyncTask<String,Void,Integer>
    {
    	@Override
    	protected Integer doInBackground(String... p)
    	{
    		HttpClient httpClient = Http.createHttpClient(mContext, 5000);
    		if(httpClient == null)
    			return 503;
    		
      		HttpPost httpPost = new HttpPost(MainActivity.url+ "/users");
      		
      		List<NameValuePair> params = new ArrayList<NameValuePair>();
      		params.add( new BasicNameValuePair( "email", mEmail.getText().toString()));
      		params.add( new BasicNameValuePair( "name", mName.getText().toString()));
      		params.add( new BasicNameValuePair( "password", mPassword.getText().toString()));
      		
      		try
      		{
      			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
      			HttpResponse respose = httpClient.execute( httpPost );
      			
      			if(respose.getStatusLine().getStatusCode() != 200)
      			{
      				return respose.getStatusLine().getStatusCode();
      			}
      			
      		} catch ( Exception e)
      		{
      			e.printStackTrace();
      		}
      		
      		return 200;
    	}
    	
    	protected void onPostExecute(Integer status)
    	{
    		if(status == 200)
    		{
    			Toast.makeText(mContext, getResources().getString(R.string.created_account), 10).show();
    			finish();
    		}
    		else if(status == 203)
    		{
    			Toast.makeText(mContext, getResources().getString(R.string.duplication), 10).show();
    		}
    		else
    		{
    			Toast.makeText(mContext, R.string.error, 10).show();
    		}
    		
    		if(mProgress != null)
    		{
    			mProgress.dismiss();
    		}
    	}
    }
}


