package com.uipeople.schedule;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetConfig extends Activity 
{
	Button configOkButton;
	CheckBox mCheckWhite;
	CheckBox mCheckGrey;
	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub 
		super.onCreate(savedInstanceState);
		setResult(RESULT_CANCELED);     
		setContentView(R.layout.widget_config);       
		configOkButton = (Button)findViewById(R.id.okconfig);     
		mCheckWhite = (CheckBox)findViewById(R.id.white);
		mCheckGrey = (CheckBox)findViewById(R.id.grey);
		
		configOkButton.setOnClickListener(configOkButtonOnClickListener);
		mCheckWhite.setOnCheckedChangeListener(checkWhiteListener);
		mCheckGrey.setOnCheckedChangeListener(checkWhiteListener);
		Intent intent = getIntent();     
		Bundle extras = intent.getExtras();     
		if (extras != null) 
		{        
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}      
		// If they gave us an intent without the widget id, just bail.     
		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) 
		{         
			finish();     
		}
	}
	
	private CompoundButton.OnCheckedChangeListener checkWhiteListener = new CompoundButton.OnCheckedChangeListener() 
	{
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			
			if(!isChecked)
				return;
			
			if(buttonView.getId() == R.id.white && isChecked)
			{
				mCheckWhite.setChecked(isChecked);
				mCheckGrey.setChecked(!isChecked);
				mCheckWhite.setClickable(!isChecked);
				mCheckGrey.setClickable(isChecked);
			} else if(isChecked)
			{
				mCheckWhite.setChecked(!isChecked);
				mCheckGrey.setChecked(isChecked);
				mCheckWhite.setClickable(isChecked);
				mCheckGrey.setClickable(!isChecked);
			}
		}
	};
	private Button.OnClickListener configOkButtonOnClickListener= new Button.OnClickListener()
	{
		@Override
		public void onClick(View arg0) 
		{ // TODO Auto-generated method stub 
			
			int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			if (currentapiVersion < android.os.Build.VERSION_CODES.HONEYCOMB)
			{

				String textMsg = getResources().getString(R.string.version);
				
				Toast.makeText(WidgetConfig.this, textMsg, 10).show();
				Intent resultValue = new Intent(); 
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId); 
				setResult(RESULT_CANCELED, resultValue); 
				finish();
				return;
			} 
			
			final Context context = WidgetConfig.this;
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context); 
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout); 
			Intent svcIntent=new Intent(context, TimeTableWidget.class);
			
		      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		      SharedPreferences.Editor edit = prefs.edit();
		     
		      
			if(mCheckWhite.isChecked())
			{
				views.setInt(R.id.weekday, "setBackgroundResource", R.drawable.white_top_round_shape);
				views.setInt(R.id.table, "setBackgroundResource", R.drawable.white_drop_shadow);
			    svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
			    svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
			    svcIntent.putExtra("thema", 1);

	            edit.putInt("thema" + mAppWidgetId, 1);
			} else
			{
				views.setInt(R.id.weekday, "setBackgroundResource", R.drawable.grey_top_round_shape);
				views.setInt(R.id.table, "setBackgroundResource", R.drawable.drop_shadow);
			    svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
			    svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
			    edit.putInt("thema" + mAppWidgetId, 0);
			    svcIntent.putExtra("thema", 0);
			}

		    edit.commit();
		    views.setRemoteAdapter(mAppWidgetId, R.id.words,
		                              svcIntent);
		    views.setTextViewText(R.id.weekday, TimeTableControl.today());
		    Intent clickIntent=new Intent(context, MainActivity.class);
		    PendingIntent clickPI=PendingIntent
		                              .getActivity(context, 0,
		                                            clickIntent,
		                                            PendingIntent.FLAG_UPDATE_CURRENT);
		      
		    views.setPendingIntentTemplate(R.id.words, clickPI);
		    appWidgetManager.updateAppWidget(mAppWidgetId, views);
		    
			Intent resultValue = new Intent(); 
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId); 
			setResult(RESULT_OK, resultValue); 
			finish();
		}
	};
}