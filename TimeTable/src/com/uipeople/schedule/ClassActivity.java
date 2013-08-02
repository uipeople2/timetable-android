package com.uipeople.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.uipeople.schedule.TimeTableControl.TypeThema;

public class ClassActivity extends Activity
{
	int colors[] = 
		{
			Color.rgb(255,255,255),
			Color.rgb(66,69,74),
			Color.rgb(255,255,255),
			
			Color.rgb(235,129,120),
			Color.rgb(239,177,177),
			Color.rgb(241,165,112),
			
			Color.rgb(246,233,162),
			Color.rgb(177,216,218),
			Color.rgb(217,227,152),
			
			Color.rgb(244,218,192),
			Color.rgb(150,191,156),
			Color.rgb(84,113,105),
			
			Color.rgb(173,120,82),
			Color.rgb(112,169,215),
			Color.rgb(159,157,201),
		};
	
	int colors_university[] =
		{
			Color.rgb(255,255,255),
			Color.rgb(66,69,74),
			Color.rgb(255,255,255),
			
			Color.rgb(241,91,100),
			Color.rgb(128,172,219),
			Color.rgb(179,216,107),
			
			Color.rgb(202,164,205),
			Color.rgb(89,190,198),
			Color.rgb(249,157,56),
			
			Color.rgb(123,200,157),
			Color.rgb(224,115,184),
			Color.rgb(135,109,232),
		};

	PopupWindow mPopup;
	int mCurrentColor = 0xffffff;
	ImageView	mColorButton;
	EditText	mSubject;
	EditText	mTeacher;
	EditText	mClassRoom;
	EditText	mContact;
	EditText	mEmail;
	Settings 	mSettings;
	Button		mBack;
	Button 		mSave;
	HashMap<Subject, Subject> mTable;
	TimeTableControl	mTimeTable;
	Context		mContext;
	TextView	mTeacherLabel;
	
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		mSettings = new Settings(this);
		super.onCreate(saveInstanceState);
		setContentView(R.layout.setup_timetable); 
		
		LinearLayout r = (LinearLayout)findViewById(R.id.layout);
		mTimeTable = (TimeTableControl)r.findViewById(R.id.table);
		
		mColorButton = (ImageView)r.findViewById(R.id.color);
		mColorButton.setOnClickListener(ColorListener);
		mSubject = (EditText)r.findViewById(R.id.subject);
		mTeacher = (EditText)r.findViewById(R.id.teacher);
		mClassRoom = (EditText)r.findViewById(R.id.room);
		mContact = (EditText)r.findViewById(R.id.phone);
		mEmail = (EditText)r.findViewById(R.id.email);
		mBack = (Button)r.findViewById(R.id.back);
		mSave = (Button)r.findViewById(R.id.save);
		mTeacherLabel = (TextView)r.findViewById(R.id.teacher_label);
		
		if(MainActivity.TYPE_THEMA == TypeThema.eUNIVERSITY)
		{
			mTeacherLabel.setText(R.string.professor);
			mTeacher.setHint(R.string.professor);
		}
		
		mBack.setOnClickListener(BackClickListener);
		mSave.setOnClickListener(SaveClickListener);
		
		mContact.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
		

		mTable = (HashMap<Subject, Subject>)mSettings.table.clone();
		for(Iterator<Entry<Subject, Subject>> i = mTable.entrySet().iterator(); i.hasNext(); )
		{
			Entry<Subject, Subject> s = i.next();
			s.getValue().edit = true;
		}
		mTimeTable.setConfig(mSettings.minTime, mSettings.maxTime);
		mTimeTable.renderSetup(CellClickListener, mTable, mSettings.weekday, mSettings.time);
		mContext = this;
		
