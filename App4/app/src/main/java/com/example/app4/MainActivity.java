package com.example.app4;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app4.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);			//载入布局
		SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
		ViewPager viewPager = findViewById(R.id.view_pager);
		viewPager.setAdapter(sectionsPagerAdapter);		//给viewPage加入容器管理器
		TabLayout tabs = findViewById(R.id.tabs);
		tabs.setupWithViewPager(viewPager);				//给TabLayout加入管理的viewPage
														//整体的逻辑是一个activity上面，同步tablayout和viewPager，而整体的配置都是在viewPager的Adapter上面


	}
}