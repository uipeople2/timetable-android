package com.uipeople.schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TimeTable extends ViewGroup {

	public TimeTable(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		createTable();
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		Paint shadow = new Paint();
		shadow.setShadowLayer(10.f, 0.0f, 2.0f, 0xFF000000);
		canvas.drawPaint(shadow);
	}
	
	private void createTable()
	{
		TableLayout t = new TableLayout(this.getContext());
		TableLayout.LayoutParams lp = new TableLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		this.addView(t, lp);
		
		TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT,
                1.0f);
		
		TableRow row = new TableRow(this.getContext());
		row.setLayoutParams(cellLp);
		LinearLayout l = new LinearLayout(this.getContext());
		row.addView(l);
		
		addHeader(l);
		t.addView(row);
		
		row = new TableRow(this.getContext());
		row.setLayoutParams(cellLp);
		l = new LinearLayout(this.getContext());
		row.addView(l);
		
		Map<Integer, Subject> m = new HashMap<Integer, Subject>();
		Map<String, Subject> s = new HashMap<String, Subject>();
		/*
		Subject c = new Subject("타이포그래픽", "미술학202", Color.rgb(241,91,100), 1);
		s.put(c.name, c);
		c = new Subject("드로잉", "강의동503", Color.rgb(128,172,219), 1);
		s.put(c.name, c);
		c = new Subject("애니메이션", "미술학101", Color.rgb(179 ,217 ,108 ), 1);
		s.put(c.name, c);
		c = new Subject("디자인심리", "강의동202", Color.rgb(202 ,164 ,205 ), 1);
		s.put(c.name, c);
		c = new Subject("기초입체미술학", "미술학305", Color.rgb(89 ,190 ,198 ), 1);
		s.put(c.name, c);
		c = new Subject("영상미술", "미술학101", Color.rgb(249 ,157 ,56 ), 1);
		s.put(c.name, c);
		c = new Subject("심리학", "강의동106", Color.rgb(123 ,200 ,156 ), 1);
		s.put(c.name, c);
		*/
		
		Subject c = new Subject("수학", "", Color.rgb(241,91,100), 1);
		s.put(c.name, c);
		c = new Subject("국어", "", Color.rgb(128,172,219), 1);
		s.put(c.name, c);
		c = new Subject("영어", "", Color.rgb(179 ,217 ,108 ), 1);
		s.put(c.name, c);
		c = new Subject("미술", "", Color.rgb(202 ,164 ,205 ), 1);
		s.put(c.name, c);
		c = new Subject("과학", "", Color.rgb(89 ,190 ,198 ), 1);
		s.put(c.name, c);
		c = new Subject("체육", "", Color.rgb(249 ,157 ,56 ), 1);
		s.put(c.name, c);
		c = new Subject("도덕", "", Color.rgb(123 ,200 ,156 ), 1);
		s.put(c.name, c);
		
		addTimeLine(l);
		
		for(int i = 0; i < 5; i++)
		{
			m = generateClass(s);
			addDayofWeek(l, m);
		}
		
		t.addView(row);
	}
	
	   public Map<Integer, Subject> generateClass(Map<String, Subject> subjects)
	    {
	    	List<Integer> list = new LinkedList<Integer>();
	    	for(int i = 1; i <= 8; i++)
	    	{
	    		list.add(i);
	    	}
	    	
	    	Collections.shuffle(list);
	    	
	    	List<Subject> valueList = new ArrayList<Subject>(subjects.values());
	    	Collections.shuffle(valueList);
	    	
			Map<Integer, Subject> m = new HashMap<Integer, Subject>(10);
			
			ListIterator iter, iterSubject;
			iter = list.listIterator();
			iterSubject = valueList.listIterator();
			Random rand = new Random();
			int n = 8;
			for(int i = 0; i < n; i++)
			{
				if(!iter.hasNext())
					break;
				
				if(!iterSubject.hasNext())
				{
					Collections.shuffle(valueList);
					iterSubject = valueList.listIterator();
				}
				
				m.put((Integer)iter.next(), (Subject)iterSubject.next());
			}
			
			return m;
	    }
	    
	    public void addHeader(LinearLayout v)
	    {
	    	ViewGroup.LayoutParams cellLp = new ViewGroup.LayoutParams(
	    			80,
	    			80);
	    	
	    	ViewGroup.LayoutParams cellLp2 = new ViewGroup.LayoutParams(
	    			128,
	    			80);
	    	
	    	
	    	ImageButton blank = new ImageButton(v.getContext());
	    	blank.setLayoutParams(cellLp);
	    	blank.setBackgroundColor(Color.rgb(66, 69, 74));
	    //	blank.setOnClickListener(LockClickListener);
	    	blank.setImageResource(R.drawable.lock);
	        v.addView(blank);
	        
	        String names[] = {"MON","TUE","WED","THU","FRI","SAT"};

	        for(int i = 0; i < 5; i++)
	        {
	        	TextView label = new TextView(v.getContext());
	        	label.setText(names[i]);
	        	label.setTypeface(null, Typeface.BOLD);
	        	label.setTextColor(Color.GRAY);
	        	label.setTextSize(15);
	        	label.setBackgroundColor(Color.rgb(66, 69, 74));
	        	label.setLayoutParams(cellLp2);
	        	label.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
	            v.addView(label);
	        }
	    }
	    
	    class Class
	    {
	    	public int hour;
	    	public Subject subject;
	    }
	    
	    class Subject
	    {
	    	public String name;
	    	public String place;
	    	public int color;
	    	public int hour;
	    	public int times;
	    	
	    	public Subject(){}
	    	public Subject(String name, String place, int color, int times)
	    	{
	    		this.name = name;
	    		this.place = place;
	    		this.color = color;
	    		this.times = times;
	    	}
	    }
	    static int tHeight = 133;
	    
	    public void addDayofWeek(LinearLayout v, Map<Integer, Subject> ar)
	    {
	    	TableLayout tl = new TableLayout(v.getContext());
	        
	        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
	                128,
	                ViewGroup.LayoutParams.FILL_PARENT);
	        tl.setLayoutParams(lp);
	        
	        TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
	                ViewGroup.LayoutParams.WRAP_CONTENT,
	                ViewGroup.LayoutParams.WRAP_CONTENT,
	                1.0f);
	         
	        for(int i = 1; i <= 8; )
	        {
	        	RelativeLayout r = new RelativeLayout(v.getContext());
	        	TextView label = new TextView(v.getContext());
	        	TableRow row = new TableRow(v.getContext());
	        	Subject c = new Subject();
	        	
	        	if(ar.containsKey(i))
	        	{
	        		c = ar.get(i);
	        	} else
	        	{
	        		c.name = "";
	            	c.color = Color.rgb(66, 69, 74);
	            	c.times = 1;
	            	c.place = "";
	        	}
	        	
	        	label.setText(c.name);
	    		//label.setBackgroundColor(c.color);
	    		label.setTextSize(20);
	    		label.setTextColor(Color.WHITE);
	        	label.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER );
	        	TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
	                    128,
	                    tHeight * c.times,
	                    1.0f);
	        	RelativeLayout.LayoutParams rel = new RelativeLayout.LayoutParams(128, tHeight * c.times);
	        	r.addView(label, rel);
