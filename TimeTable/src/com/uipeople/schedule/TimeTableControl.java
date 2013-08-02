package com.uipeople.schedule;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.http.conn.routing.RouteInfo.TunnelType;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class TimeTableControl extends LinearLayout 
{
	enum TypeThema
	{
		eNON_UNIVERSITY,
		eUNIVERSITY,
	}
	
	enum ColorThema
	{
		eGRAY,
		eWHITE,
	}
	
	enum TimeThema
	{
		eTIME,
		ePERIOD,
	}
	
	private OnClickListener	mLockListener;
	private OnClickListener	mDetailListener;
	private Boolean	mDaily = false;
	private int mSubjectHeight = MainActivity.convert(66);
	private int mTimeLineWidth = MainActivity.convert(40);
	private int mTimeLineHeight = MainActivity.convert(66);
	private Boolean mLockVisible = true;
	private int mHeaderColor = Color.rgb(66,69,74);
	private int mTableColor = Color.rgb(66, 69, 74);
	private int mCellColor = Color.rgb(255, 255, 255);
	private int mCellLineColor = Color.rgb(79, 80, 82);
	private int mCellColor2 = Color.rgb(255, 255, 255);
	private int mCellTextColor = Color.WHITE;
	private int mTimeLineColor = Color.WHITE;
	private int mHeaderTextColor = Color.WHITE;
	private int mActivateHeaderTextColor = Color.WHITE;
	private Boolean mLine = true;
	private Boolean mSetup = false;
	private int mHeaderShape;
	private int mActivateHeaderShape;
	private int mMaxTime = 8;
	private int mScroll;
	private Boolean mCapture = false;
	private CustomScrollView	mScrollView = null;
	private int mTextSize = 20;
	public int mLastX;
	public int mLastY;
	int mWeekDay;
	private TypeThema mTypeThema = TypeThema.eNON_UNIVERSITY;
	private ColorThema mColorThema = ColorThema.eGRAY;
	private TimeThema mTimeThema = TimeThema.ePERIOD;
	private Boolean mBorderLine = false;
	private int mMin;
	private int mMax;
	
	public static String today()
	{
		String names[] = {"SUN","MON","TUE","WED","THU","FRI","SAT"};
		
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return names[day];
	}
	
	public static String day(int day)
	{
		String names[] = {"MON","TUE","WED","THU","FRI","SAT","SUN"};
		return names[day - 1];
	}
	

	public TimeTableControl(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mUnlock = false;
		// TODO Auto-generated constructor stub
		mContext = context;
		mTypeThema = MainActivity.TYPE_THEMA;
		setOrientation(VERTICAL);
	}
	
	Context	mContext;
	
	private Handler mHandler = new Handler();
	private boolean mIsLongPress = false;
    private Runnable longPress = new Runnable() {

        @Override
        public void run() {         
            if (mIsLongPress) {             
            	performLongClick();
                mIsLongPress = false;
                setMoveMode(true);
                mDrag = true;
            }
        }

    };
    
    float mDeltaX, mDeltaY;
    
    private Boolean mDrag = false;
    public void setMoveMode(Boolean move)
    {
    	if(move)
    	{
	    	AlphaAnimation alpha = new AlphaAnimation(0.5F, 0.5F);
	        alpha.setDuration(0);
	        alpha.setFillAfter(true);
	        this.startAnimation(alpha);
	        mDrag = true;
    	} else
    	{
			AlphaAnimation alpha = new AlphaAnimation(1.0F, 1.0F);
			alpha.setDuration(0);
			alpha.setFillAfter(true);
			this.startAnimation(alpha);
			mDrag = false;
    	}
    	 
    }
    
    @Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
    	if (ev.getAction() == MotionEvent.ACTION_DOWN) 
    	{
    		mHandler.postDelayed(longPress, 500);
    		mDeltaY = ev.getY();
    		super.onInterceptTouchEvent(ev);
    		mDeltaX = ev.getX();
    		mScrollView.onTouchEvent(ev);
    		return true;
        } else if(ev.getAction() == MotionEvent.ACTION_MOVE)
        {
        	if(mDrag)
        	{
       		   int x = (int)(ev.getRawX() - mDeltaX);
       		   int y = (int)(ev.getRawY() - mDeltaY - 50);
       		   ((View)getParent()).setPadding(x, y, 0,0);
        		return true;
        	}
        
        	mScrollView.onTouchEvent(ev);
        	final int deltaY = Math.abs((int) (ev.getY() - mDeltaY));
    	    if ( deltaY >= 10) 
    	    {
    	    	mIsLongPress = false;
    	    	mHandler.removeCallbacks(longPress);
    	    	super.onInterceptTouchEvent(ev);
    	    	return false;
    	    }
    	    
    	    super.onInterceptTouchEvent(ev);
    	    return true;
        	
        } else if(ev.getAction() == MotionEvent.ACTION_UP)
        {
        	mScrollView.onTouchEvent(ev);
        	mIsLongPress = false;
	    	mHandler.removeCallbacks(longPress);
	    	setMoveMode(false);
	    	return super.onInterceptTouchEvent(ev);	
        } else
        {
        	mScrollView.onTouchEvent(ev);
        	mIsLongPress = false;
        	mHandler.removeCallbacks(longPress);
        	setMoveMode(false);
        }
		
		return false;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
    	if (ev.getAction() == MotionEvent.ACTION_DOWN) 
    	{
    		if(mDaily)
    		{
				mDeltaY = ev.getY();
				mIsLongPress = true;                            
				
				super.onInterceptTouchEvent(ev);
				return true;	
    		} else
    		{
    			return super.onInterceptTouchEvent(ev);
    		}
        } 
		return false;
    
	}
	
	public void renderTableHeader(LinearLayout t, int day, int weekDayCount)
	{
		TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT,1);
		
		TableRow row = new TableRow(getContext());
		LinearLayout l = new LinearLayout(getContext());
		l.setLayoutParams(cellLp);
		row.addView(l);
		
		addHeader(l,day, weekDayCount);
		t.addView(row);
	}
	
	private void insert(Map<Integer, List<Subject>> m, Subject s)
	{
		if(m.containsKey(s.time))
		{
			m.get(s.time).add(s);
		} else
		{
			List<Subject> list = new ArrayList<Subject>();
			list.add(s);
			m.put(s.time, list);
		}
	}
	
	public void renderTableBody ( LinearLayout t, HashMap<Subject, Subject> table, int day, int weekDayCount)
	{
		Map<Integer, List<Subject>> m = new HashMap<Integer, List<Subject>>();

		int min = mMin;
		int max = mMax;
		
		if(mTypeThema == mTypeThema.eUNIVERSITY)
		{
			min = min * 2;
			max = (max * 2) + 1;
		}
		
		for(Subject ss : table.values())
		{
			max = ss.time > max ? ss.time : max;
		}
		
		RelativeLayout background = new RelativeLayout(mContext);
		background.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
		
		LinearLayout timeLine = new LinearLayout(mContext);
		timeLine.setLayoutParams(new LayoutParams(mTimeLineWidth, LayoutParams.FILL_PARENT));

		final Drawable drawable = getResources().getDrawable(R.drawable.grey_bottomleft_round_shape);
        drawable.setColorFilter(mTableColor, Mode.SRC_ATOP);
        timeLine.setBackgroundDrawable(drawable);
		background.addView(timeLine);
		
		final Drawable drawable2 = getResources().getDrawable(R.drawable.grey_bottomright_round_shape);
        drawable2.setColorFilter(mCellColor, Mode.SRC_ATOP);
        background.setBackgroundDrawable(drawable2);
        
		LayoutParams cellLp = new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT
                );
		
		LinearLayout row = new LinearLayout(getContext());
		row.setLayoutParams(cellLp);
		
		background.addView(row);
		CustomScrollView sv = new CustomScrollView(getContext());
		
		LayoutParams cellLp3 = new LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		LinearLayout l2 = new LinearLayout(getContext());
		LayoutParams cellLine = new LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
		l2.setOrientation(HORIZONTAL);
		l2.setLayoutParams(cellLine);
		
		mScrollView = sv;
		
		if(mCapture)
		{
			row.addView(l2, cellLp3);
		} else
		{
			sv.addView(l2);
			row.addView(sv, cellLp3);
		}
		
		if(weekDayCount == 1)
		{
			for(Subject ss : table.values())
			{
				if(ss.weekday == day)
				{
					insert(m, ss);
				}
			}
			
			addTimeLine(l2, m, min, max);
			addDayofWeek(l2, m, day, min, max);
		} else
		{
			addTimeLine(l2, mMin, mMax);
			
			for(int i = 1; i <= weekDayCount; i++)
			{
				for(Subject ss : table.values())
				{
					if(ss.weekday == i)
					{
						insert(m, ss);
					}
				}
				
				addDayofWeek(l2, m, i, min, max);
				m.clear();
			}
		}

		t.addView(background);
	}
	
	public void setConfig(int min, int max)
	{
		mMin = min;
		mMax = max;
	}
	
	private void applyThema()
	{
		if(ColorThema.eGRAY == mColorThema)
		{
			mBorderLine = false;
			
			if(mDaily)
				mHeaderShape = R.drawable.grey_top_round_shape;
			else
			{
				setBackgroundColor(Color.rgb(0x31, 0x34, 0x39));
				mHeaderShape = R.drawable.grey_header_shape;
			}
			
			if(mTypeThema == TypeThema.eNON_UNIVERSITY)
			{
				mActivateHeaderShape = R.drawable.grey_activate_header_shape;
				mMin = 1;
				mMax = 8;
				mActivateHeaderTextColor = Color.WHITE;
			} else
			{
				mActivateHeaderShape = R.drawable.white_university_activate_header_shape;
				mHeaderTextColor = Color.rgb(185, 185, 185);
				mActivateHeaderTextColor =  Color.rgb(66, 69, 74);
			}
			mCellColor = Color.rgb(66, 69, 74);
			mCellColor2 = Color.rgb(66, 69, 74);
			mCellLineColor = Color.rgb(84, 85, 85);
			mTableColor = Color.rgb(49, 52, 57);
			mCellTextColor = Color.WHITE;
		} else
		{
			if(mTypeThema == TypeThema.eNON_UNIVERSITY)
			{
				mBorderLine = true;
				mActivateHeaderShape = R.drawable.white_activate_header_shape;
				mCellTextColor = Color.rgb(112, 112, 112);
				mMin = 1;
				mMax = 8;
				mActivateHeaderTextColor = Color.WHITE;
			} else
			{
				mBorderLine = false;
				mActivateHeaderShape = R.drawable.white_university_activate_header_shape;
				mCellTextColor = Color.WHITE;
				mActivateHeaderTextColor = Color.rgb(255, 255, 255);
			}
			
			mCellColor = Color.rgb(255, 255, 255);
			mCellColor2 = Color.rgb(250, 250, 250);
			mCellLineColor = Color.rgb(235, 235, 235);
			mTimeLineColor = Color.rgb(199,199,199);
			
			mTableColor =Color.rgb(229, 229, 229);
			
			if(mDaily)
				mHeaderShape = R.drawable.white_top_round_shape;
			else
			{
				setBackgroundColor(mTableColor);
				mHeaderShape = R.drawable.white_header_shape;
			}
		}
	}
	
	public void renderDaily( HashMap<Subject, Subject> table, int thema)
	{
		mLockVisible = false;
		mTimeLineWidth = MainActivity.convert(30);
		mDaily = true;
		mTimeLineHeight = MainActivity.convert(40);
		mColorThema = ColorThema.values()[thema];
		
		if(mTypeThema == mTypeThema.eUNIVERSITY)
		{
			mSubjectHeight = MainActivity.convert(20);
		} else
		{
			mSubjectHeight = MainActivity.convert(40);
		}
		mTextSize = 11;
		
		removeAllViews();
		applyThema();
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		
		renderTableHeader(this, day, 1);
		renderTableBody(this, table, day, 1);
	}
	
	public void renderFull(OnClickListener lock, OnClickListener detail, HashMap<Subject, Subject> table, int thema, int weekDay)
	{
		mWeekDay = weekDay;
		mLockVisible = true;
		mDaily = false;
		mLockListener = lock;
		mDetailListener = detail;
		mColorThema = ColorThema.values()[thema];
		
		mTimeLineWidth = MainActivity.convert(40);
		
		int weekDayCount = 5;
		if(mTypeThema == TypeThema.eUNIVERSITY)
		{
			mSubjectHeight = MainActivity.convert(28);
			mTimeLineHeight = MainActivity.convert(56);
			mTextSize = 11;
			if(weekDay == 1)
			{
				weekDayCount = 6;
			} else if(weekDay == 2)
			{
				weekDayCount = 7;
				mTextSize = 10;
			}
		} else
		{
			mSubjectHeight = MainActivity.convert(66);
			mTimeLineHeight = MainActivity.convert(66);
			if(weekDay == 1)
			{
				weekDayCount = 6;
			} else if(weekDay == 2)
			{
				weekDayCount = 7;
				mTextSize = 15;
			}
		}
		
		removeAllViews();
		applyThema();
    	
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		renderTableHeader(this, day, weekDayCount);
		renderTableBody(this, table, day, weekDayCount);
	}
	
	public void renderSetup(OnClickListener detail, HashMap<Subject, Subject> table, int weekDay, int max)
	{
		mBorderLine = true;
		mSetup = true;
		mLine = false;
		mLockVisible = false;
		mDaily = false;
		mTimeLineWidth = MainActivity.convert(29);
		mSubjectHeight = MainActivity.convert(40);
		mHeaderColor = Color.rgb(0xee, 0xee, 0xee);
		mTableColor = mHeaderColor;		
		mCellColor = Color.rgb(255, 255, 255);
		mCellColor2 = Color.rgb(250, 250, 250);
		mCellLineColor = Color.rgb(235, 235, 235);
		mTimeLineColor = Color.rgb(199,199,199);
		mDetailListener = detail;
		mColorThema = ColorThema.eWHITE;
		
		if(mScrollView != null)
		{
			mScroll = mScrollView.getScrollY();
		}
		
		LinearLayout t = this;
		t.removeAllViewsInLayout();
		applyThema();

		int weekDayCount = 5;
		
		if(mTypeThema == TypeThema.eUNIVERSITY)
		{
			mSubjectHeight = MainActivity.convert(30);
			mTimeLineHeight = MainActivity.convert(60);
			mTextSize = 11;
			if(weekDay == 1)
			{
				weekDayCount = 6;
			} else if(weekDay == 2)
			{
				weekDayCount = 7;
				mTextSize = 10;
			}
		} else
		{
			mSubjectHeight = MainActivity.convert(34);
			mTimeLineHeight = MainActivity.convert(34);
			mTextSize = 15;
			if(weekDay == 1)
			{
				weekDayCount = 6;
			} else if(weekDay == 2)
			{
				weekDayCount = 7;
				mTextSize = 12;
			}
		}
		
		renderTableHeader(t, 0, weekDayCount);
		renderTableBody(t, table, 0, weekDayCount);
		mScrollView.post(new Runnable() { 
		    public void run() { 
		    	mScrollView.scrollTo(0, mScroll);
		    	
		    } 
		}); 
	}
	
	public void renderFriendSubject(OnClickListener detail, FriendSubject table, int weekDay)
	{
		mLockVisible = false;
		mDaily = false;
		removeAllViews();
		applyThema();
		mDetailListener = detail;
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		int weekDayCount = 5;
		if(weekDay == 1)
		{
			weekDayCount = 6;
		} else if(weekDay == 2)
		{
			weekDayCount = 7;
			mTextSize = 15;
		}
					
		renderTableHeader(this, day, weekDayCount);
		renderTableBody(this, table.subjects, day, weekDayCount);
	}
	
	public void renderFriend(FriendTimeTable table, int weekDay)
	{
		mLockVisible = false;
		mDaily = false;
		removeAllViews();
		applyThema();
    	
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		int weekDayCount = 5;
		if(weekDay == 1)
		{
			weekDayCount = 6;
		} else if(weekDay == 2)
		{
			weekDayCount = 7;
			mTextSize = 15;
		}
					
		renderTableHeader(this, day, weekDayCount);
		renderTableBody(this, table.subjects, day, weekDayCount);
	}
	
    
	public Bitmap renderToBitmap() 
	{
		Bitmap csr = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(csr);
		mCapture = false;
		draw(canvas);
		return csr;
	}
	
    public void addHeader(LinearLayout v, int day, int weekDayCount)
    {
    	LinearLayout.LayoutParams cellLp = new LinearLayout.LayoutParams(
    			mTimeLineWidth,
    			MainActivity.convert(40));
    	
    	LinearLayout.LayoutParams cellLp2 = new LinearLayout.LayoutParams(
    			0,
    			MainActivity.convert(40), 1);
    	
    	ViewGroup.LayoutParams cellLp3 = new ViewGroup.LayoutParams(
    			LayoutParams.FILL_PARENT,
    			MainActivity.convert(40));
        
        if(mDaily)
        {
           	TextView label = new TextView(v.getContext());
        	label.setText(today());
        	label.setTypeface(null, Typeface.BOLD);
        	label.setTextColor(Color.WHITE);
        	label.setTextSize(20);
        	label.setLayoutParams(cellLp3);
        	label.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
        	
        	Resources res = getResources();
        	
	        final Drawable drawable = (Drawable)res.getDrawable(mHeaderShape);
	        label.setBackgroundDrawable(drawable);
            v.addView(label);
        } else
        {
        	ImageButton blank = new ImageButton(v.getContext());
        	blank.setId(100);
        	blank.setLayoutParams(cellLp);
        	if(mLockVisible)
        	{
    	    	blank.setOnClickListener(mLockListener);
    	    	if(mUnlock)
    	    		blank.setImageResource(R.drawable.unlock);
    	    	else
    	    		blank.setImageResource(R.drawable.lock);
        	}
        	
        	if(mSetup)
        	{
        		blank.setBackgroundColor(mHeaderColor);
        	} else
        	{
        		Resources res = getResources();
    	        final Drawable drawable = (Drawable)res.getDrawable(mHeaderShape);
    	        blank.setBackgroundDrawable(drawable);	
        	}
    	    v.addView(blank);
        	
	        for(int i = 1; i <= weekDayCount; i++)
	        {
	        	TextView label = new TextView(v.getContext());
	        	label.setText(day(i));
	        	label.setTypeface(null, Typeface.BOLD);
	        	label.setTextSize(15);
	        	label.setLayoutParams(cellLp2);
	        	label.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
	        	
	        	if(mSetup)
	        	{
	        		label.setTextColor(Color.GRAY);
	        		label.setBackgroundColor(mHeaderColor);
	        	} else
	        	{
	        		label.setTextColor(mHeaderTextColor);
	        		Resources res = getResources();
	        		final Drawable drawable; 
	        		if( i == day )
	        		{
	        			label.setTextColor(mActivateHeaderTextColor);
	        			drawable = (Drawable)res.getDrawable(mActivateHeaderShape);
	        		} else
	        		{
	        			drawable = (Drawable)res.getDrawable(mHeaderShape);
	        		}
	    	        label.setBackgroundDrawable(drawable);
	        	}
		        v.addView(label);
	        }
        }
    }
    
    private Boolean mUnlock;
    
    public void lock()
    {
    	ImageButton img = (ImageButton)findViewById(100);
    	img.setImageResource(R.drawable.lock);
    	mUnlock = false;
    }
    
    public void unlock()
    {
    	ImageButton img = (ImageButton)findViewById(100);
    	img.setImageResource(R.drawable.unlock);
    	mUnlock = true;
    }
    
    private void addCountView(Context context, ViewGroup parent, int count)
    {
    	//1이상일때만
    	if(count < 2)
    		return;
    	
    	TextView view = new TextView(context);
    	view.setText(Integer.toString(count));
    	RelativeLayout.LayoutParams rr = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    	rr.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    	rr.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    	rr.rightMargin = 3;
    	
		view.setTextSize(8);
		view.setTextColor(Color.GRAY);
			
    	parent.addView(view, rr);
    }
    
    private void addUnderline(RelativeLayout r)
    {
    	View ruler = new View(r.getContext()); 
    	ruler.setBackgroundColor(mCellLineColor);
    	r.setGravity(Gravity.CENTER_VERTICAL);
    	
    	RelativeLayout.LayoutParams p =new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, MainActivity.convert(1));
    	p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    	r.addView(ruler, p);
    }
    
    public void addDayofWeek(LinearLayout v, Map<Integer, List<Subject>> ar, int weekDay, int min, int max)
    {
    	TableLayout tl = new TableLayout(v.getContext());
        
    	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
        		0,
                ViewGroup.LayoutParams.FILL_PARENT, 1);
        tl.setLayoutParams(lp);
        
        TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
         
        int height = mSubjectHeight;
        int period = 1;
        int pp = 0;
        for(int i = min; i <= max; )
        {
        	RelativeLayout r = new RelativeLayout(v.getContext());
        	TextView label = new TextView(v.getContext());
        	TableRow row = new TableRow(v.getContext());
        	Subject c = new Subject();
        	int color = 0;
        	
        	if(!mDaily)
        	{
        		if(mTypeThema == TypeThema.eNON_UNIVERSITY &&  i % 2 == 0)
        		{
        			addUnderline(r);
        		} else
        		{
        			addUnderline(r);
        		}
        	}
        	
        	if(ar.containsKey(i))
        	{
        		ArrayList<Subject> subjects = (ArrayList<Subject>)ar.get(i);
        		addCountView(v.getContext(), r, subjects.size());	
        		c = subjects.get(0);
        		if(mTypeThema != TypeThema.eNON_UNIVERSITY)
        			period = c.period;
        		pp = period;
        		height = mSubjectHeight * period;
        		//리스트중 가장 처음 값만 보여줌
        		r.setTag(subjects);
        		if(mSetup)
        		{
        			r.setBackgroundColor(c.color);
        		} else
        		{
            		r.setBackgroundColor(color);
        		}
        		
        		if(mLine)
            	{
        			RelativeLayout.LayoutParams rr = null;
        			if(mTypeThema == TypeThema.eNON_UNIVERSITY)
        			{
        				rr = new RelativeLayout.LayoutParams(MainActivity.convert(7), height);
        			} else
        			{
        				rr = new RelativeLayout.LayoutParams(-1, height);
        			}
        			
    	        	ImageView image = new ImageView(v.getContext());
    	        	image.setBackgroundColor(c.color);
    	        	r.addView(image, rr);
            	}
        	} else
        	{
        		ArrayList<Subject> subjects = new ArrayList<Subject>();
        		c.weekday = weekDay;
        		c.subject = "";
            	c.color = 0;
            	c.period = 0;
            	c.time = i;
            	c.classRoom = "";
            	c.teacher = "";
            	subjects.add(c);
            	r.setTag(subjects);
            	period = 1;
            	
            	if(mDaily)
            	{
            		if(!ar.containsKey(i+1) && (i % 2 == 0))
            		{
            			i++;
            			continue;
            		} 
            		
            		height = mSubjectHeight;
            		
            		if(pp % 2 == 0)
            		{
                		color = mCellColor;
            		} else
            		{
            			color = mCellColor2;
            		}
            		
            		pp++;
            	} else
            	{
            		if(i % 2 == 0)
            		{
                		color = mCellColor;
            		} else
            		{
            			color = mCellColor2;
            		}
            		height = mSubjectHeight;
            		pp++;
            	}
            	
            	r.setBackgroundColor(color);
        	}
        	
        	String text = "";
        	if(mTypeThema == TypeThema.eNON_UNIVERSITY)
    		{
        		text = c.subject;
    			label.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER );
    		} else
    		{
    			text = c.subject + "\n" + c.classRoom;
    		}
        	
        	label.setText(text);
    		label.setTextSize(mTextSize);
    		if(mSetup && c.color == Color.WHITE)
    		{
    			label.setTextColor(Color.GRAY);
    			label.setBackgroundResource(R.drawable.white_color_button);
    		} else
    		{
    			label.setTextColor(mCellTextColor);
    		}
    		
    		
        	TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
        			-1,
        			height,
                    1.0f);
        	RelativeLayout.LayoutParams rel = new RelativeLayout.LayoutParams(-1,-1);
        	r.addView(label, rel);
        	
        	if(!mDaily)
        	{
        		r.setOnClickListener(mDetailListener);
        		View ruler = new View(v.getContext()); 
	        	RelativeLayout.LayoutParams p = null;
	        	ruler = new View(v.getContext()); 
            	ruler.setBackgroundColor(mCellLineColor);
            	r.setGravity(Gravity.CENTER_VERTICAL);
            	if(mBorderLine)
            	{
		        	p =new RelativeLayout.LayoutParams(MainActivity.convert(1) , ViewGroup.LayoutParams.FILL_PARENT);
		        	p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		        	r.addView(ruler, p);
            	}
		        
        	}
        	
        	row.addView(r, cellLp);
            tl.addView(row, rowLp);
            i += period;
        }

        v.addView(tl, lp);
    }
    
    public void addTimeLine(LinearLayout v,  Map<Integer, List<Subject>> ar, int min, int max)
    {	
    	LinearLayout tl = new LinearLayout(v.getContext());
       
        LayoutParams lp = new LayoutParams(
        		ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        tl.setLayoutParams(lp);
        tl.setOrientation(VERTICAL);
        tl.setBackgroundColor(Color.WHITE);
        
        int height = 0;
        int period = 0;
        Boolean empty = false;
        for(int i = min; i <= max; i++)
        {
        	if(ar.containsKey(i))
        	{
        		height = mTimeLineHeight;
        		Subject c;
        		ArrayList<Subject> subjects = (ArrayList<Subject>)ar.get(i);
        		c = subjects.get(0);
        		period = c.period;
        	} else
        	{
        		if(!ar.containsKey(i + 1) && period <= 0)
        		{
        			height = mTimeLineHeight / 2;
        		} else
        		{
        			height = mTimeLineHeight;
        			period-=2;
        		}
        	}
        	
        	if(i % 2 != 0)
        	{
        		continue;
        	}
        	
        	TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
            		mTimeLineWidth,
            		height);
        	 
        	TextView label = new TextView(v.getContext());
        	if(mTypeThema == mTypeThema.eNON_UNIVERSITY)
        	{
        		label.setText(String.format("%d", i));
        		label.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        	} else
        	{
        		label.setGravity(Gravity.RIGHT);
        		if(period == 1)
        		{
        			if(i%2 == 0)
        			{
        				label.setText(String.format("%02d", i/2));
        			} else
        			{
        				label.setText("");
        			}
        		} else
        		{
        			if(i%2 == 0)
        			{
        				label.setText(String.format("%02d", i/2));
        			} else
        			{
        				label.setText(String.format("%02d.", i/2));
        			}
        		}
        	}
        	
        	label.setTextColor(mTimeLineColor);
        	label.setPadding(0, -9, 8, 0);
        	
        	if(mDaily && i == max)
        	{
        	} else
        	{
        		label.setBackgroundColor(mTableColor);	
        	}
        	
            tl.addView(label, cellLp);
        }

        v.addView(tl);
    }
    
    public void addTimeLine(LinearLayout v, int min, int max)
    {	
        TableLayout tl = new TableLayout(v.getContext());
       
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
        		ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        tl.setLayoutParams(lp);
        
        TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        
        TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
        		mTimeLineWidth,
        		mTimeLineHeight);
        
        for(int i = min; i <= max; i++)
        {
        	TableRow row = new TableRow(v.getContext());
        	
        	TextView label = new TextView(v.getContext());
        	if(mTypeThema == mTypeThema.eNON_UNIVERSITY)
        	{
        		label.setText(String.format("%d", i));
        		label.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        	} else
        	{
        		label.setText(String.format("%02d", i));
        		label.setGravity(Gravity.TOP|Gravity.RIGHT);
        	}
        	
        	label.setTextColor(mTimeLineColor);
        	
        	label.setPadding(0, 0, 8, 0);
        	
        	if(mDaily && i == max)
        	{
            	Resources res = getResources();
    	        final Drawable drawable = res.getDrawable(R.drawable.grey_bottomleft_round_shape);
    	        drawable.setColorFilter(mTableColor, Mode.SRC_ATOP);
    	        label.setBackgroundDrawable(drawable);
        	} else
        	{
        		label.setBackgroundColor(mTableColor);	
        	}
        	
        	row.addView(label, cellLp);
            tl.addView(row, rowLp);
        }

        v.addView(tl);
    }
    
    public void setOnLockListener(OnClickListener l)
    {
    	
    }
    
    public void setOnDetailListener(OnClickListener l)
    {
    	
    }
}
