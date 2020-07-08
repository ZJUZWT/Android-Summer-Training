package com.example.app3.tab1;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.app3.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TabFragment1 extends Fragment {

	private String TAG = "TAB1";

	ValueAnimator valueAnimator;
	LottieAnimationView lottieAnimationView;		//监控lottie
	SeekBar	seekBar;
	EditText editText;
	Switch aSwitch;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		Log.d(TAG, "onAttach: ");
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView: ");
		return inflater.inflate(R.layout.tab1_layout, container, false);
	}

	@Override
	public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
		Log.d(TAG, "onViewCreated: ");
		super.onViewCreated(view, savedInstanceState);

		//对lottie实施监听
//		lottieAnimationView = getActivity().findViewById(R.id.Tab1_Lottie);
		lottieAnimationView = view.findViewById(R.id.Tab1_Lottie);
		lottieAnimationView.useHardwareAcceleration();
//		lottieAnimationView.pauseAnimation();
		lottieAnimationView.setRepeatMode(LottieDrawable.REVERSE);
		if ( seekBar == null )
			lottieAnimationView.setProgress(0.5f);
		else {
			Float temp = seekBar.getProgress() * 1.0f / seekBar.getMax();
			Log.d(TAG, "onViewCreated: "+ temp);
			lottieAnimationView.setProgress(temp);
		}
		lottieAnimationView.setProgress(0.5f);
		lottieAnimationView.pauseAnimation();
		Log.d(TAG, "onViewCreated: "+ lottieAnimationView.getProgress());

		lottieAnimationView.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Float progress = (Float)animation.getAnimatedValue();
//				lottieAnimationView.setProgress(progress);
				if ( !aSwitch.isChecked()) {
					seekBar.setProgress((int) (progress*seekBar.getMax()));
					editText.setText(String.valueOf(progress));
				}
			}
		});
//		lottieAnimationView.playAnimation();

		//对seekBar实施监听
//		seekBar = getActivity().findViewById(R.id.Tab1_seekbar);
		seekBar = view.findViewById(R.id.Tab1_seekbar);
		seekBar.setEnabled(false);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//				String temp = progress ;
				editText.setText(String.valueOf(progress*1.0f/seekBar.getMax()));
				if ( aSwitch.isChecked() )
//					valueAnimator.setCurrentFraction(progress*1.0f/seekBar.getMax());
					lottieAnimationView.setProgress(progress*1.0f/seekBar.getMax());
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		//对数字实施监听
//		editText = getActivity().findViewById(R.id.Tab1_number);
		editText = view.findViewById(R.id.Tab1_number);
		editText.setEnabled(false);

		//对switch监听
//		aSwitch = getActivity().findViewById(R.id.Tab1_switch);
		aSwitch = view.findViewById(R.id.Tab1_switch);
		aSwitch.setEnabled(true);
		aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if ( isChecked ) {
					lottieAnimationView.pauseAnimation();
//					lottieAnimationView.cancelAnimation();
//					lottieAnimationView.setProgress(seekBar.getProgress()*1.0f/seekBar.getMax());
//					valueAnimator.pause();
					seekBar.setEnabled(true);
					editText.setEnabled(true);
				} else {
//					lottieAnimationView.playAnimation();
					lottieAnimationView.resumeAnimation();
//					valueAnimator.resume();
//					valueAnimator.start();
					seekBar.setEnabled(false);
					editText.setEnabled(false);
				}
			}
		});

//		//调整animation，放在最下面是为了防止seekBar和editText是null
		//之前不知道可以直接监听，后面突发奇想直接监听竟然有重载函数
//		valueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(1000);
//		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//			@Override
//			public void onAnimationUpdate(ValueAnimator animation) {
//				Float progress = (Float)animation.getAnimatedValue();
//				lottieAnimationView.setProgress(progress);
//				if ( !aSwitch.isChecked()) {
//					seekBar.setProgress((int) (progress*seekBar.getMax()));
//					editText.setText(String.valueOf(progress));
//				}
//			}
//		});
//		valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
//		valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
//		valueAnimator.start();
	}

	//我探究了一下，发现这个每次启动3个界面就所以当移动到最右边的地方时，需要还原场景

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG,"<-----onStart----->");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG,"<-----onResume----->");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG,"<-----onPause----->");
	}

	@Override
	public void onStop() {
//		lottieAnimationView.setEnabled(false);
		aSwitch.setChecked(true);

		super.onStop();
		Log.d(TAG,"<-----onStop----->");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d(TAG,"<-----onDestroyView----->");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"<-----onDestroy----->");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(TAG,"<-----onDetach----->");
	}


}