//	        	r.setOnTouchListener(DetailClickListener);
	        	
	        	RelativeLayout.LayoutParams rel2 = new RelativeLayout.LayoutParams(15, tHeight * c.times);
	        	ImageView image = new ImageView(v.getContext());
	        	image.setBackgroundColor(c.color);
	        	r.addView(image, rel2);
	        	
	        	View ruler = new View(v.getContext()); 
	        	ruler.setBackgroundColor(Color.rgb(79, 80, 82));
	        	r.setGravity(Gravity.CENTER_VERTICAL);
	        	RelativeLayout.LayoutParams p =new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, 2);
	        	p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	        	r.addView(ruler, p);
	        	
	        	
	        	
	        	row.addView(r, cellLp);
	            tl.addView(row, rowLp);
	            i += c.times;
	        }

	        v.addView(tl);
	    }
	    
	    public void addTimeLine(LinearLayout v)
	    {
	        TableLayout tl = new TableLayout(v.getContext());
	       
	        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
	        		ViewGroup.LayoutParams.WRAP_CONTENT,
	                ViewGroup.LayoutParams.FILL_PARENT);
	        tl.setLayoutParams(lp);
	        
	        TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
	                ViewGroup.LayoutParams.FILL_PARENT,
	                ViewGroup.LayoutParams.FILL_PARENT,
	                1.0f);
	        TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
	                80,
	                tHeight,
	                1.0f);
	        for(int i = 1; i <= 8; i++)
	        {
	        	TableRow row = new TableRow(v.getContext());
	        	
	        	TextView label = new TextView(v.getContext());
	        	label.setText(String.format("%d", i));
	        	label.setTextColor(Color.WHITE);
	        	label.setGravity(Gravity.RIGHT);
	        	label.setPadding(0, 0, 8, 0);
	        	row.addView(label, cellLp);
	            tl.addView(row, rowLp);
	        }

	        v.addView(tl);
	    }

		@Override
		protected void onLayout(boolean arg0, int arg1, int arg2, int arg3,
				int arg4) {
			// TODO Auto-generated method stub
			
		}

}
