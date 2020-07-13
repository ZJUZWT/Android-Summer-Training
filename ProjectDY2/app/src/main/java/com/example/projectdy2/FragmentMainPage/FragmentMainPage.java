package com.example.projectdy2.FragmentMainPage;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectdy2.InterfaceForInteract.BringFrontWaveButton;
import com.example.projectdy2.MainActivity;
import com.example.projectdy2.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class FragmentMainPage extends Fragment {
	BringFrontWaveButton listenerForButton;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_mainpage,container,false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getContext(), getActivity().getSupportFragmentManager());
		ViewPager viewPager = view.findViewById(R.id.view_pager);
		viewPager.setAdapter(sectionsPagerAdapter);				//给viewPage加入容器管理器
		TabLayout tabLayout = view.findViewById(R.id.tabLayout);
		tabLayout.setupWithViewPager(viewPager);				//给TabLayout加入管理的viewPage
		//整体的逻辑是一个activity上面，同步tabLayout和viewPager，而整体的配置都是在viewPager的Adapter上面

//		TabItem tabItem = view.findViewById(R.id.tabLayout_left);

		tabLayout.bringToFront();
		listenerForButton = (MainActivity)view.getContext();
		listenerForButton.bringAllUp();
	}
}
