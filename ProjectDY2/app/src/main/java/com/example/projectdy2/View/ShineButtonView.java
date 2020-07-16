package com.example.projectdy2.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.example.projectdy2.R;

import androidx.annotation.Nullable;

public class ShineButtonView extends View {
	//数据库存储
	private boolean isPress;
	private boolean isInit = false ;

	private int drawableId;
	private Bitmap bitmap;
	private Picture mPicture = new Picture();
	private Paint picPaint = new Paint();

	public ShineButtonView(Context context) {
		super(context);
		initPaint();
	}
	public ShineButtonView(Context context, @Nullable AttributeSet attrs) {
		super(context,attrs);
		initPaint();
	}
	public ShineButtonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context,attrs,defStyleAttr);
		initPaint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if ( isInit ) {
			float left = (getWidth()-bitmap.getWidth())*1.0f/2;
			float top = (getHeight()-bitmap.getHeight())*1.0f/2;

			canvas.drawBitmap(bitmap, left, top, picPaint);
		}
	}

	private void initPaint() {
		picPaint.setAntiAlias(true);
//		picPaint.set
	}

	public void loadImage(boolean id) { //false是喜欢 true是收藏
		if ( !id ) {
			BitMapMethod(R.drawable.like_button);
			drawableId = R.drawable.like_button;
		}
		else {
			BitMapMethod(R.drawable.favor_button);
			drawableId = R.drawable.favor_button;
		}
	}

	public void loadImage(int id) {
		drawableId = id;
		BitMapMethod(id);
//		VectorMethod();
//		invalidate();
	}

	private void BitMapMethod(int id) {
		isInit = true ;
		bitmap = getBitmap(getContext(),id);
		invalidate();
	}

	private void VectorMethod() {
		//		Canvas canvas = mPicture.beginRecording(getWidth(), getHeight());
//		Paint paint = new Paint();
//		paint.setColor(Color.BLUE);
//		paint.setStyle(Paint.Style.FILL);

//		canvas.draw
//		canvas.translate(250,250);
//		canvas.drawCircle(0,0,100,paint);

//		mPicture.endRecording();
	}

	private static Bitmap getBitmap(Context context,int vectorDrawableId) {
		Bitmap bitmap=null;
		Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
		assert vectorDrawable != null;
		vectorDrawable.setBounds(0,0,100,100);
//		vectorDrawable.setTint();

		bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth()*2,
				vectorDrawable.getIntrinsicHeight()*2, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		vectorDrawable.draw(canvas);

		return bitmap;
	}

	@Override
	public void setOnClickListener(@Nullable OnClickListener l) {
		super.setOnClickListener(l);
	}

	public int getDrawableId() { return drawableId; }
}
