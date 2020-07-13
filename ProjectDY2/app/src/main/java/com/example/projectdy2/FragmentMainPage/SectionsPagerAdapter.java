package com.example.projectdy2.FragmentMainPage;

import android.content.Context;

import com.example.projectdy2.FragmentMainPage.FollowPage;
import com.example.projectdy2.FragmentMainPage.RecommendPage;
import com.example.projectdy2.R;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private static final String[] TAB_TITLES = new String[]{"关注","推荐"};
	private final Context mContext;

	public SectionsPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;
	}

	@Override
	public Fragment getItem(int position) {
		if ( position == 0 )
			return new FollowPage();
		else
			return new RecommendPage();
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