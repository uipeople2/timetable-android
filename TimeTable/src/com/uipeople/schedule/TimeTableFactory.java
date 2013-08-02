package com.uipeople.schedule;

import java.util.Calendar;
import java.util.HashMap;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

public class TimeTableFactory implements RemoteViewsService.RemoteViewsFactory 
{
  private Context ctxt=null;
  private int appWidgetId;
  private int mCellColor1;
  private int mCellColor2;
  private int mCellLineColor;
  private int mCellTextColor;
  private int mTimeLineColor;
  private Settings mSetting;
  private HashMap<Integer, Subject> mArray = new HashMap<Integer, Subject>();
  public TimeTableFactory(Context ctxt, Intent intent) {
      this.ctxt=ctxt;
      appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                                      AppWidgetManager.INVALID_APPWIDGET_ID);
      
      int thema = intent.getIntExtra("thema", 0);
      if(thema == 0)
      {
    	  mCellColor1 = Color.rgb(66,69,74);
    	  mCellColor2 = mCellColor1;
    	  mCellLineColor = Color.rgb(84,85,85);
    	  mCellTextColor = Color.WHITE;
    	  mTimeLineColor = Color.WHITE;
      } else
      {
    	  mCellColor1 = Color.WHITE;
    	  mCellColor2 = Color.rgb(250, 250, 250);
    	  mCellLineColor = Color.rgb(235,235,235);
    	  mCellTextColor = Color.rgb(112, 112, 112);
    	  mTimeLineColor = Color.rgb(199, 199, 199);
      }

      mSetting = new Settings(ctxt);
  }
  
  @Override
  public void onCreate() {
    // no-op
  }
  
  @Override
  public void onDestroy() {
    // no-op
  }
  
  @Override
  public int getCount() {
	  
	HashMap<Subject, Subject> m = mSetting.table;
	if(m == null)
		return 7;
	
	Calendar calendar = Calendar.getInstance();
	HashMap<Integer, Subject> mm = new HashMap<Integer, Subject>();
	int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
	
	for(Subject s : m.values())
	{
		if(s.weekday == day)
			mm.put(s.time, s);
	}
	
	int c = 0;
	for(int i = 18; i < 38; c++)
    {
    	if(mm.containsKey(i))
    	{
    		Subject ss = mm.get(i);
    		mArray.put(c, ss);
    		i+= ss.period;
    		continue;
    	}
    	
    	//°ø°­
    	if(i % 2 == 0 && !mm.containsKey(i+1))
    	{
    		Subject s = new Subject();
    		s.time = i;
    		s.weekday = day;
    		s.period = 1;
    		mArray.put(c, s);
    		i += 2;
    		continue;
    	}
    	
    	Subject s = new Subject();
		s.time = i;
		s.weekday = day;
		s.period = 1;
		mArray.put(c, s);
		i++;
    }
	
    return c;
  }

  @Override
  public RemoteViews getViewAt(int position) 
  {
	Subject ss = mArray.get(position);
	  
	int res = R.layout.widget_row1;
	switch(ss.period)
	{
	case 1:
		res = R.layout.widget_row1;
		break;
	case 2:
		res = R.layout.widget_row2;
		break;
	case 3:
		res = R.layout.widget_row3;
		break;
	case 4:
		res = R.layout.widget_row4;
		break;
	} 
	
    RemoteViews row=new RemoteViews(ctxt.getPackageName(),
    		res);
    
    Calendar calendar = Calendar.getInstance();
	int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
	
    String a = String.format("%02d", ss.time / 2);
    if(ss.time % 2 == 0)
    {
    	row.setTextViewText(R.id.time1, a);
    	row.setInt(R.id.time1, "setTextColor", mTimeLineColor);
    }
    
    int time = ss.time;
    time++;
    if(ss.period > 1 && (time % 2 == 0))
    {
    	a = String.format("%02d", time / 2);
       	row.setTextViewText(R.id.time2, a);
       	row.setInt(R.id.time2, "setTextColor", mTimeLineColor);
    }
    time++;
    if(ss.period > 2 && (time % 2 == 0))
    {
    	a = String.format("%02d", time / 2);
    	row.setTextViewText(R.id.time3, a);
    	row.setInt(R.id.time3, "setTextColor", mTimeLineColor);
    }
    time++;
    if(ss.period > 3 && (time % 2 == 0))
    {
    	a = String.format("%02d", time / 2);
    	row.setTextViewText(R.id.time4, a);
    	row.setInt(R.id.time4, "setTextColor", mTimeLineColor);
    }
    
	row.setTextViewText(R.id.label1, ss.subject);
	if(ss.period == 1)
	{
		if(position % 2 == 0)
			row.setInt(R.id.label1, "setBackgroundColor", mCellColor1);
		else
			row.setInt(R.id.label1, "setBackgroundColor", mCellColor2);
	}
	else
	{
		row.setInt(R.id.label1, "setBackgroundColor", ss.color);
	}
	
	Intent i=new Intent();
    Bundle extras=new Bundle();
    extras.putString(TimeTableProvider.EXTRA_WORD, ss.subject);
    i.putExtras(extras);
    row.setOnClickFillInIntent(R.id.label1, i);
    
    return(row);
  }

  @Override
  public RemoteViews getLoadingView() {
    return(null);
  }
  
  @Override
  public int getViewTypeCount() {
    return 4;
  }

  @Override
  public long getItemId(int position) {
    return(position);
  }

  @Override
  public boolean hasStableIds() {
    return(true);
  }

  @Override
  public void onDataSetChanged() {
    // no-op
	  mSetting = new Settings(ctxt);
  }
}
