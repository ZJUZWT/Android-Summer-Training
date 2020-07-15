package com.example.projectdy2.FragmentMainPage;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.projectdy2.FragmentMainPage.RecyclerViewManager.MainPageRVAdapter;
import com.example.projectdy2.FragmentMainPage.RecyclerViewManager.VideoListLayoutManager;
import com.example.projectdy2.InterfaceForInteract.OnViewPagerListener;
import com.example.projectdy2.R;
import com.example.projectdy2.VideoManager.api.IMiniDouyinService;
import com.example.projectdy2.VideoManager.model.GetVideosResponse;
import com.example.projectdy2.VideoManager.model.Video;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecommendPage extends Fragment {
	boolean isPressed = false;
	List<Video> data;
	List<Video> usedData = new ArrayList<>();
	RecyclerView recyclerView;
	MainPageRVAdapter adapter;
	LinearLayoutManager linearLayoutManager;
	VideoListLayoutManager myLayoutManager;

	String TAG = "测试" ;

	private Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(IMiniDouyinService.BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build();
	private IMiniDouyinService miniDouyinService = retrofit.create(IMiniDouyinService.class);

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_mainpage_video_like_page,container,false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		recyclerView = view.findViewById(R.id.like_page);
		recyclerView.setHasFixedSize(true);

		linearLayoutManager = new LinearLayoutManager(view.getContext());
		linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
		myLayoutManager = new VideoListLayoutManager(getContext(), OrientationHelper.VERTICAL,false);
		adapter = new MainPageRVAdapter();

		recyclerView.setLayoutManager(myLayoutManager);
		recyclerView.setAdapter(adapter);

		initVideo();
		initInterface();
	}


	private void initVideo() {
		miniDouyinService.getVideos().enqueue(new Callback<GetVideosResponse>() {
			@Override
			public void onResponse(Call<GetVideosResponse> call, Response<GetVideosResponse> response) {
				if (response.body() != null && response.body().videos != null) {
					data = response.body().videos;
					adapter.setData(data);

					adapter.notifyDataSetChanged();
//					adapter.notifyItemChanged(0,data.size());
				}
			}
			@Override
			public void onFailure(Call<GetVideosResponse> call, Throwable throwable) {
				Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}


	private void initInterface() {
		myLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
			@Override
			public void onInitComplete() {

			}

			@Override
			public void onPageRelease(boolean isNext, int position) {
				Log.d(TAG,"释放位置:"+position +" 下一页:"+isNext);
				int index;
				if (isNext)		index = 0;
				else 			index = 1;

				releaseVideo(position);
			}

			@Override
			public void onPageSelected(int position, boolean isNext) {
//				Log.d(TAG,"释放位置:"+position +" 下一页:"+isNext);
				int index;
				if (isNext)	index = 0;
				else		index = 1;

				playVideo(position);
			}
		});
	}

	private void releaseVideo(int index){
//		View itemView = recyclerView.getChildAt(index);
		View itemView = recyclerView.getLayoutManager().findViewByPosition(index);
		final VideoView videoView = itemView.findViewById(R.id.mainPageVideoView);
//		final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
//		final ImageView imgPlay = itemView.findViewById(R.id.img_play);
//		final
		videoView.stopPlayback();
//		imgThumb.animate().alpha(1).start();
//		imgPlay.animate().alpha(0f).start();
	}


	private void playVideo(int position) {
		isPressed = false;
		Uri videoLink ;
		int random = (int) (Math.random()*data.size());
		if ( position >= usedData.size() ) {
			usedData.add(data.get(random));
			data.remove(random);
		}
		videoLink = Uri.parse(usedData.get(position).videoUrl);

		Log.d(TAG,"position : " + position + " 视频URL:"+videoLink );
//		View itemView = recyclerView.getChildAt(position);
		View itemView = recyclerView.getLayoutManager().findViewByPosition(position);
		final VideoView videoView = itemView.findViewById(R.id.mainPageVideoView);
//		final ImageView imgPlay = itemView.findViewById(R.id.img_play);
//		final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
//		final RelativeLayout rootView = itemView.findViewById(R.id.);
//		final MediaPlayer[] mediaPlayer = new MediaPlayer[1];
//		Log.d(TAG,"position : " + position + " 视频URL:"+data.get(position).videoUrl );

		videoView.setVideoURI(videoLink);
		videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
			@Override
			public boolean onInfo(MediaPlayer mp, int what, int extra) {
//				mediaPlayer[0] = mp;
				mp.setLooping(true);
//				imgThumb.animate().alpha(0).setDuration(200).start();
				return false;
			}
		});
		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				videoView.start();
			}
		});

		videoView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isPressed) {
					isPressed = true;

					new Thread(new Runnable() {
						@Override
						public void run() {
							try { Thread.sleep(500); }
							catch (Exception e) { e.printStackTrace(); }
							finally	{ isPressed = false; }
						}
					}).start();
				} else {
					if ( videoView.isPlaying() ) videoView.pause(); else videoView.start();
				}
			}
		});
	}

}
