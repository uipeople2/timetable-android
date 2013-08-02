package com.uipeople.schedule;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.uipeople.schedule.R.color;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendListAdapter extends ArrayAdapter<FriendData> 
{
	private Context mContext;
	private int mLayoutResource;
	private ArrayList<FriendData> mList;
	private LayoutInflater mInflater;
	private View.OnClickListener	mClickListener;
	
	public FriendListAdapter(Context context, int rowLayoutResource, ArrayList<FriendData> objects)
	{
		super(context, rowLayoutResource, objects);
		this.mContext = context;
		this.mLayoutResource = rowLayoutResource;
		this.mList = objects;
		this.mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public FriendListAdapter(Context context, int rowLayoutResource, ArrayList<FriendData> objects, View.OnClickListener clickListener)
	{
		super(context, rowLayoutResource, objects);
		this.mContext = context;
		this.mLayoutResource = rowLayoutResource;
		this.mList = objects;
		this.mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mClickListener = clickListener;
	}
	
	@Override
	public int getCount()
	{
		return mList.size();
	}
	
	@Override
	public FriendData getItem(int position)
	{
		return mList.get(position);
	}
	
	@Override
	public int getPosition(FriendData item)
	{
		return mList.indexOf(item);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// convertView(View)가 없는 경우 inflater로 뷰 객체를 생성한다.
		if(convertView == null)
		{
			convertView = mInflater.inflate(mLayoutResource, null);
		}
		
		// 리스트에서 포지션에 맞는 데이터를 받아온다.
		FriendData data = getItem(position);
		
		// 데이터가 있는 경우에만 처리한다.
		if(data != null)
		{
			if( position % 2 == 0 )
				convertView.setBackgroundColor(Color.WHITE);
			else
				convertView.setBackgroundColor(0xffeeeeee);
			
			TextView school = (TextView)convertView.findViewById(R.id.school);
			if(school != null)
			{
				school.setText(data.school);
			}
			
			TextView grade = (TextView)convertView.findViewById(R.id.grade_group);
			if(grade != null)
			{
				grade.setText(data.grade);
			}
			
			ImageView image = (ImageView)convertView.findViewById(R.id.image);
			if(data.image != null && image != null)
			{
				image.setImageBitmap(ImageHelper.getRoundedCornerBitmap(data.image, 5));
			}
			
			ImageView schoolMark = (ImageView)convertView.findViewById(R.id.school_mark);
			if(schoolMark != null && data.school.length() > 0)
			{
				schoolMark.setImageResource(R.drawable.school);
			}
			
			TextView title = (TextView)convertView.findViewById(R.id.item_title);
			title.setText(data.name);
			// 어댑터에서의 findViewById() 메소드는 convertView 객체를 통해서 사용한다.
			Button button = (Button)convertView.findViewById(R.id.add);
			if(button != null)
			{
				if(data.status == 1)
				{
					button.setText(R.string.cancel);
					button.setOnClickListener(CancelClickListener);
				} else if(data.status == 2)
				{
					button.setText(R.string.accept);
					button.setOnClickListener(AcceptClickListener);
				} else if(data.status == 3)
				{
					button.setText(R.string.notice);
					button.setOnClickListener(NoticeClickListener);
				} else
				{
					button.setText(R.string.add);
					button.setOnClickListener(ClickListener);
				}
				button.setTag(position);
			} else
			{
				convertView.setOnClickListener(ShowListener);
				convertView.setOnLongClickListener(RowClickListener);
			}
		}
		
		convertView.setTag(position);
		return convertView;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v)
	{
		// 111번 라인에서 저장한 tag(position)을 꺼내온다.
		int position = (Integer) v.getTag();
		
		// 리스트에서 position에 맞는 데이터를 받아온다.
		FriendData data = getItem(position);
		
		// 다음 액티비티로 넘길 Bundle 데이터를 만든다.
	
	}
	
	private View.OnLongClickListener RowClickListener = new View.OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			
			showDialog((Integer)v.getTag());
			return false;
		}
	}; 
	
	private View.OnClickListener ShowListener = new View.OnClickListener() 
    {
	   	@Override
	   	public void onClick(View v)
	   	{
	   		Integer position = (Integer) v.getTag();
	   		FriendData data = getItem(position);
			
	   		Intent intent = new Intent(mContext, FriendTimetableActivity.class);
	   		intent.putExtra("targetid", data.email);
	   		intent.putExtra("name", data.name);
			mContext.startActivity(intent);
	   	}
    };
    
	private View.OnClickListener ClickListener = new View.OnClickListener() 
    {
	   	@Override
	   	public void onClick(View v)
	   	{
	   		Integer position = (Integer) v.getTag();
	   		
			// 리스트에서 position에 맞는 데이터를 받아온다.
			FriendData data = getItem(position);
			
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
      		params.add( new BasicNameValuePair( "targetid", data.email));
      		new WebServiceTask.HttpPostTask(mContext, MainActivity.url + "/friends", params, ClickTaskListener).execute("");
			
	   		mList.remove(data);
			notifyDataSetChanged();
	   	}
    };
    
    private View.OnClickListener AcceptClickListener = new View.OnClickListener() 
    {
	   	@Override
	   	public void onClick(View v)
	   	{
	   		int position = (Integer) v.getTag();
			
			// 리스트에서 position에 맞는 데이터를 받아온다.
			FriendData data = getItem(position);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
      		params.add( new BasicNameValuePair( "targetid", data.email));
      		new WebServiceTask.HttpPostTask(mContext, MainActivity.url + "/friends/accept", params, ClickTaskListener).execute("");
	   		mList.remove(data);
			notifyDataSetChanged();
	   	}
    };
    
    private View.OnClickListener CancelClickListener = new View.OnClickListener() 
    {
	   	@Override
	   	public void onClick(View v)
	   	{
	   		int position = (Integer) v.getTag();
			
			// 리스트에서 position에 맞는 데이터를 받아온다.
			FriendData data = getItem(position);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
      		params.add( new BasicNameValuePair( "targetid", data.email));
      		new WebServiceTask.HttpPostTask(mContext, MainActivity.url + "/friends/cancel", params, ClickTaskListener).execute("");
	   		mList.remove(data);
			notifyDataSetChanged();
	   	}
    };
    
    private View.OnClickListener NoticeClickListener = new View.OnClickListener() 
    {
	   	@Override
	   	public void onClick(View v)
	   	{
	   		int position = (Integer) v.getTag();
	   		
	   		FriendData data = getItem(position);
	   		mList.remove(position);
	   		notifyDataSetChanged();
	   		new NateOnAPI().sendMessage(data.email, mContext.getResources().getString(R.string.notice_message), SendMessageListener);
	   	}
    };
    
    private NateOnAPI.OnSendMessageListener SendMessageListener = new NateOnAPI.OnSendMessageListener() 
    {
		@Override
		public void onPostExecute(String statusCode, String data) 
		{
			// TODO Auto-generated method stub
			Toast.makeText(mContext, data, 10).show();
		}
	};
    private WebServiceTask.OnTaskListener AddResult = new WebServiceTask.OnTaskListener()
    {
		@Override
		public void onPostExecute(WebServiceTask.Result result) {
		}
    };
    
    private class Result
    {
    	public Integer statusCode;
    	public String	data;
    }
    
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

    private void showDialog(int pos)
	{
    	final int position = pos;
    	
		DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
		  		dialog.dismiss();
		  		
				// 리스트에서 position에 맞는 데이터를 받아온다.
				FriendData data = getItem(position);
		   		new WebServiceTask.HttpDeleteTask(mContext, MainActivity.url + "/friends/" + data.email, ClickTaskListener).execute("");
		   		mList.remove(position);
		   		notifyDataSetChanged();
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
			.setTitle(R.string.check_delete)
			.setPositiveButton(R.string.ok, ok)
			.setNegativeButton(R.string.cancel, cancel)
			.show();
	}
}
