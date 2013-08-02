package com.uipeople.schedule;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import com.uipeople.schedule.TimeTableControl.TypeThema;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter.LengthFilter;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SetupActivity extends Activity
{
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;

	//Control bind
	Button	mBackButton;
	CheckedTextView mGreyCheck;
	CheckedTextView mWhiteCheck;
	CheckedTextView mFridayCheck;
	CheckedTextView mSaturdayCheck;
	View	mTimeSetup;
	View	mVersionInfo;
	View	mHelp;
	View	mInit;
	Context	mContext;
	TextView	mTimeTextView;
	TextView	mEmail;
	TextView	mName;
	TextView	mSchool;
	TextView	mGrade;
	ImageView	mPicture;
	Settings	mSetting;
	View		mMyProfile;
	
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		mSetting = new Settings(this);
		mContext = this;
		super.onCreate(saveInstanceState);
		setContentView(R.layout.setup_activity); 
		
		mBackButton = (Button)findViewById(R.id.back);
		mGreyCheck = (CheckedTextView)findViewById(R.id.grey);
		mWhiteCheck = (CheckedTextView)findViewById(R.id.white);
		mFridayCheck = (CheckedTextView)findViewById(R.id.friday);
		mSaturdayCheck = (CheckedTextView)findViewById(R.id.saturday);
		//mSundayCheck = (CheckBox)findViewById(R.id.sunday);
		mTimeSetup = findViewById(R.id.timesetup);
		mVersionInfo = findViewById(R.id.version_info);
		mHelp = findViewById(R.id.help);
		mInit = findViewById(R.id.initialize);
		mTimeTextView = (TextView)findViewById(R.id.timetextview);
		mName = (TextView)findViewById(R.id.name);
		mSchool = (TextView)findViewById(R.id.school);
		mGrade = (TextView)findViewById(R.id.grade);
		mMyProfile = findViewById(R.id.my_profile);
		mPicture = (ImageView)findViewById(R.id.my_picture);
		mBackButton.setOnClickListener(BackClickListener);
	
		if(mSetting.thema == 0)
		{
			toggleThema(mGreyCheck);
		} else
		{
			toggleThema(mWhiteCheck);
		}

		if(mSetting.weekday == 0)
		{
			toggleWeekday(mFridayCheck);
		} else if(mSetting.weekday == 1)
		{
			toggleWeekday(mSaturdayCheck);
		}
		else
		{
		//	mSundayCheck.setChecked(true);
		}
		
		if(MainActivity.TYPE_THEMA == TypeThema.eNON_UNIVERSITY)
			mTimeTextView.setText(String.format("%d %s ~ %d %s", mSetting.minTime, getResources().getString(R.string.period), mSetting.maxTime, getResources().getString(R.string.period)));
		else
			mTimeTextView.setText(String.format("%02d %s ~ %02d %s", mSetting.minTime, getResources().getString(R.string.time), mSetting.maxTime, getResources().getString(R.string.time)));
		
		mGreyCheck.setOnClickListener(ThemaCheckListener);
		mWhiteCheck.setOnClickListener(ThemaCheckListener);
		mTimeSetup.setOnClickListener(TimeSetupClickListener);
		mFridayCheck.setOnClickListener(WeekdayCheckListener);
		mSaturdayCheck.setOnClickListener(WeekdayCheckListener);
		//mSundayCheck.setOnCheckedChangeListener(WeekdayClickListener);
		mInit.setOnClickListener(InitClickListener);
		mVersionInfo.setOnClickListener(VersionClickListener);
		mHelp.setOnClickListener(HelpClickListener);
		mMyProfile.setOnClickListener(MyProfileClickListener);
		mPicture.setOnClickListener(PictureClickListener);
		loadProfile();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		
		mSetting.loadSettings();
		loadProfile();
	}
	
	@Override
    protected void onPause() {
		super.onPause();
    	mSetting.saveSettings();
    }
	
	private void loadProfile()
	{
		mName.setText(mSetting.name);
		mSchool.setText(mSetting.school);
		mGrade.setText(mSetting.grade);
		
		if(mSetting.picturePath.length() > 0)
	    {
			Bitmap selectedImage = BitmapFactory.decodeFile(mSetting.picturePath);
			mPicture.setImageBitmap(selectedImage);
	    }
	}
	
	private void showDialog()
	{
		DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				mSetting.table = new HashMap<Subject, Subject>();
		  		mSetting.saveSettings();
		  		dialog.dismiss();
		  		Toast.makeText(mContext, R.string.initialized, Toast.LENGTH_SHORT).show();
			}
		};
		
		DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		};
		
		new AlertDialog.Builder(this)
			.setTitle(R.string.check_init)
			.setPositiveButton(R.string.ok, ok)
			.setNegativeButton(R.string.cancel, cancel)
			.show();
	}

	private View.OnClickListener MyProfileClickListener = new View.OnClickListener() 
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(mContext, ProfileActivity.class);
			startActivity(intent);
		}
	};
	
	private View.OnClickListener PictureClickListener = new View.OnClickListener() 
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			doTakeAlbumAction();
		}
	};
	
	private View.OnClickListener HelpClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		Intent intent = new Intent(mContext, HelpActivity.class);
		startActivity(intent);
  	}
    };
    
	private View.OnClickListener VersionClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		Intent intent = new Intent(mContext, VersionActivity.class);
		startActivity(intent);
  	}
    };
    
	private View.OnClickListener InitClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		showDialog();
  	}
    };
	
	private View.OnClickListener BackClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		finish();
  	}
    };
    
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        
        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(16);
        }
        
        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }
        
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
    
    private WheelView mNumberPicker;
    private WheelView mNumberPicker2;
    private View.OnClickListener TimeSetupClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		
  		AlertDialog.Builder builder =  new AlertDialog.Builder(mContext);
  		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() 
  		{
  		    public void onClick(DialogInterface dialog, int which) 
  		    {
  		    	mSetting.minTime = mNumberPicker.getCurrentItem() + 8;
  		    	mSetting.maxTime = mNumberPicker2.getCurrentItem() + 16;
  		    	mTimeTextView.setText(String.format("%d %s~ %d %s", mSetting.minTime, getResources().getString(R.string.period), mSetting.maxTime, getResources().getString(R.string.period)));
  		    	mSetting.saveSettings();
  		        return;
  		    }
	    });
  		
  		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
  		    public void onClick(DialogInterface dialog, int which) {
  		        return;
  		    } }); 
  		View view = getLayoutInflater().inflate(R.layout.numberpicker, null);
  		builder.setView (view);

  		final AlertDialog dialog = builder.create ();
  	    final WheelView picker = (WheelView) view.findViewById(R.id.myNumber);
  	    final WheelView picker2 = (WheelView) view.findViewById(R.id.myNumber2);
  		mNumberPicker = picker;
  		mNumberPicker2 = picker2;
  		
  		 OnWheelChangedListener listener = new OnWheelChangedListener() {
             public void onChanged(WheelView wheel, int oldValue, int newValue) {
             }
         };
         
         picker.setViewAdapter(new DateNumericAdapter(mContext, 8, 12, 8));
         picker.setCurrentItem(mSetting.minTime - 8);
         picker.addChangingListener(listener);
         
         picker2.setViewAdapter(new DateNumericAdapter(mContext, 16, 24, 16));
         picker2.setCurrentItem(mSetting.maxTime - 16);
         picker2.addChangingListener(listener);
       
  		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
  		dialog.show();
  	}
    };
    
    private void toggleThema(View v)
    {
    	if(v.equals(mGreyCheck))
		{
	        if ( !mGreyCheck.isSelected())
	        {
	        	mGreyCheck.setSelected(true);
	        	mGreyCheck.setCheckMarkDrawable (R.drawable.checkbox_on);
	        	mWhiteCheck.setSelected(false);
	        	mWhiteCheck.setCheckMarkDrawable(R.drawable.checkbox_off);
	        	mSetting.thema = 0;
	        }
		} else
		{
			if ( !mWhiteCheck.isSelected())
	        {
	        	mWhiteCheck.setSelected(true);
	        	mWhiteCheck.setCheckMarkDrawable (R.drawable.checkbox_on);
	        	mGreyCheck.setSelected(false);
	        	mGreyCheck.setCheckMarkDrawable(R.drawable.checkbox_off);
	        	mSetting.thema = 1;
	        }
		}
    }
    
    private void toggleWeekday(View v)
    {
    	if(v.equals(mFridayCheck))
		{
	        if ( !mFridayCheck.isSelected())
	        {
	        	mFridayCheck.setSelected(true);
	        	mFridayCheck.setCheckMarkDrawable (R.drawable.checkbox_on);
	        	mSaturdayCheck.setSelected(false);
	        	mSaturdayCheck.setCheckMarkDrawable(R.drawable.checkbox_off);
	        	mSetting.weekday = 0;
	        }
		} else
		{
			if ( !mSaturdayCheck.isSelected())
	        {
				mSaturdayCheck.setSelected(true);
				mSaturdayCheck.setCheckMarkDrawable (R.drawable.checkbox_on);
	        	mFridayCheck.setSelected(false);
	        	mFridayCheck.setCheckMarkDrawable(R.drawable.checkbox_off);
	        	mSetting.weekday = 1;
	        }
		}
    }
    
    private View.OnClickListener ThemaCheckListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			toggleThema(v);
		}
	};
	
	private View.OnClickListener WeekdayCheckListener = new View.OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			toggleWeekday(v);
		}
	};
    
    private OnCheckedChangeListener ThemaClickListener = new OnCheckedChangeListener() 
    {
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(buttonView.getId() == R.id.grey && isChecked)
		{
			mGreyCheck.setChecked(isChecked);
			mWhiteCheck.setChecked(!isChecked);
			mGreyCheck.setClickable(false);
			mWhiteCheck.setClickable(true);
			mSetting.thema = 0;
		} else if(isChecked)
		{
			mGreyCheck.setChecked(!isChecked);
			mWhiteCheck.setChecked(isChecked);
			mGreyCheck.setClickable(true);
			mWhiteCheck.setClickable(false);
			mSetting.thema = 1;
		}
		mSetting.saveSettings();
	}
    };
    
    private OnCheckedChangeListener WeekdayClickListener = new OnCheckedChangeListener() 
    {
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(buttonView.getId() == R.id.friday && isChecked)
		{
			mFridayCheck.setChecked(isChecked);
			mSaturdayCheck.setChecked(!isChecked);
	//		mSundayCheck.setChecked(!isChecked);
			mFridayCheck.setClickable(false);
			mSaturdayCheck.setClickable(true);
		//	mSundayCheck.setClickable(true);
			mSetting.weekday = 0;
		} else if(buttonView.getId() == R.id.saturday && isChecked)
		{
			mFridayCheck.setChecked(!isChecked);
			mSaturdayCheck.setChecked(isChecked);
	//		mSundayCheck.setChecked(!isChecked);
			mFridayCheck.setClickable(true);
			mSaturdayCheck.setClickable(false);
	//		mSundayCheck.setClickable(true);
			mSetting.weekday = 1;
		} /*else if(buttonView.getId() == R.id.sunday && isChecked)
		{
			mFridayCheck.setChecked(!isChecked);
			mSaturdayCheck.setChecked(!isChecked);
			mSundayCheck.setChecked(isChecked);
			mFridayCheck.setClickable(true);
			mSaturdayCheck.setClickable(true);
			mSundayCheck.setClickable(false);
			mSetting.weekday = 2;
		}*/
		mSetting.saveSettings();
	}
    };    
    
	private void doTakeAlbumAction()
	{
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);

    	startActivityForResult( Intent.createChooser(intent, "Select Picture"), PICK_FROM_ALBUM );
	}
	
	private Uri mImageCaptureUrl;
	
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
				mPicture.setImageBitmap(selectedImage);
				mSetting.picturePath = path;
				mSetting.saveSettings();
				break;
			}
			
			case PICK_FROM_ALBUM:
			{
				mImageCaptureUrl = data.getData();
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(mImageCaptureUrl,  "image/*");

				int a = mPicture.getWidth();
				
				intent.putExtra("scale",  true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
				Toast.makeText(mContext, "" + mPicture.getWidth(), 10).show();
				intent.putExtra("outputX",  90);
				intent.putExtra("outputY",  90);
				intent.putExtra("aspectX",  90);
				intent.putExtra("aspectY",  90);
				startActivityForResult(intent, CROP_FROM_CAMERA);
				break;
			}
		}
	}
	    
	private Uri getTempUri() 
	{
		File f = new File(Environment.getExternalStorageDirectory(),"tempPicture.jpg");
		
		try
		{
			f.createNewFile();	
		} catch(IOException e)
		{
			
		}
		
		return Uri.fromFile(f);
	}
	
}

