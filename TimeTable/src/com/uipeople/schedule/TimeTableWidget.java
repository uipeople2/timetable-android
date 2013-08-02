package com.uipeople.schedule;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class TimeTableWidget extends RemoteViewsService 
{
	 @Override
	  public RemoteViewsFactory onGetViewFactory(Intent intent) {
	    return(new TimeTableFactory(this.getApplicationContext(),
	                                 intent));
	  }
}
