package com.example.projectdy2.FragmentMainPage;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.projectdy2.DataBase.MyDataBase;
import com.example.projectdy2.DataBase.QueryDao;
import com.example.projectdy2.FragmentMainPage.RecyclerViewManager.MainPageRVAdapter;
import com.example.projectdy2.FragmentMainPage.RecyclerViewManager.MainPageRVData;
import com.example.projectdy2.InterfaceForInteract.tabLayoutLottieInterface;
import com.example.projectdy2.LoginActivity;
import com.example.projectdy2.MainActivity;
import com.example.projectdy2.R;
import com.example.projectdy2.RegisterActivity;
import com.example.projectdy2.VideoManager.model.GetVideosResponse;
import com.example.projectdy2.VideoManager.model.Video;
import com.google.android.material.snackbar.Snackbar;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikePage extends Fragment implements tabLayoutLottieInterface {
	List<Video> data;
	RecyclerView recyclerView;
	MainPageRVAdapter adapter;
	LinearLayoutManager linearLayoutManager;
	LottieAnimationView lottieAnimationView;

	//登录操作的东西
	Button buttonLogin;
	Button buttonRegister;
	View layoutLogin;

	String TAG = "测试";

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		data = ((MainActivity)context).getVideo();//通过强转成宿主activity，就可以获取到传递过来的数据
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_mainpage_video_like_page,container,false);
	}

	@Override
	public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		//处理注册
		layoutLogin = view.findViewById(R.id.like_page_login);

		recyclerView = view.findViewById(R.id.like_page);
		recyclerView.setHasFixedSize(true);

		linearLayoutManager = new LinearLayoutManager(view.getContext());
		linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

		adapter = new MainPageRVAdapter(data);

		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(adapter);

		lottieAnimationView = view.findViewById(R.id.lottie_login);
		lottieAnimationView.playAnimation();
		new Thread() {
			@Override
			public void run() {
				QueryDao query = MyDataBase.inst(getContext()).queryDao();
				if ( query.hasLogin().size() == 0 ) {
//					view.setVisibility(View.GONE);
					recyclerView.setVisibility(View.GONE);
					layoutLogin.setVisibility(View.VISIBLE);
				} else {
					layoutLogin.setVisibility(View.GONE);
				}

			}
		}.start();

		buttonLogin = view.findViewById(R.id.no_user_login);
		buttonRegister = view.findViewById(R.id.no_user_register);
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
	}

	@Override
	public void stopAnim() {
		Log.d(TAG, "stopAnim: ");
		if ( layoutLogin.getVisibility() == View.VISIBLE) lottieAnimationView.pauseAnimation();
	}

	@Override
	public void startAnim() {
		Log.d(TAG, "startAnim: ");
		if ( layoutLogin.getVisibility() == View.VISIBLE) lottieAnimationView.playAnimation();
	}
}
