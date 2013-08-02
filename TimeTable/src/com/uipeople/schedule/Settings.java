package com.uipeople.schedule;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.util.Pair;

public class Settings {
  public String imagePath = "";
  public int x = 0;
  public int y = 0;
  public int thema = 0;
  public int time = 8;
  public int minTime = 9;
  public int maxTime = 16;
  public int weekday = 0;
  public int revision = 1;
  public Boolean first;
  public HashMap<Subject, Subject> table;
  public String email ="";
  public String name ="";
  public String school ="";
  public String grade ="";
  public String password ="";
  public String group ="";
  public String picturePath = "";
  public String stringTable = "";
  private Context mContext;
  public Settings(Context context) {
    mContext = context;
    this.loadSettings();
  }

  private String toString(JSONObject o, String name)
  {
	  if(!o.containsKey(name))
		  return "";
	  
	  return o.get(name).toString();
  }
  public void loadSettings() {
	SharedPreferences settings = mContext.getSharedPreferences("setting", 0);

	this.minTime = settings.getInt("min_time", 9);
	this.maxTime = settings.getInt("max_time", 16);
    this.thema = settings.getInt("thema", this.thema);
    this.time = settings.getInt("time", this.time);
    this.weekday = settings.getInt("weekday", this.weekday);
    this.stringTable = settings.getString("table", "");
    this.imagePath = settings.getString("path", this.imagePath);
    this.x = settings.getInt("x", this.x);
    this.y = settings.getInt("y", this.y);
    this.first  = settings.getBoolean("first", true);
	this.email = (String) settings.getString("email", "");
	this.password = (String) settings.getString("password", "");
	this.name = (String) settings.getString("name", "");
	this.school = (String) settings.getString("school", "");
	this.grade = (String) settings.getString("grade", "");
	this.group = (String) settings.getString("group", "");
	this.picturePath = (String) settings.getString("picture", "");
	this.revision = settings.getInt("revision", 1);
    table = new HashMap<Subject, Subject>();
    
    JSONParser parser = new JSONParser();
    
    try {
    	 
		Object obj = parser.parse(stringTable);
		JSONArray array = (JSONArray) obj;
		
		Iterator<JSONObject> iterator = array.iterator();
		while (iterator.hasNext()) 
		{
			JSONObject i = iterator.next();
			Subject s = new Subject();
			if(!i.containsKey("weekday"))
				continue;
			
			s.weekday = ((Long) i.get("weekday")).intValue();
			s.time = ((Long)i.get("time")).intValue();
			s.color = ((Long) i.get("color")).intValue();
			s.subject = toString(i, "subject");
			s.teacher = toString(i, "teacher");
			s.classRoom = toString(i, "classroom");
			s.phone = toString(i, "phone");
			s.email = toString(i, "email");
			s.memo = toString(i, "memo");
			s.period = ((Long) i.get("period")).intValue();
			if(s.period == 0)
			{
				s.period = 1;
			}
			
			table.put(s, s);
		}
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (org.json.simple.parser.ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch(Exception e){
		
	}
    
  }

public void saveSettings() {
	SharedPreferences settings = mContext.getSharedPreferences("setting", 0);
    SharedPreferences.Editor editor = settings.edit();
    
    JSONArray list = new JSONArray();
    
    Iterator i = table.entrySet().iterator();
    while(i.hasNext())
    {
    	Entry entry = (Entry) i.next();
    	list.add(entry.getValue());
    }
    
    String ss = list.toJSONString();
    this.stringTable = ss;
    
    editor.putInt("thema", thema);
    editor.putInt("time", time);
    editor.putInt("weekday", weekday);
    editor.putString("table", ss);
    editor.putString("path", imagePath);
    editor.putInt("x", x);
    editor.putInt("y", y);
    editor.putBoolean("first",  false);
    editor.putString("name", name);
    editor.putString("email", email);
    editor.putString("password", password);
    editor.putString("school", school);
    editor.putString("grade", grade);
    editor.putString("group", group);	
    editor.putString("picture", picturePath);
    editor.putInt("revision", ++revision);
    editor.putInt("min_time", minTime);
    editor.putInt("max_time", maxTime);
    editor.commit();
  }
}
