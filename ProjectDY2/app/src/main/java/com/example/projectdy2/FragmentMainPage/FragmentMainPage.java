package com.example.projectdy2.FragmentMainPage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.projectdy2.FragmentMainPage.TabLayoutManager.SectionsPagerAdapter;
import com.example.projectdy2.InterfaceForInteract.BringFrontWaveButton;
import com.example.projectdy2.InterfaceForInteract.tabLayoutLottieInterface;
import com.example.projectdy2.MainActivity;
import com.example.projectdy2.R;
import com.example.projectdy2.VideoManager.api.IMiniDouyinService;
import com.example.projectdy2.VideoManager.model.GetVideosResponse;
import com.example.projectdy2.VideoManager.model.Video;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentMainPage extends Fragment {
	com.example.projectdy2.InterfaceForInteract.tabLayoutLottieInterface tabLayoutLottieInterface;
	private BringFrontWaveButton listenerForButton;

	String TAG = "测试";

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_mainpage,container,false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		//TODO：初始化TabLayout
		SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getContext(), getActivity().getSupportFragmentManager());
		ViewPager viewPager = view.findViewById(R.id.view_pager);
		viewPager.setAdapter(sectionsPagerAdapter);				//给viewPage加入容器管理器
//		viewPager.setCurrentItem(1);
		final TabLayout tabLayout = view.findViewById(R.id.tabLayout);
		tabLayout.setupWithViewPager(viewPager);				//给TabLayout加入管理的viewPage
//		tabLayout.getTabAt(1).select();
//		tabLayout.selectTab(tabLayout.getTabAt(1));
		//整体的逻辑是一个activity上面，同步tabLayout和viewPager，而整体的配置都是在viewPager的Adapter上面

		tabLayoutLottieInterface = (LikePage)sectionsPagerAdapter.getItem(0);
//		tabLayoutLottieInterface.startAnim();

		//TODO：调整画面层次结构
		tabLayout.bringToFront();
		listenerForButton = (MainActivity)view.getContext();
		listenerForButton.bringAllUp();

		//TODO:初始化两个RV
		// 这部分转移到两个分fragment去做

		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
//				tabLayoutLottieInterface.stopAnim();
				Log.d(TAG, "onTabUnselected: " + tab.toString());
				if ( tab == tabLayout.getTabAt(0) ) tabLayoutLottieInterface.startAnim();
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
//				Log.d(TAG, "onTabUnselected: ");
				if ( tab == tabLayout.getTabAt(0) )tabLayoutLottieInterface.stopAnim();
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
//		initRRVPage();
	}


}
