package com.uipeople.schedule;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.HttpMethod;
import com.skp.openplatform.android.sdk.common.PlanetXSDKException;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.common.RequestListener;
import com.skp.openplatform.android.sdk.common.ResponseMessage;
import com.skp.openplatform.android.sdk.oauth.OAuthInfoManager;
import com.skp.openplatform.android.sdk.oauth.OAuthListener;
import com.skp.openplatform.android.sdk.oauth.PlanetXOAuthException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TLoginActivity extends Activity {
	final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;
	final static int PICK_EXISTING_PHOTO_RESULT_CODE = 1;

	String[] permissions = { "offline_access", "publish_stream", "user_photos",
			"publish_checkins", "photo_upload" };

	static public HttpContext mHttpContext;
	Settings mSettings;
	Button mBack;
	Button mLoginNateon;
	Context mContext;

	ProgressDialog mProgress = null;

	@Override
	public void onCreate(Bundle saveInstanceState) {
		mSettings = new Settings(this);
		super.onCreate(saveInstanceState);
		setContentView(R.layout.login_activity);

		LinearLayout r = (LinearLayout) findViewById(R.id.layout);
		mBack = (Button) r.findViewById(R.id.back);
		mLoginNateon = (Button) r.findViewById(R.id.login_nateon);
		mBack.setOnClickListener(BackClickListener);
		mLoginNateon.setOnClickListener(LoginNateonClickListener);
		mContext = this;

		CookieStore store = new BasicCookieStore();
		mHttpContext = new BasicHttpContext();
		mHttpContext.setAttribute(ClientContext.COOKIE_STORE, store);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private View.OnClickListener BackClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	private View.OnClickListener RegisterClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, RegisterActivity.class);
			startActivity(intent);
		}
	};

	private View.OnClickListener LoginNateonClickListener = new View.OnClickListener() {
		public static final String CLIENT_ID = "741d7c26-e1cc-3642-bbd1-1fb54704caaf";
		public static final String CLIENT_SECRET = "55b34da0-7c39-3833-84ee-ab8050773496";
		public static final String APP_KEY = "1165e2e2-8885-3d97-a5d4-fe0adfd101d4";

		@Override
		public void onClick(View v) {
			OAuthInfoManager auth = new OAuthInfoManager(mContext);
			APIRequest.setAppKey(APP_KEY);

			OAuthInfoManager.clientId = CLIENT_ID;
			OAuthInfoManager.clientSecret = CLIENT_SECRET;
			OAuthInfoManager.scope = "nateon/buddy,nateon/profile,nateon/note";

			try {
				auth.login(mContext, oauthlis);
			} catch (PlanetXOAuthException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	OAuthListener oauthlis = new OAuthListener() {
		@Override
		public void onError(String errorMessage) {
		}

		@Override
		public void onComplete(String message) {
			Toast.makeText(mContext, "Login: " + message, Toast.LENGTH_SHORT)
					.show();
			OAuthInfoManager auth = new OAuthInfoManager(mContext);
			requestASync();
			/*
			 * LoadProfileTask task = new LoadProfileTask(); mProgress =
			 * ProgressDialog.show(mContext, null, null);
			 * mProgress.setContentView(R.layout.progress); task.execute("");
			 */
		}
	};

	HashMap<String, Object> param;
	RequestBundle requestBundle;
	APIRequest api;
	public static String SERVER = "https://apis.skplanetx.com";
	String URL = SERVER + "/nateon/profile";

	public void initRequestBundle() {
		param = new HashMap<String, Object>();
		param.put("version", "1");

		requestBundle = new RequestBundle();
		requestBundle.setUrl(URL);
		requestBundle.setParameters(param);
		requestBundle.setHttpMethod(HttpMethod.GET);
		requestBundle.setResponseType(CONTENT_TYPE.JSON);
		requestBundle.setRequestType(CONTENT_TYPE.FORM);
	}

	public void requestASync() {
		api = new APIRequest();
		initRequestBundle();

		try {
			api.request(requestBundle, reqListener);
		} catch (PlanetXSDKException e) {
			e.printStackTrace();
		}
	}

	String hnsResult = "";
	String hnsName = "";
	RequestListener reqListener = new RequestListener() {

		@Override
		public void onPlanetSDKException(PlanetXSDKException e) {
		}

		@Override
		public void onComplete(ResponseMessage result) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(result.getResultMessage());
				JSONObject obj = (JSONObject) jsonObject.get("profile");
				hnsResult = obj.getString("nateId");
				hnsName = obj.getString("name");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msgHandler.sendEmptyMessage(0);
		}
	};

	Handler msgHandler = new Handler() 
	{
		public void dispatchMessage(Message msg) 
		{
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("email", hnsResult));
			param.add(new BasicNameValuePair("name", hnsName));
			param.add(new BasicNameValuePair("deviceid", MainActivity.deviceId));
			new WebServiceTask.HttpPostTask(mContext, MainActivity.url
					+ "/session", param, TaskListener).execute("");
		};
	};

	private WebServiceTask.OnTaskListener TaskListener = new WebServiceTask.OnTaskListener() 
	{
		@Override
		public void onPostExecute(WebServiceTask.Result result) 
		{
			// TODO Auto-generated method stub
			if (result.statusCode == 200) 
			{
				// revision 을 파싱하여 현재 리비젼이 높은지 확인한다.

				JSONObject jsonObject;
				try 
				{
					jsonObject = new JSONObject(result.data);
					int revision = jsonObject.getInt("revision");
					if (revision == mSettings.revision) 
					{
						Intent intent = new Intent(mContext,
								FriendActivity.class);
						startActivity(intent);
						
						// 서버 리비전이 높으면 바로 친구 목록창을 띄운
					} else if(revision > mSettings.revision)
					{
						showDialog();
					}
					else
					{
						uploadProfile();
					}
				} catch (Exception e) 
				{

				}
			} else 
			{
				Toast.makeText(mContext,
						getResources().getString(R.string.confirm_email), 10)
						.show();
			}
		}
	};
	
	private void showDialog()
	{
		DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
		  		dialog.dismiss();
		  		
		  		loadProfile();
			}
		};
		
		DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
				uploadProfile();
			}
		};
		
		new AlertDialog.Builder(this)
			.setTitle(R.string.check_update)
			.setPositiveButton(R.string.get_timetable_from_server, ok)
			.setNegativeButton(R.string.upload_timetable, cancel)
			.show();
	}
	
	void loadProfile()
	{
		new WebServiceTask.HttpGetTask(mContext, MainActivity.url
				+ "/users/load", LoadProfileListener).execute("");
	}

	void uploadProfile() 
	{
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("email", hnsResult));
		param.add(new BasicNameValuePair("name", hnsName));
		param.add(new BasicNameValuePair("school", mSettings.school));
		param.add(new BasicNameValuePair("grade", mSettings.grade));
		param.add(new BasicNameValuePair("group", mSettings.group));
		param.add(new BasicNameValuePair("image", toBase64()));
		param.add(new BasicNameValuePair("revision", Integer
				.toString(mSettings.revision)));
		param.add(new BasicNameValuePair("subjects", mSettings.stringTable));

		new WebServiceTask.HttpPostTask(mContext, MainActivity.url
				+ "/users/sync", param, SyncListener).execute("");
	}

	private WebServiceTask.OnTaskListener SyncListener = new WebServiceTask.OnTaskListener() 
	{
		@Override
		public void onPostExecute(WebServiceTask.Result result) {
			// TODO Auto-generated method stub
			if (result.statusCode == 200) {
				Intent intent = new Intent(mContext, FriendActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(mContext,
						getResources().getString(R.string.confirm_email), 10)
						.show();
			}
		}
	};
	
	private WebServiceTask.OnTaskListener LoadProfileListener = new WebServiceTask.OnTaskListener() 
	{
		@Override
		public void onPostExecute(WebServiceTask.Result result) {
			// TODO Auto-generated method stub
			if (result.statusCode == 200) {
				MyProfile table = MyProfile.fromJson(result.data);
				mSettings.table = table.subjects;
				mSettings.revision = table.revision;
				mSettings.saveSettings();
				Intent intent = new Intent(mContext, FriendActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(mContext,
						getResources().getString(R.string.confirm_email), 10)
						.show();
			}
		}
	};


	private String toBase64() {
		Bitmap image = BitmapFactory.decodeFile(mSettings.picturePath);
		if (image == null)
			return "";

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 90, outStream);
		byte[] data = outStream.toByteArray();
		return Base64.encodeToString(data, 0);
	}

	private View.OnClickListener FindPasswordClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}
	};
}
