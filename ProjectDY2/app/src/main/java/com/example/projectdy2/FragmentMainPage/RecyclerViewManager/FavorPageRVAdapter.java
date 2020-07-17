package com.example.projectdy2.FragmentMainPage.RecyclerViewManager;

import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.example.projectdy2.DataBase.FavorRelation;
import com.example.projectdy2.R;
import com.example.projectdy2.VideoManager.model.Video;
import com.example.projectdy2.View.ShineButtonView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class FavorPageRVAdapter extends RecyclerView.Adapter<FavorPageRVAdapter.MainPageRVViewHolder> {
	//	private List<boolean> mPouse
	private List<FavorRelation> mDataSet = new ArrayList<>();

	public static class MainPageRVViewHolder extends RecyclerView.ViewHolder {
		private ShineButtonView likeButton;
		private ShineButtonView favorButton;
		private VideoView videoView;
		private View contentView;

		private String TAG = "testSIZE" ;

		public MainPageRVViewHolder(@NonNull View itemView) {
			super(itemView);
			contentView = itemView ;
		}

		public void onBind(int position,FavorRelation data) {	//绑定数据，前面只能算作容器，view什么的都是容器，而内容是string,icon什么的
			//虽然这里没有使用但是例程里面对现在position的调用还是比较有参考价值的
		}
	}

	public FavorPageRVAdapter() {                                  //构造函数只需要传递数据，只有数据能够初始化与改变
//		mDataSet.add(new MainPageRVData(R.drawable.like_button,null));
	}

//	public FavorPageRVAdapter(List<Video> mDataSet_) {                                  //构造函数只需要传递数据，只有数据能够初始化与改变
//		mDataSet.addAll(mDataSet_) ;
//	}

	public void setData(List<FavorRelation> mDataSet_) {
		mDataSet.clear();
		mDataSet.addAll(mDataSet_) ;
	}

	@Override
	public FavorPageRVAdapter.MainPageRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new MainPageRVViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_mainpage_favor_video_element, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull MainPageRVViewHolder holder, int position) {
		holder.onBind(position, mDataSet.get(position));
	}

	@Override
	public int getItemCount() {
		return mDataSet.size();
	}
}
