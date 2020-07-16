package com.example.projectdy2.FragmentChat;

import android.content.Context;

import com.example.projectdy2.R;

import java.util.ArrayList;
import java.util.List;

public class TestDataSet {

	public static List<FragmentChatData> getData(Context context) {
		List<FragmentChatData> result = new ArrayList();
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "byte"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "dance"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "zhe"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "jiang"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "da"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "xue"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "zha"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "jiu"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "shi"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "wo"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "!"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "byte"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "dance"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "zhe"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "jiang"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "da"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "xue"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "zha"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "jiu"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "shi"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "wo"));
		result.add(new FragmentChatData(context.getDrawable(R.drawable.people), "!"));
		return result;
	}
}
