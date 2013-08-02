package com.uipeople.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.HttpMethod;
import com.skp.openplatform.android.sdk.common.PlanetXSDKException;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.common.RequestListener;
import com.skp.openplatform.android.sdk.common.ResponseMessage;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class NateOnAPI 
{
	APIRequest 					api;
	RequestBundle 				requestBundle;
	
	String 						URL = Const.SERVER + "/nateon/buddies";
	Map<String, Object> 		param;
	OnLoadFriendListListener	mListener;
	OnSendMessageListener		mSendMessageListener;
	Context						mContext;
	
	static public class Result
    {
    	public String statusCode;
    	public String	data;
    }
	
	public abstract interface OnLoadFriendListListener
	{
		public abstract void onPostExecute(String statusCode, ArrayList<FriendData> friends);
	}
	
	public abstract interface OnSendMessageListener
	{
		public abstract void onPostExecute(String statusCode, String data);
	}
	
	public void loadFriendList(OnLoadFriendListListener listener)
	{
		mListener = listener;
		requestSync();
	}
	
	public void sendMessage(String receiver, String message, OnSendMessageListener listener)
	{
		mSendMessageListener = listener;
		
		param = new HashMap<String, Object>();
		param.put("version", "1");
		param.put("receivers", "ttl2172@nate.com");
		param.put("message", message);
		param.put("confirm", "N");
		
		requestBundle = new RequestBundle();
		requestBundle.setUrl(Const.SERVER + "/nateon/notes");
		requestBundle.setParameters(param);
		requestBundle.setHttpMethod(HttpMethod.POST);
		requestBundle.setResponseType(CONTENT_TYPE.JSON);
		requestBundle.setRequestType(CONTENT_TYPE.FORM);
		
		api = new APIRequest();
		
		try {
			api.request(requestBundle, reqSendMessageListener);
		} catch (PlanetXSDKException e) {
			e.printStackTrace();
		}
	}
	
	private void initRequestBundle()
	{
		param = new HashMap<String, Object>();
		param.put("version", "1");
		param.put("order", "name.asc");
		param.put("page", 1);
		param.put("count", 50);
		
		requestBundle = new RequestBundle();
		requestBundle.setUrl(URL);
		requestBundle.setParameters(param);
		requestBundle.setHttpMethod(HttpMethod.GET);
		requestBundle.setResponseType(CONTENT_TYPE.JSON);
		requestBundle.setRequestType(CONTENT_TYPE.FORM);
	}
	
	private void requestSync()
	{
		api = new APIRequest();
		initRequestBundle();
		
		try {
			api.request(requestBundle, reqListener);
		} catch (PlanetXSDKException e) {
			e.printStackTrace();
		}
	}
	
	Result mResult = new Result();
	RequestListener reqListener = new RequestListener() 
	{
		@Override
		public void onPlanetSDKException(PlanetXSDKException e) 
		{
		}
		
		@Override
		public void onComplete(ResponseMessage result) 
		{
			mResult.data = result.getResultMessage();
			mResult.statusCode = result.getStatusCode();
			msgHandler.sendEmptyMessage(0);
		}
	};			
	
	RequestListener reqSendMessageListener = new RequestListener() 
	{
		@Override
		public void onPlanetSDKException(PlanetXSDKException e) 
		{
		}
		
		@Override
		public void onComplete(ResponseMessage result) 
		{
			mResult.data = result.getResultMessage();
			mResult.statusCode = result.getStatusCode();
			msgHandler2.sendEmptyMessage(0);
		}
	};			
	
	Handler msgHandler = new Handler()
	{
		public void dispatchMessage(Message msg) 
		{
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(mResult.data);
				JSONObject obj = (JSONObject) jsonObject.get("nateon");
				obj = (JSONObject)obj.get("buddies");
				JSONArray buddies = (JSONArray)obj.get("buddy");
				
				ArrayList<FriendData> friendArray = new ArrayList<FriendData>();
				
				for(int i = 0; i < buddies.length(); i++)
				{
					JSONObject buddy = (JSONObject)buddies.get(i);
					String name = buddy.getString("name");
					String nateId = buddy.getString("nateId");
					FriendData friend = new FriendData();
					friend.email = nateId;
					friend.name = name;

					friendArray.add(friend);
				}
				
				mListener.onPostExecute(mResult.statusCode, friendArray);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
	};
	
	Handler msgHandler2 = new Handler()
	{
		public void dispatchMessage(Message msg) 
		{
			mSendMessageListener.onPostExecute(mResult.statusCode, mResult.data);
		};
	};
}
