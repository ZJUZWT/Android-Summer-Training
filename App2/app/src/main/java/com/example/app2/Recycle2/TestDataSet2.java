package com.example.app2.Recycle2;

import android.content.Context;

import com.example.app2.R;
import com.example.app2.Recycle2.TestData2;

import java.util.ArrayList;
import java.util.List;

public class TestDataSet2 {
	public static List<TestData2> getData(Context context) {//没办法，得传一个context进来才能采集
		List<TestData2> result = new ArrayList();
		result.add(new TestData2(context.getDrawable(R.drawable.people), "byte"));
		result.add(new TestData2(context.getDrawable(R.drawable.people), "dance"));
		result.add(new TestData2(context.getDrawable(R.drawable.people), "zhe"));
		result.add(new TestData2(context.getDrawable(R.drawable.people), "jiang"));
		result.add(new TestData2(context.getDrawable(R.drawable.people), "da"));
		result.add(new TestData2(context.getDrawable(R.drawable.people), "xue"));
		result.add(new TestData2(context.getDrawable(R.drawable.people), "zha"));
		result.add(new TestData2(context.getDrawable(R.drawable.people), "jiu"));
		result.add(new TestData2(context.getDrawable(R.drawable.people), "shi"));
		result.add(new TestData2(context.getDrawable(R.drawable.people), "wo"));
		result.add(new TestData2(context.getDrawable(R.drawable.people), "!"));
		return result;
	}
}
