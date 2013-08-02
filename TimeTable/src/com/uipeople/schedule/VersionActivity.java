package com.uipeople.schedule;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class VersionActivity extends Activity {
	
	Button	mBackButton;
	
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		setContentView(R.layout.version_activity);
		mBackButton = (Button)findViewById(R.id.back);
		mBackButton.setOnClickListener(BackClickListener);
	}
	
	private View.OnClickListener BackClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		finish();
  	}
    };
}
