package com.example.app3.ui.main;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

	private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
	private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
		@Override
		public String apply(Integer input) {
			return "第三页的click逻辑和touch逻辑还没有处理，除非自己非常精细，不然大概率触发touch（虽然touch的效果比click多^_^）见谅";
		}
	});

	public void setIndex(int index) {
		mIndex.setValue(index);
	}

	public LiveData<String> getText() {
		return mText;
	}
}