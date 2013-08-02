package com.uipeople.schedule;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;

public class UserData {
	public String name;
	public String school;
	public String email;
	public String grade;
	public String group;
	public ArrayList<FriendData> friends;
	public Bitmap image;
	
	public ArrayList<FriendData> toFriendList(int type)
	{
		ArrayList<FriendData> list = new ArrayList<FriendData>();
		
		for(FriendData f : friends)
		{
			if(f.status == type)
			{
				list.add(f);
			}
		}
		
		return list;
	}
	
	public ArrayList<FriendData> toRequestedList()
	{
		ArrayList<FriendData> list = new ArrayList<FriendData>();
		
		for(FriendData f : friends)
		{
			if(f.status == 1 || f.status == 2 )
			{
				list.add(f);
			}
		}
		
		return list;
	}
	
	public static UserData ConvertJsonToArray(String json)
	{
		UserData user = new UserData();
		user.friends = new ArrayList<FriendData>();
		try
		{
			JSONObject obj = new JSONObject(json);
			user.email = obj.getString("email");
			user.name = obj.getString("name");
			if(!obj.isNull("school"))
			user.school = obj.getString("school");
			if(!obj.isNull("grade"))
			user.grade = obj.getString("grade");
			if(!obj.isNull("group"))
			user.group = obj.getString("group");
			
			if(!obj.isNull("friends"))
			{
				JSONArray friends = obj.getJSONArray("friends");
				
				for(int i =0; i < friends.length(); i++)
				{
					JSONObject o = (JSONObject)friends.get(i);
					FriendData friend = new FriendData();
				
					friend.email = o.getString("email");
					friend.name = o.getString("name");
					friend.status = o.getInt("status");
					
					if(!o.isNull("school"))
					friend.school = o.getString("school");
					if(!o.isNull("grade"))
					friend.grade = o.getString("grade");
					if(!o.isNull("group"))
					friend.group = o.getString("group");
					if(!o.isNull("image"))
					{
						String data = o.getString("image");
						ByteArrayInputStream inStream = new ByteArrayInputStream(Base64.decode(data, 0));
						friend.image = BitmapFactory.decodeStream(inStream);
					}
					user.friends.add(friend);
				}
			}
		} catch (Exception e)
		{
			
		}
		
		return user;
	}
	
	public static ArrayList<FriendData> ConvertJsonToList(String json)
	{
		ArrayList<FriendData> users = new ArrayList<FriendData>();
		
		try
		{
			JSONArray obj = new JSONArray(json);
			
			for(int i = 0; i < obj.length(); i++)
			{
				FriendData user = new FriendData();
				
				JSONObject o = (JSONObject)obj.get(i);
				user.email = o.getString("email");
				if(!o.isNull("name"))
				user.name = o.getString("name");
				if(!o.isNull("status"))
				user.status = o.getInt("status");
				if(!o.isNull("school"))
				user.school = o.getString("school");
				if(!o.isNull("grade"))
				user.grade = o.getString("grade");
				if(!o.isNull("group"))
				user.group = o.getString("group");
				if(!o.isNull("image"))
				{
					try
					{
					String data = o.getString("image");
					ByteArrayInputStream inStream = new ByteArrayInputStream(Base64.decode(data, 0));
					user.image = BitmapFactory.decodeStream(inStream);
					} catch( Exception ee)
					{
						continue;
					}
				}
				
				users.add(user);
			}
		} catch (Exception e)
		{
			
		}
		
		return users;
	}
}
