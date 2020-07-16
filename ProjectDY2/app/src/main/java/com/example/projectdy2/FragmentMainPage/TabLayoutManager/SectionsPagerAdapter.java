package com.example.projectdy2.FragmentMainPage.TabLayoutManager;

import android.content.Context;

import com.example.projectdy2.FragmentMainPage.LikePage;
import com.example.projectdy2.FragmentMainPage.RecommendPage;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
	LikePage likePage = new LikePage();
	RecommendPage recommendPage = new RecommendPage();

	private static final String[] TAB_TITLES = new String[]{"收藏","推荐"};
	private final Context mContext;

	public SectionsPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;
	}

	@Override
	public Fragment getItem(int position) {
		if ( position == 0 )
			return likePage;
		else
			return recommendPage;
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		return TAB_TITLES[position];
	}

	@Override
	public int getCount() {		//这里可以控制显示的数量
		return 2;				//一定要与上面getPageTitle适配
	}
}