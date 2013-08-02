package com.uipeople.schedule;

import org.json.simple.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable; 

public class Subject implements Parcelable
{
	public Integer weekday = 0;
	public Integer time = 0;
	public Integer period = 1;
	public String subject = "";
	public Integer color = 0;
	public String teacher = "";
	public String classRoom = "";
	public String phone = "";
	public String email = ""; 
	public Boolean edit = false;
	public String memo = "";

	public Subject()
	{
		super();
	}
	
	public Subject(Parcel in) 
	{
         super(); 
         readFromParcel(in);
	}
	
	public static final Parcelable.Creator<Subject> CREATOR = new Parcelable.Creator<Subject>() 
	{
		public Subject createFromParcel(Parcel in) 
		{
			return new Subject(in);
		}

		public Subject[] newArray(int size) 
		{
			return new Subject[size];
		}
    };
     
	@Override
	public String toString()
	{
		JSONObject obj = new JSONObject();
		obj.put("weekday", weekday);
		obj.put("time", time);
		obj.put("subject", subject);
		obj.put("color", color);
		obj.put("teacher", teacher);
		obj.put("phone", phone);
		obj.put("classroom", classRoom);
		obj.put("email", email);
		obj.put("period", period);
		obj.put("memo", memo);
		return obj.toJSONString();
	}
	
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;

        Subject s = (Subject)obj;
        
        return time == s.time && weekday == s.weekday; 
    }

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(email);
		arg0.writeString(teacher);
	}
	
	public void readFromParcel(Parcel in) {
		email = in.readString();
		teacher = in.readString();
      }

}