		Bundle extras = getIntent().getExtras();
		{
			if(extras != null)
			{
				mSubject.setText(extras.getString("subject"));
				mTeacher.setText(extras.getString("teacher"));
				mClassRoom.setText(extras.getString("classroom"));
				mContact.setText(extras.getString("phone"));
				mEmail.setText(extras.getString("email"));
				
				int color = extras.getInt("color");
				if(color == 0)
				{
					color = getColor(3);
				} 
				mColorButton.setBackgroundColor(color);
				mCurrentColor = color;
			}
		}
	}
	
	private View.OnClickListener BackClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		finish();
  	}
    };
    
	private View.OnClickListener SaveClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		mSettings.table = mTable;
  		mSettings.saveSettings();
  		finish();
  	}
    };
    
	
	 private View.OnClickListener ColorListener = new View.OnClickListener() 
     {
	   	@Override
	   	public void onClick(View v) 
	   	{    		
	   		LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
	   		View popupView = layoutInflater.inflate(R.layout.color_dialog, null);
	   	 
	   		final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
	   		mPopup = popupWindow;
	   		
	   		popupWindow.showAtLocation(v, Gravity.CENTER | Gravity.CENTER_VERTICAL, 0, 0);
			
	   		TableLayout t = (TableLayout)popupView.findViewById(R.id.table);
			
			TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
	                ViewGroup.LayoutParams.FILL_PARENT,
	                ViewGroup.LayoutParams.FILL_PARENT,
	                1.0f);
			
						
			TableRow.LayoutParams cell = new TableRow.LayoutParams(
	    			MainActivity.convert(64),
	    			MainActivity.convert(40));
			
			cell.setMargins(MainActivity.convert(10), MainActivity.convert(10), MainActivity.convert(10), MainActivity.convert(10));
			TableRow row = null;
			for(int i = 0; i < 12; i++)
			{
				if( (i % 3) == 0)
				{
					row = new TableRow(t.getContext());
					row.setLayoutParams(cellLp);
					t.addView(row);
				}
				
				ImageView image = new ImageView(t.getContext());
				
				if(i == 2)
				{
				    RelativeLayout view = new RelativeLayout(t.getContext());
				    
					RelativeLayout.LayoutParams im = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					im.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					view.setLayoutParams(cell);
					
					image.setImageResource(R.drawable.close_w);
					image.setLayoutParams(im);
					view.addView(image);
					
					row.addView(view);
					view.setOnClickListener(ColorClickListener);
					view.setId(i);
				} else if(i == 0)
				{
					image.setId(i);
					image.setBackgroundResource(R.drawable.white_color_button);
					image.setLayoutParams(cell);
					row.addView(image);
					image.setOnClickListener(ColorClickListener);
				} else
				{
					image.setId(i);
					image.setBackgroundColor(getColor(i));
					image.setLayoutParams(cell);
					row.addView(image);
					image.setOnClickListener(ColorClickListener);
				}
				
				
			}
		}
     };
     
     private View.OnClickListener ColorClickListener = new View.OnClickListener() 
     {
	   	@Override
	   	public void onClick(View v) 
	   	{    		
	   		if(v.getId() == 2)
	   		{
	   			mPopup.dismiss();
	   		} else
	   		{
	   			mCurrentColor = getColor(v.getId());
	   			mColorButton.setBackgroundColor(mCurrentColor);
	   			mPopup.dismiss();
	   		}
	   	}
     };
     
     private View.OnClickListener CellClickListener = new View.OnClickListener() 
     {
	   	@Override
	   	public void onClick(View v) 
	   	{    
	   		ArrayList<Subject> list = (ArrayList<Subject>)v.getTag();
    		Subject s = list.get(0);
    		
	   		if(mTable.containsKey(s))
	   		{
	   			mTable.remove(s);
	   			mTimeTable.renderSetup(CellClickListener, mTable, mSettings.weekday, mSettings.time);
	   			return;
	   		}
	   		
	   		showTimePeriod(v);
	   	}
     };
     
     private int getColor(int i)
     {
    	 if(MainActivity.TYPE_THEMA == TypeThema.eNON_UNIVERSITY)
    	 {
    		 return colors[i];
    	 } else
    	 {
    		 return colors_university[i];
    	 }
     }
     
     private int getRemainTime(int weekday, int time)
     {
    	 Set<Integer> list = new HashSet<Integer>();
    	 for(Subject s : mTable.keySet())
    	 {
    		 if(s.period > 0 && s.weekday == weekday)
    			 list.add(s.time);
    	 }
    	 int max = 0;
    	 
    	 for(int i = time; i <= 37; i++)
    	 {
    		 if(list.contains(i))
    			 return max;
    		 
    		 max++;
    	 }
    	 
    	 return max;
     }
     
     private void showTimePeriod(View v)
     {
    	 if(mPopup != null)
 			mPopup.dismiss();
 		
 		LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
 		View popupView = layoutInflater.inflate(R.layout.time_period_popup, null);
 		RadioGroup group = (RadioGroup)popupView.findViewById(R.id.radiogroup1);
 		ImageView close = (ImageView)popupView.findViewById(R.id.close);
 		close.setOnClickListener(CloseListener);

 		ArrayList<Subject> list = (ArrayList<Subject>)v.getTag();
		Subject s = list.get(0);
		
 		int max = getRemainTime(s.weekday, s.time);
 		for(int i = 2; i <= max; i++)
 		{
 			RadioButton radio = new RadioButton(mContext);
	 		radio.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	 		int miniute = (i * 30) % 60;
	 		int hour = (i * 30) / 60;
	 		radio.setText(String.format("%d½Ã°£ %dºÐ", hour, miniute));
	 		radio.setId(i);
	 		radio.setTextColor(Color.BLACK);
	 		radio.setTag(i);
	 		group.addView(radio);
 		}

 		group.setTag(v.getTag());
 		group.setOnCheckedChangeListener(ChangedListener);
 		final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
 		mPopup = popupWindow;
 		
 		popupWindow.showAtLocation(v, Gravity.CENTER | Gravity.TOP, 0, 0);
     }
     
     private View.OnClickListener CloseListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mPopup.dismiss();
		}
	};
     
     private RadioGroup.OnCheckedChangeListener ChangedListener = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			mPopup.dismiss();
			RadioButton radio = (RadioButton)group.findViewById(checkedId);
			
			ArrayList<Subject> list = (ArrayList<Subject>)group.getTag();
    		Subject s = list.get(0);
			s.classRoom = mClassRoom.getText().toString();
	   		s.color = mCurrentColor;
	   		s.email = mEmail.getText().toString();
	   		s.phone = mContact.getText().toString();
	   		s.subject = mSubject.getText().toString();
	   		s.teacher = mTeacher.getText().toString();
	   		s.edit = false;
	   		s.period = (Integer)radio.getTag();
	   		
	   		mTable.put(s, s);
	   		mTimeTable.renderSetup(CellClickListener, mTable, mSettings.weekday, mSettings.time);
		}
	};
     private View.OnClickListener TimeClickListener = new View.OnClickListener() 
     {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mPopup.dismiss();

			ArrayList<Subject> list = (ArrayList<Subject>)v.getTag();
    		Subject s = list.get(0);
			s.classRoom = mClassRoom.getText().toString();
	   		s.color = mCurrentColor;
	   		s.email = mEmail.getText().toString();
	   		s.phone = mContact.getText().toString();
	   		s.subject = mSubject.getText().toString();
	   		s.teacher = mTeacher.getText().toString();
	   		s.edit = false;
	   		
	   		mTable.put(s, s);
	   		mTimeTable.renderSetup(CellClickListener, mTable, mSettings.weekday, mSettings.time);
			
		}
	};
}

