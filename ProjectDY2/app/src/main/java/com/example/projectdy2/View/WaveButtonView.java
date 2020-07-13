package com.example.projectdy2.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.example.projectdy2.R;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

//控件用于定义一个带字的波浪控件
//选中会有波浪动画
//未选中会显示灰色
public class WaveButtonView extends View {

	private boolean isPressed;
	private String textString = "temp";
	float waveProgress;

	private Path wavePath = new Path();
	private Paint textPaint = new Paint();
	private Paint wavePaint = new Paint();

	public WaveButtonView(Context context) {
		super(context);
		init();
	}
	public WaveButtonView(Context context, @Nullable AttributeSet attrs) {
		super(context,attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveButtonView);
		textString = typedArray.getString(R.styleable.WaveButtonView_textString);
		isPressed = typedArray.getBoolean(R.styleable.WaveButtonView_isPressed,false);
		waveProgress = typedArray.getFloat(R.styleable.WaveButtonView_waveProgress,0.f);
		init();
	}
	public WaveButtonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context,attrs,defStyleAttr);
		TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.WaveButtonView);
		textString = typedArray.getString(R.styleable.WaveButtonView_textString);
		isPressed = typedArray.getBoolean(R.styleable.WaveButtonView_isPressed,false);
		waveProgress = typedArray.getFloat(R.styleable.WaveButtonView_waveProgress,0.f);
		init();
	}

	private void init() {
		setBackgroundColor(0x00000000);

		textPaint.setAntiAlias(true);
		textPaint.setTextSize(50);
		textPaint.setColor(getResources().getColor(R.color.colorLightThemeBlue));
		textPaint.setTextAlign(Paint.Align.CENTER);

		wavePaint.setAntiAlias(true);
		wavePaint.setColor(getResources().getColor(R.color.WaveButtonWave));
	}

	public void setTextString(String text) {
		textString = text; invalidate();
	}

	//两种绘画猜想
	@Override
	protected void onDraw(Canvas canvas) {
		if ( isPressed ) {//drawWave
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
				//第一种方式，透明着色背景
				WaveDrawMethod1(canvas);
			}else {
				//第二种方式，反色着色背景
				WaveDrawMethod2(canvas);
			}
		} else {
			int top = getTop();
			int left = getLeft();
			int right = getRight();
			int bottom = getBottom();

			textPaint.setColor(getResources().getColor(R.color.WaveButtonNotPress));

			Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
			int baseLine = (int) ((bottom+top)/2+(fontMetrics.bottom-fontMetrics.top)/2-fontMetrics.bottom);
			canvas.drawText(textString , (right-left)*1.0f/2 , baseLine , textPaint);
		}
	}

	final int waveNum = 3;

	private void WaveDrawMethod1(Canvas canvas) {
		int top = getTop();
		int left = getLeft();
		int right = getRight();
		int bottom = getBottom();

		//第一步画字
		textPaint.setColor(getResources().getColor(R.color.WaveButtonPress));

		Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
		int baseLine = (int) ((bottom+top)/2+(fontMetrics.bottom-fontMetrics.top)/2-fontMetrics.bottom);
		canvas.drawText(textString , (right-left)*1.0f/2 , baseLine , textPaint);

		//第二步画波浪
		final float waveHeight = (bottom-top)*1.0f/10 ;
		final float waveWidth = (right-left)*1.0f/waveNum ;

		wavePath.moveTo(0, (bottom-top)*1.0f/2 );
		for ( float i = left ; i < right ; i += waveWidth) {
			wavePath.rQuadTo(waveWidth/4,-waveHeight,waveWidth/2,0);
			wavePath.rQuadTo(waveWidth/4,waveHeight,waveWidth/2,0);
		}
		wavePath.rLineTo(0,(bottom-top));
		wavePath.rLineTo(-(right-left)*1.0f,0);
		wavePath.rLineTo(0,-(bottom-top));
		wavePath.close();

		canvas.drawPath(wavePath,wavePaint);
	}
	@RequiresApi(api = Build.VERSION_CODES.O)
	private void WaveDrawMethod2(Canvas canvas) {
		int top = getTop();
		int left = getLeft();
		int right = getRight();
		int bottom = getBottom();
		wavePath.reset();

		//第一步画波浪
		final float waveHeight = (bottom-top)*1.0f/10 ;
		final float waveWidth = (right-left)*1.0f/waveNum ;

		wavePath.moveTo((-2+waveProgress)*waveWidth, (bottom-top)*1.0f/2 );
		for ( float i = left-2*waveWidth ; i < right ; i += waveWidth) {
			wavePath.rQuadTo(waveWidth/4,-waveHeight,waveWidth/2,0);
			wavePath.rQuadTo(waveWidth/4,waveHeight,waveWidth/2,0);
		}
		wavePath.rLineTo(0,(bottom-top));
		wavePath.rLineTo(-waveWidth*(waveNum+2),0);
		wavePath.rLineTo(0,-(bottom-top));
		wavePath.close();
		canvas.drawPath(wavePath,wavePaint);

		//第二步画字（为了覆盖上面波浪，所以顺序要调换）
		textPaint.setColor(0xFFFFFFFF);

		Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
		int baseLine = (int) ((bottom+top)/2+(fontMetrics.bottom-fontMetrics.top)/2-fontMetrics.bottom);
		canvas.drawText(textString , (right-left)*1.0f/2 , baseLine , textPaint);

		canvas.clipOutPath(wavePath);

		textPaint.setColor(getResources().getColor(R.color.WaveButtonPress));
		canvas.drawText(textString , (right-left)*1.0f/2 , baseLine , textPaint);
	}

	public void setIsPressed(boolean p) { isPressed = p ; }
	public boolean getIsPressed() { return isPressed ; }

	public void setWaveProgress(float p) { waveProgress = p ; invalidate(); }

}
