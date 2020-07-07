package com.example.app2.Recycle1;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LinearItemDecorationUp extends RecyclerView.ItemDecoration {

	private Paint mPaint;

	public LinearItemDecorationUp(int color) {
		// 直接绘制颜色  只是用来测试
		mPaint = new Paint();
		mPaint.setColor(color);
		mPaint.setAntiAlias(true);
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
		int childCount = parent.getChildCount();

		Rect rect = new Rect();
		rect.top = parent.getTop();
		rect.bottom = parent.getBottom();				//调整矩形绘制范围
		for (int i = 0; i < childCount; i++) {
			View childView = parent.getChildAt(i);
			rect.left = childView.getRight();
			rect.right = rect.left+5;
			c.drawRect(rect, mPaint);
		}
	}

	@Override
	public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
		super.onDrawOver(c, parent, state);
		Rect rect = new Rect();

		rect.left = parent.getLeft();
		rect.right = parent.getRight();
		rect.top = parent.getBottom()-5;
		rect.bottom = rect.top+5;
		c.drawRect(rect, mPaint);

		//两个界面分割采用上面画线的方 法
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		outRect.right += 5;
	}
}
