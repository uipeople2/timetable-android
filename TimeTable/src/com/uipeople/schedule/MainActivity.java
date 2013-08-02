package com.uipeople.schedule;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.SyncStateContract.Helpers;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;
import android.content.DialogInterface;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.uipeople.schedule.TimeTableControl;
import com.uipeople.schedule.TimeTableControl.TypeThema;

public class MainActivity extends Activity implements OnClickListener  
{
	public static final int PICK_FROM_CAMERA = 0;
	public static final int PICK_FROM_ALBUM = 1;
	public static final int CROP_FROM_CAMERA = 2;
	public static final String url = "http://uipeople.herokuapp.com";
	//public static final String url = "http://192.168.25.5:3000";
	//public static final TypeThema TYPE_THEMA = TypeThema.eNON_UNIVERSITY;
	public static final TypeThema TYPE_THEMA = TypeThema.eUNIVERSITY;
	
	private CustomViewPager mPager;
	private ImageView mBackgroundImageView;
	private View mDailyImageView;
	private Uri mImageCaptureUrl;
	private View mMainView;
	float _deltaX, _deltaY;
	float mDeltaX, mDeltaY;
	private int drag;
	boolean mEditMode;
	PopupWindow mPopup;
	boolean mMore;
	boolean mMemo;
	Path mPath;
	public static Settings mSetting;
	TimeTableControl mDaily;
	GestureDetector mGestureDetector;
	Subject	mCurrentSubject;
	Context mContext;
	TimeTableControl mFull;
	static float mDensity; 
	ProgressDialog	mProgress = null;
	static public String deviceId = "";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	mContext = this;
    	mSetting = new Settings(this);
        super.onCreate(savedInstanceState);		
        setContentView(R.layout.main);
        mPath = new Path(this, this, MenuListener);
        mMainView = (View)findViewById(R.id.pager);
        mPager = (CustomViewPager)findViewById(R.id.pager);
        mPager.setAdapter(new PagerAdapterClass(getApplicationContext()));
        _deltaX = mMainView.getWidth() * 0.5f;    _deltaY = mMainView.getHeight() * 0.5f;
        
        if(mSetting.first)
        {
	        Intent intent = new Intent(mContext, HelpFirstStartActivity.class);
			startActivity(intent);
        }
        mEditMode = false;
        
        Display dis = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        dis.getMetrics(metrics);
        
