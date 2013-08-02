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

public class TimePeriodListAdapter extends ArrayAdapter<Integer> 
{
	private Context mContext;
	private int mLayoutResource;
	private ArrayList<Integer> mList;
	private LayoutInflater mInflater;
	private View.OnClickListener	mClickListener;
	
	public TimePeriodListAdapter(Context context, int rowLayoutResource, ArrayList<Integer> objects, View.OnClickListener clickListener)
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
	public Integer getItem(int position)
	{
		return mList.get(position);
	}
	
	@Override
	public int getPosition(Integer item)
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
				
		convertView.setOnClickListener(mClickListener);
		convertView.setTag(position);
		return convertView;
	}
}
