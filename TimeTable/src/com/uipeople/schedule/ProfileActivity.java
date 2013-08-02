package com.uipeople.schedule;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

//로긴 시도후 계정정보가 없거나 로길실패면 로긴 액티비티 띄움
public class ProfileActivity extends Activity
{
	Settings 	mSettings;
	Button		mBack;
	Button 		mSave;
	Context		mContext;
	EditText	mName;
	EditText	mSchool;
	EditText	mGrade;
	EditText	mGroup;
	ImageView	mPicture;
	
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		mSettings = new Settings(this);
		super.onCreate(saveInstanceState);
		setContentView(R.layout.profile_activity); 
		
		LinearLayout r = (LinearLayout)findViewById(R.id.layout);
		mBack = (Button)r.findViewById(R.id.back);
		mSave = (Button)r.findViewById(R.id.save);
		mName = (EditText)r.findViewById(R.id.name);
		mSchool = (EditText)r.findViewById(R.id.school);
		mGrade = (EditText)r.findViewById(R.id.grade);
		mGroup = (EditText)r.findViewById(R.id.group);
		mPicture = (ImageView)r.findViewById(R.id.my_picture);
		
		mBack.setOnClickListener(BackClickListener);
		mSave.setOnClickListener(SaveClickListener);
		mPicture.setOnClickListener(PictureClickListener);
		mContext = this;
		
		loadProfile();
	}
	
	private void loadProfile()
	{
		mName.setText(mSettings.name);
		mSchool.setText(mSettings.school);
		mGrade.setText(mSettings.grade);
		mGroup.setText(mSettings.group);
		
		if(mSettings.picturePath.length() > 0)
	    {
			Bitmap selectedImage = BitmapFactory.decodeFile(mSettings.picturePath);
			mPicture.setImageBitmap(selectedImage);
	    }
	}
	
	private void saveProfile()
	{
		mSettings.name = mName.getText().toString();
		mSettings.school = mSchool.getText().toString();
		mSettings.grade = mGrade.getText().toString();
		mSettings.group = mGroup.getText().toString();
		mSettings.saveSettings();
	}
	
	private View.OnClickListener PictureClickListener = new OnClickListener() 
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog();
		}
	};
	
	private View.OnClickListener BackClickListener = new View.OnClickListener() 
    {
  	@Override
  	public void onClick(View v) 
  	{
  		finish();
  	}
    };
    
	private View.OnClickListener SaveClickListener = new View.OnClickListener() 
    {
		@Override
	  	public void onClick(View v) 
	  	{
			saveProfile();
			finish();
	  	}
    };

	private void showDialog()
	{
		DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("");
		builder.setNegativeButton(R.string.cancel, cancel);
		LayoutInflater factory = LayoutInflater.from(this);
		final View customView = factory.inflate(R.layout.background_dialog, null);
		builder.setView(customView);
		alert = builder.create();
		
		customView.findViewById(R.id.camera).setOnClickListener( new OnClickListener()
		{
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				alert.dismiss();
				doTakePhotoAction();
			}
		});
		
		customView.findViewById(R.id.album).setOnClickListener( new OnClickListener()
		{
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				alert.dismiss();
				doTakeAlbumAction();
			}
		});
		
		customView.findViewById(R.id.origin).setOnClickListener( new OnClickListener()
		{
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				alert.dismiss();
				doTakeOrigin();
			}
		});
		
		alert.show();
	}
	private AlertDialog alert = null;
	private Uri	mImageCaptureUri;
	
	private void doTakeOrigin()
	{
		String path = getTempUri().getPath();
		Bitmap selectedImage = BitmapFactory.decodeFile(path);
		mPicture.setImageResource(R.drawable.photo);
		mSettings.picturePath = "";
	}
    
	private void doTakePhotoAction()
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
		
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
		startActivityForResult( intent, MainActivity.PICK_FROM_CAMERA);
	}
	
	private void doTakeAlbumAction()
	{
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);

    	startActivityForResult( Intent.createChooser(intent, "Select Picture"), MainActivity.PICK_FROM_ALBUM );
	}
	
	protected void onActivityResult( int requestCode, int resultCode, Intent data )
	{
		if(resultCode != RESULT_OK)
			return;
		
		switch(requestCode)
		{
			case MainActivity.CROP_FROM_CAMERA:
			{
				String path = getTempUri().getPath();
				Bitmap selectedImage = BitmapFactory.decodeFile(path);
				mPicture.setImageBitmap(selectedImage);
				mSettings.picturePath = path;
				break;
			}
			
			case MainActivity.PICK_FROM_ALBUM:
			{
				mImageCaptureUri = data.getData();
				callCrop();
				break;
			}
			
			case MainActivity.PICK_FROM_CAMERA:
			{
				callCrop();
				break;
			}
		}
	}
	
	private void callCrop()
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(mImageCaptureUri,  "image/*");

		intent.putExtra("scale",  true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
		intent.putExtra("outputX",  mPicture.getWidth());
		intent.putExtra("outputY",  mPicture.getHeight());
		intent.putExtra("aspectX",  mPicture.getWidth());
		intent.putExtra("aspectY",  mPicture.getHeight());
		startActivityForResult(intent, MainActivity.CROP_FROM_CAMERA);
	}
	    
	private Uri getTempUri() 
	{
		File f = new File(Environment.getExternalStorageDirectory(),"temp.jpg");
		
		try
		{
			f.createNewFile();	
		} catch(IOException e)
		{
			
		}
		
		return Uri.fromFile(f);
	}
}

