package com.bytedance.videoplayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView {
	public float width1;
	public float height1;

	String TAG = "测试：";

	public MySurfaceView(Context context) {
		super(context);
	}

	public void setMeasure(float width,float height){
		this.width1 = width;
		this.height1 = height;
	}

	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		if ( this.width1 != 0 ){
			Log.d(TAG, "width : " + width1 + "  height : " + height1);
			width = (int) width1;
			height = (int) height1;
		}
//		else {
//			width = 100 ;
//			height = 100 ;
//		}
		setMeasuredDimension(width, height);
	}
}