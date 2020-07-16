package com.example.projectdy2.FragmentMainPage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectdy2.FragmentMainPage.TabLayoutManager.SectionsPagerAdapter;
import com.example.projectdy2.InterfaceForInteract.BringFrontWaveButton;
import com.example.projectdy2.InterfaceForInteract.FindCurrentTab;
import com.example.projectdy2.InterfaceForInteract.RefreshList;
import com.example.projectdy2.InterfaceForInteract.showLikePage;
import com.example.projectdy2.InterfaceForInteract.showRecommendPage;
import com.example.projectdy2.MainActivity;
import com.example.projectdy2.R;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class FragmentMainPage extends Fragment implements RefreshList, FindCurrentTab {
	showLikePage showLikePage;
	showRecommendPage showRecommendPage;
	private BringFrontWaveButton listenerForButton;
	TabLayout tabLayout;

	String TAG = "测试";
	SectionsPagerAdapter sectionsPagerAdapter;
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_mainpage,container,false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		//TODO：初始化TabLayout
		sectionsPagerAdapter = new SectionsPagerAdapter(getContext(), getActivity().getSupportFragmentManager());
		ViewPager viewPager = view.findViewById(R.id.view_pager);
		viewPager.setAdapter(sectionsPagerAdapter);				//给viewPage加入容器管理器
//		viewPager.setCurrentItem(1);
		tabLayout = view.findViewById(R.id.tabLayout);
		tabLayout.setupWithViewPager(viewPager);				//给TabLayout加入管理的viewPage
//		tabLayout.getTabAt(1).select();
//		tabLayout.selectTab(tabLayout.getTabAt(1));
		//整体的逻辑是一个activity上面，同步tabLayout和viewPager，而整体的配置都是在viewPager的Adapter上面

		showLikePage = (LikePage)sectionsPagerAdapter.getItem(0);
		showRecommendPage = (showRecommendPage)sectionsPagerAdapter.getItem(1);
		tabLayout.getSelectedTabPosition();
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
				if ( tab == tabLayout.getTabAt(0) ) showLikePage.showLikePage();
				if ( tab == tabLayout.getTabAt(1) ) showRecommendPage.showRecommendPage();
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
//				Log.d(TAG, "onTabUnselected: ");
				if ( tab == tabLayout.getTabAt(0) ) showLikePage.pauseLikePage();
				if ( tab == tabLayout.getTabAt(1) ) showRecommendPage.pauseRecommendPage();
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
//		initRRVPage();
	}

	@Override
	public void refresh() {
		RefreshList refreshList = (RefreshList) sectionsPagerAdapter.getItem(1);
		refreshList.refresh();
	}

	@Override
	public int tabPosition() {
		return tabLayout.getSelectedTabPosition();
	}
}