        mDensity = metrics.density;
        
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        new SendTask().execute("");
        applyTypeThemaReource();
    }
    
    int closeButtonImage;
    int headerTextColor;
    int detailTextColor;
    int moreOpenButton;
    int moreCloseButton;
    int smsButtonImage;
    int phoneButtonImage;
    int mailButtonImage;
    int detailBackground;
    int memoBackground;
    
    int smsButtonDiableImage;
    int phoneButtonDisableImage;
    int mailButtonDisableImage;
    
    private void applyTypeThemaReource()
    {
		if(mSetting.thema == 0) // gray
		{
		 	PathButton button = (PathButton)findViewById(R.id.list);
		 	button.setImageResource(R.drawable.menu_list);
		 	button.setBackgroundResource(R.drawable.drop_shadow_circle);
		 	button.setAlpha(0.0f);
		 	
		 	button = (PathButton)findViewById(R.id.photo);
		 	button.setImageResource(R.drawable.menu_photo);
		 	button.setBackgroundResource(R.drawable.drop_shadow_circle);
		 	button.setAlpha(0.0f);
		 	
		 	button = (PathButton)findViewById(R.id.share);
		 	button.setImageResource(R.drawable.menu_share);
		 	button.setBackgroundResource(R.drawable.drop_shadow_circle);
		 	button.setAlpha(0.0f);
		 	
		 	button = (PathButton)findViewById(R.id.setting);
		 	button.setImageResource(R.drawable.menu_setting);
		 	button.setBackgroundResource(R.drawable.drop_shadow_circle);
		 	button.setAlpha(0.0f);
		} else
		{
		 	PathButton button = (PathButton)findViewById(R.id.list);
		 	button.setImageResource(R.drawable.menu_list_w);
		 	button.setBackground(null);
		 	button.setAlpha(0.0f);
		 	
		 	button = (PathButton)findViewById(R.id.photo);
		 	button.setImageResource(R.drawable.menu_photo_w);
		 	button.setBackground(null);
		 	button.setAlpha(0.0f);
		 	
		 	button = (PathButton)findViewById(R.id.share);
		 	button.setImageResource(R.drawable.menu_share_w);
		 	button.setBackground(null);
		 	button.setAlpha(0.0f);
		 	
		 	button = (PathButton)findViewById(R.id.setting);
		 	button.setImageResource(R.drawable.menu_setting_w);
		 	button.setBackground(null);
		 	button.setAlpha(0.0f);
		}
    	 
		if(MainActivity.TYPE_THEMA == TypeThema.eNON_UNIVERSITY)
        {
			ImageButton plus_button = (ImageButton)findViewById(R.id.plus_button);
        	plus_button.setImageResource(R.drawable.menu_1);
        	plus_button.setBackgroundResource(R.drawable.drop_shadow_circle);
        } else
        {
        	ImageButton plus_button = (ImageButton)findViewById(R.id.plus_button);
        	plus_button.setImageResource(R.drawable.menu_1_dw);
        	plus_button.setBackground(null);
        }
		
    	if(MainActivity.TYPE_THEMA == TYPE_THEMA.eNON_UNIVERSITY)
    	{
    		if(mSetting.thema == 1) // White
    		{
    			closeButtonImage = R.drawable.close_w;
    			moreOpenButton = R.drawable.detail_1_w;
    			moreCloseButton = R.drawable.detail_2_w;
    			headerTextColor = Color.rgb(0xf1, 0x5b, 0x64);
    			detailBackground = R.drawable.white_detail_background;
    			detailTextColor = Color.rgb(112, 112, 112);
    			memoBackground = Color.rgb(238, 238, 238);
    			smsButtonImage = R.drawable.text_on_60x50_w;
    			phoneButtonImage = R.drawable.phone_on_60x50_w;
    			mailButtonImage = R.drawable.mail_on_60x50_w;
    			smsButtonDiableImage = R.drawable.text_no_60x50_w;
    			phoneButtonDisableImage = R.drawable.phone_no_60x50_w;
    			mailButtonDisableImage = R.drawable.mail_no_60x50_w;
			} else
			{
    			closeButtonImage = R.drawable.close;
    			moreOpenButton = R.drawable.detail_1;
    			moreCloseButton = R.drawable.detail_2;
    			headerTextColor = Color.rgb(0xf1, 0x5b, 0x64);
    			detailBackground = R.drawable.gray_detail_background;
    			detailTextColor = Color.WHITE;
    			memoBackground = Color.WHITE;
    			smsButtonImage = R.drawable.text_60x50;
    			phoneButtonImage = R.drawable.phone_60x50;
    			mailButtonImage = R.drawable.mail_60x50;
    			smsButtonDiableImage = R.drawable.text_off_60x50;
    			phoneButtonDisableImage = R.drawable.phone_off_60x50;
    			mailButtonDisableImage = R.drawable.mail_off_60x50;
			}
		} else
    	{
    		closeButtonImage = R.drawable.close_dw;
			moreOpenButton = R.drawable.detail_1_dw;
			moreCloseButton = R.drawable.detail_2_dw;
			headerTextColor = Color.rgb(194, 223, 32);
			detailBackground = R.drawable.white_detail_background;
			detailTextColor = Color.rgb(112, 112, 112);
			smsButtonImage = R.drawable.text_on_60x50_dw;
			phoneButtonImage = R.drawable.phone_on_60x50_dw;
			mailButtonImage = R.drawable.mail_on_60x50_dw;
			smsButtonDiableImage = R.drawable.text_no_60x50_w;
			phoneButtonDisableImage = R.drawable.phone_no_60x50_w;
			mailButtonDisableImage = R.drawable.mail_no_60x50_w;
			memoBackground = Color.rgb(238, 238, 238);
    	}
    }
    
    
    private class SendTask extends AsyncTask<String,Void,Integer>
	{
		@Override
		protected Integer doInBackground(String... p)
		{
			final String regId = GCMRegistrar.getRegistrationId(mContext);
	        if("".equals(regId))   //구글 가이드에는 regId.equals("")로 되어 있는데 Exception을 피하기 위해 수정
	        {
	              GCMRegistrar.register(mContext, "395464293794");
	              deviceId = GCMRegistrar.getRegistrationId(mContext);
	              
	        } else
	        {
	        	deviceId = regId;
	        }
	  		
	  		return 0;
		}
		
		protected void onPostExecute(Integer status)
		{
			
		}
	}
    
	
	
    static int convert(int v)
    {
        return (int) (v * mDensity);
    }
    
    protected void onRestart() {
		super.onRestart();
    	mSetting.loadSettings();
    	applyTypeThemaReource();
    	((PagerAdapterClass)mPager.getAdapter()).update();
	}
    
    @Override
    protected void onStop() {
    	if(mDaily == null)
    		return;
    	
    	mSetting.x = ((View)mDaily.getParent()).getPaddingLeft();
    	mSetting.y = ((View)mDaily.getParent()).getPaddingTop();
    	mSetting.saveSettings();
    	super.onStop();
    }
    
    private View.OnClickListener MenuListener = new View.OnClickListener() 
    {
	   	@Override
	   	public void onClick(View v) 
	   	{
	   		switch(v.getId()) {
			case  R.id.setting:
			{
				Intent intent = new Intent(mContext, SetupActivity.class);
				startActivity(intent);
			}
			break;
			
			case R.id.share:
			{
				Bitmap icon = mFull.renderToBitmap();
				Intent share = new Intent(Intent.ACTION_SEND);
				share.setType("image/jpeg");
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
				File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
				try {
				    f.createNewFile();
				    FileOutputStream fo = new FileOutputStream(f);
				    fo.write(bytes.toByteArray());
				} catch (IOException e) {                       
				        e.printStackTrace();
				}
				share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
				startActivity(Intent.createChooser(share, "Share Image"));
			}
			break;
			
			case R.id.photo:
			{
				showDialog();
			}
			break;
			
			case R.id.list:
			{
				Intent intent = new Intent(mContext, TLoginActivity.class);
	  			startActivity(intent);
			}
			break;
			}
	   	}
    };
    
    private View.OnLongClickListener TimeTableListener = new View.OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			mPager.setPagingEnabled(false);
			return false;
		}
	};
	
    private View.OnTouchListener TouchListener = new View.OnTouchListener() 
    {
        public boolean onTouch(View v, MotionEvent event) 
        {
           if(event.getAction() == MotionEvent.ACTION_UP)
           {
        	  mPager.setPagingEnabled(true);
           }
           return false;
         }
      };
        
      private View.OnClickListener LockClickListener = new View.OnClickListener() 
      {
    	@Override
    	public void onClick(View v) 
    	{
    		LinearLayout r = (LinearLayout)findViewById(R.id.layout);
    		TimeTableControl vv = (TimeTableControl)r.findViewById(R.id.table);
    		TextView vvv = (TextView)r.findViewById(R.id.edit);
    		ImageView lock = (ImageView)v;
    		if(mEditMode == false)
    		{    		        
    			vvv.getLayoutParams().height = convert(40);
    			vvv.requestLayout();
    			vv.unlock();
        		mEditMode = true;	
    		} else
    		{
    			vvv.getLayoutParams().height = 0;
    			vvv.requestLayout();
    			vv.lock();
    			mEditMode = false;
    		}
    		
    	}
      };
      
      int min(int a, int b)
      {
    	  if( a > b )
    		  return b;
    	  
    	  return a;
      }
      
      private View.OnClickListener DetailClickListener = new View.OnClickListener() 
      {
    	@Override
    	 public void onClick(View v) 
    	{    	
    		
   			ArrayList<Subject> list = (ArrayList<Subject>)v.getTag();
   			Subject s = list.get(0);
    		
    		mCurrentSubject = s;
    		
    		if(mEditMode == true)
    		{
    			Intent intent = new Intent(MainActivity.this, ClassActivity.class);
    			intent.putExtra("subject", s.subject);
    			intent.putExtra("teacher", s.teacher);
    			intent.putExtra("phone", s.phone);
    			intent.putExtra("email", s.email);
    			intent.putExtra("color", s.color);
    			intent.putExtra("classroom", s.classRoom);
    			startActivity(intent);
    			return;
    		}
    		
    		if(s.subject.length() == 0)
    			return;
    		
    		if(mPopup != null)
    			mPopup.dismiss();
    		
    		LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
    		View popupView = layoutInflater.inflate(R.layout.detail_view, null);
    	 
    		final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
    		TableLayout table = (TableLayout)popupView.findViewById(R.id.item);
    		ImageView closeButton = (ImageView)popupView.findViewById(R.id.close);
    		ImageView moreButton = (ImageView)popupView.findViewById(R.id.more);
    		ImageView smsButton = (ImageView)popupView.findViewById(R.id.sms);
    		ImageView phoneButton = (ImageView)popupView.findViewById(R.id.phone);
    		ImageView mailButton = (ImageView)popupView.findViewById(R.id.mail);
    		
    		TextView subject = (TextView)popupView.findViewById(R.id.subject);
    		TextView teacher = (TextView)popupView.findViewById(R.id.teacher);
    		
    		TextView room = (TextView)popupView.findViewById(R.id.room);
    		TextView header = (TextView)popupView.findViewById(R.id.header);
    		View headerLine = (View)popupView.findViewById(R.id.header_line);
    		
    		header.setTextColor(headerTextColor);
    		headerLine.setBackgroundColor(headerTextColor);
    		closeButton.setImageResource(closeButtonImage);
    		moreButton.setImageResource(moreOpenButton);
    		table.setBackgroundResource(detailBackground);
    		subject.setTextColor(detailTextColor);
    		teacher.setTextColor(detailTextColor);
    		room.setTextColor(detailTextColor);
    		
			if(s.phone.length() > 0)
			{
				smsButton.setOnClickListener(SMSListener);
	    		phoneButton.setOnClickListener(PhoneListener);
	    		smsButton.setImageResource(smsButtonImage);
	    		phoneButton.setImageResource(phoneButtonImage);
			} else
			{
				smsButton.setImageResource(smsButtonDiableImage);
	    		phoneButton.setImageResource(phoneButtonDisableImage);
			}
	    			
			if(s.email.length() > 0)
			{
				mailButton.setOnClickListener(MailListener);
    			mailButton.setImageResource(mailButtonImage);
			} else
			{
				mailButton.setImageResource(mailButtonDisableImage);
			}
    		 
    		String names[] = {"SUN","MON","TUE","WED","THU","FRI","SAT"};

    		String textSubject = getResources().getString(R.string.subject);
    		String textTeacher = getResources().getString(R.string.teacher);
    		if(TYPE_THEMA == TYPE_THEMA.eUNIVERSITY)
    		{
    			textTeacher = getResources().getString(R.string.professor);
    		} 
    		String textRoom = getResources().getString(R.string.room);
    		
    		subject.setText(String.format("%s : %s", textSubject, s.subject));
    		teacher.setText(String.format("%s : %s", textTeacher, s.teacher));
    		room.setText(String.format("%s : %s", textRoom, s.classRoom));
    		
    		if(MainActivity.TYPE_THEMA == TYPE_THEMA.eNON_UNIVERSITY)
    		{
    			header.setText(String.format("%s %d %s", names[s.weekday], s.time, getResources().getString(R.string.cls)));
    		} else
    		{
    			String time = timeToString(s);
    			header.setText(String.format("%s %s", names[s.weekday], time));
    		}
    		closeButton.setOnClickListener(CloseDetailListener);
    		moreButton.setOnClickListener(MoreDetailListener);
    		
    		EditText memo = (EditText)popupView.findViewById(R.id.memo);
    		memo.setText(s.memo);
    		memo.setOnClickListener(MemoClickListener);
    		memo.setFocusable(false);
    		memo.setBackgroundColor(memoBackground);
    		mPopup = popupWindow;
    		popupWindow.showAtLocation(v, Gravity.CENTER | Gravity.TOP, 0, 0);
    		mMore = false;
    		mMemo = false;
    	}
      };
      
      private String timeToString(Subject s)
      {
    	  String temp = "";
    	  int hour = s.time / 2;
    	  
    	  if(s.time % 2 == 0)
    	  {
    		  temp = String.format("%02d:00 ", hour);
    	  } else
    	  {
    		  temp = String.format("%02d:30 ", hour);
    	  }
    	  
    	  int hourEnd = (s.time + s.period) / 2;
    	  
    	  if((s.time + s.period) % 2 == 0)
    	  {
    		  temp += String.format("~ %02d:00", hourEnd);
    	  } else
    	  {
    		  temp += String.format("~ %02d:30", hourEnd);
    	  }
    	  
    	  return temp;
      }
      
      private View.OnClickListener MemoClickListener = new View.OnClickListener() 
      {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			EditText memo = (EditText)v;
			if(mMemo)
			{
				memo.setFocusableInTouchMode(false);
				memo.setFocusable(false);
				InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE); 
				imm.hideSoftInputFromWindow(v.getWindowToken(),0);
				mMemo = false;
			} else
			{
	      		memo.setFocusableInTouchMode(true);
	      		InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE); 
	      		imm.showSoftInput(v, 0);
	      		mMemo = true;
			}
		}
      };
      
	  private View.OnClickListener CloseDetailListener = new View.OnClickListener() 
	  {
		@Override
		public void onClick(View v) 
		{
			EditText memo = (EditText)mPopup.getContentView().findViewById(R.id.memo);
			mCurrentSubject.memo = memo.getText().toString();
			mPopup.dismiss();
		}
	  };

	  private View.OnClickListener MoreDetailListener = new View.OnClickListener() 
	  {
		@Override
		public void onClick(View v) 
		{
			View vv = mPopup.getContentView().findViewById(R.id.moreDisplay);
			ImageView more = (ImageView)mPopup.getContentView().findViewById(R.id.more);
			
			if(mMore)
			{
				more.setImageResource(moreOpenButton);
				vv.getLayoutParams().height = 0;
				mMore = false;
				
				View memo = mPopup.getContentView().findViewById(R.id.memo);
				memo.setFocusable(false);
				InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE); 
				imm.hideSoftInputFromWindow(memo.getWindowToken(),0);
				mMemo = false;
				
			} else
			{
				more.setImageResource(moreCloseButton);
				vv.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
				mMore = true;
			}
		}
	  };
	  
	  private View.OnClickListener SMSListener = new View.OnClickListener() 
	  {
		@Override
		public void onClick(View v) 
		{
			Intent smsIntent = new Intent(Intent.ACTION_VIEW);
			smsIntent.setType("vnd.android-dir/mms-sms");
			smsIntent.putExtra("address", mCurrentSubject.phone);
			startActivity(smsIntent);
		}
	  };
	  
	  private View.OnClickListener PhoneListener = new View.OnClickListener() 
	  {
		@Override
		public void onClick(View v) 
		{
			Intent callIntent = new Intent(Intent.ACTION_VIEW);
			callIntent.setData(Uri.parse("tel:"+ mCurrentSubject.phone));
			callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			startActivity(callIntent);
		}
	  };
	  
	  private View.OnClickListener MailListener = new View.OnClickListener() 
	  {
		@Override
		public void onClick(View v) 
		{
			Uri uri = Uri.parse("mailto:" + mCurrentSubject.email);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(Intent.createChooser(it, "Choose Email Client"));  
		}
	  };

    private View.OnLongClickListener mPagerListener2 = new View.OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			showDialog();
			return false;
		}
	};
	
	
	private void showDialog()
	{
		DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("");
		builder.setNegativeButton(R.string.cancel, cancel);
		LayoutInflater factory = LayoutInflater.from(this);
		final View customView = factory.inflate(R.layout.background_dialog, null);
		builder.setView(customView);
		alert = builder.create();
		
		customView.findViewById(R.id.camera).setOnClickListener( new OnClickListener()
		{
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MainActivity.this.alert.dismiss();
				doTakePhotoAction();
			}
		});
		
		customView.findViewById(R.id.album).setOnClickListener( new OnClickListener()
		{
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MainActivity.this.alert.dismiss();
				doTakeAlbumAction();
			}
		});
		
		customView.findViewById(R.id.origin).setOnClickListener( new OnClickListener()
		{
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MainActivity.this.alert.dismiss();
				doTakeOrigin();
			}
		});
		
		alert.show();
	}
	private AlertDialog alert = null;
	
	private void doTakeOrigin()
	{
		String path = getTempUri().getPath();
		Bitmap selectedImage = BitmapFactory.decodeFile(path);
		mBackgroundImageView.setImageResource(R.drawable.original);
		mSetting.imagePath = "";
		mSetting.saveSettings();
		mPager.setCurrentItem(0, true);
	}
    
	private void doTakePhotoAction()
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		mImageCaptureUrl = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
		
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUrl);
		startActivityForResult( intent, PICK_FROM_CAMERA);
	}
	
	private void doTakeAlbumAction()
	{
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);

    	startActivityForResult( Intent.createChooser(intent, "Select Picture"), PICK_FROM_ALBUM );
	}
	
	protected void onActivityResult( int requestCode, int resultCode, Intent data )
	{
		if(resultCode != RESULT_OK)
			return;
		
		switch(requestCode)
		{
			case CROP_FROM_CAMERA:
			{
				String path = getTempUri().getPath();
				Bitmap selectedImage = BitmapFactory.decodeFile(path);
				mBackgroundImageView.setImageBitmap(selectedImage);
				mSetting.imagePath = path;
				mSetting.saveSettings();
				
				mPager.setCurrentItem(0, false);
				break;
			}
			
			case PICK_FROM_ALBUM:
			{
				mImageCaptureUrl = data.getData();
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(mImageCaptureUrl,  "image/*");

				int a = mMainView.getWidth();
				
				intent.putExtra("scale",  true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
				intent.putExtra("outputX",  mMainView.getWidth());
				intent.putExtra("outputY",  mMainView.getHeight());
				intent.putExtra("aspectX",  mMainView.getWidth());
				intent.putExtra("aspectY",  mMainView.getHeight());
				startActivityForResult(intent, CROP_FROM_CAMERA);
				break;
			}
			
			case PICK_FROM_CAMERA:
			{
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(mImageCaptureUrl,  "image/*");
				
				intent.putExtra("scale",  true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
				intent.putExtra("outputX",  mMainView.getWidth());
				intent.putExtra("outputY",  mMainView.getHeight());
				intent.putExtra("aspectX",  mMainView.getWidth());
				intent.putExtra("aspectY",  mMainView.getHeight());
				startActivityForResult(intent, CROP_FROM_CAMERA);
			}
		}
	}
	    
	private Uri getTempUri() 
	{
		File f = new File(Environment.getExternalStorageDirectory(),"temp.jpg");
		
		try
		{
			f.createNewFile();	
		} catch(IOException e)
		{
			
		}
		
		return Uri.fromFile(f);
	}
	
    @Override
    public void onClick(View v)
    {
  
    }
    
    private void setCurrentInflateItem(int type){
        if(type==0){
            mPager.setCurrentItem(0);
        }else if(type==1){
            mPager.setCurrentItem(1);
        }else{
            mPager.setCurrentItem(2);
        }
    }
    
    public class PagerAdapterClass extends PagerAdapter {
    	private LayoutInflater mInflater;
    	private ImageButton mPlus;
    	private Boolean isMenuOpened;
    	
    	public PagerAdapterClass(Context c) {
    		super();
    		mInflater = LayoutInflater.from(c);
    	}
    	
    	@Override
    	public int getCount() {
    		return 2;
    	}
    	
    	@Override
    	public Object instantiateItem(View pager, int position) {
    		View v = null;
    		if(position == 0) {
    			v = mInflater.inflate(R.layout.inflate_one, null);
    			v.findViewById(R.id.layout2).setOnLongClickListener(mPagerListener2);
    		    mBackgroundImageView = (ImageView)v.findViewById(R.id.iv_one); 
    		    TimeTableControl c = (TimeTableControl)v.findViewById(R.id.daily);
    		    c.setOnTouchListener(TouchListener);
    		    c.setOnLongClickListener(TimeTableListener);
    		    c.setConfig(mSetting.minTime, mSetting.maxTime);
    		    c.renderDaily(mSetting.table, mSetting.thema);
    		    mDaily = c;
    		 
    		    ((View)c.getParent()).setPadding(mSetting.x, mSetting.y, 0,0);
    		    
    		    if(mSetting.imagePath.equals(""))
    		    {
    				mBackgroundImageView.setImageResource(R.drawable.original);
    		    } else
    		    {
					Bitmap selectedImage = BitmapFactory.decodeFile(mSetting.imagePath);
					mBackgroundImageView.setImageBitmap(selectedImage);
    		    }
    		  
    		} else if(position == 1) {
    			v = mInflater.inflate(R.layout.inflate_two, null);
    			LinearLayout r = (LinearLayout)v.findViewById(R.id.layout);
    			TimeTableControl c = (TimeTableControl)r.findViewById(R.id.table);
    			c.setConfig(mSetting.minTime, mSetting.maxTime);
    			c.renderFull(LockClickListener, DetailClickListener, mSetting.table, mSetting.thema, mSetting.weekday);
    			mFull = c;
    		}
    		
    		((CustomViewPager)pager).addView(v, 0);
    		return v;
    	}
    	
    	@Override
    	public void destroyItem(View pager, int position, Object view) {
    		((CustomViewPager)pager).removeView((View)view);
    	}
    	
    	@Override
    	public boolean isViewFromObject(View pager, Object obj) {
    		return pager == obj;
    	}
    	
    	@Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
    	@Override public Parcelable saveState() { return null; }
    	@Override public void startUpdate(View arg0) {}
    	@Override public void finishUpdate(View arg0) {}

    	public void update()
    	{
    		mDaily.setConfig(mSetting.minTime, mSetting.maxTime);
    		mFull.setConfig(mSetting.minTime, mSetting.maxTime);
    		mDaily.renderDaily(mSetting.table, mSetting.thema);
    		mFull.renderFull(LockClickListener, DetailClickListener, mSetting.table, mSetting.thema, mSetting.weekday);
    		
    		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			if (currentapiVersion < android.os.Build.VERSION_CODES.HONEYCOMB)
			{
				return;
			} 
			
    		Intent intent = new Intent("android.appwidget.action.notify");
    		intent.putExtra("table", mSetting.table);
    		sendBroadcast(intent);
    	}
    
    }
  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent = new Intent(mContext, SetupActivity.class);
		startActivity(intent);
		return true;
		
    	
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	return (super.onOptionsItemSelected(item));
    }
}
