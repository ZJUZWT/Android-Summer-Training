package com.example.projectdy2.FragmentMy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.airbnb.lottie.LottieAnimationView;
import com.example.projectdy2.DataBase.MyDataBase;
import com.example.projectdy2.DataBase.QueryDao;
import com.example.projectdy2.LoginActivity;
import com.example.projectdy2.R;
import com.example.projectdy2.RegisterActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentMy extends Fragment {
	Button buttonLogin;
	Button buttonRegister;
	View layoutLogin;
	View viewBanner;
	ImageButton headImage;

	Button buttonLogout;

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

		viewBanner = view.findViewById(R.id.view2);
		viewBanner.setVisibility(View.INVISIBLE);
		layoutLogin = view.findViewById(R.id.my_page_login);
		layoutLogin.setVisibility(View.INVISIBLE);
		lottieAnimationView = view.findViewById(R.id.lottie_login);
		lottieAnimationView.setVisibility(View.INVISIBLE);
		buttonLogout = view.findViewById(R.id.logoutButton);
		buttonLogout.setVisibility(View.INVISIBLE);
		headImage = view.findViewById(R.id.imageButton);
		headImage.setVisibility(View.INVISIBLE);

		buttonLogin = view.findViewById(R.id.no_user_login);
		buttonLogin.setVisibility(View.INVISIBLE);
		buttonRegister = view.findViewById(R.id.no_user_register);
		buttonRegister.setVisibility(View.INVISIBLE);
		buttonLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
		});
		buttonRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), RegisterActivity.class);
				startActivity(intent);
			}
		});

		buttonLogout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					@Override
					public void run() {
						QueryDao query = MyDataBase.inst(getContext()).queryDao();
						query.logout();
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								onResume();

							}
						});
					}
				}.start();
			}
		});
	}

	@Override
	public void onResume() {
		Log.d(TAG, "onResume: ");
		new Thread() {
			@Override
			public void run() {
				QueryDao query = MyDataBase.inst(getContext()).queryDao();
				if ( query.hasLogin().size() == 0 ) {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							layoutLogin.setVisibility(View.VISIBLE);
							lottieAnimationView.setVisibility(View.VISIBLE);
							buttonLogin.setVisibility(View.VISIBLE);
							buttonRegister.setVisibility(View.VISIBLE);
							viewBanner.setVisibility(View.GONE);
							headImage.setVisibility(View.GONE);
							buttonLogout.setVisibility(View.GONE);
							lottieAnimationView.playAnimation();
						}
					});
				} else {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							layoutLogin.setVisibility(View.GONE);
							viewBanner.setVisibility(View.VISIBLE);
							headImage.setVisibility(View.VISIBLE);
							buttonLogout.setVisibility(View.VISIBLE);
						}
					});
				}
			}
		}.start();
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
