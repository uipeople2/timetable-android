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
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
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

/**
 * This is an example of a custom preference type. The preference counts the
 * number of clicks it has received and stores/retrieves it from the storage.
 */
public class CustomTimeTablePreference extends Preference {
    private int mClickCounter;

    // This is the constructor called by the inflater
    public CustomTimeTablePreference(Context context, AttributeSet attrs) {
        super(context, null);

        setWidgetLayoutResource(R.layout.setup_timetable);
    }

    public void addHeader(LinearLayout v)
    {
    	ViewGroup.LayoutParams cellLp = new ViewGroup.LayoutParams(
    			80,
    			80);
    	
    	ViewGroup.LayoutParams cellLp2 = new ViewGroup.LayoutParams(
    			tWidth,
    			80);
    	
    	
    	ImageButton blank = new ImageButton(v.getContext());
    	blank.setLayoutParams(cellLp);
    	blank.setBackgroundColor(Color.rgb(66, 69, 74));
        v.addView(blank);
        
        String names[] = {"MON","TUE","WED","THU","FRI","SAT"};

        for(int i = 0; i < 5; i++)
        {
        	TextView label = new TextView(v.getContext());
        	label.setText(names[i]);
        	label.setTypeface(null, Typeface.BOLD);
        	label.setTextColor(Color.GRAY);
        	label.setTextSize(10);
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
    static int tHeight = 100;
    static int tWidth = 105;
    
    public void addDayofWeek(LinearLayout v, Map<Integer, Subject> ar)
    {
    	TableLayout tl = new TableLayout(v.getContext());
        
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
        		tWidth,
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
        	label.setBackgroundResource(R.drawable.cell_shape);
        	TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
        			tWidth,
                    tHeight * c.times,
                    1.0f);
        	RelativeLayout.LayoutParams rel = new RelativeLayout.LayoutParams(tWidth, tHeight * c.times);
        	r.addView(label, rel);
        //	r.setOnTouchListener(DetailClickListener);
        	
        	RelativeLayout.LayoutParams rel2 = new RelativeLayout.LayoutParams(15, tHeight * c.times);
        	ImageView image = new ImageView(v.getContext());
        	image.setBackgroundColor(c.color);
        //	r.addView(image, rel2);
        	
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
        	label.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        	label.setPadding(0, 0, 8, 0);
        	label.setBackgroundResource(R.drawable.cell_shape);
        	
        	row.addView(label, cellLp);
            tl.addView(row, rowLp);
        }

        v.addView(tl);
    }
    
    @Override
    protected void onBindView(View view) {
        super.onBindView(view);	

        TableLayout t = (TableLayout)view.findViewById(R.id.setupTimeTable);
		
		TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT,
                1.0f);
		
		TableRow row = new TableRow(t.getContext());
		row.setLayoutParams(cellLp);
		LinearLayout l = new LinearLayout(t.getContext());
		row.addView(l);
		addHeader(l);
		t.addView(row);
		
		row = new TableRow(t.getContext());
		row.setLayoutParams(cellLp);
		l = new LinearLayout(t.getContext());
		row.addView(l);
		
		
		addTimeLine(l);
		Map<Integer, Subject> m = new HashMap<Integer, Subject>();
		Map<String, Subject> s = new HashMap<String, Subject>();
		
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
		for(int i = 0; i < 5; i++)
		{
			Map<Integer, Subject> mm = new HashMap<Integer, Subject>(10);
			addDayofWeek(l, mm);
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
    
    @Override
    protected void onClick() {
        int newValue = mClickCounter + 1;
        // Give the client a chance to ignore this change if they deem it
        // invalid
        if (!callChangeListener(newValue)) {
            // They don't want the value to be set
            return;
        }

        // Increment counter
        mClickCounter = newValue;

        // Save to persistent storage (this method will make sure this
        // preference should be persistent, along with other useful checks)
        persistInt(mClickCounter);

        // Data has changed, notify so UI can be refreshed!
        notifyChanged();
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        // This preference type's value type is Integer, so we read the default
        // value from the attributes as an Integer.
        return a.getInteger(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        if (restoreValue) {
            // Restore state
           // mClickCounter = getPersistedInt(mClickCounter);
        } else {
            // Set state
            //int value = (Integer) defaultValue;
            //mClickCounter = value;
            //persistInt(value);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        /*
         * Suppose a client uses this preference type without persisting. We
         * must save the instance state so it is able to, for example, survive
         * orientation changes.
         */

        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }

        // Save the instance state
        final SavedState myState = new SavedState(superState);
        myState.clickCounter = mClickCounter;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        // Restore the instance state
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        mClickCounter = myState.clickCounter;
        notifyChanged();
    }

    /**
     * SavedState, a subclass of {@link BaseSavedState}, will store the state
     * of MyPreference, a subclass of Preference.
     * <p>
     * It is important to always call through to super methods.
     */
    private static class SavedState extends BaseSavedState {
        int clickCounter;

        public SavedState(Parcel source) {
            super(source);

            // Restore the click counter
            clickCounter = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);

            // Save the click counter
            dest.writeInt(clickCounter);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
    
    @Override
    public View getView(final View convertView, final ViewGroup parent) {
        final View v = super.getView(convertView, parent);
        final int hieght = 670;
        final int width = 1000;
        final android.view.ViewGroup.LayoutParams params = new android.view.ViewGroup.LayoutParams(hieght, width);
        v.setLayoutParams(params );
        v.setPadding(0, 0, 0, 0);        
        return v;
    }

}
