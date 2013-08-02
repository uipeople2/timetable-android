package com.uipeople.schedule;

import java.util.HashMap;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.Toast;

public class TimeTableProvider extends AppWidgetProvider {
	
	 public static String EXTRA_WORD=
			    "com.uipeople.schedule.WORD";
	 
  	 @Override
	 public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
		 for (int i=0; i<appWidgetIds.length; i++) {
		      Intent svcIntent=new Intent(context, TimeTableWidget.class);
		      
		      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		      int thema = prefs.getInt("thema" + appWidgetIds[i], 0);
		      
		      svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
		      svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
		      svcIntent.putExtra("thema", thema);
		      
		      RemoteViews widget=new RemoteViews(context.getPackageName(),
		                                          R.layout.widget_layout);
		      widget.setRemoteAdapter(appWidgetIds[i], R.id.words,
		                              svcIntent);
		      
		      Intent clickIntent=new Intent(context, MainActivity.class);
		      PendingIntent clickPI=PendingIntent
		                              .getActivity(context, 0,
		                                            clickIntent,
		                                            PendingIntent.FLAG_UPDATE_CURRENT);
		      
		      widget.setPendingIntentTemplate(R.id.words, clickPI);

		      widget.setTextViewText(R.id.weekday, TimeTableControl.today());
		      
		      if(thema == 1)
		      {
			      widget.setInt(R.id.weekday, "setBackgroundResource", R.drawable.white_top_round_shape);
			      widget.setInt(R.id.table, "setBackgroundResource", R.drawable.white_drop_shadow);
		      } else
		      {
		    	  widget.setInt(R.id.weekday, "setBackgroundResource", R.drawable.grey_top_round_shape);
		    	  widget.setInt(R.id.table, "setBackgroundResource", R.drawable.drop_shadow);

		      }

		      appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
		 }
		 
		 appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.words);
		 super.onUpdate(context, appWidgetManager, appWidgetIds);
	 }

	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if(intent.getAction().equals("android.appwidget.action.notify"))
		{
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
			                           new ComponentName(context, TimeTableProvider.class));
			
			for (int i=0; i<appWidgetIds.length; i++) {
			      Intent svcIntent=new Intent(context, TimeTableWidget.class);
			      
			      svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			      svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
			      
			      RemoteViews widget=new RemoteViews(context.getPackageName(),
			                                          R.layout.widget_layout);

			      widget.setRemoteAdapter(appWidgetIds[i], R.id.words,
			                              svcIntent);

			      Intent clickIntent=new Intent(context, MainActivity.class);
			      PendingIntent clickPI=PendingIntent
			                              .getActivity(context, 0,
			                                            clickIntent,
			                                            PendingIntent.FLAG_UPDATE_CURRENT);
			      
			      widget.setPendingIntentTemplate(R.id.words, clickPI);

			      widget.setTextViewText(R.id.weekday, TimeTableControl.today());
			      appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
			 }
			appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.words);
		}
		super.onReceive(context, intent);
	}
}