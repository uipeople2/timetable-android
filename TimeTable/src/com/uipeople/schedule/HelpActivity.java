package com.uipeople.schedule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HelpActivity extends Activity {
	
	Button	mBackButton;
	Button	mFirstButton;
	Button	mMenuButton;
	Button	mEditDeleteButton;
	Button	mBackgroundButton;
	Context	mContext;
	
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		mContext = this;
		super.onCreate(saveInstanceState);
		setContentView(R.layout.help_activity);
		mBackButton = (Button)findViewById(R.id.back);
		mFirstButton = (Button)findViewById(R.id.first);
		mMenuButton = (Button)findViewById(R.id.menu);
		mBackgroundButton = (Button)findViewById(R.id.background);
		mEditDeleteButton = (Button)findViewById(R.id.edit);
		
		mBackButton.setOnClickListener(BackClickListener);
		mFirstButton.setOnClickListener(FirstClickListener);
		mMenuButton.setOnClickListener(MenuClickListener);
		mBackgroundButton.setOnClickListener(BackgroundClickListener);
		mEditDeleteButton.setOnClickListener(EditDeleteClickListener);
	}
	
	private View.OnClickListener BackClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		finish();
  	}
    };
    
    private View.OnClickListener FirstClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		Intent intent = new Intent(mContext, HelpFirstActivity.class);
		startActivity(intent);
  	}
    };
    
    private View.OnClickListener MenuClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		Intent intent = new Intent(mContext, HelpMenuActivity.class);
		startActivity(intent);
  	}
    };
    
    private View.OnClickListener BackgroundClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		Intent intent = new Intent(mContext, HelpBackgroundActivity.class);
		startActivity(intent);
  	}
    };
    
    private View.OnClickListener EditDeleteClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		Intent intent = new Intent(mContext, HelpEditDeleteActivity.class);
		startActivity(intent);
  	}
    };
}
