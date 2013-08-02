package com.skplanet.openplatform.testproject;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.BaseActivity;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.HttpMethod;
import com.skp.openplatform.android.sdk.common.PlanetXSDKException;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.common.RequestListener;
import com.skp.openplatform.android.sdk.common.ResponseMessage;

public class CyworldNoteWriteArticle extends BaseActivity implements OnClickListener {

	//API Call
	APIRequest api;
	RequestBundle requestBundle;
	
	final String USERID = "67324899";
	
	// Comm Data
	String URL = Const.SERVER + "/cyworld/note/"+USERID+"/items";
	Map<String, Object> param;

	// UI
	Button btnASync;
	Button btnSync;
	TextView tvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.clogwritearticle);

		initUI();
		allocListener();

	}

	public void initUI() {
		btnASync = (Button) findViewById(R.id.DETAIL_BTN_ASYNC);
		btnSync = (Button) findViewById(R.id.DETAIL_BTN_SYNC);
		tvResult = (TextView) findViewById(R.id.DETAIL_TV_RESULT);

	}

	public void allocListener() {
		btnASync.setOnClickListener(this);
		btnSync.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		switch (id) {
		case R.id.DETAIL_BTN_ASYNC: {
			clearResult();
			requestASync();
			break;
		}
		case R.id.DETAIL_BTN_SYNC: {
			clearResult();
			requestSync();
			break;
		}
		default:
			break;
		}

	}
	
	
	public void initRequestBundle()
	{
		param = new HashMap<String, Object>();
		param.put("version", "1");
		param.put("contents", "Content Test Value");
		param.put("attachId", "");
		
		requestBundle = new RequestBundle();
		requestBundle.setUrl(URL);
		requestBundle.setParameters(param);
		requestBundle.setHttpMethod(HttpMethod.POST);
		requestBundle.setRequestType(CONTENT_TYPE.FORM);
		requestBundle.setResponseType(CONTENT_TYPE.XML);
		
	}
	
	public void requestSync()
	{
		api = new APIRequest();
		initRequestBundle();
		
		ResponseMessage result = new ResponseMessage();
		try {
			result = api.request(requestBundle);
			setResult(result.getStatusCode() + "\n" + result.toString());
		} catch (PlanetXSDKException e) {
			setResult(e.toString());
		}
	}
	
	public void clearResult()
	{
		setResult("");
	}
	
	public void setResult(String result)
	{
		tvResult.setText(result);
	}
	
	public void requestASync()
	{
		api = new APIRequest();
		initRequestBundle();
		
		try {
			api.request(requestBundle, reqListener);
		} catch (PlanetXSDKException e) {
			e.printStackTrace();
		}
	}
	
	
	String hndResult = "";
	
	Handler msgHandler = new Handler(){
		public void dispatchMessage(Message msg) {
			setResult(hndResult);
		};
	};
	
	RequestListener reqListener = new RequestListener() {
		
		@Override
		public void onPlanetSDKException(PlanetXSDKException e) {
			hndResult = e.toString();
			msgHandler.sendEmptyMessage(0);
		}
		
		@Override
		public void onComplete(ResponseMessage result) {
			hndResult = result.getStatusCode() + "\n" + result.toString();
			msgHandler.sendEmptyMessage(0);
		}
	};
	
	
}
