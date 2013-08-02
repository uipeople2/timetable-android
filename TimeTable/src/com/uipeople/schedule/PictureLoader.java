package com.uipeople.schedule;

import java.io.File;
import java.io.IOException;
import java.util.EventListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class PictureLoader {

	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private AlertDialog alert = null;
	private Context mContext;
	private Activity mParent;
	private String mPrefix;
	private Uri mImageCaptureUrl;
	private int mWidth;
	private int mHeight;
	private OnEvent	mEvent;
	
	private void showDialog(Context context, String prefixFilename, int width, int height, OnEvent event)
	{
		mPrefix = prefixFilename;
		mWidth = width;
		mHeight = height;
		mEvent = event;
		
		if(mPrefix.length() == 0)
		{
			mPrefix = "tmp_";
		}
		
		mParent = (Activity)context;
		DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("");
		builder.setNegativeButton(R.string.cancel, cancel);
		LayoutInflater factory = LayoutInflater.from(context);
		final View customView = factory.inflate(R.layout.background_dialog, null);
		builder.setView(customView);
		alert = builder.create();
		mContext = context;
		
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
	
	private void doTakeOrigin()
	{
		mEvent.onEvent(0);
	}
    
	private void doTakePhotoAction()
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		String url = mPrefix + String.valueOf(System.currentTimeMillis()) + ".jpg";
		mImageCaptureUrl = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
		
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUrl);
		mParent.startActivityForResult( intent, PICK_FROM_CAMERA);
	}
	
	private void doTakeAlbumAction()
	{
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);

    	mParent.startActivityForResult( Intent.createChooser(intent, "Select Picture"), PICK_FROM_ALBUM );
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
