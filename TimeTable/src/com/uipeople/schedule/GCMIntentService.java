package com.uipeople.schedule;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService{
	private static final String tag = "GCMIntentService";
    private static final String PROJECT_ID = "395464293794";

    public GCMIntentService(){ this(PROJECT_ID); }
    public GCMIntentService(String project_id) { super(project_id); }
	private static int count = 1;

 
    /** Ǫ�÷� ���� �޽��� */
    @Override
    protected void onMessage(Context context, Intent intent) 
    {
    	Bundle b = intent.getExtras();
        String name = b.get("name").toString();
        String type = b.get("type").toString();
    	ActivityManager am = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE);
		
		List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
		
		String className = taskInfo.get(0).topActivity.getClassName();
		String message = name + "���� ģ�� ��û�Ͽ����.";
		if(className.equals("com.uipeople.schedule.FriendAddActivity"))
		{
            if(type.equals("4"))
            {
            	message = name + "���� ģ�� �¶��Ͽ����.";
            } else
            {
            	 Intent bIntent = new Intent("notify-request");
     	        // You can also include some extra data.
//            	 bIntent.putExtra("message", intent); //or get some info from Gcm intent and put it into this one.
            	 bIntent.putExtra("message", message);
     	        LocalBroadcastManager.getInstance(context).sendBroadcast(bIntent);
     	        
            }
			return;
		}
            
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification note = new Notification(R.drawable.ic_launcher, "Today Class", System.currentTimeMillis());
        note.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR; 
        Intent notificationIntent = new Intent(context, FriendAddActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
               Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        note.setLatestEventInfo(context, "Today Class", message, pendingIntent);
        note.number = count++;
        note.defaults |= Notification.DEFAULT_SOUND;
		note.defaults |= Notification.DEFAULT_VIBRATE;
		note.defaults |= Notification.DEFAULT_LIGHTS;
		note.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, note);     
    }

    /**�ܸ����� GCM ���� ��� ���� �� ��� id�� �޴´�*/
    @Override
    protected void onRegistered(Context context, String regId) {
    }
    
    


    /**�ܸ����� GCM ���� ��� ������ �ϸ� ������ ��� id�� �޴´�*/
    @Override
    protected void onUnregistered(Context context, String regId) {
    }
    
	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public class NotificationConfirm extends Activity 
	{     
		@Override    
		protected void onCreate(Bundle savedInstanceState) 
		{        
			super.onCreate(savedInstanceState);        
			TextView tv = new TextView(this);        
			tv.setText("��Ƽ�����̼��� ���� �Ͽ����ϴ�.");        
			setContentView(tv);                 
			// notification �Ŵ��� ����        
			NotificationManager nm =                 
					(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			// ��ϵ� notification �� ���� �Ѵ�.        
			nm.cancel(1234);             
		}
	}
}

