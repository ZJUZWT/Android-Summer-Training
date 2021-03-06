package com.example.app4.Clock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.example.app4.Time;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Clock3View extends View {
	private static final int FULL_CIRCLE_DEGREE = 360;
	private static final int UNIT_DEGREE = 6;

	private static final float UNIT_LINE_WIDTH = 8; // 刻度线的宽度
	private static final int HIGHLIGHT_UNIT_ALPHA = 0xFF;
	private static final int NORMAL_UNIT_ALPHA = 0x80;

	private static final float HOUR_NEEDLE_LENGTH_RATIO = 0.4f; // 时针长度相对表盘半径的比例
	private static final float MINUTE_NEEDLE_LENGTH_RATIO = 0.6f; // 分针长度相对表盘半径的比例
	private static final float SECOND_NEEDLE_LENGTH_RATIO = 0.8f; // 秒针长度相对表盘半径的比例
	private static final float HOUR_NEEDLE_WIDTH = 12; // 时针的宽度
	private static final float MINUTE_NEEDLE_WIDTH = 8; // 分针的宽度
	private static final float SECOND_NEEDLE_WIDTH = 4; // 秒针的宽度

	private Calendar calendar = Calendar.getInstance();

	private float radius = 0; // 表盘半径
	private float centerX = 0; // 表盘圆心X坐标
	private float centerY = 0; // 表盘圆心Y坐标

	private List<PointF> unitLinePositions = new ArrayList<>();
	private Paint unitPaint = new Paint();
	private Paint needlePaint = new Paint();
	private Paint needlePadPaint = new Paint();
	private Paint numberPaint = new Paint();

	private Handler handler = new Handler();
	private Time debugTime = new Time(0,0,0,0);

	private String TAG = "CLOCK1" ;

	public Clock3View(Context context) {
		super(context);
		init();
	}

	public Clock3View(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Clock3View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		unitPaint.setAntiAlias(true);
		unitPaint.setColor(Color.WHITE);
		unitPaint.setStrokeWidth(UNIT_LINE_WIDTH);
		unitPaint.setStrokeCap(Paint.Cap.ROUND);
//		unitPaint.setStyle(Paint.Style.STROKE);
		unitPaint.setStyle(Paint.Style.FILL);

		// TODO 设置绘制时、分、秒针的画笔: needlePaint
		needlePaint.setAntiAlias(true);
		needlePaint.setStrokeCap(Paint.Cap.ROUND);
		needlePaint.setStyle(Paint.Style.FILL);

		needlePadPaint.setAntiAlias(true);
//		needlePadPaint.setStyle(P);

		// TODO 设置绘制时间数字的画笔: numberPaint
		numberPaint.setAntiAlias(true);
//		numberPaint.set
//		Handler
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		configWhenLayoutChanged();
	}

	private void configWhenLayoutChanged() {
		float newRadius = Math.min(getWidth(), getHeight()) / 2f;
		if (newRadius == radius) {
			return;
		}
		radius = newRadius;
		centerX = getWidth() / 2f;
		centerY = getHeight() / 2f;

		// 当视图的宽高确定后就可以提前计算表盘的刻度线的起止坐标了
		for (int degree = -90; degree < FULL_CIRCLE_DEGREE-90; degree += UNIT_DEGREE) {
			double radians = Math.toRadians(degree);
			float X = (float) (centerX + radius * 0.93f * Math.cos(radians));
			float Y = (float) (centerY + radius * 0.93f * Math.sin(radians));
			unitLinePositions.add(new PointF(X,Y));
		}
	}

	@Override
	protected void onDraw(final Canvas canvas) {
//		Log.d(TAG, "onDraw: ");

		super.onDraw(canvas);
//		drawUnit(canvas);
		drawUnit(canvas,getCurrentTime());
//		drawTimeNeedles(canvas,getCurrentTime());
//		debugTime.addSecond();
		drawTimeNeedles(canvas,getCurrentTime());
		drawTimeNumbers(canvas,getCurrentTime());
		// TODO 实现时间的转动，每一秒刷新一次
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				invalidate();
			}
		},10) ;
	}

	// 绘制表盘上的刻度
	float animationRadius = 40;
	private void drawUnit(Canvas canvas,Time time) {
		int second = time.getSeconds();
		float millisecond = (float)time.getMilliseconds();

		for (int i = 0; i < unitLinePositions.size(); i++) {
			PointF linePosition = unitLinePositions.get(i);

			float radius ;
			float cx = linePosition.x;
			float cy = linePosition.y;
			float dx;
			float dy;
			if (i % 5 == 0) {
				unitPaint.setAlpha(HIGHLIGHT_UNIT_ALPHA);
				radius = 10 ;
			} else {
				unitPaint.setAlpha(NORMAL_UNIT_ALPHA);
				radius = 7;
			}
			if ( (i == (second+1)%60 && millisecond > 500) || (i == second && millisecond < 500)) {
				float deltaX = cx-centerX;
				float deltaY = cy-centerY;

				dx = deltaX/(float) Math.sqrt(deltaX*deltaX+deltaY*deltaY)*animationRadius*(float)Math.sin((500-(i-second)*1000+millisecond)/1000.0f*Math.PI) ;
				dy = deltaY/(float) Math.sqrt(deltaX*deltaX+deltaY*deltaY)*animationRadius*(float)Math.sin((500-(i-second)*1000+millisecond)/1000.0f*Math.PI) ;
			} else {
				dx = 0 ;
				dy = 0 ;
			}

			canvas.drawCircle(cx+dx,cy+dy,radius,unitPaint);
		}
	}

	private void drawTimeNeedles(Canvas canvas,Time time) {
//		Time time = getCurrentTime();
		int hour = time.getHours();
		int minute = time.getMinutes();
		int second = time.getSeconds();
		int millisecond = time.getMilliseconds();
		// TODO 根据当前时间，绘制时针、分针、秒针
		/**
		 * 思路：
		 * 1、以时针为例，计算从0点（12点）到当前时间，时针需要转动的角度
		 * 2、根据转动角度、时针长度和圆心坐标计算出时针终点坐标（起始点为圆心）
		 * 3、从圆心到终点画一条线，此为时针
		 * 注1：计算时针转动角度时要把时和分都得考虑进去
		 * 注2：计算坐标时需要用到正余弦计算，请用Math.sin()和Math.cos()方法
		 * 注3：Math.sin()和Math.cos()方法计算时使用不是角度而是弧度，所以需要先把角度转换成弧度，
		 *     可以使用Math.toRadians()方法转换，例如Math.toRadians(180) = 3.1415926...(PI)
		 * 注4：Android视图坐标系的0度方向是从圆心指向表盘3点方向，指向表盘的0点时是-90度或270度方向，要注意角度的转换
		 */

		//绘制秒针
		float secondDegree = second*1.0f/60*360 + millisecond*1.0f/1000*6-90;
		float secondEndX = (float) (centerX + radius * SECOND_NEEDLE_LENGTH_RATIO * Math.cos(Math.toRadians(secondDegree)));
		float secondEndY = (float) (centerY + radius * SECOND_NEEDLE_LENGTH_RATIO * Math.sin(Math.toRadians(secondDegree)));
		needlePaint.setColor(0xCCCCCCFF);
		needlePaint.setStrokeWidth(SECOND_NEEDLE_WIDTH);
		canvas.drawLine(centerX,centerY,secondEndX,secondEndY,needlePaint);
		//绘制秒针基盘
		needlePadPaint.setColor(0xCCCCCCFF);
		needlePadPaint.setStyle(Paint.Style.FILL);
		canvas.drawCircle(centerX,centerY,radius*0.015f,needlePadPaint);

		//绘制时针
		float hourDegree = hour*1.0f/12*360 + minute*1.0f/60*30-90;
		float hourEndX = (float) (centerX + radius * HOUR_NEEDLE_LENGTH_RATIO * Math.cos(Math.toRadians(hourDegree)));
		float hourEndY = (float) (centerY + radius * HOUR_NEEDLE_LENGTH_RATIO * Math.sin(Math.toRadians(hourDegree)));
		needlePaint.setColor(0xFFFFFFFF);
		needlePaint.setStrokeWidth(HOUR_NEEDLE_WIDTH);
		canvas.drawLine(
				centerX+(float)(radius *0.035f* Math.cos(Math.toRadians(hourDegree))),
				centerY+(float)(radius *0.035f* Math.sin(Math.toRadians(hourDegree))),hourEndX,hourEndY,needlePaint);

		//绘制分针
		float minuteDegree = minute*1.0f/60*360+second*1.0f/60*6-90;
		float minuteEndX = (float) (centerX + radius * MINUTE_NEEDLE_LENGTH_RATIO * Math.cos(Math.toRadians(minuteDegree)));
		float minuteEndY = (float) (centerY + radius * MINUTE_NEEDLE_LENGTH_RATIO * Math.sin(Math.toRadians(minuteDegree)));
		needlePaint.setColor(0xFFFFFFFF);
		needlePaint.setStrokeWidth(MINUTE_NEEDLE_WIDTH);
		canvas.drawLine(
				centerX+(float)(radius *0.035f* Math.cos(Math.toRadians(minuteDegree))),
				centerY+(float)(radius *0.035f* Math.sin(Math.toRadians(minuteDegree))),minuteEndX,minuteEndY,needlePaint);
		//绘制时分针基盘
		needlePadPaint.setColor(0xFFFFFFFF);
		needlePadPaint.setStyle(Paint.Style.STROKE);
		needlePadPaint.setStrokeWidth(10.0f);
		canvas.drawCircle(centerX,centerY,radius*0.03f,needlePadPaint);

	}

	private void drawTimeNumbers(Canvas canvas,Time time) {
		// TODO 绘制表盘时间数字（可选）
		int hour = time.getHours();
		String hourString = String.valueOf(hour);
		while (hourString.length()<2) hourString = '0'+hourString;

		int minute = time.getMinutes();
		String minuteString = String.valueOf(minute);
		while (minuteString.length()<2) minuteString = '0'+minuteString;

		int second = time.getSeconds();
		String secondString = String.valueOf(second);
		while (secondString.length()<2) secondString = '0'+secondString;

		int millisecond = time.getMilliseconds();
		String millString = String.valueOf(millisecond);
		while (millString.length()<3) millString = '0'+millString;

		//绘制总指示
		numberPaint.setColor(0xFFFFFFFF);
		numberPaint.setTextSize(100);
		numberPaint.setTextAlign(Paint.Align.CENTER);

		Paint.FontMetrics fontMetrics = numberPaint.getFontMetrics();
		int baseLine = (int) (200+(fontMetrics.bottom-fontMetrics.top)/2-fontMetrics.bottom);
		canvas.drawText(hourString+" : "+minuteString+" : "+secondString+" . "+ millString , centerX , baseLine , numberPaint);

		//绘制分针上面的指示
		numberPaint.setTextSize(20);
//		numberPaint.setTextAlign(Paint.Align.CENTER);
		float minuteDegree = minute*1.0f/60*360+second*1.0f/60*6-90;
		float minuteEndX = (float) (centerX + radius * MINUTE_NEEDLE_LENGTH_RATIO * Math.cos(Math.toRadians(minuteDegree))* 1.1f) ;
		float minuteEndY = (float) (centerY + radius * MINUTE_NEEDLE_LENGTH_RATIO * Math.sin(Math.toRadians(minuteDegree))* 1.1f) ;
		fontMetrics = numberPaint.getFontMetrics();
		baseLine = (int) (minuteEndY+(fontMetrics.bottom-fontMetrics.top)/2-fontMetrics.bottom);
		canvas.drawText(minuteString , minuteEndX , baseLine , numberPaint);

		//绘制时针上面的指示
		numberPaint.setTextSize(30);
//		numberPaint.setTextAlign(Paint.Align.CENTER);
		float hourDegree = hour*1.0f/12*360 + minute*1.0f/60*30-90;
		float hourEndX = (float) (centerX + radius * HOUR_NEEDLE_LENGTH_RATIO * Math.cos(Math.toRadians(hourDegree))* 1.2f) ;
		float hourEndY = (float) (centerY + radius * HOUR_NEEDLE_LENGTH_RATIO * Math.sin(Math.toRadians(hourDegree))* 1.2f) ;
		fontMetrics = numberPaint.getFontMetrics();
		baseLine = (int) (hourEndY+(fontMetrics.bottom-fontMetrics.top)/2-fontMetrics.bottom);
		canvas.drawText(hourString , hourEndX , baseLine , numberPaint);
	}

	// 获取当前的时间：时、分、秒
	private Time getCurrentTime() {
		calendar.setTimeInMillis(System.currentTimeMillis());
		return new Time(
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.SECOND),
				calendar.get(Calendar.MILLISECOND));
	}
}
