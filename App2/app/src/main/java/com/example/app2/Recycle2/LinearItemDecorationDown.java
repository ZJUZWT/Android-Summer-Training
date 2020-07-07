package com.example.app2.Recycle2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class LinearItemDecorationDown extends RecyclerView.ItemDecoration {

	private Paint mPaint;

	public LinearItemDecorationDown(int color) {
		// 直接绘制颜色  只是用来测试
		mPaint = new Paint();
		mPaint.setColor(color);
		mPaint.setAntiAlias(true);
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
		int childCount = parent.getChildCount();

		Rect rect = new Rect();
		rect.left = parent.getLeft();
		rect.right = parent.getRight();				//调整矩形绘制范围
		for (int i = 0; i < childCount; i++) {
			View childView = parent.getChildAt(i);
			rect.top = childView.getBottom();
			rect.bottom = rect.top+5;
			c.drawRect(rect, mPaint);
		}
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		outRect.top += 5;
	}
}
