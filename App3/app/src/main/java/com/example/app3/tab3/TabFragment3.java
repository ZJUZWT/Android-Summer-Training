package com.example.app3.tab3;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.example.app3.R;

import java.io.ObjectInputValidation;
import java.util.Collection;
import java.util.Iterator;

import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

public class TabFragment3 extends Fragment {

	Button button;

	AnimatorSet animatorSet;
	ObjectAnimator objectAnimator;
//	PAnimater

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tab3_layout, container, false);
	}

	@Override
	public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
		button = view.findViewById(R.id.button);

		ButtonListener buttonListener = new ButtonListener();
		button.setOnClickListener(buttonListener);
		button.setOnTouchListener(buttonListener);

	}

	static class ButtonListener implements View.OnClickListener, View.OnTouchListener {

		boolean touchFlag;
		AnimatorSet animatorSet = new AnimatorSet();
		float x = -1;
		float y = -1;
		float lastX = -1;
		float lastY = -1;

		public void onClick(View v) { //发现可以通过list来控制多个流程同步异步操作
			Button button = v.findViewById(R.id.button);
			x = button.getX();
			y = button.getY();

			animatorSet = new AnimatorSet();
			ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(button,"alpha",0.1f);
			alphaAnimator.setInterpolator(new LinearInterpolator());
			alphaAnimator.setDuration(500);

			ObjectAnimator alphaAnimatorRecover = ObjectAnimator.ofFloat(button,"alpha",1.0f);
			alphaAnimatorRecover.setInterpolator(new LinearInterpolator());
			alphaAnimatorRecover.setDuration(500);

			ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(button,"rotation",0,360);
			rotateAnimator.setInterpolator(new AnticipateInterpolator());
			rotateAnimator.setDuration(1000);

//			Collection<Animator> collection = new Collection<Animator>();//这玩意也太难用了吧

//			collection.add(alphaAnimator);
//			collection.add(alphaAnimatorRecover);
//			animatorSet.playTogether();
//			animatorSet.playTogether(collection,rotateAnimator);
			animatorSet.play(rotateAnimator).with(alphaAnimator).before(alphaAnimatorRecover);
			animatorSet.start();
		}

		public boolean onTouch(View v, MotionEvent event) {
			Button button = v.findViewById(R.id.button);

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if ( x == -1 ) {
					x = button.getX(); y = button.getY();
				}
				lastX = event.getRawX() ; lastY = event.getRawY();

				animatorSet.end();
				touchFlag = false ;

				animatorSet = new AnimatorSet();
				ObjectAnimator shakeAnimator = ObjectAnimator.ofFloat(button,"rotation",-10,10) ;
				shakeAnimator.setDuration(50);
				shakeAnimator.setInterpolator(new LinearOutSlowInInterpolator());
				shakeAnimator.setRepeatCount(ValueAnimator.INFINITE);
				shakeAnimator.setRepeatMode(ValueAnimator.REVERSE);

				ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(button,"alpha",0.4f) ;
				alphaAnimator.setDuration(100);
				ObjectAnimator enlargeXAnimator = ObjectAnimator.ofFloat(button,"scaleX" , 1.0f,1.5f);
				enlargeXAnimator.setDuration(100);
				ObjectAnimator enlargeYAnimator = ObjectAnimator.ofFloat(button,"scaleY" , 1.0f,1.5f);
				enlargeYAnimator.setDuration(100);

				animatorSet.playTogether(shakeAnimator,enlargeXAnimator,enlargeYAnimator,alphaAnimator);
				animatorSet.start();
			}

			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if ( x == -1 ) return false;
				touchFlag = true;
				button.setX(button.getX()+(event.getRawX()-lastX));
				button.setY(button.getY()+(event.getRawY()-lastY));
				lastX = event.getRawX();
				lastY = event.getRawY();
			}

			if (event.getAction() == MotionEvent.ACTION_UP) {
				animatorSet.end();
				animatorSet = new AnimatorSet();

				ObjectAnimator shakeAnimatorRecover = ObjectAnimator.ofFloat(button, "rotation", button.getRotation(), 0);
				shakeAnimatorRecover.setDuration(50);

				ObjectAnimator alphaAnimatorRecover = ObjectAnimator.ofFloat(button, "alpha", 1.0f);
				alphaAnimatorRecover.setDuration(100);
				ObjectAnimator enlargeXAnimatorRecover = ObjectAnimator.ofFloat(button, "scaleX", 1.5f, 1.0f);
				enlargeXAnimatorRecover.setDuration(100);
				ObjectAnimator enlargeYAnimatorRecover = ObjectAnimator.ofFloat(button, "scaleY", 1.5f, 1.0f);
				enlargeYAnimatorRecover.setDuration(100);

				ObjectAnimator translationXAnimation = ObjectAnimator.ofFloat(button,"translationX" , 0);
				translationXAnimation.setDuration(500);
				ObjectAnimator translationYAnimation = ObjectAnimator.ofFloat(button,"translationY" , 0);
				translationYAnimation.setDuration(500);

				animatorSet.playTogether(shakeAnimatorRecover,enlargeXAnimatorRecover,enlargeYAnimatorRecover,alphaAnimatorRecover,translationXAnimation,translationYAnimation);
//				animatorSet.playTogether(shakeAnimatorRecover, enlargeXAnimatorRecover, enlargeYAnimatorRecover, alphaAnimatorRecover);
				animatorSet.start();
				return touchFlag;
			}
			return false;
		}
	}
}
