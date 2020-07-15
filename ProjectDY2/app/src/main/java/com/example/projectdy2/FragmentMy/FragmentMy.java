package com.example.projectdy2.FragmentMy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.example.projectdy2.DataBase.MyDataBase;
import com.example.projectdy2.DataBase.QueryDao;
import com.example.projectdy2.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentMy extends Fragment {
	Button buttonLogin;
	Button buttonRegister;
	View layoutLogin;

	LottieAnimationView lottieAnimationView;

	String TAG = "生命周期";

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView: ");
		return inflater.inflate(R.layout.fragment_my,container,false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		Log.d(TAG, "onViewCreated: ");
		
		super.onViewCreated(view, savedInstanceState);

		layoutLogin = view.findViewById(R.id.my_page_login);
		lottieAnimationView = view.findViewById(R.id.lottie_login);

		new Thread() {
			@Override
			public void run() {
				QueryDao query = MyDataBase.inst(getContext()).queryDao();
				if ( query.hasLogin().size() == 0 ) {
//					view.setVisibility(View.GONE);
//					recyclerView.setVisibility(View.GONE);
					layoutLogin.setVisibility(View.VISIBLE);
					lottieAnimationView.playAnimation();
				} else {
					layoutLogin.setVisibility(View.GONE);
				}

			}
		}.start();
	}

	@Override
	public void onResume() {
		Log.d(TAG, "onResume: ");
		super.onResume();
	}

	@Override
	public void onPause() {
		Log.d(TAG, "onPause: ");
		super.onPause();
	}

	@Override
	public void onStop() {
		Log.d(TAG, "onStop: ");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		Log.d(TAG, "onDestroyView: ");
		super.onDestroyView();
	}
}
