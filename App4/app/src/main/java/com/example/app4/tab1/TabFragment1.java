package com.example.app4.tab1;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.app4.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TabFragment1 extends Fragment {

	private String TAG = "TAB1";

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
