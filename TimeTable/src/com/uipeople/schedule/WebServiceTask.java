package com.uipeople.schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class WebServiceTask 
{
	static public class Result
    {
    	public Integer statusCode;
    	public String	data;
    }
	
	static public String convertStreamToString( InputStream ins )
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
	
	public abstract interface OnTaskListener
	{
		public abstract void onPostExecute(Result result);
	}
    
	static public class HttpPostTask extends AsyncTask<String,Void,Result>
    {
		private ProgressDialog	mProgress;
		private List<NameValuePair>	mParams;
		private String mUrl;
		private OnTaskListener mListener;
		public HttpPostTask(Context context, String url, List<NameValuePair> params, OnTaskListener listener)
		{
			mUrl = url;
			mParams = params;
			mListener = listener;
			
			mProgress = ProgressDialog.show(context, null, null);
	  		mProgress.setContentView(R.layout.progress);
		}
		
    	@Override
    	protected Result doInBackground(String... p)
    	{
    		DefaultHttpClient httpClient = new DefaultHttpClient();
      		HttpPost httpPost = new HttpPost(mUrl);
      		Integer statusCode = 0;
      		Result result = new Result();
      		
      		try
      		{
      			httpPost.setEntity(new UrlEncodedFormEntity(mParams, HTTP.UTF_8));
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
    		mListener.onPostExecute(result);
    		
    		if(mProgress != null)
    		{
    			mProgress.dismiss();
    		}
    	}
    }
	
	static public class HttpGetTask extends AsyncTask<String,Void,Result>
    {
		private ProgressDialog	mProgress = null;
		private String mUrl;
		private OnTaskListener mListener;
		private Boolean mLock;
		public HttpGetTask(Context context, String url, OnTaskListener listener)
		{
			mUrl = url;
			mListener = listener;
			mProgress = ProgressDialog.show(context, null, null);
	  		mProgress.setContentView(R.layout.progress);
	  		mLock = true;
		}
		
		public HttpGetTask(Context context, String url, OnTaskListener listener, Boolean lock)
		{
			mUrl = url;
			mListener = listener;
			mLock = lock;
			if(mLock == true)
			{
				mProgress = ProgressDialog.show(context, null, null);
		  		mProgress.setContentView(R.layout.progress);	
			}
		}
		
    	@Override
    	protected Result doInBackground(String... p)
    	{
    		DefaultHttpClient httpClient = new DefaultHttpClient();
      		HttpGet httpPost = new HttpGet(mUrl);
      		Integer statusCode = 0;
      		Result result = new Result();
      		
      		try
      		{
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
    		mListener.onPostExecute(result);
    		
    		if(mProgress != null)
    		{
    			mProgress.dismiss();
    		}
    	}
    }
	
	static public class HttpDeleteTask extends AsyncTask<String,Void,Result>
    {
		private ProgressDialog	mProgress;
		private String mUrl;
		private OnTaskListener mListener;
		public HttpDeleteTask(Context context, String url, OnTaskListener listener)
		{
			mUrl = url;
			mListener = listener;
			
			mProgress = ProgressDialog.show(context, null, null);
	  		mProgress.setContentView(R.layout.progress);
		}
		
    	@Override
    	protected Result doInBackground(String... p)
    	{
    		DefaultHttpClient httpClient = new DefaultHttpClient();
    		HttpDelete httpPost = new HttpDelete(mUrl);
      		Integer statusCode = 0;
      		Result result = new Result();
      		
      		try
      		{
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
    		mListener.onPostExecute(result);
    		
    		if(mProgress != null)
    		{
    			mProgress.dismiss();
    		}
    	}
    }

}
