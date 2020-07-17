package com.example.projectdy2.FragmentMainPage.RecyclerViewManager;

import android.content.Context;
import android.view.View;

import com.example.projectdy2.InterfaceForInteract.OnViewPagerListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class VideoListLayoutManager extends LinearLayoutManager implements RecyclerView.OnChildAttachStateChangeListener {
	private int mDrift;

	private PagerSnapHelper mPagerSnapHelper;
	private OnViewPagerListener mOnViewPagerListener;
	public VideoListLayoutManager(Context context) {
		super(context);
	}

	public VideoListLayoutManager(Context context, int orientation, boolean reverseLayout) {
		super(context, orientation, reverseLayout);
		mPagerSnapHelper = new PagerSnapHelper();
	}

	@Override
	public void onAttachedToWindow(RecyclerView view) {

		view.addOnChildAttachStateChangeListener(this);
		mPagerSnapHelper.attachToRecyclerView(view);
		super.onAttachedToWindow(view);
	}

	@Override
	public void onChildViewAttachedToWindow(@NonNull View view) {
		if (mDrift > 0) {
			if (mOnViewPagerListener != null) {
				mOnViewPagerListener.onPageSelected(getPosition(view), true);
			}

		}else {
			if (mOnViewPagerListener != null) {
				mOnViewPagerListener.onPageSelected(getPosition(view), false);
			}
		}
	}

	public void setOnViewPagerListener(OnViewPagerListener mOnViewPagerListener) {
		this.mOnViewPagerListener = mOnViewPagerListener;
	}

	@Override
	public void onScrollStateChanged(int state) {
		if (state == RecyclerView.SCROLL_STATE_IDLE) {
			View view = mPagerSnapHelper.findSnapView(this);
			int position = getPosition(view);
			if (mOnViewPagerListener != null)
				mOnViewPagerListener.onPageSelected(position, position == getItemCount() - 1);
		}
		super.onScrollStateChanged(state);
	}

	@Override
	public void onChildViewDetachedFromWindow(@NonNull View view) {
		if (mDrift >= 0){
			if (mOnViewPagerListener != null) mOnViewPagerListener.onPageRelease(true,getPosition(view));
		}else {
			if (mOnViewPagerListener != null) mOnViewPagerListener.onPageRelease(false,getPosition(view));
		}
	}


	@Override
	public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
		this.mDrift = dy;
		return super.scrollVerticallyBy(dy, recycler, state);
	}

	@Override
	public boolean canScrollVertically() {
		return true;
	}
}