package com.example.app2.Recycle1;

import android.content.Context;

import com.example.app2.R;

import java.util.ArrayList;
import java.util.List;

public class TestDataSet1 {
	public static List<TestData1> getData(Context context) {//没办法，得传一个context进来才能采集
		List<TestData1> result = new ArrayList();
		result.add(new TestData1(context.getDrawable(R.drawable.ic_launcher_foreground), "粉丝"));
		result.add(new TestData1(context.getDrawable(R.drawable.ic_launcher_foreground), "赞"));
		result.add(new TestData1(context.getDrawable(R.drawable.ic_launcher_foreground), "@我的"));
		result.add(new TestData1(context.getDrawable(R.drawable.ic_launcher_foreground), "评论"));
		result.add(new TestData1(context.getDrawable(R.drawable.ic_launcher_foreground), "起飞"));
		result.add(new TestData1(context.getDrawable(R.drawable.ic_launcher_foreground), "这里"));
		result.add(new TestData1(context.getDrawable(R.drawable.ic_launcher_foreground), "三个字"));
		result.add(new TestData1(context.getDrawable(R.drawable.ic_launcher_foreground), "继续"));
		result.add(new TestData1(context.getDrawable(R.drawable.ic_launcher_foreground), "新的"));
		result.add(new TestData1(context.getDrawable(R.drawable.ic_launcher_foreground), "这个"));
		result.add(new TestData1(context.getDrawable(R.drawable.ic_launcher_foreground), "还行"));
		return result;
	}
}
